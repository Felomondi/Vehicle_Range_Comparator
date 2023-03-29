
/**
 * Interface that specifies the behaviors a vehicle object should support.
 * 
 * @author Felix
 * @data 2022/08/04
 */
public interface IVehicle {
    
    /**
     * A textual representation of the vehicle composed by the vehicle year and name, separated by a single space.
     * For example, "2023 HondaCivicTypeR".
     * 
     * @return A String representing the vehicle.
     */
    String toString();
    
    
    
    /**
     * The number of miles the vehicle can travel on a single charge/fuel tank, based on its storage capacity and consumption.
     * 
     * @return the vehicle's range, in miles.
     */
    double range();
}