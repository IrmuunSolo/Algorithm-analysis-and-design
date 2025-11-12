package com.example.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import com.example.data.ShapefileRoadGraphLoader;
import com.example.graph.Graph;
import com.example.graph.Node;
import com.example.Algorithms.BFS;
import com.example.Algorithms.DFS;
import com.example.Algorithms.Dijkstra;
import com.example.util.GeoUtils;

public class UBMapFrame extends JFrame {
    private final JXMapViewer map = new JXMapViewer();
    private final JLabel status = new JLabel("Ready");
    private final JLabel result = new JLabel(" ");
    private final JButton loadBtn = new JButton("Load UB Roads");
    private final JButton clearBtn = new JButton("Clear");
    private final JButton routeBtn = new JButton("Route");
    private final JComboBox<String> algoSelect = new JComboBox<>(new String[] { "Dijkstra", "DFS", "BFS" });

    private final JTextField algoBox = createInfoBox();
    private final JTextField nodesBox = createInfoBox();
    private final JTextField distBox = createInfoBox();
    private final JTextField timeBox = createInfoBox();
    private final JTextField msBox = createInfoBox();
    private final JTextField memBox = createInfoBox();

    private Graph graph;
    private Node startNode;
    private Node endNode;

    private RoutePainter routePainter;
    private PointsPainter pointsPainter = new PointsPainter();

    private static final double MIN_LON = 106.6;
    private static final double MAX_LON = 107.2;
    private static final double MIN_LAT = 47.7;
    private static final double MAX_LAT = 48.2;

    private static final int MIN_ZOOM = 0;
    private static final int MAX_ZOOM = 12;

    private static final File DEFAULT_SHP = new File("mapdata/gis_osm_roads_free_1.shp");

    public UBMapFrame() {
        super("UB Route Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1100, 800));

        add(map, BorderLayout.CENTER);

        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(8, 8, 8, 8));
        side.setPreferredSize(new Dimension(300, 0));

        JLabel algoLbl = new JLabel("Algorithm:");
        for (javax.swing.JComponent jc : new javax.swing.JComponent[] { loadBtn, algoLbl, algoSelect, routeBtn,
                clearBtn, status, result, algoBox, nodesBox, distBox, timeBox, msBox, memBox }) {
            jc.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        }

        result.setFont(result.getFont().deriveFont(Font.BOLD, 14f));
        result.setHorizontalAlignment(SwingConstants.CENTER);

        algoSelect.setMaximumRowCount(4);

        side.add(loadBtn);
        side.add(Box.createVerticalStrut(10));
        side.add(algoLbl);
        side.add(Box.createVerticalStrut(4));

        algoSelect.setPrototypeDisplayValue("Dijkstra");
        algoSelect.setPreferredSize(new Dimension(160, 28));
        algoSelect.setMaximumSize(new Dimension(200, 28));
        algoSelect.setMinimumSize(new Dimension(120, 28));
        side.add(algoSelect);
        side.add(Box.createVerticalStrut(12));
        side.add(makeLabeledBox("Алгоритм:", algoBox));
        side.add(Box.createVerticalStrut(8));
        side.add(makeLabeledBox("Дамжсан зангилаа:", nodesBox));
        side.add(Box.createVerticalStrut(8));
        side.add(makeLabeledBox("Замын урт:", distBox));
        side.add(Box.createVerticalStrut(8));
        side.add(makeLabeledBox("Туулах хугацаа:", timeBox));
        side.add(Box.createVerticalStrut(8));
        side.add(makeLabeledBox("Гүйцэтгэлийн хугацаа:", msBox));
        side.add(Box.createVerticalStrut(8));
        side.add(makeLabeledBox("Санах ой:", memBox));
        side.add(Box.createVerticalStrut(12));
        side.add(routeBtn);
        side.add(Box.createVerticalStrut(6));
        side.add(clearBtn);
        side.add(Box.createVerticalGlue());
        side.add(status);
        side.add(Box.createVerticalStrut(6));

        add(side, BorderLayout.EAST);

        try {
            System.setProperty("http.agent", "ubmaproute/1.0 (UB route planner)");
        } catch (Throwable ignore) {
        }
        TileFactoryInfo info = new HttpsOsmTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(4);
        map.setTileFactory(tileFactory);

        GeoPosition ub = new GeoPosition(47.9185, 106.9176);
        map.setZoom(8);
        map.setAddressLocation(ub);

        installHandlers();

        updateOverlay();
        pack();
        setLocationRelativeTo(null);
    }

