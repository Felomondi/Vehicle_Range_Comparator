
/**
 * An implementation of the electric vehicle class.
 *
 * @author (Felix )
 */
public class ElectricVehicle extends AVehicle {
    private final int batteryCap;
    private final int whpm;

    public ElectricVehicle(final int year, final String name, final int batteryCap, final int whpm) {
        super(year, name);
        this.batteryCap = batteryCap;
        this.whpm = whpm;
        
        if (batteryCap <= 0 || whpm <= 0) {
            throw new IllegalArgumentException("Battery capacity and energy consumption must both be positive");
        }
        
    }

    public double range() {
        return (double) batteryCap / (double) whpm;
    }// Calculates the range as a quotient 
}
