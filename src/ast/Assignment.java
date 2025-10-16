package ast;

import environment.Environment;

/**
 * Represents an assignment statement in the AST, consisting of a variable and an expression.\
 * 
 * @author Vouk
 * @version Octoboer 10, 2025
 */
public class Assignment extends Statement 
{
    private final Variable var;
    private final Expression expr;

    /**
     * Constructs an Assignment with the given variable and expression.
     *
     * @param var the variable to assign to
     * @param expr the expression to assign
     */
    public Assignment(Variable var, Expression expr) 
    {
        this.var = var;
        this.expr = expr;
    }

    /**
     * Returns the variable being assigned to.
     *
     * @return the variable
     */
    public Variable getVar() 
    {
        return var;
    }

    /**
     * Returns the expression assigned to the variable.
     *
     * @return the expression
     */
    public Expression getExpr() 
    {
        return expr;
    }

    /**
     * Executes the variable by adding the appropriate variable to the environment.
     * 
     * @param env the current environment
     */
    @Override
    public void exec(Environment env)
    {
        env.setVariable(var.getName(), expr.eval(env));
    }
}