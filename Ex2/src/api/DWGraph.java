package api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DWGraph implements DirectedWeightedGraph {

    private int node_size = 0;
    private int edge_size = 0;
    private int MC = 0;

    private HashMap<Integer, NodeData> nodes = new HashMap<>();
    private HashMap<String, EdgeData> edges = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, EdgeData>> edges_from_node = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, EdgeData>> edges_to_node = new HashMap<>();


    //Iterator<Map.Entry<Integer, Edge>> edge_iterator =


    public DWGraph() {}
//    public DWGraph(DWGraph g){
//        this.nodes = new HashMap<>(g.nodes);
//        this.edges = new HashMap<>(g.edges);
//        this.edges_from_node = new
//    }

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
        MC++;
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
            if (!edges_to_node.containsKey(dest)) {
                edges_to_node.put(dest, new HashMap<Integer, EdgeData>());
            }
            edges_to_node.get(dest).put(src, edge);
            edge_size++;
            MC++;
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
        if (edges_from_node.containsKey(key)) {
            Iterator<EdgeData> edge_iterator = edgeIter(key);
            while (edge_iterator.hasNext()) {
                EdgeData current = edge_iterator.next();
                edges.remove(current);
                edges_from_node.get(key).remove(current);
                edge_size--;
                MC++;
            }
            edge_iterator = edges_to_node.get(key).values().iterator();
            while (edge_iterator.hasNext()) {
                EdgeData current = edge_iterator.next();
                edges.remove(current);
                edges_to_node.get(key).remove(current);
                edge_size--;
                MC++;
            }
        }
        if (nodes.containsKey(key)) node_size--;
        MC++;
        return nodes.remove(key);
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        String tuple = tuple(src, dest);
        if (edges.containsKey(tuple)) {
            EdgeData e = edges.remove(tuple);
            edges_from_node.get(src).remove(tuple);
            edge_size--;
            MC++;
            return e;
        }
        return null;
    }

    @Override
    public int nodeSize() {
        return this.node_size;
    }

    @Override
    public int edgeSize() {
        return this.edge_size;
    }

    @Override
    public int getMC() {
        return this.MC;
    }
}
