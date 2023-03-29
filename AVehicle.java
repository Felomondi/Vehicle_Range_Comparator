
/**
 * @author (Felix)
 */
public abstract class AVehicle implements IVehicle {
    private int year;
    private String name;

    protected AVehicle(int year, String name) {
        this.year = year;
        this.name = name;
    }

    public String toString() {
        return year + " " + name;
    }

    public abstract double range();
}
