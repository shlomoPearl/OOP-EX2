package api;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class GraphAlgorithm implements DirectedWeightedGraphAlgorithms {

    private DirectedWeightedGraph g;

    public GraphAlgorithm() {

    }

    @Override
    public void init(DirectedWeightedGraph g) {
        this.g = g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.g;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return new DWGraph((DWGraph) this.g);
    }

    @Override
    public boolean isConnected() {
//        Iterator<NodeData> node_iter = g.nodeIter();
//        while (node_iter.hasNext()){
//            Node current = (Node) node_iter.next();
//
//        }
        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        Node a = (Node) g.getNode(dest);
        return a.getInWeight();
    }

    private void initialMax(HashMap<Integer, Node> hm, int src) {
        Iterator<Node> wIter = hm.values().iterator();
        while (wIter.hasNext()) {
            if (wIter.next().getKey() != src) {
                wIter.next().setInWeight(Double.MAX_VALUE);
            } else {
                wIter.next().setInWeight(0);
            }
        }
    }

    private Node minWeight(HashMap<Integer, Node> hm) {
        Node ans = hm.get(0);
        Iterator<Node> hmIter = hm.values().iterator();
        while (hmIter.hasNext()) {
            Node current = hmIter.next();
            if (current.getInWeight() < ans.getInWeight()) {
                ans = current;
            }
        }
        return ans;
    }
    

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        List<NodeData> ans = new ArrayList<>();
        ans.add(g.getNode(src));

        HashMap<Integer, Node> unCheckedNode = new HashMap<>();
        DWGraph copy_graph = new DWGraph((DWGraph) this.g);

        Iterator<NodeData> nodeIter = g.nodeIter();
        while (nodeIter.hasNext()) {
            unCheckedNode.put(nodeIter.next().getKey(), (Node) nodeIter.next());
        }

        initialMax(unCheckedNode, src);

        while (!unCheckedNode.isEmpty()) {
            Node currentNode = minWeight(unCheckedNode);
            unCheckedNode.remove(currentNode.getKey());

            Iterator<EdgeData> edgeIterator = copy_graph.edgeIter(currentNode.getKey());
            while (edgeIterator.hasNext()) {
                Edge currentEdge = (Edge) edgeIterator.next();
                Node nextNode = (Node) copy_graph.getNode(currentEdge.getDest());
                if (currentEdge.getWeight() + currentNode.getInWeight() < nextNode.getInWeight()) {
                    nextNode.setInWeight(currentEdge.getWeight() + currentNode.getInWeight());
                }
            }

        }
        return ans;
    }

    @Override
    public NodeData center() {
        return null;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }

    @Override
    public boolean save(String file) {
        Gson gson = new Gson();
        try {
            gson.toJson(g, new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean load(String file) {

        return false;
    }
}
