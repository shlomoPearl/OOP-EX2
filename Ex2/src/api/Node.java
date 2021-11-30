package api;

import java.util.HashMap;

public class Node implements NodeData {

    private int key;
    private Location location;
    private String info;
    private int tag;
//    private HashMap<Integer, Node> neighbors;

    public Node(int key, Location geo) {
        this.key = key;
        this.location = new Location(geo);
        this.tag = 0;
        this.info = "";
    }

    public Node(Node n) {
        this.key = n.key;
        this.location = new Location(n.location);
        this.tag = n.tag;
        this.info = n.info;
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public GeoLocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.location = new Location((Location) p);
    }

    //pass
    @Override
    public double getWeight() {
        return 0;
    }

    //pass
    @Override
    public void setWeight(double w) {

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
