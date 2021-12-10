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


    public GraphAlgorithm() {
    }

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
     * @return a deep copy of a directed weighted graph
     */
    @Override
    public DirectedWeightedGraph copy() {
        return new DWGraph((DWGraph) this.g);
    }

    /**
     * recursive implementation of DFS algorithm
     *
     * @param graph      - the graph on which the DFS algorithm is to be preformed
     * @param start_node - the vertex to start the search from
     * @return true if all vertices were visited - otherwise false
     */
    private boolean dfs(DWGraph graph, Node start_node) {
        start_node.setTag(1);
        if (graph.edgeIter(start_node.getKey()) != null) {
            Iterator<EdgeData> edgeIter = graph.edgeIter(start_node.getKey());
            try {
                while (edgeIter.hasNext()) {
                    Node next = (Node) graph.getNode(edgeIter.next().getDest());
                    if (next.getTag() != 1) {
                        dfs(graph, next); // recursive call on "non-visited" neighbours
                    }
                }
            } catch (ConcurrentModificationException e) {
                throw new RuntimeException(edges_from_node_change());
            }
        }

        // iterate and change result value to false if there exists a vertex that wasn't visited:
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
     *
     * @return true if the graph is connected and false otherwise.
     */
    @Override
    public boolean isConnected() {
        Iterator<NodeData> nodeIter = g.nodeIter();
        try {
            if (nodeIter.hasNext()) {
                // perform DFS on original graph
                Node current = (Node) nodeIter.next();
                int key = current.getKey();
                boolean first_pass = dfs((DWGraph) g, current); // true: if all vertices were visited.
                                                                // false: otherwise

                DWGraph transpose = ((DWGraph) g).transpose();
                // reset tags for DFS pass on the transposed graph
                Iterator<NodeData> transpose_iter = transpose.nodeIter();
                while (transpose_iter.hasNext()) {
                    transpose_iter.next().setTag(0);
                }

                Node new_current = (Node) transpose.getNode(key); // beginning DFS from same vertex of initial pass
                boolean second_pass = dfs(transpose, new_current); // true: if all vertices were visited.
                                                                    // false: otherwise
                return first_pass && second_pass; // both must be true for the graph to be declared a connected one
            }
        } catch (ConcurrentModificationException e) {
            throw new RuntimeException(graph_change());
        }
        return true;
    }

    /**
     * in a directed weighted graph the sum of the weights of the edges which construct
     * the shortest path between two vertices, may be referred to as "distance to be travelled" between them.
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return The sum of the weights of the edges, over the shortest path between two vertices.
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
     *
     * @param map - a collection of vertices stored in a map (key : value)
     * @return the key of the node with the smallest in-weight out of the nodes from the given map,
     * if said map is empty - returns -1
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
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return a list of all the vertices on said path, in travelling order.
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
     * a function designed to update the in-weight variables of all vertices in
     * the graph, to their shortest path distance, relative to a given source vertex,
     * and then returns the largest of said in-weight variables.
     *
     * @param source - the key of the source vertex
     * @return The largest in-weight (distance from source) out of all the vertices,
     * relative to the source vertex.
     */
    private double shortestPathsForCenter(int source) {

        HashMap<Integer, Node> unCheckedNodes = create_unCheckedNodes();
        initialize_all_to_maxValue(unCheckedNodes, source); // this func. initializes the source vertex in-weight to 0,
                                                            // while all the other in weights are initialized to +infinity
        double max_shortest_path = Double.MIN_VALUE;
        try {
            while (!unCheckedNodes.isEmpty()) {
                // in first iteration: current_key will be the source
                // in the second iteration: current_key will be the sources neighbour with the smallest in-weight
                // ans so on..
                int current_key = node_with_min_weight(unCheckedNodes);
                Node current_node = (Node) g.getNode(current_key);
                unCheckedNodes.remove(current_key);

                if (g.edgeIter(current_key) != null) {
                    // iterating over neighbours using the out-going edges map:
                    Iterator<EdgeData> edgeIterator = g.edgeIter(current_key);
                    while (edgeIterator.hasNext()) {
                        Edge currentEdge = (Edge) edgeIterator.next();
                        Node nextNode = (Node) g.getNode(currentEdge.getDest());
                        // updating in-weights to the smallest possible value (there might be a few ways to get from a to b)
                        if (currentEdge.getWeight() + current_node.getInWeight() < nextNode.getInWeight()) {
                            nextNode.setInWeight(currentEdge.getWeight() + current_node.getInWeight());
                        }
                    }
                }
            }
            // iterating over all nodes, saving and returning the maximum shortest path distance from source
            Iterator<NodeData> node_iterator = g.nodeIter();
            while (node_iterator.hasNext()) {
                max_shortest_path = Math.max(max_shortest_path, ((Node) node_iterator.next()).getInWeight());
            }
        } catch (ConcurrentModificationException e) {
            throw new RuntimeException(edges_from_node_change());
        }
        return max_shortest_path;
    }

    /**
     * the center of a directed weighted graph is defined as follows: the vertex which has the smallest maximum
     * distance from all other nodes. where distance is defined to be the shortest path distance.
     * this function calculates said vertex.
     * this func. assumes the graph is connected.
     *
     * @return the center of a connected directed weighted graph
     */
    @Override
    public NodeData center() {
        NodeData center = null;
        double min_max_dist = Double.MAX_VALUE;
        Iterator<NodeData> node_iter = g.nodeIter();
        try {
            while (node_iter.hasNext()) {
                Node current = (Node) node_iter.next();
                double current_node_max_shortest_path = shortestPathsForCenter(current.getKey());
                if (current_node_max_shortest_path < min_max_dist) {
                    min_max_dist = current_node_max_shortest_path;
                    center = current;
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
     *
     * @param unCheckedNodes - a list of vertices
     * @return a String representation of the keys of the pair of vertices. Later to be parsed by the isConnected func.
     */
    private String chooseStartNodes(List<NodeData> unCheckedNodes) {
        String pair = "";
        double min_distance = Double.MAX_VALUE;

        for (NodeData n : unCheckedNodes) {
            shortestPathsForCenter(n.getKey()); // update in-weight variables relative to n
            for (NodeData m : unCheckedNodes) { // iterate over all the other* vertices
                if (n.getKey() == m.getKey()) continue; // *skips over itself
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
     *
     * @param unCheckedNodes - a list of vertices
     * @param src            - the key of the source vertex
     * @return the key (ID) of said vertex.
     */
    private int closest_node(List<NodeData> unCheckedNodes, int src) {
        shortestPathsForCenter(src); // updates the in-weight variables relative to source
        int result = src;
        double min_weight = Double.MAX_VALUE;
        for (NodeData nodeData : unCheckedNodes) {
            if (((Node) nodeData).getInWeight() < min_weight) {
                result = nodeData.getKey(); // invariant: result gets key of the closest vertex so far
            }
            // at end og for loop: result stores the key of the closest vertex to the source vertex
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
     *
     * @param cities - a list of vertices representing cities to be visited
     * @return a list of vertices, representing the "cheapest" route through all cities from the given collection,
     * in the order in which said vertices were to be passed when travelling on said route.
     */
    @Override
    public List<NodeData> tsp(List<NodeData> cities) {

        List<NodeData> unCheckedNodes = new LinkedList<>(cities);

        String[] pair = chooseStartNodes(unCheckedNodes).split(",");
        // parsing from String to two int variables:
        int source = Integer.parseInt(pair[0]);
        int dest = Integer.parseInt(pair[1]);

        // create fist leg of the final path:
        List<NodeData> result = new ArrayList<>(shortestPath(source, dest));
        for (NodeData node : result) {
            unCheckedNodes.remove(node);
        }


        while (!unCheckedNodes.isEmpty()) {
            int current_key = closest_node(unCheckedNodes, dest); // find closest to last vertex on current leg
            List<NodeData> path = shortestPath(dest, current_key); // create the path between the two
            path.remove(0);     // remove the dest from the path list
            // in order to prevent duplicates
            result.addAll(path); // add path to existing leg
            dest = current_key; // update dest

            // update unCheckedNodes:
            for (NodeData node : result) {
                unCheckedNodes.remove(node);
            }
            // iterate
        }
        return result;
    }

    /**
     * this func. saves the graph object in pretty JSON format, in a JSON file which
     * is then stored in the directory represented by the given path, under the given file name.
     *
     * @param file - the file name (may include a relative path).
     * @return true if the save was successful - otherwise false.
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
                // converts edge object to json object
                Edge current = (Edge) edgeIter.next();
                JSONObject edge = new JSONObject();
                edge.put("src", current.getSrc());
                edge.put("w", current.getWeight());
                edge.put("dest", current.getDest());
                //add to json array
                edge_list.add(edge);
            }

            // add edge json array to final json object
            graph.put("Edges", edge_list);
            Iterator<NodeData> nodeIter = g.nodeIter();
            JSONArray node_list = new JSONArray();

            while (nodeIter.hasNext()) {
                // converts Node object to json object:
                Node current = (Node) nodeIter.next();
                JSONObject node = new JSONObject();
                String pos = current.getLocation().x() +
                        ", " + current.getLocation().y() +
                        ", " + current.getLocation().z();
                node.put("pos", pos);
                node.put("id", current.getKey());
                // add to jason array
                node_list.add(node);
            }

            // add node json array to final json object
            graph.put("Nodes", node_list);
            // define indentation specifics fo "pretty printing"
            save.write(graph.toString(4));
            // close the file writer
            save.close();

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
     *
     * @param file - the file name (may include a relative path).
     * @return true if load was successful - otherwise false.
     */
    @Override
    public boolean load(String file) {
        DWGraph loaded_graph = new DWGraph();
        try {
            // initiate new graph
            JsonObject graph = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
            JsonArray node_list = (JsonArray) graph.get("Nodes");

            // create nodes from node array in json and add to graph:
            for (JsonElement json_node : node_list) {
                int id = json_node.getAsJsonObject().get("id").getAsInt();
                String[] geo_location = json_node.getAsJsonObject().get("pos").getAsString().split(",");
                double x = Double.parseDouble(geo_location[0]);
                double y = Double.parseDouble(geo_location[1]);
                double z = Double.parseDouble(geo_location[2]);
                loaded_graph.addNode(new Node(id, new Location(x, y, z)));
            }

            JsonArray edge_list = (JsonArray) graph.get("Edges");
            // create edges in graph using info from edges array in json:
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
        // update g:
        g = loaded_graph;
        return true;
    }
}