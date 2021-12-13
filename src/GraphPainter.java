import api.*;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class GraphPainter extends JPanel {


    private double x_factor;
    private double y_factor;
    DWGraph graph;

    private void updateScale() {
        double abs_x = Math.abs(graph.getMaxX() - graph.getMinX());
        double abs_y = Math.abs(graph.getMaxY() - graph.getMinY());
        x_factor = this.getWidth() / abs_x;
        y_factor = this.getHeight() / abs_y;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGraph(g);

    }

    private void drawGraph(Graphics g) {
        Iterator<NodeData> nodeIter = this.graph.nodeIter();
        while (nodeIter.hasNext()) {
            Node node = (Node) nodeIter.next();
            drawNode(node, g);
        }
        Iterator<EdgeData> edgeIter = this.graph.edgeIter();
        while (edgeIter.hasNext()) {
            Edge edge = (Edge) edgeIter.next();
            drawEdge(edge, g);
        }
    }

    private void drawNode(Node node, Graphics g) {
        int x = (int) (x_factor*node.getLocation().x());
        int y = (int) (y_factor*node.getLocation().y());
        int id = node.getKey();
        String pos = x + "," + y;
        g.setColor(new Color(0, 0, 0));
        g.drawOval(x - 10, y - 10, 20, 20);
        g.drawString(pos,x-10,y-25);
    }

    private void drawEdge(Edge edge, Graphics g) {

    }
}
