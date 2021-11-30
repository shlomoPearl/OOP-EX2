package api;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class GraphAlgorithm implements DirectedWeightedGraphAlgorithms {

    private DirectedWeightedGraph g;

    public GraphAlgorithm(DirectedWeightedGraph g) {

    }

    @Override
    public void init(DirectedWeightedGraph g) {
        this.g = g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.g;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public NodeData center() {
        return null;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }

    @Override
    public boolean save(String file) {
        Gson gson=new Gson();
        try {
            gson.toJson(g, new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean load(String file) {

        return false;
    }
}
