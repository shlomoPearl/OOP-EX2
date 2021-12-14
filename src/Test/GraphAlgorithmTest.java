package Test;

import api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphAlgorithmTest {

    final double EPS = 0.001;

    Node n1 = new Node(1, new Location(0, 0, 0));
    Node n2 = new Node(2, new Location(1, 1, 0));
    Node n3 = new Node(3, new Location(1, 1, 0));
    Node n4 = new Node(4, new Location(1, 1, 0));
    Node n5 = new Node(5, new Location(1, 1, 0));
    Node n6 = new Node(6, new Location(1, 1, 0));

    DWGraph graph = new DWGraph();

    GraphAlgorithm graph1 = new GraphAlgorithm();
    GraphAlgorithm graph2 = new GraphAlgorithm();
    GraphAlgorithm graph3 = new GraphAlgorithm();
    GraphAlgorithm graph4 = new GraphAlgorithm();
    GraphAlgorithm graph5 = new GraphAlgorithm();


    @org.junit.jupiter.api.Test
    void init() {
        graph1.init(graph);

        assertEquals(graph, graph1.getGraph());
    }

    @org.junit.jupiter.api.Test
    void getGraph() {
        graph1.init(graph);

        assertEquals(graph1.getGraph(), graph);
    }

    @org.junit.jupiter.api.Test
    void copy() {
        graph1.init(graph);
        DWGraph copy = (DWGraph) graph1.copy();
        assertEquals(copy.toString(), graph1.getGraph().toString());
        assertNotSame(copy,graph1);
    }

    @org.junit.jupiter.api.Test
    void isConnected() {
        graph1.load("G1.json");
        graph2.load("G2.json");
        graph3.load("G3.json");

        boolean a = graph1.isConnected();
        boolean b = graph2.isConnected();
        boolean c = graph3.isConnected();

        assertTrue(a);
        assertTrue(b);
        assertTrue(c);

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
        graph.connect(n5.getKey(), n4.getKey(), 2);
        graph.connect(n5.getKey(), n6.getKey(), 5);

        graph4.init(graph);
        a = graph4.isConnected();

        assertFalse(a);
    }

    @org.junit.jupiter.api.Test
    void shortestPathDist() {
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
        graph.connect(n5.getKey(), n4.getKey(), 2);
        graph.connect(n5.getKey(), n6.getKey(), 5);
        graph1.init(graph);

        double a = graph1.shortestPathDist(2, 4);
        double b = graph1.shortestPathDist(6, 5);
        double c = graph1.shortestPathDist(1, 3);

        assertEquals(6, a);
        assertEquals(-1, b);
        assertEquals(3, c);

        graph1.load("G1.json");
        graph2.load("G2.json");
        graph3.load("G3.json");

        a = graph1.shortestPathDist(2, 10);
        b = graph2.shortestPathDist(2, 10);
        c = graph3.shortestPathDist(2, 10);

        assertEquals(a, 7.222204, EPS);
        assertEquals(b, 6.678252, EPS);
        assertEquals(c, 2.022258, EPS);

    }

    @org.junit.jupiter.api.Test
    void shortestPath() {

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
        graph.connect(n5.getKey(), n4.getKey(), 2);
        graph.connect(n5.getKey(), n6.getKey(), 5);

        graph1.init(graph);

        List<NodeData> a = graph1.shortestPath(2, 4);
        List<NodeData> b = graph1.shortestPath(6, 5);
        List<NodeData> c = graph1.shortestPath(1, 3);

        String ans1 = "[2, 3, 5, 4]";
        String ans2 = "[1, 2, 3]";

        assertEquals(ans1, a.toString());
        assertNull(b);
        assertEquals(ans2, c.toString());
    }

    @org.junit.jupiter.api.Test
    void center() {

        graph1.load("G1.json");
        graph2.load("G2.json");
        graph3.load("G3.json");
        assertEquals(8, graph1.center().getKey());
        assertEquals(0, graph2.center().getKey());
        assertEquals(40, graph3.center().getKey());
    }

    @org.junit.jupiter.api.Test
    void tsp() {

        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(n4);
        graph.addNode(n5);
        graph.addNode(n6);

        graph.connect(n1.getKey(), n2.getKey(), 2);
        graph.connect(n1.getKey(), n3.getKey(), 4);
        graph.connect(n2.getKey(), n4.getKey(), 17);
        graph.connect(n2.getKey(), n3.getKey(), 1);
        graph.connect(n4.getKey(), n6.getKey(), 1);
        graph.connect(n3.getKey(), n5.getKey(), 3);
        graph.connect(n5.getKey(), n4.getKey(), 5);
        graph.connect(n5.getKey(), n6.getKey(), 1);
        graph.connect(n6.getKey(), n4.getKey(), 1);

        graph4.init(graph);

        List<NodeData> check = new ArrayList<>();

        check.add(n4);
        check.add(n5);
        check.add(n3);
        check.add(n2);

        String s = "[2, 3, 5, 6, 4]";

        assertEquals(s, graph4.tsp(check).toString());

        check.add(n1);
        check.remove(n3);
        check.remove(n5);

        s = "[1, 2, 3, 5, 6, 4]";

        assertEquals(s, graph4.tsp(check).toString());
    }


    @org.junit.jupiter.api.Test
    void saveNload() {

        DWGraph graph = new DWGraph();

        Node n1 = new Node(1, new Location(0, 0, 0));
        Node n2 = new Node(2, new Location(1, 1, 0));
        Node n3 = new Node(3, new Location(1, 1, 0));

        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);

        graph.connect(n1.getKey(), n2.getKey(), 1);
        graph.connect(n1.getKey(), n3.getKey(), 1);
        graph.connect(n2.getKey(), n1.getKey(), 5);
        graph.connect(n2.getKey(), n3.getKey(), 5);
        graph.connect(n3.getKey(), n2.getKey(), 5);
        graph.connect(n3.getKey(), n1.getKey(), 5);

        GraphAlgorithm g1 = new GraphAlgorithm();
        g1.init(graph);
        g1.save("test.json");
        g1.load("test.json");
        String st = g1.getGraph().toString();
        String st2 = graph.toString();
        assertEquals(st, st2);
    }
}