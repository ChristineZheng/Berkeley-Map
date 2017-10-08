import java.util.HashSet;
import java.util.Set;

/**
 * Created by ChristineZheng on 4/13/16.
 */
public class MapNode implements Comparable<MapNode> {
    private String id;
    private String lat;
    private String lon;
    private Set<MapNode> connectionSet;
    private MapNode pre;
    private double prior;
    private double distSoFar;


    public MapNode(String id, String lat, String lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.connectionSet = new HashSet<>();
    }
    // get
    public String iD() {
        return this.id;
    }
    public String lat() {
        return this.lat;
    }
    public String lon() {
        return this.lon;
    }
    public Set<MapNode> adjacent() {
        return this.connectionSet;
    }
    public MapNode previous() {
        return this.pre;
    }
    public double prior() {
        return this.prior;
    }
    public double distSoFar() {
        return this.distSoFar;
    }

    // set
    public void setID(String x) {
        this.id = x;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }
    public void setConnecionSet(Set<MapNode> x) {
        this.connectionSet = x;
    }
    public Set<MapNode> connection() {
        return this.connectionSet;
    }

    public void connect(MapNode o) {
        this.connectionSet.add(o);
    }
    public void setThree(MapNode p, double pri, double dist) {
        this.pre = p;
        this.prior = pri;
        this.distSoFar = dist;
    }

    public void setPrevious(MapNode o) {
        this.pre = o;
    }

    public void setPrior(double p) {
        this.prior = p;
    }

    public void setDistSoFar(double d) {
        this.distSoFar = d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MapNode mapNode = (MapNode) o;

        if (!id.equals(mapNode.id)) {
            return false;
        }
        if (!lat.equals(mapNode.lat)) {
            return false;
        }
        if (!lon.equals(mapNode.lon)) {
            return false;
        }
        return connectionSet.equals(mapNode.connectionSet);

    }

    @Override
    public int compareTo(MapNode o) {
        if (this.prior < o.prior) {
            return -1;
        } else if (this.prior == o.prior) {
            return 0;
        } else {
            return 1;
        }
    }


    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + lat.hashCode();
        result = 31 * result + lon.hashCode();
        //result = 31 * result + connectionSet.hashCode();
        return result;
    }

}
