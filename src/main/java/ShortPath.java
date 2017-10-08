import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Set;
import java.util.LinkedList;
import java.util.HashSet;
/**
 * Created by ChristineZheng on 4/15/16.
 */
public class ShortPath {
    PriorityQueue<MapNode> queue;
    MapNode source;
    MapNode target;
    MapNode solutionMapNode;
    Stack<MapNode> path;
    LinkedList<Long> pathID;
    Set<MapNode> visited;
    Set<MapNode> queueSet;

    public ShortPath(MapNode s, MapNode t) {

        queue = new PriorityQueue<>();
        visited = new HashSet<>();
        queueSet = new HashSet<>();
        source = s;
        target = t;
        source.setThree(null, dist(source, target), 0.0);

        queue.offer(source);
        queueSet.add(source);
        boolean firstRound = true;
        boolean done = false;

        while (!done) {
            MapNode remove = queue.poll();
            visited.add(remove);

            if (remove.equals(target)) {
                solutionMapNode = remove;
                done = true;
            } else {
                for (MapNode k : remove.adjacent()) {

                    if (firstRound || !visited.contains(k)) {
                        double distanceSoFar = remove.distSoFar() + dist(remove, k);
                        double newPrior = distanceSoFar + dist(k, target);

                        if (queueSet.contains(k)) {
                            MapNode temp = new MapNode(k.iD(), k.lat(), k.lon());
                            temp.setConnecionSet(k.connection());
                            temp.setThree(remove, newPrior, distanceSoFar);
                            queue.offer(temp);
                            queueSet.add(temp);
                        } else {
                            k.setThree(remove, newPrior, distanceSoFar);
                            queue.offer(k);
                            queueSet.add(k);
                        }
                    }
                }
                firstRound = false;
            }
        }
        path = new Stack<>();
        while (solutionMapNode != null) {
            path.push(solutionMapNode);
            solutionMapNode = solutionMapNode.previous();
        }
        pathID = new LinkedList<>();
        while (!path.isEmpty()) {
            pathID.add(Long.valueOf(path.pop().iD()));
        }
    }
    public double dist(MapNode n1, MapNode n2) {
        double X = (Double.parseDouble(n1.lon()) - Double.parseDouble(n2.lon()));
        double Y = (Double.parseDouble(n1.lat()) - Double.parseDouble(n2.lat()));
        return Math.sqrt(X * X + Y * Y);
    }
    public LinkedList<Long> solutionPathID() {
        return pathID;
    }
}
// have a set store the node that has been visited(removed from queue)
// use hashMap for edgeTo: for each node how you got there
// remove from queue and update the priority
// only when say its done when you remove destination node from queue
// priority = distSoFar + Euclidean
