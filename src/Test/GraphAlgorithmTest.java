package Test;

import api.DWGraph;
import api.GraphAlgorithm;
import api.Location;
import api.Node;

import static org.junit.jupiter.api.Assertions.*;

class GraphAlgorithmTest {

    GraphAlgorithm graph1 = new GraphAlgorithm();
    GraphAlgorithm graph2 = new GraphAlgorithm();
    GraphAlgorithm graph3 = new GraphAlgorithm();


    @org.junit.jupiter.api.Test
    void init() {

    }

    @org.junit.jupiter.api.Test
    void getGraph() {
    }

    @org.junit.jupiter.api.Test
    void copy() {
    }

    @org.junit.jupiter.api.Test
    void isConnected() {
    }

    @org.junit.jupiter.api.Test
    void shortestPathDist() {
    }

    @org.junit.jupiter.api.Test
    void shortestPath() {
    }

    @org.junit.jupiter.api.Test
    void center() {
        graph1.load("G1.json");
    }

    @org.junit.jupiter.api.Test
    void tsp() {

    }

    @org.junit.jupiter.api.Test
    void save() {
        DWGraph graph = new DWGraph();
        Node n1 = new Node(1, new Location(0, 0, 0));
        Node n2 = new Node(2, new Location(1, 1, 0));
        Node n3 = new Node(3, new Location(1, 1, 0));
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.connect(n1.getKey(),n2.getKey(),1);
        graph.connect(n1.getKey(),n3.getKey(),1);
        graph.connect(n2.getKey(),n1.getKey(),5);
        graph.connect(n2.getKey(),n3.getKey(),5);
        graph.connect(n3.getKey(),n2.getKey(),5);
        graph.connect(n3.getKey(),n1.getKey(),5);
        graph1.init(graph);
        graph1.save("check");

    }

    @org.junit.jupiter.api.Test
    void load() {
        DWGraph graph = new DWGraph();
        Node n1 = new Node(1, new Location(0, 0, 0));
        Node n2 = new Node(2, new Location(1, 1, 0));
        Node n3 = new Node(3, new Location(1, 1, 0));
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.connect(n1.getKey(),n2.getKey(),1);
        graph.connect(n1.getKey(),n3.getKey(),1);
        graph.connect(n2.getKey(),n1.getKey(),5);
        graph.connect(n2.getKey(),n3.getKey(),5);
        graph.connect(n3.getKey(),n2.getKey(),5);
        graph.connect(n3.getKey(),n1.getKey(),5);
        GraphAlgorithm g1 = new GraphAlgorithm();
        g1.load("C:\\Users\\shlom\\IdeaProjects\\Ex2\\check.json");
        String st = g1.getGraph().toString();
        String st2 = graph.toString();
            assertEquals(st,st2);
    }
}