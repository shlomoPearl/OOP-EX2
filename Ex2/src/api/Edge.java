package api;

public class Edge implements EdgeData {

    private int source;
    private int destination;
    //?private  double weight;
    private String info;
    private int tag;

    public Edge(int source, int destination){
        this.destination = destination;
        this.source = source;
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
        return 0;
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
