package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class GraphAlgorithm implements DirectedWeightedGraphAlgorithms {


    private DirectedWeightedGraph g;


    public GraphAlgorithm() {}

    /**
     * @return message to be displayed in a Runtime exception thrown due to changes made during iteration
     */
    private String graph_change() {
        return "Graph has been modified during iteration. Iterator not up to date.";
    }


    /**
     * @return message to be displayed in a Runtime exception thrown due to changes made during iteration
     * over edges out-going from a specific vertex
     */
    private String edges_from_node_change() {
        return "Out-going edges of a certain node have been" +
                " modified during iteration over them. Iterator not up to date.";
    }


    @Override
    public void init(DirectedWeightedGraph g) {
        this.g = g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.g;
    }

    /**
     * @return deep copy of a directed weighted graph
     */
    @Override
    public DirectedWeightedGraph copy() {
        return new DWGraph((DWGraph) this.g);
    }

    /**
     * recursive implementation of DFS algorithm
     * returns: true if all vertices were visited - otherwise false
     * @param graph - the graph on which the DFS algorithm is to be preformed
     * @param start_node - the vertex to start the search from
     * @return boolean
     */
    private boolean dfs(DWGraph graph, Node start_node) {
        start_node.setTag(1);
        if (graph.edgeIter(start_node.getKey()) != null) {
            Iterator<EdgeData> edgeIter = graph.edgeIter(start_node.getKey());
            try {
                while (edgeIter.hasNext()) {
                    Node next = (Node) graph.getNode(edgeIter.next().getDest());
                    if (next.getTag() != 1) {
                        dfs(graph, next);
                    }
                }
            } catch (ConcurrentModificationException e) {
                throw new RuntimeException(edges_from_node_change());
            }
        }
        boolean result = true;

        Iterator<NodeData> nodeIter = graph.nodeIter();
        try {
            while (nodeIter.hasNext()) {
                Node current = (Node) nodeIter.next();
                result = result && (current.getTag() == 1);
            }
        } catch (ConcurrentModificationException e) {
            throw new RuntimeException(graph_change());
        }
        return result;
    }

    /**
     * a connected directed weighted graph is defined: there exist a path between any two
     * given vertices from the graph.
     * implementation: determines weather or not all vertices were visited via DFS "pass" over the graph.
     * then determines weather or not all vertices were visited via DFS "pass" over the graph after it was transposed
     * (i.e: directions of all edges have been flipped), while beginning the "pass" from the same vertex that the first
     * pass was started from. if in both cases all vertices were visited, the graph is connected.
     * returns: true if the graph is connected and false otherwise.
     * @return boolean
     */
    @Override
    public boolean isConnected() {
        Iterator<NodeData> nodeIter = g.nodeIter();
        try {
            if (nodeIter.hasNext()) {
                Node current = (Node) nodeIter.next();
                int key = current.getKey();
                boolean first_pass = dfs((DWGraph) g, current);
                DWGraph transpose = ((DWGraph) g).transpose();
                Iterator<NodeData> transpose_iter = transpose.nodeIter();
                while (transpose_iter.hasNext()) {
                    transpose_iter.next().setTag(0);
                }
                Node new_current = (Node) transpose.getNode(key);
                boolean second_pass = dfs(transpose, new_current);
                return first_pass && second_pass;
            }
        } catch (ConcurrentModificationException e) {
            throw new RuntimeException(graph_change());
        }
        return true;
    }

    /**
     * in a directed weighted graph the sum of the weights of the edges which construct
     * the shortest path between two vertices, may be referred to as "distance to be travelled" between them.
     * returns: the sum of the weights of the edges, over the shortest path between two vertices.
     * @param src - start node
     * @param dest - end (target) node
     * @return double
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (shortestPath(src, dest) != null) {
            return ((Node) g.getNode(dest)).getInWeight();
        }
        return -1;
    }

    // assists the "shortest path" functions.
    private void initialize_all_to_maxValue(HashMap<Integer, Node> hm, int src) {
        for (Node current : hm.values()) {
            if (current.getKey() != src) {
                current.setInWeight(Double.MAX_VALUE);
            } else {
                current.setInWeight(0);
            }
        }
    }

    /**
     * assists the "shortest path" functions.
     * returns: the key of the node with the smallest in-weight out of the nodes from the given map,
     * if said map is empty - returns -1
     * @param map - a collection of vertices stored in a map (key : value)
     * @return int
     */
    private int node_with_min_weight(HashMap<Integer, Node> map) {
        Node result = new Node(-1, null);
        result.setInWeight(Double.MAX_VALUE);
        if (!map.isEmpty()) {
            for (Node current : map.values()) {
                if (current.getInWeight() < result.getInWeight()) {
                    result = current;
                }
            }
        }
        return result.getKey();
    }

    // assists the "shortest path" functions.

    private HashMap<Integer, Node> create_unCheckedNodes() {

        HashMap<Integer, Node> unCheckedNodes = new HashMap<>();

        Iterator<NodeData> nodeIter = g.nodeIter();
        try {
            while (nodeIter.hasNext()) {
                Node current = (Node) nodeIter.next();
                unCheckedNodes.put(current.getKey(), current);
            }
        } catch (ConcurrentModificationException e) {
            throw new RuntimeException(graph_change());
        }
        return unCheckedNodes;
    }

    /**
     * a certain variation of the well known Dijkstra algorithm, which can be used to find the shortest path between
     * two vertices on a weighted graph.
     * returns: a list of all the vertices on said path, in travelling order.
     * @param src - start node
     * @param dest - end (target) node
     * @return List<NodeData>
     */
    @Override
    public List<NodeData> shortestPath(int src, int dest) {

        HashMap<Integer, Node> unCheckedNodes = create_unCheckedNodes();
        initialize_all_to_maxValue(unCheckedNodes, src);
        LinkedList<NodeData> result = new LinkedList<>();

        if (src == dest) { // the path is from the source to itself
            result.add(g.getNode(src));
            return result;
        }

        while (!unCheckedNodes.isEmpty()) {

            int current_key = node_with_min_weight(unCheckedNodes);
            Node current_node = (Node) g.getNode(current_key);
            unCheckedNodes.remove(current_key);

            if (g.edgeIter(current_key) != null) {
                Iterator<EdgeData> edgeIterator = g.edgeIter(current_key);
                try {
                    while (edgeIterator.hasNext()) {

                        Edge currentEdge = (Edge) edgeIterator.next();

                        Node nextNode = (Node) g.getNode(currentEdge.getDest());
                        Node prevNode = (Node) g.getNode(currentEdge.getSrc());

                        if (currentEdge.getWeight() + current_node.getInWeight() < nextNode.getInWeight()) {
                            // we have found a shorter path to the nexNode, than the last time we encountered it:
                            //set nextNode's new in-weight
                            nextNode.setInWeight(currentEdge.getWeight() + current_node.getInWeight());
                            nextNode.setKeyPrevNode(current_node.getKey()); //update its prevNode variable
                            // (prevNode is for reverse reconstructing the shortest path route)

                            if (nextNode.getKey() == dest) {
                                // we have reached the target
                                result.clear();
                                result.add(nextNode);

                                // reverse reconstruct path
                                while (prevNode.getKey() != src) {
                                    result.addFirst(prevNode);
                                    prevNode = (Node) g.getNode(prevNode.getKeyPrevNode());
                                }
                                result.addFirst(g.getNode(src));
                            }
                        }
                    }
                } catch (ConcurrentModificationException e) {
                    throw new RuntimeException(edges_from_node_change());
                }
            }
        }
        return (!result.isEmpty()) ? result : null;
    }

    /**
     * a void function designed to update the in-weight variables of all vertices in
     * the graph, to their shortest path distance, relative to a given source vertex.
     * @param source - the key of the source vertex
     */
    private void shortestPathsForCenter(int source) {

        HashMap<Integer, Node> unCheckedNodes = create_unCheckedNodes();

        initialize_all_to_maxValue(unCheckedNodes, source);

        while (!unCheckedNodes.isEmpty()) {
            int current_key = node_with_min_weight(unCheckedNodes);
            Node current_node = (Node) g.getNode(current_key);
            unCheckedNodes.remove(current_key);

            if (g.edgeIter(current_key) != null) {
                Iterator<EdgeData> edgeIterator = g.edgeIter(current_key);
                try {
                    while (edgeIterator.hasNext()) {
                        Edge currentEdge = (Edge) edgeIterator.next();
                        Node nextNode = (Node) g.getNode(currentEdge.getDest());

                        if (currentEdge.getWeight() + current_node.getInWeight() < nextNode.getInWeight()) {
                            nextNode.setInWeight(currentEdge.getWeight() + current_node.getInWeight());
                            nextNode.setKeyPrevNode(current_key);
                        }
                    }
                } catch (ConcurrentModificationException e) {
                    throw new RuntimeException(edges_from_node_change());
                }
            }
        }
    }

    /**
     * the center of a directed weighted graph is defined as follows: the vertex which has the smallest maximum
     * distance from all other nodes. where distance is defined to be the shortest path distance.
     * this function calculates and returns said vertex
     * @return NodeData
     */
    @Override
    public NodeData center() {
        NodeData center = null;
        double min_max_dist = Double.MAX_VALUE;
        Iterator<NodeData> nodeIter1 = g.nodeIter();
        try {
            while (nodeIter1.hasNext()) {
                Node current1 = (Node) nodeIter1.next();
                shortestPathsForCenter(current1.getKey());
                Iterator<NodeData> nodeIter2 = g.nodeIter();
                double current_max_dist = Double.MIN_VALUE;
                while (nodeIter2.hasNext()) {
                    Node current2 = (Node) nodeIter2.next();
                    current_max_dist = Math.max(current2.getInWeight(), current_max_dist);
                }
                if (current_max_dist < min_max_dist) {
                    min_max_dist = current_max_dist;
                    center = current1;
                }
            }
        } catch (ConcurrentModificationException e) {
            throw new RuntimeException(graph_change());
        }
        return center;
    }

    /**
     * assists the isConnected func. by returning a pair of vertices out of a given collection, which
     * are the two closest two each other (meaning they are the two with the shortest path distance between
     * them, out of all possible pairs from the given collection).
     * returns: a String representation of the keys of the pair of vertices. Later to be parsed by the isConnected func.
     * @param unCheckedNodes - a list of vertices
     * @return String
     */
    private String chooseStartNodes(List<NodeData> unCheckedNodes) {
        String pair = "";
        double min_distance = Double.MAX_VALUE;
        for (NodeData n : unCheckedNodes) {
            shortestPathsForCenter(n.getKey());
            for (NodeData m : unCheckedNodes) {
                if (n.getKey() == m.getKey()) continue;
                double current_in_weight = ((Node) m).getInWeight();
                if (current_in_weight < min_distance) {
                    min_distance = current_in_weight;
                    pair = n.getKey() + "," + m.getKey();
                }
            }
        }
        return pair;
    }

    /**
     * assists the isConnected func. by returning the vertex, out of a given collection, which
     * has the shortest path distance from a given source vertex.
     * returns: the key (ID) of said vertex.
     * @param unCheckedNodes - a list of vertices
     * @param src - the key of the source vertex
     * @return int
     */
    private int closest_node(List<NodeData> unCheckedNodes, int src) {
        shortestPathsForCenter(src);
        int result = src;
        double min_weight = Double.MAX_VALUE;
        for (NodeData nodeData : unCheckedNodes) {
            if (((Node) nodeData).getInWeight() < min_weight) {
                result = nodeData.getKey();
            }
        }
        return result;
    }

    /**
     * a func. to solve a lenient variation of the well known problem from the field og mathematics
     * and computer sciences: The Travelling Salesman Problem.
     * This is a greedy algorithm which calculates the shortest ("cheapest") route which
     * passes through certain cities (vertices on the graph) from a given list. each city must be visited only
     * once, and travelling through "other" cities (i.e: through vertices which are not from the
     * original given collection) is permitted.
     * returns: a list of vertices, representing the "cheapest" route through all cities from the given
     * collection, in the order in which said vertices were to be passed when travelling on said route.
     * @param cities - a list of vertices representing cities to be visited
     * @return List</NodeData>
     */
    @Override
    public List<NodeData> tsp(List<NodeData> cities) {

        List<NodeData> unCheckedNodes = new LinkedList<>(cities);

        String[] pair = chooseStartNodes(unCheckedNodes).split(",");
        int source = Integer.parseInt(pair[0]);
        int dest = Integer.parseInt(pair[1]);

        List<NodeData> result = new ArrayList<>(shortestPath(source, dest));
        for (NodeData node : result) {
            unCheckedNodes.remove(node);
        }

        while (!unCheckedNodes.isEmpty()) {
            int current_key = closest_node(unCheckedNodes, dest);
            List<NodeData> path = shortestPath(dest, current_key);
            path.remove(0);
            result.addAll(path);
            dest = current_key;
            for (NodeData node : result) {
                unCheckedNodes.remove(node);
            }
        }
        return result;
    }

    /**
     * this func. saves the graph object in pretty JSON format, in a JSON file which
     * is then stored in the directory represented by the given path, under the given file name.
     * returns: true if the save was successful - otherwise false.
     * @param file - the file name (may include a relative path).
     * @return boolean
     */
    @Override
    public boolean save(String file) {
        if (null == g) return false;
        else try {
            FileWriter save = new FileWriter(file);
            Iterator<EdgeData> edgeIter = g.edgeIter();
            JSONObject graph = new JSONObject();
            JSONArray edge_list = new JSONArray();

            while (edgeIter.hasNext()) {
                Edge current = (Edge) edgeIter.next();
                JSONObject edge = new JSONObject();
                edge.put("src", current.getSrc());
                edge.put("w", current.getWeight());
                edge.put("dest", current.getDest());
                edge_list.add(edge);
            }

            graph.put("Edges", edge_list);
            Iterator<NodeData> nodeIter = g.nodeIter();
            JSONArray node_list = new JSONArray();

            while (nodeIter.hasNext()) {
                Node current = (Node) nodeIter.next();
                JSONObject node = new JSONObject();
                String pos = current.getLocation().x() + ", " + current.getLocation().y() + ", " + current.getLocation().z();
                node.put("pos", pos);
                node.put("id", current.getKey());
                node_list.add(node);
                graph.put("Nodes", node_list);
                save.write(graph.toString(4));
                save.close();
            }
        } catch (ConcurrentModificationException e) {
            throw new RuntimeException(graph_change());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * this func. loads the graph object from a JSON file from a given directory,
     * represented by a path String and file name.
     * returns: true if load was successful - otherwise false.
     * @param file - the file name (may include a relative path).
     * @return boolean
     */
    @Override
    public boolean load(String file) {
        DWGraph loaded_graph = new DWGraph();
        try {
            //initiate new graph
            JsonObject graph = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
            JsonArray node_list = (JsonArray) graph.get("Nodes");

            //create nodes from node array in json and add to graph:
            for (JsonElement json_node : node_list) {
                int id = json_node.getAsJsonObject().get("id").getAsInt();
                String[] geo_location = json_node.getAsJsonObject().get("pos").getAsString().split(",");
                double x = Double.parseDouble(geo_location[0]);
                double y = Double.parseDouble(geo_location[1]);
                double z = Double.parseDouble(geo_location[2]);
                loaded_graph.addNode(new Node(id, new Location(x, y, z)));
            }

            JsonArray edge_list = (JsonArray) graph.get("Edges");
            //create edges in graph using info from edges array in json:
            for (JsonElement json_edge : edge_list) {
                int src = json_edge.getAsJsonObject().get("src").getAsInt();
                int dest = json_edge.getAsJsonObject().get("dest").getAsInt();
                double w = json_edge.getAsJsonObject().get("w").getAsDouble();
                loaded_graph.connect(src, dest, w);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        //update g:
        g = loaded_graph;
        return true;
    }
}