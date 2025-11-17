package ast;

import emitter.Emitter;
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

    /**
     * Compiles the variable reference into assembly code.
     * Generates code that loads the variable's value from memory into register $v0.
     * 
     * @param e the emitter to use to compile the variable reference
     */
    @Override
    public void compile(Emitter e)
    {
        e.emit("la $t0, var" + name);
        e.emit("lw $v0, ($t0)");
    }
}