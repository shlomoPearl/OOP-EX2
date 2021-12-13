import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

public class GraphPainter extends JPanel implements ActionListener {


    private double x_factor;
    private double y_factor;
    DWGraph graph;

    public GraphPainter(DWGraph graph){
        super();
        this.graph = graph;
        this.setBackground(new Color(150,220,223));
        updateScale();
    }

    private void updateScale() {
        double abs_x = Math.abs(graph.getMaxX() - graph.getMinX());
        double abs_y = Math.abs(graph.getMaxY() - graph.getMinY());
        x_factor = this.getWidth() / abs_x;
        y_factor = this.getHeight() / abs_y;
    }


    @Override
    public void paintComponent(Graphics g) {
        updateScale();
        super.paintComponent(g);
        updateScale();
        drawGraph(g);
    }

    public void drawGraph(Graphics g) {
        DWGraph dwg = this.graph;
        Iterator<NodeData> nodeIter = dwg.nodeIter();
        while (nodeIter.hasNext()) {
            Node node = (Node) nodeIter.next();
            g.setColor(Color.BLACK);
            drawNode(node, g);
        }
//        Iterator<EdgeData> edgeIter = dwg.edgeIter();
//        while (edgeIter.hasNext()) {
//            Edge edge = (Edge) edgeIter.next();
//            g.setColor(Color.YELLOW);
//            drawEdge(edge, g);
//        }
        this.repaint();
    }

    private void drawNode(Node node, Graphics g) {
        int x = (int) (x_factor * node.getLocation().x());
        int y = (int) (y_factor * node.getLocation().y());
        int id = node.getKey();
        String pos = "ID: " + id + "," + x + "," + y;
        g.drawOval(x - 10, y - 10, 20, 20);
        g.drawString(pos, x - 10, y - 25);
        repaint();
    }

    private void drawEdge(Edge edge, Graphics g) {

    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
