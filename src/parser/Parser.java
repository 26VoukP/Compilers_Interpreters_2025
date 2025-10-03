package parser;

import java.util.*;
import scanner.ScanErrorException;
import scanner.Scanner;

/**
 * The Parser class is responsible for parsing tokens provided by the Scanner.
 * It validates the syntax of the input and provides methods to parse specific constructs.
 * 
 * @author Vouk Praun-Petrovic
 * @version October 2, 2025
 */
public class Parser 
{
    private String lexeme; // The current lexeme being processed
    private String lexemeType; // The type of the current lexeme
    private AbstractMap.SimpleEntry<String, 
            java.util.AbstractMap.SimpleEntry<String, Integer>> token;
    private int lineNumber; // The current line number
    private Scanner scanner; // The Scanner instance providing tokens
    private Map<String, Integer> varTable;
    private static final String STATEMENT_TERMINATOR = ";";

    /**
     * Constructs a Parser with the given Scanner.
     *
     * Precondition: The Scanner instance is not null.
     * Postcondition: The Parser is initialized and ready to parse tokens.
     *
     * @param s the Scanner instance to use for tokenizing input
     */
    public Parser(Scanner s) 
    {
        this.scanner = s;
        try 
        {
            this.token = scanner.nextToken();
        } 
        catch (ScanErrorException e) 
        {
            System.err.println("ScanErrorException: " + e.getMessage());
            System.exit(1); // Exit the program with a non-zero status
        }
        this.lineNumber = token.getValue().getValue();
        this.lexeme = token.getKey();
        this.lexemeType = token.getValue().getKey();
    }

    /**
     * Returns the current line number.
     *
     * Precondition: None.
     * Postcondition: The current line number is returned.
     *
     * @return the current line number
     */
    public int getLineNumber() 
    {
        return lineNumber;
    }

    /**
     * Consumes the expected lexeme and advances to the next token.
     *
     * Precondition: The expected lexeme is not null.
     * Postcondition: The current lexeme is updated to the next token.
     *
     * @param expectedLexeme the lexeme expected to be consumed
     * @throws IllegalArgumentException if the expected lexeme does not match the current lexeme
     */
    private void eat(String expectedLexeme) throws IllegalArgumentException 
    {
        if (!lexeme.equals(expectedLexeme)) 
        {
            throw new IllegalArgumentException("Expected: " + expectedLexeme 
                    + ", found: " + lexeme);
        }

        try 
        {
            this.token = scanner.nextToken();
            this.lineNumber = token.getValue().getValue();
            this.lexeme = token.getKey();
            this.lexemeType = token.getValue().getKey();
        } 
        catch (ScanErrorException e) 
        {
            System.err.println("ScanErrorException: " + e.getMessage());
            System.exit(1); // Exit the program with a non-zero status
        }
    }

    /**
     * Checks if the current lexeme matches the expected lexeme.
     *
     * Precondition: The expected lexeme is not null.
     * Postcondition: Throws an exception if the current lexeme does not match the expected lexeme.
     *
     * @param expectedLexeme the expected lexeme
     * @throws ParseErrorException if the current lexeme does not match the expected lexeme
     */
    public void checkExpectedLexeme(String expectedLexeme) throws ParseErrorException 
    {
        if (!lexeme.equals(expectedLexeme)) 
        {
            throw new ParseErrorException("Unexpected token: " + lexeme + " at line " + lineNumber);
        }
    }

    /**
     * Checks if the current lexeme satisfies the given condition.
     *
     * Precondition: The condition is not null.
     * Postcondition: Throws an exception if the condition is not satisfied.
     *
     * @param expectedLexeme the expected lexeme
     * @param condition a predicate to test the lexeme
     * @throws ParseErrorException if the condition is not satisfied
     */
    public void checkExpectedLexeme(
            String expectedLexeme, 
            java.util.function.Predicate<String> condition
    ) throws ParseErrorException 
    {
        if (!condition.test(lexeme)) 
        {
            throw new ParseErrorException("Unexpected token: " + lexeme + " at line " + lineNumber);
        }
    }

    /**
     * Parses a number from the current lexeme.
     *
     * Precondition: The current lexeme is a valid number.
     * Postcondition: The number is parsed and the lexeme is advanced.
     *
     * @return the parsed number
     * @throws ParseErrorException if the current lexeme is not a valid number
     */
    private int parseNumber() throws ParseErrorException 
    {
        int number;

        try 
        {
            number = Integer.parseInt(lexeme);
        } 
        catch (NumberFormatException e) 
        {
            throw new ParseErrorException("Invalid number: " + lexeme + " at line " + lineNumber);
        }
        eat(lexeme);
        return number;
    }


    /**
     * Parses a statement, which can be a program, print statement, or variable definition.
     * 
     * Precondition: The current lexeme is the start of a valid statement.
     * Postcondition: The statement is parsed, and the lexeme is advanced past the statement
     *
     * @throws ParseErrorException if the syntax of the statement is invalid
     */
    public void parseStatement() throws ParseErrorException 
    {
        if (lexeme.equals("BEGIN"))  
        {
            scanProgram();
        }
        else if (lexeme.equals("WRITELN")) 
        {
            scanPrintStatement();
        }
        else if (lexemeType.equals(Scanner.IDENTIFIER))
        {
            scanDefinition();
        }
        else if (lexeme.equals(Scanner.EOF))
        {
            // End of file reached, nothing to parse
        }
        else 
        {
            throw new ParseErrorException("Unexpected token: " + lexeme + " at line " + lineNumber);
        }
    }

