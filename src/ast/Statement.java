package ast;

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
}
