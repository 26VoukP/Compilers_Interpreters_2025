package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Abstract base class for all expression nodes in the AST.
 * 
 * @author Vouki
 * @version October 15, 2025
 */
public abstract class Expression 
{
    /**
     * Evaluates the expression in the given environment and returns its numeric value.
     * 
     * @param env the environment in which to evaluate the expression
     * @return the integer of the expression
     */
    public abstract int eval(Environment env);

    /**
     * Compiles the expression into assembly code.
     * 
     * @param e the emitter to use to compile the expression
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Expression compile method not implemented.");
    }
}