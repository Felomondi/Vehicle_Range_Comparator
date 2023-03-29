import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * A class to test the functionality of the VehicleRangeComparator user interface code.
 *
 * @author  Felix
 */
public class VehicleRangeComparatorTest{

    /**
     * A helper method that attemps to create a vehicle by passing in valid input to readVehicle().
     * 
     * @param vehicleClass the class of vehicle to attempt to create.
     * @param year the desired year for the vehicle.
     * @param name the desired name for the vehicle.
     * @param capacity the desired fuel capacity for the vehicle.
     * @param consumption the desired fuel consumption for the vehicle.
     * 
     * @throws IllegalArgumentException if vehicleClass is neither IceVehicle.class nor EvVehicle.class.
     */
    private void readValidVehicle(Class vehicleClass, final int year, final String name, 
    final int capacity, final int consumption){

        float expectedRange = 0;
        String vehicleType = null;
        if (vehicleClass == IceVehicle.class){
            vehicleType = "ice";
            expectedRange = capacity * consumption;
        } else if (vehicleClass == ElectricVehicle.class){
            vehicleType = "ev";
            expectedRange = capacity / (float) consumption;
        } else { 
            final String errorMsg = String.format("Unsupported vehicle class %s", vehicleClass);
            throw new IllegalArgumentException(errorMsg);
        }

        // set arguments
        final String inputStr = String.format("%s %d %s %d %d", vehicleType, year, name, capacity, consumption);
        final Scanner scanner = new Scanner(inputStr);

        // change default output stream
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // attempt to read vehicle
        final IVehicle vehicle = VehicleRangeComparator.readVehicle(scanner);

        // check vehicle characteristics
        assertTrue(vehicleClass.isInstance(vehicle)); // no instanceof - it only supports explicit (literal) class names
        final String expectedToString = String.format("%d %s", year, name);
        assertEquals(expectedToString, vehicle.toString());
        assertEquals(expectedRange, vehicle.range());

        final String expectedOutput  = "Specify vehicle: ";
        assertEquals(expectedOutput, outContent.toString(), "Expected message not printed");

    }

    /**
     * Attempts to pass an invalid input to readVehicle() and inspects its response.
     * 
     * @param input the user input to provide to readVehicle().
     * @param expectedErrorMsg the error message readVehicle() is expected to print to System.out.
     */    
    private void readInvalidVehicleSingle(final String input, final String expectedErrorMsg){
        readInvalidVehicleMultiple(input, expectedErrorMsg, 1);
    }

    /**
     * Attempts to pass an invalid input to readVehicle() multiple times in a row and inspects its response.
     * 
     * @param input the user input to provide to readVehicle().
     * @param expectedErrorMsg the error message readVehicle() is expected to print to System.out.
     * @param nattemps the number of times the invalid input should be provided to readVehicle().
     */
    private void readInvalidVehicleMultiple(final String input, final String expectedErrorMsg, int nattempts){

        // set arguments: invalid input nattempt times, followed by a valid input to make readVehicle() return
        final String singleInput = String.format("%s\n", input);
        final StringBuilder inputSb = new StringBuilder();
        for (int i=0; i < nattempts; i++) inputSb.append(singleInput);
        inputSb.append("ice 2023 HondaCivicTypeR 12 30");
        final Scanner scanner = new Scanner(inputSb.toString());

        // change default output stream
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // attempt to read vehicle
        VehicleRangeComparator.readVehicle(scanner);

        // set expected output: error message nattempt times, "Specify vehicle for each attempt".
        final String singleExpectedOutput = String.format("Specify vehicle: %s\n", expectedErrorMsg);
        final StringBuilder expectedOutputSb = new StringBuilder();
        for (int i=0; i < nattempts; i++) expectedOutputSb.append(singleExpectedOutput);
        expectedOutputSb.append("Specify vehicle: ");

        final String expectedStr = StringUtils.normalizeLineEnds(expectedOutputSb.toString());
        final String actualStr = StringUtils.normalizeLineEnds(outContent.toString());
        assertEquals(expectedStr, actualStr, "Expected error message not printed");
    }

    /**
     * Test whether the readVehicle() method adheres to the stated specification.
     */
    @Test
    public void readValidVehicleTest(){

        // 1. create valid vehicles

        // create an ice vehicle
        readValidVehicle(IceVehicle.class, 2023, "ChevroletCorvette", 19, 30);
        // create an ev vehicle
        readValidVehicle(ElectricVehicle.class, 2025, "RivianR1t", 150000, 300);
    }

    @Test
    public void readInvalidTypeVehicleTest(){
        // 2. attempt to create invalid vehicles
        // vehicle type is neither ice nor ev
        readInvalidVehicleSingle("fcev 2016 HondaClarity 1000 100", "Invalid vehicle type. Ice or ev only, please.");
    }

    @Test
    public void readTypeMismatchVehicleTest(){

        // non-integer value provided for integer field
        readInvalidVehicleSingle("ice ice HondaCivic 1000 1000", "Non-integer value provided for integer field. Please try again.");
        // readInvalidVehicleSingle("ice 2023 34 Honda 1000 1000", "Non-integer value provided for integer field. Please try again.");
        // readInvalidVehicleSingle("ice 2023 HondaCivicTypeR 1000.4 1000", "Non-integer value provided for integer field. Please try again.");
        // readInvalidVehicleSingle("ice 2023 HondaCivicTypeR CTR 1000", "Non-integer value provided for integer field. Please try again.");
        // readInvalidVehicleSingle("ice 2023 HondaCivicTypeR 12 1000.3", "Non-integer value provided for integer field. Please try again.");
        // readInvalidVehicleSingle("ice 2023 HondaCivicTypeR 12 championshipwhite", "Non-integer value provided for integer field. Please try again.");
    }

    @Test
    public void readNonPositiveEVParamVehicleTest(){
        // nonpositive battery capacity or consumption for EV
        readInvalidVehicleSingle("ev 2022 FordMustangMachE -10 100", "Battery capacity and energy consumption must both be positive. Please try again.");
        readInvalidVehicleSingle("ev 2022 FordMustangMachE 1000 -100", "Battery capacity and energy consumption must both be positive. Please try again.");
    }

    @Test
    public void readNonPositiveICEParamVehicleTest(){
        // nonpositive fuel capacity or consumption for ICE vehicle 
        readInvalidVehicleSingle("ice 2022 MazdaMx5 -10 100", "Fuel tank capacity and fuel consumption must both be positive. Please try again.");
        readInvalidVehicleSingle("ice 2022 MazdaMx5 12 -100", "Fuel tank capacity and fuel consumption must both be positive. Please try again.");

    }

    @Test
    public void readMultipleVehicleTest(){
        // check to see that the user can make multiple consecutive errors
        readInvalidVehicleMultiple("ice 2023 HondaCivicTypeR 12 1000.3", "Non-integer value provided for integer field. Please try again.", 10);
    }

    /**
     * A simple class containing utility methods to work with Strings.
     * 
     * @author Felix
     */
    private static class StringUtils {
        /**
         * Converts Windows-style line endings (carriage return, line feed combination)
         * to Unix-style (line feed only).
         * 
         * @param s the string to be normalized.
         * @return a String with the same contents as input s, except with any Windows-style
         * line endings converted to Unix-style endings.
         */
        public static String normalizeLineEnds(String s) {
            return s.replace("\r\n", "\n").replace('\r','\n');
        }
    }
}

