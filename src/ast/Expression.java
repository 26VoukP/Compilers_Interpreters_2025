package ast;

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
}