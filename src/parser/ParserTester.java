package parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import scanner.Scanner;

/**
 * A tester class for the Parser.
 */
public class ParserTester 
{
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
        
        Parser parser = new Parser(scanner);
        try 
        {
            parser.parseStatement();
        } 
        catch (ParseErrorException e) 
        {
            System.err.println("Parse error at line " + parser.getLineNumber() + ": " + e.getMessage());
        }
    }
}