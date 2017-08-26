package classes;

public class Helpers {
    public static int randBetween(int max, int min) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
    public static double randBetween(double max, double min) {
        double range = (max - min) + 1;
        return (double)(Math.random() * range) + min;
    }

}
