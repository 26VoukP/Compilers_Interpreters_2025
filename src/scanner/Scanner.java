package scanner;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A Scanner is responsible for reading an input stream, one character at a
 * time, and separating the input into tokens.  A token is defined as:
 *  1. A 'word' which is defined as a non-empty sequence of characters that 
 *     begins with an alpha character and then consists of alpha characters, 
 *     numbers, the single quote character "'", or the hyphen character "-". 
 *  2. An 'end-of-sentence' delimiter defined as any one of the characters 
 *     ".", "?", "!".
 *  3. An end-of-file token which is returned when the scanner is asked for a
 *     token and the input is at the end-of-file.
 *  4. A phrase separator which consists of one of the characters ",",":" or
 *     ";".
 *  5. A digit.
 *  6. Any other character not defined above.
 * @author Vouk Praun-Petrovic
 * @version August 29, 2025
 *
 * Q: What if the next character had been a newline? An open parenthesis?
 * A: If the next character is a newline or '(', next token will return the 
 * token "if". When it is next called it will ignore the newline or return '(' if present.
 * 
 */
public class Scanner
{
    private final BufferedReader in;
    private char currentChar;
    private boolean eof;
    // private int lineNumber;

    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  
     * Precondition: inStream is not null.
     * Postcondition: Scanner is initialized and ready to read characters.
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        // lineNumber = 1;
        getNextChar();
    }
    /**
     * Scanner constructor for constructing a scanner that 
     * scans a given input string.  
     * Precondition: inString is not null.
     * Postcondition: Scanner is initialized and ready to read characters.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Reads the next character from the input stream into the currentChar variable.
     * Precondition: Input stream is open and readable.
     * Postcondition: currentChar is updated with the next character, or eof is set to true.
     */
    private void getNextChar()
    {
        try 
        {
            int next = in.read();
            if (next == -1 || (char) next == '.')
            {
                eof = true;
                currentChar = '\0';
            }
            else
            {
                currentChar = (char) next;
            }
        } 
        catch (IOException e) 
        {
            System.err.println("IOException occurred while reading input. Terminating program.");
            System.exit(1);
        }
    }

    /**
     * Consumes the expected character from the input stream.
     * Precondition: currentChar matches the expected character.
     * Postcondition: currentChar is updated to the next character in the stream.
     * @param expected the character expected to be read
     * @throws ScanErrorException if the expected character does not match the current character
     */
    private void eat(char expected) throws ScanErrorException
    {
        if (expected != currentChar)
        {
            throw new ScanErrorException(
                "Unexpected character read from input stream. Expected: "
                + expected + " got: " + currentChar);
        }
        getNextChar();
    }

    /**
     * Checks if the given character is a digit (0-9).
     * Precondition: None.
     * Postcondition: Returns true if the character is a digit, false otherwise.
     * @param c the character to check
     * @return true if the character is a digit, false otherwise
     */
    public static boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }

    /**
     * Checks if the given character is an alphabetic letter (a-z or A-Z).
     * Precondition: None.
     * Postcondition: Returns true if the character is a letter, false otherwise.
     * @param c the character to check
     * @return true if the character is a letter, false otherwise
     */
    public static boolean isLetter(char c)
    {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    /**
     * Checks if the given character is an operand.
     * Precondition: None.
     * Postcondition: Returns true if the character is an operand, false otherwise.
     * @param c the character to check
     * @return true if the character is an operand, false otherwise
     */
    public static boolean isOperand(char c) 
    {
        char[] operands = {'=', '+', '-', '*', '/', '%', '(', ')', '<', '>', ':'}; 
        for (char o : operands) 
        {
            if (c == o) 
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given token is a comment header ("//" or "/*").
     * Precondition: None.
     * Postcondition: Returns true if the token is a comment header, false otherwise.
     * @param token the token to check
     * @return true if the token is a comment header, false otherwise
     */
    public static boolean isCommentHeader(String token)
    {
        return token.equals("//") || token.equals("/*");
    }

    /**
     * Removes a comment from the input stream based on the given comment header token.
     * Precondition: commmentHeaderToken is either "//" or "/*".
     * Postcondition: The comment is skipped in the input stream.
     * @param commmentHeaderToken the comment header token ("//" or "/*")
     * @throws ScanErrorException if an incorrect comment header is passed
     */
    @SuppressWarnings("ConvertToStringSwitch")
    public void removeInputStreamComment(String commmentHeaderToken) throws ScanErrorException
    {
        if (commmentHeaderToken.equals("//"))
        {
            while (currentChar != '\n')
            {
                
                eat(currentChar);
            }
        }
        else if (commmentHeaderToken.equals("/*"))
        {
            String scannedToken = "";
            while (!scannedToken.equals("*/"))
            {
                if (eof)
                {
                    return;
                }
                else if (isOperand(currentChar))
                {
                    scannedToken = scanOperand();
                }
                else
                {
                    eat(currentChar);
                }
            }
        }
        else
        {
            throw new ScanErrorException("Incorrect comment header passed to comment remover.");
        }
    }

    /**
     * Checks if the given character is a whitespace character.
     * Precondition: None.
     * Postcondition: Returns true if the character is whitespace, false otherwise.
     * @param c the character to check
     * @return true if the character is whitespace, false otherwise
     */
    public static boolean isWhitespace(char c)
    {
        char[] whitespaceCharacters = {' ', '\t', '\r', '\n'}; 
        for (char ws : whitespaceCharacters) 
        {
            if (c == ws) 
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans and accumulates characters while any of the given conditions are true.
     * Precondition: conditions is not null and contains valid BooleanSupplier functions.
     * Postcondition: Returns the accumulated string of characters that satisfy the conditions.
     * @param conditions an ArrayList of BooleanSupplier functions 
     *        that return true to continue scanning
     * @return the accumulated string
     * @throws ScanErrorException if there's an error while reading characters
     */
    private String scanWhileCondition(
            ArrayList<java.util.function.BooleanSupplier> conditions
    ) throws ScanErrorException 
    {
        String result = "";
        boolean shouldContinue = true;
        while (shouldContinue) 
        {
            shouldContinue = false;
            for (java.util.function.BooleanSupplier condition : conditions)
            {
                if (condition.getAsBoolean()) 
                {
                    shouldContinue = true;
                    break;
                }
            }
            if (shouldContinue) 
            {
                result += Character.toString(currentChar);
                eat(currentChar);
            }
        }
        return result;
    }

    /**
     * Scans and returns a sequence of digits as a number token.
     * Precondition: currentChar is a digit.
     * Postcondition: Returns the scanned number token as a String.
     * @return the scanned number token as a String
     * @throws ScanErrorException if the current character is not a digit
     */
    private String scanNumber() throws ScanErrorException
    {
        if (!isDigit(currentChar))
        {
            throw new ScanErrorException("Value: " + currentChar + " received is not a number.");
        }
        ArrayList<java.util.function.BooleanSupplier> digitCheckers = new ArrayList<>(
                Arrays.asList(() -> isDigit(currentChar))
        );
        return scanWhileCondition(digitCheckers);
    }

    /**
     * Scans input stream and returns identifier.
     * Precondition: currentChar is a letter.
     * Postcondition: Returns the scanned identifier as a String.
     * @return String of identifier 
     * @throws ScanErrorException if the first character is not a letter
     */
    private String scanIdentifier() throws ScanErrorException
    {
        if (!isLetter(currentChar))
        {
            throw new ScanErrorException("Value: " + currentChar + " is not an identifier.");
        }
        ArrayList<java.util.function.BooleanSupplier> letCheckers = new ArrayList<>(Arrays.asList(
                () -> isLetter(currentChar),
                () -> isDigit(currentChar)
        ));
        return scanWhileCondition(letCheckers);
    }

    /**
     * Scans input stream and returns operand.
     * Precondition: currentChar is an operand.
     * Postcondition: Returns the scanned operand as a String.
     * @return String of operand
     * @throws ScanErrorException if the first character is not an operand
     */
    private String scanOperand() throws ScanErrorException
    {
        if (!isOperand(currentChar))
        {
            throw new ScanErrorException("Value: " + currentChar + " received is not an operand.");
        }
        ArrayList<java.util.function.BooleanSupplier> opCheckers = new ArrayList<>(Arrays.asList(
                () -> isOperand(currentChar)
        ));
        return scanWhileCondition(opCheckers);
    }

    /**
     * Checks if there is another character to be read from the input stream.
     * Precondition: None.
     * Postcondition: Returns true if there is another character to be read, false otherwise.
     * @return true if there is another character to be read from the input stream; otherwise,
     *         false
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * Returns the next token from the input stream.
     * Precondition: Input stream is open and readable.
     * Postcondition: Returns the next token as a String, or throws an exception 
     * for unrecognized characters.
     * @return the next token as a String
     * @throws ScanErrorException if an unexpected character is encountered
     */
    public String nextToken() throws ScanErrorException
    {
        // Skip whitespace
        while (!eof && (isWhitespace(currentChar)))
        {
            // if (currentChar == '\n') 
            // {
            //     lineNumber++;
            // }
            eat(currentChar);
        }
        if (eof)
        {
            return "EOF";
        }
        if (currentChar == ';')
        {
            eat(currentChar);
            return ";";
        }
        if (isOperand(currentChar))
        {
            String scannedOperand = scanOperand();
            if (isCommentHeader(scannedOperand))
            {
                removeInputStreamComment(scannedOperand);
                return nextToken();
            }
            return scannedOperand;
        }
        if (isDigit(currentChar))
        {
            return scanNumber();
        }
        else if (isLetter(currentChar))
        {
            return scanIdentifier();
        }
        else
        {
            throw new ScanErrorException(
                "Unrecognized character " + currentChar + " while getting next token"
            );
        }
    }
}
