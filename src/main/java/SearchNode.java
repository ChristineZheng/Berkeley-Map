/**
 * Created by ChristineZheng on 4/18/16.
 */
public class SearchNode implements Comparable<SearchNode> {
    //private MapNode node;
    private double prior;
    private double distSoFar;
    private SearchNode pre;
    private String id;

    //public SearchNode(SearchNode p, MapNode n, double pr, double d) {
    public SearchNode(SearchNode p, String ids, double pr, double d) {
        this.pre = p;
        //this.node = n;
        this.id = ids;
        this.prior = pr;
        this.distSoFar = d;

    }
    public SearchNode pre() {
        return this.pre;
    }

    //public MapNode node() {
    public String iD() {
        return this.id;
    }
    public double prior() {
        return this.prior;
    }
    public double dist() {
        return this.distSoFar;
    }

    @Override
    public int compareTo(SearchNode o) {
        if (this.prior < o.prior) {
            return -1;
        } else if (this.prior == o.prior) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        //SearchNode mapNode = (SearchNode) o;
        return this.id.equals(((SearchNode) o).id);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id.hashCode();
        temp = Double.doubleToLongBits(prior);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(distSoFar);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + pre.hashCode();
        return result;
    }
}
