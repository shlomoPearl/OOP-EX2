import api.GraphAlgorithm;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
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

    api.GraphAlgorithm graph_algo;

    public GraphRepresentationWindow() {
        initFrame();
        addMenu();
        initPanel();
    }

    private void initPanel() {
        JPanel panel = new JPanel();
//        jPanel.setLayout(new FlowLayout());
        panel.setBackground(new Color(150,250,250));
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
        JLabel response = new JLabel("");

        if (e.getSource() == saveButton) { ///V
            p = new ParameterWindow("s", this);
            p.setVisible(true);

        } else if (e.getSource() == loadButton) {  ///V
            p = new ParameterWindow("l", this);
            p.setBounds(380, 80, 550, 80);
            p.setVisible(true);

        } else if (e.getSource() == addNodeButton) { //
            p = new ParameterWindow("AN", this);
            p.setVisible(true);

        } else if (e.getSource() == connectButton) {
            p = new ParameterWindow("c", this);
            p.setVisible(true);

        } else if (e.getSource() == shortestPath) { ///
            p = new ParameterWindow("SP", this);
            p.setVisible(true);

        } else if (e.getSource() == shortestPathDist) { ///
            p = new ParameterWindow("SPD", this);
            p.setVisible(true);

        } else if (e.getSource() == removeEdgeButton) {   ///
            JTextField source = new JTextField(5);
            JTextField destination = new JTextField(5);
            JFrame panel_frame = new JFrame();
            panel_frame.setSize(100, 500);
            JPanel myPanel = new JPanel();
            myPanel.add(new JLabel("Source Vertex:"));
            myPanel.add(source);
            myPanel.add(Box.createVerticalStrut(15)); // a spacer
            myPanel.add(new JLabel("Destination Vertex:"));
            myPanel.add(destination);


            int result = JOptionPane.showConfirmDialog(
                    myPanel, myPanel,
                    "Input Box",
                    JOptionPane.OK_CANCEL_OPTION);
//            //if (result == JOptionPane.OK_OPTION) {
//              //  System.out.println("source: " + source.getText());
//               // System.out.println("dest: " + destination.getText());
//            }
///*
//            String delete_edge = JOptionPane.showInputDialog("source: ")+
//                    ","+JOptionPane.showInputDialog("destination: ");
//            String [] src_dest = delete_edge.split(",");
//            if (src_dest.length == 2) {
//                try {
//                    int src = Integer.parseInt(src_dest[0]);
//                    int dest = Integer.parseInt(src_dest[1]);
//                    graph_algo.getGraph().removeEdge(src, dest);
//                }catch (IllegalArgumentException exception){
//                    JOptionPane.showMessageDialog(null,"Source and destination should be numbers","Wrong!",JOptionPane.ERROR_MESSAGE);
//                }
//            }
////            p = new ParameterWindow("RE", this);
////            p.setVisible(true);
//
//*/


        } else if (e.getSource() == removeNodeButton) {  //V

            String id = JOptionPane.showInputDialog("Which Node delete?");
            try {
                int idDelete = Integer.parseInt(id);

                graph_algo.getGraph().removeNode(idDelete);
            }catch (IllegalArgumentException exception){
                JOptionPane.showMessageDialog(null,"The ID should be numbers","Wrong!",JOptionPane.ERROR_MESSAGE);
            }

//            p = new ParameterWindow("RN", this);
//            p.setVisible(true);

        } else if (e.getSource() == tspButton) {


//            p = new ParameterWindow("TSP", this);
//            p.setVisible(true);

        } else if (e.getSource() == isConnected) {

//            response.setText(answer);
//            response.setBounds(500, 40, response.getPreferredSize().width, response.HEIGHT);
//            response.setFont(new Font("ComicSans", Font.BOLD, 50));
//            response.setVisible(true);
//            this.add(response);

            String T = "Yay! The Graph is Connected!";
            String F = "Nope! Not Connected... Sorry :-(";
            String answer = (graph_algo.isConnected()) ? T : F;
            JOptionPane.showMessageDialog(null,answer,"is connected?",JOptionPane.CLOSED_OPTION);

        } else if (e.getSource() == getEdgeSize) {  //V

            int edge_size = graph_algo.getGraph().edgeSize();
            String answer = ("There are " + edge_size + " Edges in this graph");
            JOptionPane.showMessageDialog(null,answer,"No. Of Edges:",JOptionPane.CLOSED_OPTION);


        } else if (e.getSource() == getNodeSize) {  //V

            int node_size = graph_algo.getGraph().nodeSize();
            String answer = ("There are " + node_size + " Nodes in this graph");
            JOptionPane.showMessageDialog(null,answer,"No. Of Nodes:",JOptionPane.CLOSED_OPTION);

        } else if (e.getSource() == center) {

            int center = graph_algo.center().getKey();
            String answer = (" The ID number of center vertex is: " + center);
            JOptionPane.showMessageDialog(null,answer,"Center:",JOptionPane.CLOSED_OPTION);

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


    public static void main(String[] args) {
        new GraphRepresentationWindow();
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
