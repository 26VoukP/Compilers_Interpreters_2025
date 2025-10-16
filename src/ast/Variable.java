package ast;

import environment.Environment;

/**
 * Represents a variable in the AST.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public class Variable extends Expression
{
    private final String name;

    /**
     * Constructs a Variable with the given name.
     * 
     * @param name the name of the variable
     */
    public Variable(String name)
    {
        this.name = name;
    }

    /**
     * Returns the name of the variable.
     * 
     * @return the variable name
     */
    public String getName() 
    {
        return name;
    }

    /**
     * Evaluates the variable by looking up its value in the environment.
     * 
     * @param env the environment in which to evaluate the variable
     * @return the integer value of the variable
     */
    @Override
    public int eval(Environment env) 
    {
        return env.getVariable(name);
    }
}