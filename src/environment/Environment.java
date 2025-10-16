package environment;

import java.util.*;

/**
 * Represents the environment in which variables are stored and retrieved.
 * 
 * @author Vouk
 * @version October 10, 2025
 */
public class Environment 
{
    private final Map<String, Integer> variables;

    /** 
     * Constructs a new Environment. 
     */
    public Environment()
    {
        variables = new HashMap<>();
    }

    /** Sets the value of a variable in the environment.
     * 
     * @param n the name of the variable
     * @param v the value to set the variable to
     */
    public void setVariable(String n, int v)
    {
        if (variables.containsKey(n))
        {
            variables.replace(n, v);
            return;
        }
        variables.put(n, v);
    }

    /** Gets the value of a variable in the environment.
     * 
     * @param n the name of the variable
     * @return the value of the variable
     */
    public int getVariable(String n)
    {
        if (!variables.containsKey(n))
            throw new NoSuchElementException("Variable " + n + " not found.");
        return variables.get(n);
    }
}
