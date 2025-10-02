package scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * A tester class for the Scanner.
 * Reads tokens from a .txt file and prints them one by one.
 * 
 * @author Vouk Praun-Petrovic
 * @version September 3, 2025
 */
public class ScannerTester 
{
    /**
     * The path to the test file used by the ScannerTester.
     * Precondition: The file path must point to a valid, readable file.
     * Postcondition: The file path is stored as a constant.
     */
    public static final String TEST_FILE_PATH = "src/scanner/scannerTestAdvanced.txt";

    /**
     * The main method to run the ScannerTester.
     * Precondition: The test file exists and is accessible. 
     * Postcondition: Tokens from the test file are printed to the console, or errors are reported.
     * @param args command-line arguments
     */
    public static void main(String[] args) 
    {
        try 
        {
            // Pass a .txt file to the Scanner constructor
            FileInputStream fileInputStream = new FileInputStream(TEST_FILE_PATH);
            Scanner scanner = new Scanner(fileInputStream);

            // Read tokens one by one
            while (scanner.hasNext()) 
            {
                try 
                {
                    String token = scanner.nextToken().getKey();
                    System.out.println("Token found: " + token);
                } 
                catch (ScanErrorException e) 
                {
                    System.err.println("ScanErrorException: " + e.getMessage());
                    break; // Stop processing on unrecognized character
                }
            }
        } 
        catch (FileNotFoundException e) 
        {
            System.err.println("File not found: " + e.getMessage());
        }
    }
}
