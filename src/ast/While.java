package ast;

import environment.Environment;

/**
 * Represents a while loop statement in the AST.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public class While extends Statement
{
    private final Condition condition;
    private final Statement body;

    /**
     * Constructs a While statement with the given condition and body.
     * 
     * @param condition the condition to evaluate
     * @param body the body of the while loop
     */
    public While(Condition condition, Statement body) 
    {
        this.condition = condition;
        this.body = body;
    }

    /**
     * Executes the while loop in the given environment.
     * 
     * @param env the environment in which to execute the statement
     */
    @Override
    public void exec(Environment env)
    {
        while (condition.eval(env) == 1)
        {
            body.exec(env);
        }
    }


}