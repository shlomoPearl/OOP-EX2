package api;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

public class DWGraph implements DirectedWeightedGraph {

    private int node_size = 0;
    private int edge_size = 0;
    private int MC = 0;

    private HashMap<Integer, NodeData> nodes = new HashMap<>();
    private HashMap<String, EdgeData> edges = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, EdgeData>> edges_from_node = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, EdgeData>> edges_to_node = new HashMap<>();


    public DWGraph() {}

    // copy constructor
    public DWGraph(DWGraph g) {

        HashMap<Integer, NodeData> nodes_copy = new HashMap<>();
        Iterator<NodeData> node_iterator = g.nodeIter();
        try {
            while (node_iterator.hasNext()) {
                Node current = (Node) node_iterator.next();
                nodes_copy.put(current.getKey(), new Node(current));
            }
        } catch (ConcurrentModificationException e) {
            throw new RuntimeException("Graph has been modified during iteration. Iterator not up to date.");
        }
        HashMap<String, EdgeData> edges_copy = new HashMap<>();
        Iterator<EdgeData> edge_iterator = g.edgeIter();
        try {
            while (edge_iterator.hasNext()) {
                Edge current = (Edge) edge_iterator.next();
                edges_copy.put(tuple(current.getSrc(), current.getDest()), new Edge(current));
            }
        } catch (ConcurrentModificationException e) {
            throw new RuntimeException("Graph has been modified during iteration. Iterator not up to date.");
        }

        HashMap<Integer, HashMap<Integer, EdgeData>> edges_f_n_copy = new HashMap<>();
        Integer[] keys = new Integer[g.edges_from_node.size()];
        g.edges_from_node.keySet().toArray(keys);
        for (Integer key : keys) {
            edges_f_n_copy.put(key, new HashMap<>());
            HashMap<Integer, EdgeData> current = edges_f_n_copy.get(key);
            Integer[] destinations = current.keySet().toArray(new Integer[g.edges_from_node.get(key).size()]);
            for (Integer dest : destinations) {
                if (dest != null) {
                    current.put(dest, new Edge((Edge) g.edges_from_node.get(key).get(dest)));
                }
            }
        }

        HashMap<Integer, HashMap<Integer, EdgeData>> edges_t_n_copy = new HashMap<>();
        Integer[] destinations = new Integer[g.edges_to_node.size()];
        g.edges_to_node.keySet().toArray(destinations);
        for (Integer dest : destinations) {
            edges_t_n_copy.put(dest, new HashMap<>());
            HashMap<Integer, EdgeData> current = edges_t_n_copy.get(dest);
            Integer[] keys2 = current.keySet().toArray(new Integer[g.edges_to_node.get(dest).size()]);
            for (Integer key : keys2) {
                if (key != null) {
                    current.put(key, new Edge((Edge) g.edges_to_node.get(dest).get(key)));
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
        nodes.put(n.getKey(), n);
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
                edges_from_node.put(src, new HashMap<>());
            }
            edges_from_node.get(src).put(dest, edge);

            if (!edges_to_node.containsKey(dest)) {
                edges_to_node.put(dest, new HashMap<>());
            }
            edges_to_node.get(dest).put(src, edge);
            edge_size++;
            MC++;
        }
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return nodes.values().iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return edges.values().iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        HashMap<Integer, EdgeData> hm = edges_from_node.get(node_id);
        if (hm != null) {
            return hm.values().iterator();
        } else {
            return null;
        }
    }

    @Override
    public NodeData removeNode(int key) {
        Iterator<EdgeData> edge_iterator = edgeIter(key);
        if (edges_from_node.containsKey(key)) {
            while (edge_iterator.hasNext()) {
                EdgeData current = edge_iterator.next();
                edges.remove(tuple(current.getSrc(), current.getDest()));
                edge_size--;
                MC++;
            }
            edges_from_node.remove(key);
        }
        if (edges_to_node.containsKey(key)) {
            edge_iterator = edges_to_node.get(key).values().iterator();
            while (edge_iterator.hasNext()) {
                EdgeData current = edge_iterator.next();
                edges.remove(tuple(current.getSrc(), current.getDest()));
                edge_size--;
                MC++;
            }
            edges_to_node.remove(key);
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
            edges_from_node.get(src).remove(dest);
            edges_to_node.get(dest).remove(src);
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

    @Override
    public String toString() {
        return "Nodes: " + nodes.values() + "\nEdges: " + edges.values();
    }
}