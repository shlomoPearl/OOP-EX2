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
    }


    private void initFrame() {
        this.setLayout(new FlowLayout());
        this.setTitle("Directed Weighted Graph Representation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(380, 80, 800, 600);
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

        if (e.getSource() == saveButton) {
            p = new ParameterWindow("s", this);
            p.setVisible(true);

        } else if (e.getSource() == loadButton) {
            p = new ParameterWindow("l", this);
            p.setBounds(380, 80, 550, 80);
            p.setVisible(true);

        } else if (e.getSource() == addNodeButton) {
            p = new ParameterWindow("AN", this);
            p.setVisible(true);

        } else if (e.getSource() == connectButton) {
            p = new ParameterWindow("c", this);
            p.setVisible(true);

        } else if (e.getSource() == shortestPath) {
            p = new ParameterWindow("SP", this);
            p.setVisible(true);

        } else if (e.getSource() == shortestPathDist) {
            p = new ParameterWindow("SPD", this);
            p.setVisible(true);

        } else if (e.getSource() == removeEdgeButton) {
            p = new ParameterWindow("RE", this);
            p.setVisible(true);

        } else if (e.getSource() == removeNodeButton) {
            p = new ParameterWindow("RN", this);
            p.setVisible(true);

        } else if (e.getSource() == tspButton) {
            p = new ParameterWindow("TSP", this);
            p.setVisible(true);

        } else if (e.getSource() == isConnected) {
            String T = "Yay! The Graph is Connected!";
            String F = "Nope! Not Connected... Sorry :-(";
            String answer = (graph_algo.isConnected()) ? T : F;
            response.setText(answer);
            response.setBounds(500, 40, response.getPreferredSize().width, response.HEIGHT);
            response.setFont(new Font("ComicSans", Font.BOLD, 50));
            response.setVisible(true);
            this.add(response);


        } else if (e.getSource() == getEdgeSize) {
            int edge_size = graph_algo.getGraph().edgeSize();
            String answer = ("There are " + edge_size + " Edges in this graph");
            response.setText(answer);
            response.setBounds(500, 40, response.getPreferredSize().width, response.HEIGHT);
            response.setFont(new Font("ComicSans", Font.BOLD, 50));
            response.setVisible(true);
            this.add(response);

        } else if (e.getSource() == getNodeSize) {


        } else if (e.getSource() == center) {

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
