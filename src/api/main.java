package api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class main {

    public static void main(String[] args) {
        DWGraph graph = new DWGraph();
        Node n1 = new Node(1, new Location(0, 0, 0));
        Node n2 = new Node(2, new Location(1, 1, 0));
        Node n3 = new Node(3, new Location(1, 1, 0));
        Node n4 = new Node(4, new Location(1, 1, 0));
        Node n5 = new Node(5, new Location(1, 1, 0));
        Node n6 = new Node(6, new Location(1, 1, 0));

        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(n4);
        graph.addNode(n5);
        graph.addNode(n6);
        graph.connect(n1.getKey(), n2.getKey(), 2);
        graph.connect(n1.getKey(), n3.getKey(), 4);
        graph.connect(n2.getKey(), n4.getKey(), 7);
        graph.connect(n2.getKey(), n3.getKey(), 1);
        graph.connect(n4.getKey(), n6.getKey(), 1);
        graph.connect(n3.getKey(), n5.getKey(), 3);
        graph.connect(n3.getKey(), n1.getKey(), 3);
        graph.connect(n5.getKey(), n4.getKey(), 2);
        graph.connect(n5.getKey(), n6.getKey(), 5);
        graph.connect(n5.getKey(), n3.getKey(), 5);
        graph.connect(n6.getKey(), n5.getKey(), 5);

        GraphAlgorithm g1 =new GraphAlgorithm();
        g1.init(graph);
        double d = g1.shortestPathDist(2,4);
        LinkedList<NodeData> list= (LinkedList<NodeData>) g1.shortestPath(2,4);
        g1.save("shlomo.json");
        System.out.println(d);
        System.out.println(list.get(2).getKey());
        System.out.println(g1.isConnected());

    }


}
