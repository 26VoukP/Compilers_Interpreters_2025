package ast;

import environment.Environment;

/**
 * Represents an if statement in the AST.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public class If extends Statement
{
    private final Condition condition;
    private final Statement statement;
    private final Statement elseS;

    /**
     * Constructs an If statement with the given condition and statement.
     * 
     * @param c the condition to evaluate
     * @param s the statement to execute if the condition is true
     */
    public If(Condition c, Statement s)
    {
        this.condition = c;
        this.statement = s;
        this.elseS = null;
    }

    /**
     * Constructs an If statement with the given condition, statement, and else statement.
     * 
     * @param c the condition to evaluate
     * @param s1 the statement to execute if the condition is true
     * @param s2 the statement to execute if the condition is false
     */
    public If(Condition c, Statement s1, Statement s2)
    {
        this.condition = c;
        this.statement = s1;
        this.elseS = s2;
    }

    /**
     * Executes the if statement in the given environment.
     * 
     * @param env the environment in which to execute the statement
     */
    @Override
    public void exec(Environment env)
    {
        if (condition.eval(env) == 1)
        {
            statement.exec(env);
        }
        else if (condition.eval(env) == 0 && elseS != null)
        {
            elseS.exec(env);
        }
    }
}