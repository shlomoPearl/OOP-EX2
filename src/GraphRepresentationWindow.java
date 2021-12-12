import api.GraphAlgorithm;
import api.Location;
import api.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphRepresentationWindow extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
    ParameterWindow p;

    MenuItem loadButton = new MenuItem("Load Graph");
    MenuItem saveButton = new MenuItem("Save Graph");
    MenuItem addNodeButton = new MenuItem("Add Vertex");
    MenuItem connectButton = new MenuItem("Connect Vertices");
    MenuItem removeNodeButton = new MenuItem("Remove Vertex");
    MenuItem removeEdgeButton = new MenuItem("Remove Edge");
    MenuItem shortestPath = new MenuItem("Shortest Path");
    MenuItem shortestPathDist = new MenuItem("Shortest Path Distance");
    MenuItem isConnected = new MenuItem("Connected?");
    MenuItem center = new MenuItem("Graph Center");
    MenuItem getNodeSize = new MenuItem("No. of Vertices");
    MenuItem tspButton = new MenuItem("TSP");
    MenuItem getEdgeSize = new MenuItem("No. of Edges");

    api.GraphAlgorithm graph_algo = new GraphAlgorithm();

    public GraphRepresentationWindow() {
        initFrame();
        addMenu();
        initPanel();
        graph_algo.load("C:/Users/Hp/Documents/GitHub/OOP-EX2/data/1000Nodes.json");
    }

    public static void main(String[] args) {
        new GraphRepresentationWindow();
    }

    private void initPanel() {
        JPanel panel = new JPanel();
//        jPanel.setLayout(new FlowLayout());
        panel.setBackground(new Color(150, 250, 250));
        //jPanel.setBounds(380, 80, 800, 600);
        //   jPanel.setSize(800,600);
//        jPanel.setVisible(true);
        this.add(panel);


    }

    private void initFrame() {
        this.setLayout(new FlowLayout());
        this.setTitle("Directed Weighted Graph Representation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(300, 80, 800, 600);
        //this.setSize(800,600);
        this.setVisible(true);
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
                    int x_co = Integer.parseInt(x.getText());
                    int y_co = Integer.parseInt(y.getText());
                    int z_co = Integer.parseInt(z.getText());

                    graph_algo.getGraph().addNode(new Node(id, new Location(x_co, y_co, z_co)));
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
                        System.out.println(SP_list);
                        JOptionPane.showMessageDialog(null, SP_list, "The Shortest Path Is:", JOptionPane.INFORMATION_MESSAGE);

                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException iae) {
                        JOptionPane.showMessageDialog(null, "Invalid Input!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }

                } else if (e.getSource() == shortestPathDist) {
                p = new ParameterWindow("SPD", this);
                p.setVisible(true);

            } else if (e.getSource() == tspButton) {

            } else if (e.getSource() == isConnected) {

                String T = "Yay! The Graph is Connected!";
                String F = "Nope! Not Connected... Sorry :-(";
                String answer = (graph_algo.isConnected()) ? T : F;
                JOptionPane.showMessageDialog(null, answer, "is connected?", JOptionPane.CLOSED_OPTION);

            } else if (e.getSource() == getEdgeSize) {  //V

                int edge_size = graph_algo.getGraph().edgeSize();
                String answer = ("There are " + edge_size + " Edges in this graph");
                JOptionPane.showMessageDialog(null, answer, "No. Of Edges:", JOptionPane.CLOSED_OPTION);


            } else if (e.getSource() == getNodeSize) {  //V

                int node_size = graph_algo.getGraph().nodeSize();
                String answer = ("There are " + node_size + " Nodes in this graph");
                JOptionPane.showMessageDialog(null, answer, "No. Of Nodes:", JOptionPane.CLOSED_OPTION);

            } else if (e.getSource() == center) {

                int center = graph_algo.center().getKey();
                String answer = (" The ID number of center vertex is: " + center);
                JOptionPane.showMessageDialog(null, answer, "Center:", JOptionPane.CLOSED_OPTION);

            }
        }
    }


    public void save(String path) {
        graph_algo.save(path);
        System.out.println(graph_algo.getGraph());
    }

    protected void load(String path) {
        graph_algo = new GraphAlgorithm();
        graph_algo.load(path);
        System.out.println(graph_algo.getGraph());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
