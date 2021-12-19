package Test;

import api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DWGraphTest {


    Node n1 = new Node(1, new Location(0, 0, 0));
    Node n2 = new Node(2, new Location(1, 1, 0));
    Node n3 = new Node(3, new Location(1, 1, 0));
    Node n4 = new Node(4, new Location(1, 1, 0));
    Node n5 = new Node(5, new Location(1, 1, 0));
    Node n6 = new Node(6, new Location(1, 1, 0));


    @Test
    void getNode() {
        DWGraph g = new DWGraph();
        g.addNode(n1);
        g.addNode(n2);
        Node t1 = (Node) g.getNode(1);
        Node t2 = (Node) g.getNode(2);

        assertEquals(n1, t1);
        assertEquals(n2, t2);
    }

    @Test
    void getEdge() {
        DWGraph g = new DWGraph();
        g.connect(n1.getKey(), n2.getKey(), 2);
        g.connect(n1.getKey(), n3.getKey(), 1);

        Edge e1 = (Edge) g.getEdge(1, 2);

        assertEquals(g.getEdge(n1.getKey(), n2.getKey()), e1);
    }

    @Test
    void addNode() {
        DWGraph g = new DWGraph();
        assertEquals(0, g.nodeSize());
        assertEquals(0, g.getMC());

        g.addNode(n1);

        assertEquals(1, g.nodeSize());
        assertEquals(1, g.getMC());

        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);
        g.addNode(n6);

        assertEquals(6, g.nodeSize());
        assertEquals(6, g.getMC());
    }

    @Test
    void connect() {
        DWGraph g = new DWGraph();
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.connect(n1.getKey(), n2.getKey(), 2);
        g.connect(n1.getKey(), n3.getKey(), 1);

        Edge e1 = (Edge) g.getEdge(1, 2);

        assertEquals(2,g.edgeSize());
        assertEquals(5,g.getMC());
        assertEquals(2,e1.getWeight());
    }


    @Test
    void removeNode() {
        DWGraph g = new DWGraph();
        g.removeNode(1);

        assertEquals(0,g.nodeSize());
        assertEquals(0,g.getMC());

        g.addNode(n1);
        g.addNode(n2);
        g.removeNode(2);

        assertEquals(1,g.nodeSize());
        assertEquals(3,g.getMC());
    }

    @Test
    void removeEdge() {
        DWGraph g = new DWGraph();
        g.removeEdge(1,2);
        assertEquals(0,g.edgeSize());
        assertEquals(0,g.getMC());

        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);

        g.connect(n1.getKey(), n2.getKey(), 2);
        g.connect(n1.getKey(), n3.getKey(), 1);
        g.removeEdge(1,2);

        assertEquals(1,g.edgeSize());
        assertEquals(6,g.getMC());
    }

    @Test
    void nodeSize() {
        DWGraph g = new DWGraph();
        assertEquals(0, g.nodeSize());
        assertEquals(0, g.getMC());

        g.addNode(n1);

        assertEquals(1, g.nodeSize());
        assertEquals(1, g.getMC());
    }

    @Test
    void edgeSize() {
        DWGraph g = new DWGraph();
        assertEquals(0,g.edgeSize());
        assertEquals(0,g.getMC());

        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);

        g.connect(n1.getKey(), n2.getKey(), 2);
        g.connect(n1.getKey(), n3.getKey(), 1);

        assertEquals(2,g.edgeSize());
        assertEquals(5,g.getMC());

        g.removeEdge(1,2);

        assertEquals(1,g.edgeSize());
        assertEquals(6,g.getMC());
    }

    @Test
    void getMC() {
        DWGraph g = new DWGraph();
        assertEquals(0,g.getMC());
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);

        g.connect(n1.getKey(), n2.getKey(), 2);
        g.connect(n1.getKey(), n3.getKey(), 1);

        assertEquals(5,g.getMC());

        g.removeEdge(1,2);

        assertEquals(6,g.getMC());
    }
}