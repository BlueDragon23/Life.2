package classes;

public class Location {
    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Location add(int x, int y) {
        return new Location(this.x + x, this.y + y);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Location)) {
            return false;
        }
        Location other = (Location) o;
        return (other.x == x) && (other.y == y);
    }

    public String toString() {
        return "x: " + x + ", y: " + y;
    }

}