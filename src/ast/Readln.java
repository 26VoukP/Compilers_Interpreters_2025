package ast;

import environment.Environment;
import java.util.AbstractMap;
import scanner.*;

/**
 * Represents a readln statement that reads an integer from standard input
 * and assigns it to a variable.
 * 
 * @author Vouk
 * @version October 10, 2025
 */
public class Readln extends Statement 
{
    private final Variable var;

    /** 
     * Constructs a Readln statement with the specified variable.
     * 
     * @param var the variable to assign the input value to
     */
    public Readln(Variable var) 
    {
        this.var = var;
    }

    /** 
     * Executes the readln statement by reading an integer from standard input
     * and assigning it to the specified variable in the given environment.
     * 
     * @param env the environment in which to execute the statement
     */
    @Override
    public void exec(Environment env)
    {
        Scanner scanner = new Scanner(System.in);
        try
        {
            AbstractMap.SimpleEntry<String, String> token = scanner.nextToken();
            if (!token.getValue().equals(Scanner.NUMBER))
                throw new ScanErrorException("Expected a number, got: " + token.getKey());
            
            env.setVariable(var.getName(), Integer.parseInt(token.getKey()));
        }
        catch (ScanErrorException e)
        {
            throw new RuntimeException("Error reading input: " + e.getMessage());
        }
    }
}