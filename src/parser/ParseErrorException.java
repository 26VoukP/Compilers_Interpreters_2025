package parser;

/**
 * Custom exception class for handling parsing-specific errors.
 * 
 * @author Vouk Praun-Petrovic
 * @version October 2, 2025
 */
public class ParseErrorException extends Exception 
{
    /**
     * Constructs a new ParseErrorException with the specified detail message.
     *
     * @param message the detail message
     */
    public ParseErrorException(String message) 
    {
        super(message);
    }

    /**
     * Constructs a new ParseErrorException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ParseErrorException(String message, Throwable cause) 
    {
        super(message, cause);
    }
}