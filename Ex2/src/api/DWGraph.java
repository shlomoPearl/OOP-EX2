package api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DWGraph implements DirectedWeightedGraph {

    private int node_size = 0;
    private int edge_size = 0;

    private HashMap<Integer, NodeData> nodes = new HashMap<>();
    private HashMap<String, EdgeData> edges = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, EdgeData>> edges_from_node = new HashMap<>();

    //Iterator<Map.Entry<Integer, Edge>> edge_iterator =


    public DWGraph() {
    }

    private String tuple(int src, int dest) {
        return "(" + src + ", " + dest + ")"; //+ ", "+w
    }

    @Override
    public NodeData getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        return edges.get(tuple(src, dest));
    }

    @Override
    public void addNode(NodeData n) {
        nodes.put(n.getKey(), (Node) n);
    }

    @Override
    public void connect(int src, int dest, double w) {
        Edge edge = new Edge(src, dest, w);
        String tuple = tuple(src, dest);
        if (!edges.containsKey(tuple)) {
            edges.put(tuple, edge);
            if (!edges_from_node.containsKey(src)) {
                edges_from_node.put(src, new HashMap<Integer, EdgeData>());
            }
            edges_from_node.get(src).put(dest, edge);
            edge_size++;
        }
    }

    @Override
    public Iterator<NodeData> nodeIter() {

        Iterator<NodeData> node_iterator = nodes.values().iterator();
        return node_iterator;
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        Iterator<EdgeData> edge_iterator = edges.values().iterator();
        return edge_iterator;
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        HashMap hm = edges_from_node.get(node_id);
        Iterator<EdgeData> ne_Iterator = hm.values().iterator();
        return ne_Iterator;
    }

    @Override
    public NodeData removeNode(int key) {

        return null;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        String tuple = tuple(src, dest);
        if (edges.containsKey(tuple)) {
            EdgeData e = edges.remove(tuple);
            edges_from_node.get(src).remove(tuple);
            return e;
        }
        return null;
    }

    @Override
    public int nodeSize() {
        return 0;
    }

    @Override
    public int edgeSize() {
        return 0;
    }

    @Override
    public int getMC() {
        return 0;
    }
}
