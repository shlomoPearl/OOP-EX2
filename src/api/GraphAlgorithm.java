package api;


//import java.io.FileWriter;
//import java.io.IOException;
//
//import org.json.simple.JSONArray;
////import org.json.JSONArray;
//import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.*;
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


    private boolean dfs(DWGraph g, Node n) {
        n.setTag(1);
        if (g.edgeIter(n.getKey()) != null) {
            Iterator<EdgeData> edgeIter = g.edgeIter(n.getKey());
            while (edgeIter.hasNext()) {
                Node next = (Node) g.getNode(edgeIter.next().getDest());
                if (next.getTag() != 1) {
                    dfs(g, next);
                }
            }
        }
        boolean result = true;
        Iterator<NodeData> nodeIter = g.nodeIter();
        while (nodeIter.hasNext()) {
            Node current = (Node) nodeIter.next();
            result = result && (current.getTag() == 1);
        }
        return result;
    }

    @Override
    public boolean isConnected() {
        Iterator<NodeData> nodeIter = g.nodeIter();
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
            Node current = wIter.next();
            if (current.getKey() != src) {

                current.setInWeight(Double.MAX_VALUE);
            } else {
                current.setInWeight(0);
            }
        }
    }

    private Node minWeight(HashMap<Integer, Node> hm) {
        Iterator<Node> hmIter = hm.values().iterator();
        Node ans = null;
        if (hmIter.hasNext()) {
            ans = hmIter.next();
            while (hmIter.hasNext()) {
                Node current = hmIter.next();
                if (current.getInWeight() < ans.getInWeight()) {
                    ans = current;
                }
            }
        }
        return ans;
    }


    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        if (src == dest) return null;
        LinkedList<NodeData> ans = new LinkedList<>();

        HashMap<Integer, Node> unCheckedNode = new HashMap<>();

        Iterator<NodeData> nodeIter = g.nodeIter();
        while (nodeIter.hasNext()) {
            Node current = (Node) nodeIter.next();
            unCheckedNode.put(current.getKey(), current);
        }

        initialMax(unCheckedNode, src);

        while (!unCheckedNode.isEmpty()) {
            Node currentNode = minWeight(unCheckedNode);
            unCheckedNode.remove(currentNode.getKey());
            if (g.edgeIter(currentNode.getKey()) != null) {
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
            } else continue;
        }
        return (!ans.isEmpty()) ? ans : null;
    }


    @Override
    public NodeData center() {
        double[] ans = new double[2];
        ans[1] = Double.MAX_VALUE;
        double count;
        Iterator<NodeData> nodeIter1 = g.nodeIter();
        while (nodeIter1.hasNext()) {
            Node current1 = (Node) nodeIter1.next();
            shortestPath(current1.getKey());
            Iterator<NodeData> nodeIter2 = g.nodeIter();
            count = 0;
            while (nodeIter2.hasNext()) {
                Node current2 = (Node) nodeIter2.next();
                count += current2.getInWeight();
            }
            if (count < ans[1]) {
                ans[0] = current1.getKey();
                ans[1] = count;
            }
        }
        return g.getNode((int) ans[0]);
    }

    private void shortestPath(int src) {
        HashMap<Integer, Node> unCheckedNode = new HashMap<>();
        Iterator<NodeData> nodeIter = g.nodeIter();
        while (nodeIter.hasNext()) {
            Node current = (Node) nodeIter.next();
            unCheckedNode.put(current.getKey(), current);
        }
        initialMax(unCheckedNode, src);

        while (!unCheckedNode.isEmpty()) {
            Node currentNode = minWeight(unCheckedNode);
            unCheckedNode.remove(currentNode.getKey());
            if (g.edgeIter(currentNode.getKey()) != null) {
                Iterator<EdgeData> edgeIterator = g.edgeIter(currentNode.getKey());
                while (edgeIterator.hasNext()) {
                    Edge currentEdge = (Edge) edgeIterator.next();
                    Node nextNode = (Node) g.getNode(currentEdge.getDest());
                    Node prevNode = (Node) g.getNode(currentEdge.getSrc());
                    if (currentEdge.getWeight() + currentNode.getInWeight() < nextNode.getInWeight()) {
                        nextNode.setInWeight(currentEdge.getWeight() + currentNode.getInWeight());
                        nextNode.setKeyPrevNode(currentNode.getKey());
                    }
                }
            } else continue;
        }
    }

    private double[] chooseStartNode(List<NodeData> unCheckNode) {
        double[] temp = new double[3];
        temp[2] = Double.MAX_VALUE;
        for (int i = 0; i < unCheckNode.size(); i++) {
            shortestPath(unCheckNode.get(i).getKey());
            for (int j = 0; j < unCheckNode.size(); j++) {
                if (i == j) continue;
                if (((Node) (unCheckNode.get(j))).getInWeight() < temp[2]) {
                    temp[0] = unCheckNode.get(i).getKey();
                    temp[1] = unCheckNode.get(j).getKey();
                    temp[2] = ((Node) (unCheckNode.get(j))).getInWeight();
                }
            }
        }
        return temp;
    }

    private double[] minWeight(List<NodeData> unCheckNode, int src) {
        shortestPath(src);
        double[] temp2 = new double[2];
        temp2[1] = Double.MAX_VALUE;
        for (int i = 0; i < unCheckNode.size(); i++) {
            if (((Node) (unCheckNode.get(i))).getInWeight() < temp2[1]) {
                temp2[0] = unCheckNode.get(i).getKey();
                temp2[1] = ((Node) (unCheckNode.get(i))).getInWeight();
            }
        }
        return temp2;
    }

    private void removeDupi(List<NodeData> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).getKey() == list.get(i + 1).getKey()) {
                list.remove(i);
            }
        }
    }


    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        List<NodeData> ans = new ArrayList<>();
        List<NodeData> unCheckNode = new LinkedList<>();
        unCheckNode.addAll(cities);

        double[] temp = chooseStartNode(unCheckNode);
        int s, d;
        s = (int) temp[0];
        d = (int) temp[1];
        ans.addAll(shortestPath(s, d));
        for (int i = 0; i < ans.size(); i++) {
            if (unCheckNode.contains(ans.get(i))) {
                unCheckNode.remove(ans.get(i));
            }
        }
        while (!unCheckNode.isEmpty()) {
            double[] minW = minWeight(unCheckNode, d);
            ans.addAll((shortestPath(d, (int) minW[0])));
            d = (int) minW[0];
            for (int i = 0; i < ans.size(); i++) {
                if (unCheckNode.contains(ans.get(i))) {
                    unCheckNode.remove(ans.get(i));
                }
            }
        }
        removeDupi(ans);
        if (ans != null) return ans;
        return null;
    }

    @Override
    public boolean save(String file) {
        try {
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
            JSONObject nodes = new JSONObject();
            JSONArray node_list = new JSONArray();
            while (nodeIter.hasNext()) {
                Node current = (Node) nodeIter.next();
                JSONObject node = new JSONObject();
                String pos = current.getLocation().x() + ", " + current.getLocation().y() + ", " + current.getLocation().z();
                node.put("pos", pos);
                node.put("id", current.getKey());
                node_list.add(node);
            }
            graph.put("Nodes", node_list);
            save.write(graph.toString(4));
            save.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return true;
    }

    @Override
    public boolean load(String file) {

        return false;
    }
}
