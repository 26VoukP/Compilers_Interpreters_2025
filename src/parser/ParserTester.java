package parser;

import ast.Block;
import environment.Environment;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import scanner.Scanner;

/**
 * A tester class for the Parser.
 * 
 * @author Vouk Praun-Petrovic
 * @version October 2, 2025
 */
public class ParserTester 
{
    /**
     * Main method to test the Parser with a sample input file.
     * 
     * precondition: The file "src/parser/ParseTester.txt" exists and is readable.
     * postcondition: The Parser is tested with the input from the file.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) 
    {
        Scanner scanner;
        try 
        {
            // Create a Scanner to scan the ParseTester.txt file
            scanner = new Scanner(new FileInputStream("src/parser/ParseTester.txt"));
        }
        catch (FileNotFoundException e) 
        {
            System.err.println("File not found: " + e.getMessage());
            return; // Exit if the file is not found
        }
        
        Environment env = new Environment();
        Parser parser = new Parser(scanner);
        try 
        {
            Block b = parser.parseFile();
            b.exec(env);
        } 
        catch (ParseErrorException e) 
        {
            System.err.println("Parsing error at line " + parser.getLineNumber() + 
                    ": " + e.getMessage());
        }
        catch (RuntimeException e)
        {
            System.err.println("Runtime error: " + e.getMessage());
        }
    }
}