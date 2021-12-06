package api;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class GraphAlgorithm implements DirectedWeightedGraphAlgorithms {

    private DirectedWeightedGraph g;
    private boolean[] visited1 = new boolean[g.nodeSize()];
    private boolean[] visited2 = new boolean[g.nodeSize()];

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


    private boolean dfs(DWGraph g ,Node n){
        n.setTag(1);
        Iterator<EdgeData> edgeIter = g.edgeIter(n.getKey());
        while (edgeIter.hasNext()){
            Node next = (Node) g.getNode(edgeIter.next().getDest());
            if (next.getTag() != 1) {
                dfs(g, next);
            }
        }
        boolean result = true;
        Iterator<NodeData> nodeIter= g.nodeIter();
        while (nodeIter.hasNext()){
            Node current = (Node) nodeIter.next();
            result = result && (current.getTag() == 1);
        }
        return result;
    }

    @Override
    public boolean isConnected() {
        Iterator<NodeData> nodeIter = g.nodeIter();
        if (nodeIter.hasNext()){
            Node current = (Node) nodeIter.next();
            int key = current.getKey();
            boolean first_pass = dfs((DWGraph) g,current);
            DWGraph transpose = ((DWGraph) g).transpose();
            Iterator<NodeData> transpose_iter = transpose.nodeIter();
            while (transpose_iter.hasNext()){
                transpose_iter.next().setTag(0);
            }
            Node new_current = (Node) transpose.getNode(key);
            boolean second_pass = dfs(transpose,new_current);
            return first_pass && second_pass;
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if (shortestPath(src, dest) != null) {
            return ((Node) g.getNode(dest)).getInWeight();
        }
        return -1;
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
        LinkedList<NodeData> ans = new LinkedList<>();

        HashMap<Integer, Node> unCheckedNode = new HashMap<>();

        Iterator<NodeData> nodeIter = g.nodeIter();
        while (nodeIter.hasNext()) {
            unCheckedNode.put(nodeIter.next().getKey(), (Node) nodeIter.next());
        }

        initialMax(unCheckedNode, src);

        while (!unCheckedNode.isEmpty()) {
            Node currentNode = minWeight(unCheckedNode);
            unCheckedNode.remove(currentNode.getKey());

            Iterator<EdgeData> edgeIterator = g.edgeIter(currentNode.getKey());
            while (edgeIterator.hasNext()) {
                Edge currentEdge = (Edge) edgeIterator.next();
                Node nextNode = (Node) g.getNode(currentEdge.getDest());
                Node prevNode = (Node) g.getNode(currentEdge.getSrc());
                if (currentEdge.getWeight() + currentNode.getInWeight() < nextNode.getInWeight()) {
                    nextNode.setInWeight(currentEdge.getWeight() + currentNode.getInWeight());
                    nextNode.setKeyPrevNode(currentNode.getKey());
                    if (nextNode.getKey() == dest) {
                        ans.clear();
                        ans.add(nextNode);
                        while (prevNode.getKey() != src) {
                            ans.addFirst(prevNode);
                            prevNode = (Node) g.getNode(prevNode.getKeyPrevNode());
                        }
                        ans.addFirst((Node) g.getNode(src));
                    }
                }
            }
        }
        return (!ans.isEmpty()) ? ans : null;
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
