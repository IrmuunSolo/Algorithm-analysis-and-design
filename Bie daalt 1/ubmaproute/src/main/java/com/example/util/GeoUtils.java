package com.example.util;

import java.util.List;

import com.example.graph.Node;

public class GeoUtils {
    private static final double R = 6371000.0; // meters

    public static double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2)*Math.sin(dLat/2)
                 + Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLon/2)*Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    public static boolean withinBbox(double lon, double lat, double minLon, double minLat, double maxLon, double maxLat) {
        return lon >= minLon && lon <= maxLon && lat >= minLat && lat <= maxLat;
    }

    public static String keyFor(double lat, double lon) {
        return String.format("%.5f,%.5f", lat, lon);
    }

    public static double pathLengthMeters(List<Node> path) {
        if (path == null || path.size() < 2) return 0.0;
        double sum = 0.0;
        for (int i = 1; i < path.size(); i++) {
            Node a = path.get(i-1), b = path.get(i);
            sum += haversineMeters(a.lat, a.lon, b.lat, b.lon);
        }
        return sum;
    }
}

