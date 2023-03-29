import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A class to test the functionality of IVehicle implementations.
 *
 * @author Felix
 */
public class VehicleTest{
    
    /* array of vehicle classes */
    private static final Class vehicleClasses[] = {ElectricVehicle.class, IceVehicle.class};
    
    /* array of vehicle constructor parameter types */
    private static final Class[] consParamTypes = {int.class, String.class, int.class, int.class};

    
    /**
     * A helper method that attemps to create a vehicle with legal characteristics (positive fuel capacity and consumption).
     * 
     * @param clazz the class of vehicle to attempt to create.
     * @param year the desired year for the vehicle.
     * @param name the desired name for the vehicle.
     * @param capacity the desired fuel capacity for the vehicle.
     * @param consumption the desired fuel consumption for the vehicle.
     * 
     * @throws IllegalArgumentException if either capacity or consumption are nonpositve, as that would make the vehicle 
     * illegal.
     */
    private IVehicle makeLegalVehicle(final Class clazz, final int year, final String name, 
                                      final int capacity, final int consumption){
        
        if (capacity <= 0 || consumption <= 0)
            throw new IllegalArgumentException("Test code error: capacity and consumption must both be positive.");

        IVehicle vehicle = null;
        try {
            final Constructor<IVehicle> cons = clazz.getConstructor(VehicleTest.consParamTypes);
            vehicle = cons.newInstance(year, name, capacity, consumption);
        
        } catch(NoSuchMethodException e){ // couldn't find constructor with correct signature
            final String errorMsg = 
                String.format("Couldn't find %s constructor with correct parameter-type list (int, String, int, int)", 
                clazz.getName());
            fail(errorMsg);
            
        } catch(InvocationTargetException e){ // couldn't find constructor with correct signature
            final String errorMsg = 
                String.format("Unexpected exception thrown calling %s(%d, %s, %d, %d): %s", 
                clazz.getName(), year, name, capacity, consumption, e.getCause());
            fail(errorMsg);

        }  catch (InstantiationException | IllegalAccessException e){ // shouldn't ever happen
                fail(e.getMessage());
        }
        return vehicle;
    }
    
    /**
     * A helper method that attemps to create a vehicle with illegal characteristics (nonpositive fuel capacity or 
     * consumption).
     * 
     * @param clazz the class of vehicle to attempt to create.
     * @param capacity the desired fuel capacity for the vehicle.
     * @param consumption the desired fuel consumption for the vehicle.
     * 
     * @throws IllegalArgumentException if capacity and consumption are both positive, as that would make the
     *         vehicle perfectly legal.
     */
    private void makeIllegalVehicle(final Class clazz, final int capacity, final int consumption){
    
        if (capacity > 0 && consumption > 0) 
          throw new IllegalArgumentException("Test code error: either capacity or consumption must be nonpositive.");
        
        try {
            final Constructor<IVehicle> cons = clazz.getConstructor(VehicleTest.consParamTypes); 
            Exception exception = assertThrows(InvocationTargetException.class, () -> 
                {
                    cons.newInstance(2022, "TeslaModel3", capacity, consumption);
                });
            
            assertTrue(exception.getCause() instanceof IllegalArgumentException, 
                       "Creating vehicle with nonpositive fuel capacity or consumption should have thrown IllegalArgumentException");
            
        }catch(NoSuchMethodException e){ // couldn't find constructor with correct signature
            final String errorMsg = 
                String.format("Couldn't find %s constructor with correct parameter-type list (int, String, int, int)", 
                clazz.getName());
            fail(errorMsg);
        }
    }
    
    /**
     * Tests the vehicle constructor methods, by checking whether they have the 
     * correct method signatures and reject negative capacity and consumption values.
     */
    @Test
    public void constructorTest(){
        
        for (Class clazz : VehicleTest.vehicleClasses){ // for each vehicle type
            makeLegalVehicle(clazz, 2032, "TeslaCybertruck", 10, 20); // valid capacity and consumption
            makeIllegalVehicle(clazz, -1, 1);  // invalid capacity
            makeIllegalVehicle(clazz,  1, -1); // invalid consumption
            makeIllegalVehicle(clazz, -1, -1); // invalid capacity and consumption
        }
    }

    
    /**
     * Tests whether the vehicle classes implement the toString() method correctly.
     * It should just print the vehicle year and name, in that order, separated by a single space.
     */
    @Test
    public void toStringTest(){
        
        for (Class clazz : VehicleTest.vehicleClasses){ 
            final IVehicle vehicle = makeLegalVehicle(clazz, 2042, "TeslaCybertruck", 1, 1);
            final String errorMsg = String.format("%s's toString() not returning year and name", clazz.getName());
            assertEquals("2042 TeslaCybertruck", vehicle.toString(), errorMsg);
        }        
    
    }
    
    /**
     * Tests whether range is computed correctly for both ICE and electric vehicles.
     */
    @Test
    public void rangeTest(){
        
        // for ICE vehicle
        IVehicle vehicle = new IceVehicle(2023, "SubaruBrz", 13, 27);
        String errorMsg = "Range of ICE vehicle with 13 gallon tank and 27 mpg != 351 miles";
        assertEquals(351.0, vehicle.range(), errorMsg);
        
        // for EV
        vehicle = new ElectricVehicle(2023, "FordMustangMachE", 500, 40);
        errorMsg = "Range of EV with 500 Wh battery and 40 Whpm != 12.5 miles";
        assertEquals(12.5, vehicle.range(), errorMsg);
    }
     
}
