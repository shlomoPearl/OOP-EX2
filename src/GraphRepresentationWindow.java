import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GraphRepresentationWindow extends JFrame implements ActionListener {

    ParameterWindow p;

    MenuItem loadButton = new MenuItem("Load Graph");
    MenuItem saveButton = new MenuItem("Save Graph");
    MenuItem addNodeButton = new MenuItem("Add Vertex");
    MenuItem connectButton = new MenuItem("Connect Vertices");
    MenuItem removeNodeButton = new MenuItem("Remove Vertex");
    MenuItem removeEdgeButton = new MenuItem("Remove Edge");
    MenuItem shortestPath = new MenuItem("Shortest Path");
    MenuItem shortestPathDist = new MenuItem("Distance");
    MenuItem isConnected = new MenuItem("Connected?");
    MenuItem center = new MenuItem("Graph Center");
    MenuItem getNodeSize = new MenuItem("No. of Vertices");
    MenuItem tspButton = new MenuItem("TSP");
    MenuItem getEdgeSize = new MenuItem("No. of Edges");

    api.GraphAlgorithm graph_algo = new GraphAlgorithm();

    private double x_factor = 0;
    private double y_factor = 0;

    public GraphRepresentationWindow() {
        graph_algo.init(new DWGraph());
        this.setTitle("Directed Weighted Graph Representation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        addMenu();
    }

    public GraphRepresentationWindow(DirectedWeightedGraphAlgorithms algo) {
        graph_algo = (GraphAlgorithm) algo;
        this.setTitle("Directed Weighted Graph Representation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        addMenu();
    }

    public DWGraph getGraph_algo() {
        return (DWGraph) this.graph_algo.getGraph();
    }

    @Override
    public void paint(Graphics canvas) {
        int w = this.getWidth();
        int h = this.getHeight();
        updateScale();
        Image buffer_image;
        Graphics buffer_graphics;
        // Create a new "canvas"
        buffer_image = createImage(w, h);
        buffer_graphics = buffer_image.getGraphics();

        // Draw on the new "canvas"
        paintComponents(buffer_graphics);

        // "Switch" the old "canvas" for the new one
        canvas.drawImage(buffer_image, 0, 0, this);
    }


    /**
     * painting the graph
     *
     * @param g
     */
    @Override
    public void paintComponents(Graphics g) {
        updateScale();
        super.paintComponents(g);
        int w = this.getWidth();
        int h = this.getHeight();
        g.setColor(new Color(255, 242, 247));
        g.fillRect(0, 0, w, h);
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 14));
        drawGraph(g);

    }

    private void updateScale() {
        double abs_x = Math.abs(getGraph_algo().getMaxX() - getGraph_algo().getMinX());
        double abs_y = Math.abs(getGraph_algo().getMaxY() - getGraph_algo().getMinY());
        x_factor = ((double) this.getWidth() - 100) / abs_x;
        y_factor = ((double) this.getHeight() - 100) / abs_y;
    }


    public void drawGraph(Graphics g) {
        Iterator<NodeData> nodeIter = getGraph_algo().nodeIter();
        while (nodeIter.hasNext()) {
            Node node = (Node) nodeIter.next();
            g.setColor(Color.BLACK);
            drawNode(node, g);

        }
        Iterator<EdgeData> edgeIter = getGraph_algo().edgeIter();
        while (edgeIter.hasNext()) {
            Edge edge = (Edge) edgeIter.next();
            g.setColor(new Color(0, 200, 0));
            drawEdge(edge, g);
        }

    }

    private void drawNode(Node node, Graphics g) {

        // x and y to draw to
        int x = (int) (x_factor * (node.getLocation().x() - getGraph_algo().getMinX())) + 40;
        int y = (int) (y_factor * (node.getLocation().y() - getGraph_algo().getMinY())) + 80;
        int id = node.getKey();
        g.fillOval(x, y, 20, 20);
        g.setColor(Color.RED);
        Font f = new Font("ComicSans", Font.BOLD, 15);
        g.setFont(f);
        g.drawString("" + id, x, y - 5);
    }

    private void drawEdge(Edge edge, Graphics g) {
        Node source = (Node) getGraph_algo().getNode(edge.getSrc());
        Node destination = (Node) getGraph_algo().getNode(edge.getDest());

        int x1 = (int) (x_factor * (source.getLocation().x() - getGraph_algo().getMinX())) + 50;
        int y1 = (int) (y_factor * (source.getLocation().y() - getGraph_algo().getMinY())) + 90;
        int x2 = (int) (x_factor * (destination.getLocation().x() - getGraph_algo().getMinX())) + 50;
        int y2 = (int) (y_factor * (destination.getLocation().y() - getGraph_algo().getMinY())) + 90;

        drawArrowLine(g, x1, y1, x2, y2, 10, 7);
    }

    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }



    private void addMenu() {
        MenuBar bar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu actionsMenu = new Menu("Actions");
        Menu algorithmsMenu = new Menu("Algorithms");
        bar.add(fileMenu);
        bar.add(actionsMenu);
        bar.add(algorithmsMenu);
        this.setMenuBar(bar);

        //File menu

        loadButton.addActionListener(this);
        saveButton.addActionListener(this);
        fileMenu.add(loadButton);
        fileMenu.add(saveButton);

        //action menu

        addNodeButton.addActionListener(this);
        connectButton.addActionListener(this);
        removeNodeButton.addActionListener(this);
        removeEdgeButton.addActionListener(this);
        actionsMenu.add(addNodeButton);
        actionsMenu.add(connectButton);
        actionsMenu.add(removeNodeButton);
        actionsMenu.add(removeEdgeButton);

        //algorithm menu

        shortestPath.addActionListener(this);
        shortestPathDist.addActionListener(this);
        tspButton.addActionListener(this);
        isConnected.addActionListener(this);
        center.addActionListener(this);
        getNodeSize.addActionListener(this);
        getEdgeSize.addActionListener(this);
        algorithmsMenu.add(shortestPath);
        algorithmsMenu.add(shortestPathDist);
        algorithmsMenu.add(tspButton);
        algorithmsMenu.add(isConnected);
        algorithmsMenu.add(center);
        algorithmsMenu.add(getNodeSize);
        algorithmsMenu.add(getEdgeSize);


    }


    @Override
    public void actionPerformed(ActionEvent e) {

        // Actions:

        // Add Node
        if (e.getSource() == addNodeButton) {

            JTextField key = new JTextField(5);
            JTextField x = new JTextField(5);
            JTextField y = new JTextField(5);
            JTextField z = new JTextField(5);

            JPanel add_node_input_panel = new JPanel();
            add_node_input_panel.add(new JLabel("New Vertex's ID:"));
            add_node_input_panel.add(key);
            add_node_input_panel.add(Box.createVerticalStrut(15)); // a spacer
            add_node_input_panel.add(new JLabel("Geo-Location:"));
            add_node_input_panel.add(new JLabel("x:"));
            add_node_input_panel.add(x);
            add_node_input_panel.add(Box.createVerticalStrut(15)); // a spacer
            add_node_input_panel.add(new JLabel("y:"));
            add_node_input_panel.add(y);
            add_node_input_panel.add(Box.createVerticalStrut(15)); // a spacer
            add_node_input_panel.add(new JLabel("z:"));
            add_node_input_panel.add(z);

            int add_node_result = JOptionPane.showConfirmDialog(
                    null, add_node_input_panel,
                    "Add Vertex:",
                    JOptionPane.OK_CANCEL_OPTION);
            if (add_node_result == JOptionPane.OK_OPTION) {
                try {
                    int id = Integer.parseInt(key.getText());
                    double x_co = Double.parseDouble(x.getText());
                    double y_co = Double.parseDouble(y.getText());
                    double z_co = Double.parseDouble(z.getText());
                    graph_algo.getGraph().addNode(new Node(id, new Location(x_co, y_co, z_co)));
                    repaint();

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException iae) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                }

            }

            // Connect

        } else if (e.getSource() == connectButton) {

            JTextField add_source = new JTextField(5);
            JTextField add_destination = new JTextField(5);
            JTextField add_edge_weight = new JTextField(5);
            JPanel add_edge_input_panel = new JPanel();
            add_edge_input_panel.add(new JLabel("Source Vertex ID:"));
            add_edge_input_panel.add(add_source);
            add_edge_input_panel.add(Box.createVerticalStrut(15)); // a spacer
            add_edge_input_panel.add(new JLabel("Destination Vertex ID:"));
            add_edge_input_panel.add(add_destination);
            add_edge_input_panel.add(new JLabel("Weight:"));
            add_edge_input_panel.add(add_edge_weight);

            int add_edge_result = JOptionPane.showConfirmDialog(
                    null, add_edge_input_panel,
                    "Connect Vertices (Create Edge):",
                    JOptionPane.OK_CANCEL_OPTION);

            if (add_edge_result == JOptionPane.OK_OPTION) {
                try {
                    int src = Integer.parseInt(add_source.getText());
                    int dest = Integer.parseInt(add_destination.getText());
                    double w = Double.parseDouble(add_edge_weight.getText());

                    graph_algo.getGraph().connect(src, dest, w);
                    repaint();
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException iae) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Remove Edge

        } else if (e.getSource() == removeEdgeButton) {

            JTextField remove_source = new JTextField(5);
            JTextField remove_destination = new JTextField(5);
            JPanel remove_edge_input_panel = new JPanel();
            remove_edge_input_panel.add(new JLabel("Source Vertex ID:"));
            remove_edge_input_panel.add(remove_source);
            remove_edge_input_panel.add(Box.createVerticalStrut(15)); // a spacer
            remove_edge_input_panel.add(new JLabel("Destination Vertex ID:"));
            remove_edge_input_panel.add(remove_destination);

            int remove_edge_result = JOptionPane.showConfirmDialog(
                    null, remove_edge_input_panel,
                    "Remove Edge:",
                    JOptionPane.OK_CANCEL_OPTION);

            if (remove_edge_result == JOptionPane.OK_OPTION) {
                try {
                    int src = Integer.parseInt(remove_source.getText());
                    int dest = Integer.parseInt(remove_destination.getText());
                    graph_algo.getGraph().removeEdge(src, dest);
                    repaint();
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException iae) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Remove Node

        } else if (e.getSource() == removeNodeButton) {  //V

            JTextField remove_key = new JTextField(5);
            JPanel remove_node_input_panel = new JPanel();
            remove_node_input_panel.add(new JLabel("Vertex ID:"));
            remove_node_input_panel.add(remove_key);

            int remove_node_result = JOptionPane.showConfirmDialog(
                    null, remove_node_input_panel,
                    "Remove Vertex:", JOptionPane.OK_CANCEL_OPTION);
            if (remove_node_result == JOptionPane.OK_OPTION) {
                try {
                    int id = Integer.parseInt(remove_key.getText());
                    graph_algo.getGraph().removeNode(id);
                    repaint();
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException iae) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
            // Algorithms:

            // Save to json file

        } else if (e.getSource() == saveButton) {
            p = new ParameterWindow("s", this);
            p.setVisible(true);

            // Load from json file
        } else if (e.getSource() == loadButton) {
            p = new ParameterWindow("l", this);
            p.setBounds(380, 80, 550, 80);
            p.setVisible(true);


            // Shortest path

        } else if (e.getSource() == shortestPath) {

            JTextField SP_src = new JTextField(5);
            JTextField SP_dest = new JTextField(5);
            JPanel SP_input_panel = new JPanel();
            SP_input_panel.add(new JLabel("Source Vertex ID:"));
            SP_input_panel.add(SP_src);
            SP_input_panel.add(Box.createVerticalStrut(15)); // a spacer
            SP_input_panel.add(new JLabel("Destination Vertex ID:"));
            SP_input_panel.add(SP_dest);

            int SP_input_panel_result = JOptionPane.showConfirmDialog(
                    null, SP_input_panel,
                    "Shortest Path Between:", JOptionPane.OK_CANCEL_OPTION);
            if (SP_input_panel_result == JOptionPane.OK_OPTION) {
                try {
                    int src = Integer.parseInt(SP_src.getText());
                    int dest = Integer.parseInt(SP_dest.getText());

                    String SP_list = graph_algo.shortestPath(src, dest).toString();
                    SP_list = SP_list.substring(1, SP_list.length() - 1);
                    JOptionPane.showMessageDialog(null, SP_list, "The Shortest Path Is:", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException iae) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }


            // Shortest Path Distance:

        } else if (e.getSource() == shortestPathDist) {

            JTextField SPD_src = new JTextField(5);
            JTextField SPD_dest = new JTextField(5);
            JPanel SPD_input_panel = new JPanel();
            SPD_input_panel.add(new JLabel("Source Vertex ID:"));
            SPD_input_panel.add(SPD_src);
            SPD_input_panel.add(Box.createVerticalStrut(15)); // a spacer
            SPD_input_panel.add(new JLabel("Destination Vertex ID:"));
            SPD_input_panel.add(SPD_dest);

            int SPD_input_panel_result = JOptionPane.showConfirmDialog(
                    null, SPD_input_panel,
                    "Distance Between:", JOptionPane.OK_CANCEL_OPTION);
            if (SPD_input_panel_result == JOptionPane.OK_OPTION) {
                try {

                    int src = Integer.parseInt(SPD_src.getText());
                    int dest = Integer.parseInt(SPD_dest.getText());

                    double SPD = graph_algo.shortestPathDist(src, dest);
                    JOptionPane.showMessageDialog(null, "" + SPD, "The Distance Is:", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException iae) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }

            //TSP:

        } else if (e.getSource() == tspButton) {

            JTextField tsp_list = new JTextField(50);
            JPanel TSP_input_panel = new JPanel();
            TSP_input_panel.add(new JLabel("List Of Vertices ID's (i.e: id1, id2, ... , idn):"));
            TSP_input_panel.add(tsp_list);

            int TSP_input_panel_result = JOptionPane.showConfirmDialog(
                    null, TSP_input_panel,
                    "Shortest Travel:", JOptionPane.OK_CANCEL_OPTION);
            if (TSP_input_panel_result == JOptionPane.OK_OPTION) {
                try {
                    String[] tsp_string_list = tsp_list.getText().split(",");
                    List<NodeData> cities = new ArrayList<>();
                    for (int i = 0; i < tsp_string_list.length; i++) {
                        NodeData current = graph_algo.getGraph().getNode(Integer.parseInt(tsp_string_list[i]));
                        cities.add(current);
                    }

                    String SP_list = graph_algo.tsp(cities).toString();
                    SP_list = SP_list.substring(1, SP_list.length() - 1);
                    JOptionPane.showMessageDialog(null, SP_list, "The Shortest Path Is:", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException iae) {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (e.getSource() == isConnected) {

            String T = "Yep! The Graph is Connected!";
            String F = "Nope! Not Connected... Sorry :-(";
            String answer = (graph_algo.isConnected()) ? T : F;
            JOptionPane.showMessageDialog(null, answer, "Is This Graph Connected?", JOptionPane.CLOSED_OPTION);

        } else if (e.getSource() == getEdgeSize) {

            int edge_size = graph_algo.getGraph().edgeSize();
            String answer = ("There are " + edge_size + " edges in this graph.");
            JOptionPane.showMessageDialog(null, answer, "No. Of Edges:", JOptionPane.CLOSED_OPTION);


        } else if (e.getSource() == getNodeSize) {

            int node_size = graph_algo.getGraph().nodeSize();
            String answer = ("There are " + node_size + " vertices in this graph.");
            JOptionPane.showMessageDialog(null, answer, "No. of Vertices:", JOptionPane.CLOSED_OPTION);

        } else if (e.getSource() == center) {

            NodeData center = graph_algo.center();
            String answer = (null != center) ? "The ID of center vertex is: " + center.getKey() : "The graph is " +
                    "not connected and therefore has no center.";
            JOptionPane.showMessageDialog(null, answer, "Center:", JOptionPane.CLOSED_OPTION);


        }
    }


    public void save(String path) {
        graph_algo.save(path);

    }

    protected void load(String path) {
        graph_algo = new GraphAlgorithm();
        graph_algo.load(path);
        repaint();
    }

}