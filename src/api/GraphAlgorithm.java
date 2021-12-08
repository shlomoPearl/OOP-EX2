package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class GraphAlgorithm implements DirectedWeightedGraphAlgorithms {

    private DirectedWeightedGraph g;
//    private boolean[] visited1 = new boolean[g.nodeSize()];
//    private boolean[] visited2 = new boolean[g.nodeSize()];

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
        if(g.edgeIter(n.getKey())!=null) {
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
        if (src == dest)return null;
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
            }else continue;
        }
        return (!ans.isEmpty()) ? ans : null;
    }

    @Override
    public NodeData center() {
        return null;
    }

    private String tuple(int src, int dest) {
        return "(" + src + ", " + dest + ")"; //+ ", "+w
    }


    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        HashMap<String,Double> path = new HashMap<>();

        for (int i = 0; i < cities.size(); i++) {

        }
        return null;
    }

    @Override
    public boolean save(String file) {
        JsonObject jsonObject = new JsonObject();
        Iterator<EdgeData> edgeIter = g.edgeIter();
        Iterator<NodeData> nodeIter = g.nodeIter();
        while (edgeIter.hasNext()){
            Edge current = (Edge) edgeIter.next();
            jsonObject.addProperty("src",current.getSrc());
            jsonObject.addProperty("w",current.getWeight());
            jsonObject.addProperty("dest",current.getDest());
        }
        while (nodeIter.hasNext()){
            Node current = (Node) nodeIter.next();
            jsonObject.addProperty("pos",""+current.getLocation().x()+","+current.getLocation().y()+","+current.getLocation().z());
            jsonObject.addProperty("id",current.getKey());
        }

        GsonBuilder  builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        gson.toJson(jsonObject);

        //gson.toJson(jsonObject);

        //System.out.println(jsonObject);
//        try{
//            FileWriter save = new FileWriter(file);
//            save.write(jsonObject.getAsString());
//            save.close();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        Gson gson = new Gson();
//        file = gson.toJson(g.getNode(2));
//        System.out.println(file);
//      //  file = gson.toJson(g.getNode(2));
////        try {
////            gson.toJson(g, new FileWriter(file));
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
        return true;
    }

    @Override
    public boolean load(String file) {

        return false;
    }
}
