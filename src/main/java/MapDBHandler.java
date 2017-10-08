import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parses OSM XML files using an XML SAX parser. Used to construct the graph of roads for
 * pathfinding, under some constraints.
 * See OSM documentation on
 * <a href="http://wiki.openstreetmap.org/wiki/Key:highway">the highway tag</a>,
 * <a href="http://wiki.openstreetmap.org/wiki/Way">the way XML element</a>,
 * <a href="http://wiki.openstreetmap.org/wiki/Node">the node XML element</a>,
 * and the java
 * <a href="https://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html">SAX parser tutorial</a>.
 *
 * @author Alan Yao
 */
public class MapDBHandler extends DefaultHandler {
    /**
     * Only allow for non-service roads; this prevents going on pedestrian streets as much as
     * possible. Note that in Berkeley, many of the campus roads are tagged as motor vehicle
     * roads, but in practice we walk all over them with such impunity that we forget cars can
     * actually drive on them.
     */
    private static final Set<String> ALLOWED_HIGHWAY_TYPES = new HashSet<>(Arrays.asList
            ("motorway", "trunk", "primary", "secondary", "tertiary", "unclassified",
                    "residential", "living_street", "motorway_link", "trunk_link", "primary_link",
                    "secondary_link", "tertiary_link"));
    private String activeState = "";
    private final GraphDB g;

    private HashMap<String, MapNode> AllMapNodeL;
    private HashSet<String> wayNodeIDL;
    private MapNode nodeTmp;
    private boolean validHighway;
    private ArrayList<String> idListTemp;

    public MapDBHandler(GraphDB g) {
        this.g = g;
        AllMapNodeL = new HashMap<>();
        wayNodeIDL = new HashSet<>();
        idListTemp = new ArrayList<>();
        validHighway = false;

    }

    /**
     * Called at the beginning of an element. Typically, you will want to handle each element in
     * here, and you may want to track the parent element.
     *
     * @param uri        The Namespace URI, or the empty string if the element has no Namespace
     *                   URI or if Namespace processing is not being performed.
     *
     * @param localName  The local name (without prefix), or the empty string if Namespace
     *                   processing is not being performed.
     * @param qName      The qualified name (with prefix), or the empty string if qualified
     *                   names are not available. This tells us which element we're looking at.
     *
     * @param attributes The attributes attached to the element. If there are no attributes, it
     *                   shall be an empty Attributes object.
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     * @see Attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        /* Some example code on how you might begin to parse XML files. */

        // store all nodes in linklist
        if (qName.equals("node")) {
            activeState = "node";
            String id = attributes.getValue("id");
            nodeTmp = new MapNode(id, attributes.getValue("lat"), attributes.getValue("lon"));
            AllMapNodeL.put(id, nodeTmp);

        } else if (qName.equals("way")) {
            activeState = "way";
            //System.out.println("Beginning a way...");

        } else if (activeState.equals("way") && qName.equals("tag")) {
            String k = attributes.getValue("k");
            String v = attributes.getValue("v");

            // if valid highway type, store the ids in hashSet
            if (k.equals("highway") && ALLOWED_HIGHWAY_TYPES.contains(v)) {
                validHighway = true;
            }

            // if belongs to a way, store in arrayList
        } else if (activeState.equals("way") && qName.equals("nd")) {
            idListTemp.add(attributes.getValue("ref"));

        } else if (activeState.equals("node") && qName.equals("tag") && attributes.getValue("k")
                .equals("name")) {
            int a = 1;     // this doesn't do anything, for style check
        }
    }

    /**
     * Receive notification of the end of an element. You may want to take specific terminating
     * actions here, like finalizing vertices or edges found.
     *
     * @param uri       The Namespace URI, or the empty string if the element has no Namespace
     *                  URI or if Namespace processing is not being performed.
     *
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName     The qualified name (with prefix), or the empty string if qualified
     *                  names are not available.
     *
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("way")) {

            if (!validHighway) {
                idListTemp.clear();
            } else {
                int length = idListTemp.size();
                for (int i = 0; i < length; i++) {
                    String curID = idListTemp.get(i);
                    wayNodeIDL.add(curID);
                    if (i - 1 >= 0) {
                        //AllMapNodeL.get(curID).connect(AllMapNodeL.get(idListTemp.get(i - 1)));
                        AllMapNodeL.get(curID).connect(AllMapNodeL.get(idListTemp.get(i - 1)));
                    }
                    if (i + 1 < length) {
                        //AllMapNodeL.get(curID).connect(AllMapNodeL.get(idListTemp.get(i + 1)));
                        AllMapNodeL.get(curID).connect(AllMapNodeL.get(idListTemp.get(i + 1)));
                    }
                }
                validHighway = false;
                idListTemp.clear();
            }
            // There should be 130462 Nodes before cleaning, and 28654 after.
        }
    }

    public void setAllMapNodeL(HashMap<String, MapNode> x) {
        this.AllMapNodeL = x;
    }

    public HashMap<String, MapNode> allNodes() {
        return AllMapNodeL;
    }

    public HashSet<String> validNodes() {
        return wayNodeIDL;
    }

    public double distance(MapNode n1, double lon, double lat) {
        double X = (Double.parseDouble(n1.lon()) - lon);
        double Y = (Double.parseDouble(n1.lat()) - lat);
        return Math.sqrt(X * X + Y * Y);
    }

    public MapNode closest(HashMap<String, MapNode> map, double clickLon, double clickLat) {
        double tempDist;
        double shortestDist = Double.POSITIVE_INFINITY;
        MapNode tempNode;
        MapNode closestNode = new MapNode("1", "1", "1");

        for (String id : map.keySet()) {
            tempNode = map.get(id);
            tempDist = distance(tempNode, clickLon, clickLat);
            if (tempDist == 0) {
                closestNode = tempNode;
                return closestNode;
            }
            if (tempDist <= shortestDist) {
                shortestDist = tempDist;
                closestNode = tempNode;
            }
        }
        return closestNode;
    }
}
