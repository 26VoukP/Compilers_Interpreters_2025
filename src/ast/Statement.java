package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Abstract base class for all statement nodes in the AST.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public abstract class Statement 
{
    /**
     * Executes the statement in the given environment.
     * @param env the environment in which to execute the statement
     */
    public abstract void exec(Environment env);

    /**
     * Compiles the statement into assembly code.
     * 
     * @param e the emitter to use to compile the statement
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Statement compile method not implemented.");
    }
}
