import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by ChristineZheng on 4/10/16.
 */

public class QuadTree {
    private Node root;
    double queryBoxPerPixel;

    public QuadTree() {
        root = new Node(0, MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON, MapServer.ROOT_LRLAT,
                MapServer.ROOT_LRLON, 0);
    }

    public ArrayList<Node> traversal(
            double qbPerPixel, double upLeftLon, double upLeftLat, double lowRightLon,
            double lowRightLat, HashSet<Double> lon, HashSet<Double> lat) {
        ArrayList<Node> tiles = new ArrayList<>();
        queryBoxPerPixel = qbPerPixel;
        tiles = traversalHelper(root, upLeftLon, upLeftLat, lowRightLon,
                lowRightLat, tiles, lon, lat);
        Collections.sort(tiles);
        return tiles;
    }

    private ArrayList<Node> traversalHelper(
            Node cur, double upLeftLon, double upLeftLat, double lowRightLon, double lowRightLat,
            ArrayList<Node> tiles, HashSet<Double> lon, HashSet<Double> lat) {

        double p1 = cur.ullon();
        double p2 = cur.ullat();
        double p3 = cur.lrlon();
        double p4 = cur.lrlat();
        double tilePerPixel = (cur.lrlon() - cur.ullon()) / 256;

        if (cur.level() < 8) {

            if (!(p1 >= lowRightLon || p2 <= lowRightLat || p3 <= upLeftLon || p4 >= upLeftLat)) {
                if (!(tilePerPixel <= queryBoxPerPixel) && cur.level() < 7) {
                    traversalHelper(cur.upLeft(), upLeftLon, upLeftLat,
                            lowRightLon, lowRightLat, tiles, lon, lat);
                    traversalHelper(cur.upRight(), upLeftLon, upLeftLat,
                            lowRightLon, lowRightLat, tiles, lon, lat);
                    traversalHelper(cur.lowerLeft(), upLeftLon, upLeftLat,
                            lowRightLon, lowRightLat, tiles, lon, lat);
                    traversalHelper(cur.lowerRight(), upLeftLon, upLeftLat,
                            lowRightLon, lowRightLat, tiles, lon, lat);

                } else {
                    tiles.add(cur);
                    lon.add(cur.ullon());
                    lat.add(cur.ullat());
                }
            }
        }
        return tiles;
    }
}
