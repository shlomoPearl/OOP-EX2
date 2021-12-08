package api;

import com.google.gson.Gson;

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


    public DWGraph() {
    }

    // copy constructor
    public DWGraph(DWGraph g) {

        HashMap<Integer, NodeData> nodes_copy = new HashMap<>();
        Iterator<NodeData> node_iterator = g.nodeIter();
        while (node_iterator.hasNext()) {
            Node current = (Node) node_iterator.next();
            nodes_copy.put(current.getKey(), new Node(current));
        }

        HashMap<String, EdgeData> edges_copy = new HashMap<>();
        Iterator<EdgeData> edge_iterator = g.edgeIter();
        while (edge_iterator.hasNext()) {
            Edge current = (Edge) edge_iterator.next();
            edges_copy.put(tuple(current.getSrc(), current.getDest()), new Edge(current));
        }

        HashMap<Integer, HashMap<Integer, EdgeData>> edges_f_n_copy = new HashMap<>();
        Integer[] keys = g.edges_from_node.keySet().toArray(new Integer[g.edges_from_node.size()]);
        for (Integer key : keys) {
            edges_f_n_copy.put(key, new HashMap<Integer, EdgeData>());
            HashMap<Integer, EdgeData> current = edges_f_n_copy.get(key);
            Integer[] dests = current.keySet().toArray(new Integer[g.edges_from_node.get(key).size()]);
            for (Integer dest : dests) {
                if (dest != null) {
                    current.put((int) dest, new Edge((Edge) g.edges_from_node.get(key).get(dest)));
                }
            }
        }

        HashMap<Integer, HashMap<Integer, EdgeData>> edges_t_n_copy = new HashMap<>();
        Integer[] dests = g.edges_to_node.keySet().toArray(new Integer[g.edges_to_node.size()]);
        for (Integer dest : dests) {
            edges_t_n_copy.put(dest, new HashMap<Integer, EdgeData>());
            HashMap<Integer, EdgeData> current = edges_t_n_copy.get(dest);
            Integer[] keys2 = current.keySet().toArray(new Integer[g.edges_to_node.get(dest).size()]);
            for (Integer key : keys2) {
                if (key != null) {
                    current.put((int) key, new Edge((Edge) g.edges_to_node.get(dest).get(key)));
                }
            }
        }
        this.nodes = nodes_copy;
        this.edges = edges_copy;
        this.edges_from_node = edges_f_n_copy;
        this.edges_to_node = edges_t_n_copy;

        this.node_size = g.node_size;
        this.edge_size = g.edge_size;
        this.MC = g.MC;
    }


    private String tuple(int src, int dest) {
        return "(" + src + ", " + dest + ")"; //+ ", "+w
    }

    //    public HashMap<Integer, HashMap<Integer, EdgeData>> getEdges_from_node() {
//        return this.edges_from_node;
//    }
    public DWGraph transpose() {
        DWGraph transpose = new DWGraph(this);
        transpose.edges_from_node.clear();
        transpose.edges_to_node.clear();
        transpose.edges.clear();
        Iterator<EdgeData> edgeIter = this.edgeIter();
        while (edgeIter.hasNext()) {
            Edge current = (Edge) edgeIter.next();
            int src = current.getSrc();
            int dest = current.getDest();
            double w = current.getWeight();
            transpose.connect(dest, src, w);
        }
        return transpose;
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
        node_size++;
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
        if (hm != null) {
            Iterator<EdgeData> ne_Iterator = hm.values().iterator();
            return ne_Iterator;
        }else return null;
    }

    @Override
    public NodeData removeNode(int key) {
        Iterator<EdgeData> edge_iterator = edgeIter(key);
        if (edges_from_node.containsKey(key)) {
            while (edge_iterator.hasNext()) {
                EdgeData current = edge_iterator.next();
                edges.remove(current);
                edges_from_node.get(key).remove(current);
                edge_size--;
                MC++;
            }
        }
        if (edges_to_node.containsKey(key)) {
            edge_iterator = edges_to_node.get(key).values().iterator();
            while (edge_iterator.hasNext()) {
                EdgeData current = edge_iterator.next();
                edges.remove(current);
                edges_to_node.get(key).remove(current);
                edge_size--;
                MC++;
            }
        }
        if (nodes.containsKey(key)) {
            node_size--;
            MC++;
        }
        return nodes.remove(key);
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        String tuple = tuple(src, dest);
        if (edges.containsKey(tuple)) {
            EdgeData e = edges.remove(tuple);
            edges_from_node.get(src).remove(tuple);
            edges_to_node.get(dest).remove(tuple);
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
