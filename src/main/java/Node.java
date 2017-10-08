/**
 * Created by ChristineZheng on 4/10/16.
 */
public class Node implements Comparable<Node> {
    private long fileName;
    private double ullat, ullon, lrlat, lrlon;
    private Node upLeft, upRight, lowerLeft, lowerRight;
    private int level;

    public Node(long fileName, double ullat, double ullon, double lrlat, double lrlon, int level) {
        this.fileName = fileName;
        this.ullat = ullat;
        this.ullon = ullon;
        this.lrlat = lrlat;
        this.lrlon = lrlon;
        this.level = level;

        if (this.level < 7) {
            this.upLeft = new Node(this.fileName * 10 + 1, this.ullat, this.ullon,
                    (this.ullat + this.lrlat) / 2, (this.ullon + this.lrlon) / 2, this.level + 1);
            this.upRight = new Node(this.fileName * 10 + 2, this.ullat,
                    (this.ullon + this.lrlon) / 2, (this.ullat + this.lrlat) / 2,
                    this.lrlon, this.level + 1);
            this.lowerLeft = new Node(this.fileName * 10 + 3, (this.ullat + this.lrlat) / 2,
                    this.ullon, this.lrlat, (this.ullon + this.lrlon) / 2, this.level + 1);
            this.lowerRight = new Node(this.fileName * 10 + 4, (this.ullat + this.lrlat) / 2,
                    (this.ullon + this.lrlon) / 2, this.lrlat, this.lrlon, this.level + 1);
        }
    }
    // get
    public long fileName() {
        return this.fileName;
    }
    public double ullat() {
        return this.ullat;
    }
    public double ullon() {
        return this.ullon;
    }
    public double lrlat() {
        return this.lrlat;
    }
    public double lrlon() {
        return this.lrlon;
    }
    public Node upLeft() {
        return this.upLeft;
    }
    public Node upRight() {
        return this.upRight;
    }
    public Node lowerLeft() {
        return this.lowerLeft;
    }
    public Node lowerRight() {
        return this.lowerRight;
    }
    public int level() {
        return this.level;
    }


    @Override
    public int compareTo(Node o) {
        if (this.ullon == o.ullon && this.ullat == o.ullat) {
            return 0;
        } else if (this.ullon < o.ullon && this.ullat == o.ullat
                || this.ullon == o.ullon && this.ullat > o.ullat || this.ullat > o.ullat) {
            return -1;
        } else {
            return 1;
        }
    }
/*
    @Override
    public String toString() {
        return "Node{" +
                "fileName=" + fileName +
                ", ullat=" + ullat +
                ", ullon=" + ullon +
                ", lrlat=" + lrlat +
                ", lrlon=" + lrlon +
                ", upLeft=" + upLeft +
                ", upRight=" + upRight +
                ", lowerLeft=" + lowerLeft +
                ", lowerRight=" + lowerRight +
                ", level=" + level +
                '}';
    }
*/
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Node node = (Node) o;

        if (fileName != node.fileName) {
            return false;
        }
        if (Double.compare(node.ullat, ullat) != 0) {
            return false;
        }
        if (Double.compare(node.ullon, ullon) != 0) {
            return false;
        }
        if (Double.compare(node.lrlat, lrlat) != 0) {
            return false;
        }
        if (Double.compare(node.lrlon, lrlon) != 0) {
            return false;
        }
        if (level != node.level) {
            return false;
        }
        if (upLeft != null ? !upLeft.equals(node.upLeft) : node.upLeft != null) {
            return false;
        }
        if (upRight != null ? !upRight.equals(node.upRight) : node.upRight != null) {
            return false;
        }
        if (lowerLeft != null ? !lowerLeft.equals(node.lowerLeft) : node.lowerLeft != null) {
            return false;
        }
        return lowerRight != null ? lowerRight.equals(node.lowerRight) : node.lowerRight == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (fileName ^ (fileName >>> 32));
        temp = Double.doubleToLongBits(ullat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ullon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lrlat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lrlon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (upLeft != null ? upLeft.hashCode() : 0);
        result = 31 * result + (upRight != null ? upRight.hashCode() : 0);
        result = 31 * result + (lowerLeft != null ? lowerLeft.hashCode() : 0);
        result = 31 * result + (lowerRight != null ? lowerRight.hashCode() : 0);
        result = 31 * result + level;
        return result;
    }
}
