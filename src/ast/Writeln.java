package ast;

import environment.Environment;

/**
 * Represents a writeln statement in the AST.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public class Writeln extends Statement
{
    private final Expression expr;

    /**
     * Constructs a Writeln statement with the given expression.
     * 
     * @param expr the expression to be printed
     */
    public Writeln(Expression expr)
    {
        this.expr = expr;
    }

    /**
     * Returns the expression to be printed.
     * 
     * @return the expression
     */
    public Expression getExpr() 
    {
        return expr;
    }

    /**
     * Executes the writeln statement by evaluating the expression and printing its value.
     * 
     * @param env the environment in which to execute the statement
     */
    @Override
    public void exec(Environment env)
    {
        System.out.println(expr.eval(env));
    }
}