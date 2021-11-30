package api;

public class Edge implements EdgeData {

    private int source;
    private int destination;
    private double weight;
    private String info;
    private int tag;

    public Edge(int source, int destination, double weight) {
        this.destination = destination;
        this.source = source;
        this.weight = weight;
    }

    public Edge(Edge e) {
        this.source = e.source;
        this.destination = e.destination;
        this.tag = e.tag;
        this.info = e.info;
        this.weight = e.weight;
    }

    @Override
    public int getSrc() {
        return this.source;
    }

    @Override
    public int getDest() {
        return this.destination;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }
}
