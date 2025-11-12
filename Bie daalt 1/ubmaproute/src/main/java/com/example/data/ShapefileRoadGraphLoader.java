package com.example.data;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.example.graph.Graph;
import com.example.graph.Node;
import com.example.util.GeoUtils;

public class ShapefileRoadGraphLoader {
    private final File shapefile;
    private final double minLon, minLat, maxLon, maxLat;

    public ShapefileRoadGraphLoader(File shapefile, double minLon, double minLat, double maxLon, double maxLat) {
        this.shapefile = shapefile;
        this.minLon = minLon;
        this.minLat = minLat;
        this.maxLon = maxLon;
        this.maxLat = maxLat;
    }

    public Graph loadGraph() throws Exception {
        Graph graph = new Graph();
        Map<String, Object> map = new HashMap<>();
        map.put("url", toUrl(shapefile));

        DataStore store = DataStoreFinder.getDataStore(map);
        if (store == null)
            throw new IllegalStateException("Cannot open shapefile: " + shapefile);
        String typeName = store.getTypeNames()[0];
        SimpleFeatureSource source = store.getFeatureSource(typeName);

        CoordinateReferenceSystem crs = source.getSchema().getCoordinateReferenceSystem();

        String geomAttr = source.getSchema().getGeometryDescriptor().getLocalName();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

        Filter filter = ff.bbox(ff.property(geomAttr), minLon, minLat, maxLon, maxLat, "EPSG:4326");
        Query query = new Query(typeName, filter);

        try (SimpleFeatureIterator it = source.getFeatures(query).features()) {
            long nextId = 1;
            Map<String, Long> coordToId = new HashMap<>();
            GeometryFactory gf = new GeometryFactory();
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                Object geomObj = f.getDefaultGeometry();
                if (!(geomObj instanceof Geometry))
                    continue;
                Geometry geom = (Geometry) geomObj;

                // Нэг чиглэлтэй ирмэгийг авах ("yes", "-1", "no"/null)
                String onewayRaw = null;
                try {
                    Object v = f.getAttribute("oneway");
                    if (v != null)
                        onewayRaw = String.valueOf(v).trim().toLowerCase();
                } catch (Exception ignore) {
                }
                int dirFlag = 0; // 0=both, 1=forward, -1=reverse
                if ("yes".equals(onewayRaw) || "1".equals(onewayRaw) || "true".equals(onewayRaw))
                    dirFlag = 1;
                else if ("-1".equals(onewayRaw))
                    dirFlag = -1;

                String fclass = getStringAttr(f, "fclass");
                String access = getStringAttr(f, "access");
                String surface = getStringAttr(f, "surface");
                double maxSpeed = parseMaxSpeedKmh(getStringAttr(f, "maxspeed"));
                if (Double.isNaN(maxSpeed))
                    maxSpeed = defaultSpeedForClass(fclass, surface);
                boolean drivable = isDrivable(fclass, access);

                if (geom instanceof MultiLineString) {
                    MultiLineString mls = (MultiLineString) geom;
                    for (int i = 0; i < mls.getNumGeometries(); i++) {
                        LineString ls = (LineString) mls.getGeometryN(i);
                        if (drivable)
                            processLineString(ls, graph, coordToId, nextId, dirFlag, maxSpeed);
                        nextId += ls.getNumPoints();
                    }
                } else if (geom instanceof LineString) {
                    LineString ls = (LineString) geom;
                    if (drivable)
                        processLineString(ls, graph, coordToId, nextId, dirFlag, maxSpeed);
                    nextId += ls.getNumPoints();
                }
            }
        } finally {
            store.dispose();
        }

        return graph;
    }

    private void processLineString(LineString ls, Graph graph, Map<String, Long> coordToId, long startId, int dirFlag,
            double speedKmh) {
        Coordinate[] coords = ls.getCoordinates();
        Node prev = null;
        long id = startId;
        for (Coordinate c : coords) {

            double lon = c.getX();
            double lat = c.getY();
            if (!GeoUtils.withinBbox(lon, lat, minLon, minLat, maxLon, maxLat))
                continue;
            String key = GeoUtils.keyFor(lat, lon);
            Long existing = coordToId.get(key);
            long nodeId;
            if (existing == null) {
                nodeId = id++;
                coordToId.put(key, nodeId);
                graph.addNode(nodeId, lat, lon);
            } else {
                nodeId = existing;
            }
            Node cur = graph.addNode(nodeId, lat, lon);
            if (prev != null) {
                double lengthM = com.example.util.GeoUtils.haversineMeters(prev.lat, prev.lon, cur.lat, cur.lon);
                double weightSec = lengthM / Math.max(1.0, (speedKmh / 3.6));
                if (dirFlag == 1)
                    graph.addEdge(prev, cur, weightSec);
                else if (dirFlag == -1)
                    graph.addEdge(cur, prev, weightSec);
                else
                    graph.addUndirectedEdge(prev, cur, weightSec);
            }
            prev = cur;
        }
    }

    private static String getStringAttr(SimpleFeature f, String name) {
        try {
            Object v = f.getAttribute(name);
            return v == null ? null : String.valueOf(v);
        } catch (Exception ignore) {
            return null;
        }
    }

    private static boolean isDrivable(String fclass, String access) {
        String cls = s(fclass);
        String acc = s(access);
        if ("no".equals(acc) || "private".equals(acc))
            return false;

        if (in(cls, "footway", "pedestrian", "steps", "path", "cycleway", "bridleway"))
            return false;
        return true;
    }

    private static double defaultSpeedForClass(String fclass, String surface) {
        String cls = s(fclass);
        if (in(cls, "motorway"))
            return 90;
        if (in(cls, "trunk"))
            return 80;
        if (in(cls, "primary"))
            return 70;
        if (in(cls, "secondary"))
            return 60;
        if (in(cls, "tertiary"))
            return 50;
        if (in(cls, "residential"))
            return 30;
        if (in(cls, "living_street"))
            return 20;
        if (in(cls, "service"))
            return 20;
        if (in(cls, "unclassified"))
            return 40;
        if (in(cls, "track"))
            return 25;
        return 40;
    }

    private static double parseMaxSpeedKmh(String raw) {
        if (raw == null)
            return Double.NaN;
        String s = raw.trim().toLowerCase();
        try {

            return Double.parseDouble(s.replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    private static boolean in(String val, String... opts) {
        for (String o : opts)
            if (o.equals(val))
                return true;
        return false;
    }

    private static String s(String v) {
        return v == null ? "" : v.trim().toLowerCase();
    }

    private static java.net.URL toUrl(File f) throws MalformedURLException {
        return f.toURI().toURL();
    }
}
