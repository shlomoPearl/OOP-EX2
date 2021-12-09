package Test;

import api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DWGraphTest {


    DWGraph graph1 = new DWGraph();
    Node n1 = new Node(1, new Location(0, 0, 0));
    Node n2 = new Node(2, new Location(1, 1, 0));
    Node n3 = new Node(3, new Location(1, 1, 0));
    Node n4 = new Node(4, new Location(1, 1, 0));
    Node n5 = new Node(5, new Location(1, 1, 0));
    Node n6 = new Node(6, new Location(1, 1, 0));

    @Test
    void getNode() {
        graph1.addNode(n1);
        graph1.addNode(n2);
        Node t1 = (Node) graph1.getNode(1);
        Node t2 = (Node) graph1.getNode(2);
        assertEquals(n1, t1);
        assertEquals(n2, t2);
    }

    @Test
    void getEdge() {
        graph1.connect(n1.getKey(), n2.getKey(), 2);
        graph1.connect(n1.getKey(), n3.getKey(), 1);
        Edge e1 = (Edge) graph1.getEdge(1, 2);
        assertEquals(graph1.getEdge(n1.getKey(), n2.getKey()), e1);
    }

    @Test
    void addNode() {
        assertEquals(0, graph1.nodeSize());
        assertEquals(0, graph1.getMC());
        graph1.addNode(n1);
        assertEquals(1, graph1.nodeSize());
        assertEquals(1, graph1.getMC());
        graph1.addNode(n2);
        graph1.addNode(n3);
        graph1.addNode(n4);
        graph1.addNode(n5);
        graph1.addNode(n6);
        assertEquals(6, graph1.nodeSize());
        assertEquals(6, graph1.getMC());
    }

    @Test
    void connect() {
        graph1.connect(n1.getKey(), n2.getKey(), 2);
        graph1.connect(n1.getKey(), n3.getKey(), 1);
        Edge e1 = (Edge) graph1.getEdge(1, 2);
        Edge e2 = (Edge) graph1.getEdge(1,3);
        assertEquals(2,graph1.edgeSize());
        assertEquals(2,graph1.getMC());
        assertEquals(2,e1.getWeight());
    }

    @Test
    void nodeIter() {

    }

    @Test
    void edgeIter() {
    }

    @Test
    void testEdgeIter() {
    }

    @Test
    void removeNode() {
        graph1.removeNode(1);
        assertEquals(0,graph1.nodeSize());
        assertEquals(0,graph1.getMC());
        graph1.addNode(n1);
        graph1.addNode(n2);
        graph1.removeNode(2);
        assertEquals(1,graph1.nodeSize());
        assertEquals(3,graph1.getMC());
    }

    @Test
    void removeEdge() {
        graph1.removeEdge(1,2);
        assertEquals(0,graph1.edgeSize());
        assertEquals(0,graph1.getMC());
        graph1.connect(n1.getKey(), n2.getKey(), 2);
        graph1.connect(n1.getKey(), n3.getKey(), 1);
        graph1.removeEdge(1,2);
        assertEquals(1,graph1.edgeSize());
        assertEquals(3,graph1.getMC());

    }

    @Test
    void nodeSize() {
        assertEquals(0, graph1.nodeSize());
        assertEquals(0, graph1.getMC());
        graph1.addNode(n1);
        assertEquals(1, graph1.nodeSize());
        assertEquals(1, graph1.getMC());
    }

    @Test
    void edgeSize() {
        assertEquals(0,graph1.edgeSize());
        assertEquals(0,graph1.getMC());
        graph1.connect(n1.getKey(), n2.getKey(), 2);
        graph1.connect(n1.getKey(), n3.getKey(), 1);
        assertEquals(2,graph1.edgeSize());
        assertEquals(2,graph1.getMC());
        graph1.removeEdge(1,2);
        assertEquals(1,graph1.edgeSize());
        assertEquals(3,graph1.getMC());
    }

    @Test
    void getMC() {
        assertEquals(0,graph1.getMC());
        graph1.connect(n1.getKey(), n2.getKey(), 2);
        graph1.connect(n1.getKey(), n3.getKey(), 1);
        assertEquals(2,graph1.getMC());
        graph1.removeEdge(1,2);
        assertEquals(3,graph1.getMC());

    }
}