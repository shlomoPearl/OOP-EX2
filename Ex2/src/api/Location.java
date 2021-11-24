package api;

public class Location implements GeoLocation{

    private double x;
    private double y;
    private double z;

    public Location(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(Location other){
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(GeoLocation g) {
        double x_diff = Math.pow(this.x - g.x(),2);
        double y_diff = Math.pow(this.y - g.y(),2);
        double z_diff = Math.pow(this.z - g.z(),2);
        return Math.sqrt(x_diff + y_diff + z_diff);
    }
}