    private GraphPainter roadsPainter;

    private void updateOverlay() {
        List<Painter<JXMapViewer>> painters = new ArrayList<>();
        if (roadsPainter != null)
            painters.add(roadsPainter);
        painters.add(pointsPainter);
        if (routePainter != null)
            painters.add(routePainter);
        map.setOverlayPainter(new CompoundPainter<>(painters));
        map.repaint();
    }

    private void installHandlers() {
        loadBtn.addActionListener(this::onLoad);
        clearBtn.addActionListener(e -> clearAll());
        routeBtn.addActionListener(e -> computeRoute());

        PanMouseInputListener pan = new PanMouseInputListener(map);
        map.addMouseListener(pan);
        map.addMouseMotionListener(pan);

        map.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                clampCenter();
            }
        });

        map.addMouseWheelListener((MouseWheelEvent e) -> {
            int zoom = map.getZoom();
            int rot = e.getWheelRotation();
            int newZoom = zoom + (rot > 0 ? 1 : -1);
            if (newZoom < MIN_ZOOM)
                newZoom = MIN_ZOOM;
            if (newZoom > MAX_ZOOM)
                newZoom = MAX_ZOOM;
            if (newZoom != zoom) {
                map.setZoom(newZoom);
                map.setAddressLocation(map.convertPointToGeoPosition(e.getPoint()));
                clampCenter();
            }
        });

        map.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (graph == null) {
                    setStatus("Эхлээд замаа уншуулна уу!.");
                    return;
                }
                if (!SwingUtilities.isLeftMouseButton(e))
                    return;
                GeoPosition pos = map.convertPointToGeoPosition(e.getPoint());

                pos = clampToBounds(pos);
                if (pos == null)
                    return;

                Node nearest = graph.findNearestNode(pos.getLatitude(), pos.getLongitude());
                if (nearest == null) {
                    setStatus("Ойрхон зангилаа олсонгүй.");
                    return;
                }

                if (startNode == null || (startNode != null && endNode != null)) {
                    startNode = nearest;
                    endNode = null;
                    routePainter = null;
                    pointsPainter.setStart(startNode);
                    pointsPainter.setEnd(null);
                    setStatus("Эхлэх цэг: " + fmtCoord(startNode));
                } else {
                    endNode = nearest;
                    pointsPainter.setEnd(endNode);
                    setStatus("Эцсийн цэг: " + fmtCoord(endNode));
                }
                updateOverlay();
            }
        });
    }

    private void onLoad(ActionEvent e) {
        try {
            setStatus("Замуудыг уншиж байна ...");
            ShapefileRoadGraphLoader loader = new ShapefileRoadGraphLoader(DEFAULT_SHP,
                    MIN_LON, MIN_LAT, MAX_LON, MAX_LAT);
            this.graph = loader.loadGraph();
            System.out.println("Create graph: Nodes=" + graph.getNodeCount() + ", Edges=" + graph.getEdgeCount());
            setStatus("Уншсан зангилаа=" + graph.getNodeCount() + ", Ирмэг=" + graph.getEdgeCount());

            this.roadsPainter = new GraphPainter(graph);
            updateOverlay();
        } catch (Exception ex) {
            ex.printStackTrace();
            setStatus("Зам уншихад алдаа гарлаа: " + ex.getMessage());
        }
    }

    private void computeRoute() {
        if (graph == null) {
            setStatus("Эхлээд замаа уншуулна уу!.");
            return;
        }
        if (startNode == null || endNode == null) {
            setStatus("Эхлэх болон хүрэх цэгээ сонго.");
            return;
        }
        try {
            List<Node> path;
            String algo = Objects.toString(algoSelect.getSelectedItem(), "Dijkstra");
            System.gc();
            long memBefore = usedMemory();
            long t0 = System.currentTimeMillis();
            switch (algo) {
                case "DFS":
                    path = new DFS(graph).path(startNode, endNode);
                    break;
                case "BFS":
                    path = new BFS(graph).shortestPath(startNode, endNode);
                    break;
                default:
                    path = new Dijkstra(graph).shortestPath(startNode, endNode);
            }
            long dt = System.currentTimeMillis() - t0;
            long memAfter = usedMemory();
            double memMB = Math.max(0, memAfter - memBefore) / (1024.0 * 1024.0);

            if (path == null || path.isEmpty()) {
                setStatus("Зам олдсонгүй.");
                return;
            }

            drawPath(path);
            double meters = GeoUtils.pathLengthMeters(path);
            double secs = graph.pathWeightSeconds(path);
            String summary = String.format(
                    "Алгоритм: %s | Зангилаа: %,dnodes | Замын урт: %.2fkm | Гүйцэтгэлийн Хугацаа: %.1fmin | Тооцоо: %dms | Санах ой: %.2fMB",
                    algo.toLowerCase(), path.size(), meters / 1000.0, secs / 60.0, dt, memMB);

            algoBox.setText(algo);
            nodesBox.setText(String.format("%dnodes", path.size()));
            distBox.setText(String.format("%.2fkm", meters / 1000.0));
            timeBox.setText(String.format("%.1fmin", secs / 60.0));
            msBox.setText(String.format("%dms", dt));
            memBox.setText(String.format("%.2fMB", memMB));
            setStatus("Замыг тооцолсон");
        } catch (Exception ex) {
            ex.printStackTrace();
            setStatus("Замчлал амжилтгүй: " + ex.getMessage());
        }
    }

    private void drawPath(List<Node> path) {
        List<GeoPosition> coords = new ArrayList<>();
        for (Node n : path)
            coords.add(new GeoPosition(n.lat, n.lon));
        routePainter = new RoutePainter(coords, Color.BLUE, 3f);
        updateOverlay();
    }

    private void clearAll() {
        routePainter = null;
        startNode = endNode = null;
        pointsPainter.setStart(null);
        pointsPainter.setEnd(null);
        algoBox.setText("");
        nodesBox.setText("");
        distBox.setText("");
        timeBox.setText("");
        msBox.setText("");
        memBox.setText("");
        setStatus("Cleared.");
        updateOverlay();
    }

    private String fmtCoord(Node n) {
        return String.format("(%.5f, %.5f)", n.lat, n.lon);
    }

    private void setStatus(String s) {
        status.setText(s);
    }

    private void setResult(String s) {
        result.setText(s);
    }

    private static long usedMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

    private void clampCenter() {

        Point centerPixel = new Point(map.getWidth() / 2, map.getHeight() / 2);
        GeoPosition center = map.convertPointToGeoPosition(centerPixel);
        GeoPosition clamped = clampToBounds(center);
        if (!center.equals(clamped)) {
            map.setAddressLocation(clamped);
        }
    }

    private GeoPosition clampToBounds(GeoPosition p) {
        if (p == null)
            return null;
        double lat = p.getLatitude();
        double lon = p.getLongitude();
        if (lat < MIN_LAT)
            lat = MIN_LAT;
        if (lat > MAX_LAT)
            lat = MAX_LAT;
        if (lon < MIN_LON)
            lon = MIN_LON;
        if (lon > MAX_LON)
            lon = MAX_LON;
        return new GeoPosition(lat, lon);
    }

    private static JTextField createInfoBox() {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        tf.setHorizontalAlignment(SwingConstants.CENTER);
        tf.setBorder(new LineBorder(new Color(180, 180, 180)));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return tf;
    }

    private static JPanel makeLabeledBox(String label, JTextField field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel l = new JLabel(label);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(l);
        p.add(Box.createVerticalStrut(4));
        p.add(field);
        return p;
    }

    private static class HttpsOsmTileFactoryInfo extends OSMTileFactoryInfo {
        @Override
        public String getTileUrl(int x, int y, int zoom) {
            String url = super.getTileUrl(x, y, zoom);
            if (url != null && url.startsWith("http://")) {
                return "https://" + url.substring("http://".length());
            }
            return url;
        }
    }

    private static class GraphPainter implements Painter<JXMapViewer> {
        private final com.example.graph.Graph g;

        GraphPainter(com.example.graph.Graph g) {
            this.g = g;
        }

        @Override
        public void paint(Graphics2D g2, JXMapViewer map, int w, int h) {
            if (g == null)
                return;
            Graphics2D g = (Graphics2D) g2.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(80, 80, 80));
            g.setStroke(new BasicStroke(1f));
            Rectangle rect = map.getViewportBounds();
            g.translate(-rect.x, -rect.y);
            for (com.example.graph.Node n : this.g.getNodes()) {
                for (com.example.graph.Edge e : this.g.edgesOf(n)) {

                    if (e.from.id < e.to.id) {
                        GeoPosition a = new GeoPosition(e.from.lat, e.from.lon);
                        GeoPosition b = new GeoPosition(e.to.lat, e.to.lon);
                        Point2D pa = map.getTileFactory().geoToPixel(a, map.getZoom());
                        Point2D pb = map.getTileFactory().geoToPixel(b, map.getZoom());
                        g.drawLine((int) pa.getX(), (int) pa.getY(), (int) pb.getX(), (int) pb.getY());
                    }
                }
            }
            g.dispose();
        }
    }

    private static class RoutePainter implements Painter<JXMapViewer> {
        private final List<GeoPosition> track;
        private final Color color;
        private final float width;

        RoutePainter(List<GeoPosition> track, Color color, float width) {
            this.track = track;
            this.color = color;
            this.width = width;
        }

        @Override
        public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
            if (track == null || track.size() < 2)
                return;
            g = (Graphics2D) g.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(color);
            g.setStroke(new BasicStroke(width));
            Rectangle rect = map.getViewportBounds();
            g.translate(-rect.x, -rect.y);
            Point2D prev = null;
            for (GeoPosition gp : track) {
                Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
                if (prev != null)
                    g.drawLine((int) prev.getX(), (int) prev.getY(), (int) pt.getX(), (int) pt.getY());
                prev = pt;
            }
            g.dispose();
        }
    }

    private static class PointsPainter implements Painter<JXMapViewer> {
        private Node start, end;

        void setStart(Node n) {
            this.start = n;
        }

        void setEnd(Node n) {
            this.end = n;
        }

        @Override
        public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
            g = (Graphics2D) g.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Rectangle rect = map.getViewportBounds();
            g.translate(-rect.x, -rect.y);
            if (start != null)
                drawPoint(g, map, start, Color.GREEN);
            if (end != null)
                drawPoint(g, map, end, Color.RED);
            g.dispose();
        }

        private void drawPoint(Graphics2D g, JXMapViewer map, Node n, Color c) {
            GeoPosition gp = new GeoPosition(n.lat, n.lon);
            Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
            int r = 6;
            g.setColor(c);
            g.fillOval((int) pt.getX() - r, (int) pt.getY() - r, r * 2, r * 2);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1f));
            g.drawOval((int) pt.getX() - r, (int) pt.getY() - r, r * 2, r * 2);
        }
    }
}