    /**
     * Parses a program enclosed between BEGIN and END.
     *
     * Precondition: The current lexeme is "BEGIN".
     * Postcondition: The program is parsed, and the lexeme is advanced past 
     * "END" and the statement terminator.
     *
     * @throws ParseErrorException if the syntax of the program is invalid
     */
    private void scanProgram() throws ParseErrorException
    {
        eat("BEGIN");
        while (!lexeme.equals("END"))
        {
            parseStatement(); 
        }
        eat("END");
        eat(STATEMENT_TERMINATOR);
    }

    /**
     * Parses a print statement of the form WRITELN(expression).
     *
     * Precondition: The current lexeme is "WRITELN".
     * Postcondition: The print statement is parsed, and the lexeme is 
     * advanced past the statement terminator.
     *
     * @throws ParseErrorException if the syntax of the print statement is invalid
     */
    private void scanPrintStatement() throws ParseErrorException
    {
        eat("WRITELN");
        eat("(");
        System.out.println(parseTerm());
        eat(")");
        eat(STATEMENT_TERMINATOR);
    }

    /**
     * Parses a variable definition of the form identifier := expression.
     *
     * Precondition: The current lexeme is an identifier.
     * Postcondition: The variable is added to the variable table with its value, and the lexeme is
     * advanced past the statement terminator.
     *
     * @throws ParseErrorException if the syntax of the variable definition is invalid
     */
    private void scanDefinition() throws ParseErrorException
    {
        String varName = lexeme;
        eat(lexeme);
        eat(":=");
        int value = parseTerm();
        if (varTable == null) 
        {
            varTable = new HashMap<>();
        }
        varTable.put(varName, value);
        eat(STATEMENT_TERMINATOR);
    }

    /**
     * Parses a term, which is a factor followed by zero or more multiplication or 
     * division operators and factors.
     *
     * Precondition: The current lexeme is the start of a valid term.
     * Postcondition: The term is parsed, and the lexeme is advanced past the term.
     *
     * @return the parsed term
     * @throws ParseErrorException if the syntax of the term is invalid
     */
    public int parseTerm() throws ParseErrorException
    {
        int result = parseFactor();
        while (true) 
        {
            switch (lexeme) 
            {
                case "*" -> 
                {
                    eat(lexeme);
                    result *= parseFactor();
                }
                case "/" -> 
                {
                    eat(lexeme);
                    result /= parseFactor();
                }
                case "+" -> 
                {
                    eat(lexeme);
                    result += parseTerm();
                }
                case "-" -> 
                {
                    eat(lexeme);
                    result -= parseTerm();
                }
                default -> 
                {
                    return result;
                }
            }
        }
    }


    /**
     * Parses a factor, which can be a number, an identifier, or a parenthesized expression.
     *
     * Precondition: The current lexeme is the start of a valid factor.
     * Postcondition: The factor is parsed, and the lexeme is advanced past the factor.
     *
     * @return the parsed factor
     * @throws ParseErrorException if the syntax of the factor is invalid
     */
    public int parseFactor() throws ParseErrorException
    {
        if (lexeme.equals("("))
        {
            // It's a valid opening parenthesis
            eat(lexeme);
            // Parse the expression inside the parentheses
            int parsedTerm = parseTerm();
            eat(")");
            return parsedTerm;
        }

        if (lexemeType.equals(Scanner.IDENTIFIER))
        {
            String varName = lexeme;
            eat(lexeme);
            if (varTable != null && varTable.containsKey(varName)) 
            {
                return varTable.get(varName);
            } 
            else 
            {
                throw new ParseErrorException("Undefined variable: " 
                        + varName + " at line " + lineNumber);
            }
        }
        else if (lexemeType.equals(Scanner.NUMBER))
        {
            return parseNumber();
        }
        else if (lexeme.equals("-"))
        {
            // It's a unary minus
            eat("-");
            return -parseFactor();
        }
        else
        {
            throw new ParseErrorException("Unexpected token: " + lexeme + " at line " + lineNumber);
        }
    }

    /**
     * Checks if there are more tokens to parse.
     *
     * Precondition: None.
     * Postcondition: Returns true if the current lexeme is not EOF, false otherwise.
     *
     * @return true if there are more tokens, false otherwise
     */
    public boolean hasMoreTokens()
    {
        return !lexeme.equals(Scanner.EOF);
    }

    /**
     * Parses the entire file by repeatedly parsing statements until EOF is reached.
     *
     * Precondition: The file contains valid syntax.
     * Postcondition: All statements in the file are parsed.
     *
     * @throws ParseErrorException if the syntax of any statement is invalid
     */
    public void parseFile() throws ParseErrorException
    {
        while (hasMoreTokens())
        {
            parseStatement();
        }
    }
}

