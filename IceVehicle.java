
/**
 * An implementation of the ICE vehicle class .
 *
 * @author (Felix)
 */
public class IceVehicle extends AVehicle {
    private final int fuelCap;
    private final int mpg;

    public IceVehicle(final int year, final String name, final int fuelCap, final int mpg) {
        super(year, name);
        this.fuelCap = fuelCap;
        this.mpg = mpg;
        if (fuelCap <= 0 || mpg <= 0) {
            throw new IllegalArgumentException("Fuel tank capacity and fuel consumption must both be positive");
        }
        
    }

    public double range() {
        return (double) fuelCap * (double) mpg;
    }//calculates the range as a product
}
