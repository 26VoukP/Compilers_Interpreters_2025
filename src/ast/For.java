package ast;

import environment.Environment;

/**
 * Represents a for loop statement in the AST.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public class For extends Statement
{
    private final Assignment initialization;
    private final Statement varUpdate;
    private final Condition condition;
    private final Statement body;

    /**
     * Constructs a For statement with the given initialization, maximum number, and body.
     * 
     * @param initialization the initialization assignment
     * @param maxVal the maximum value expression
     * @param body the body of the for loop
     */
    public For(Assignment initialization, Expression maxVal, Statement body)
    {
        this.initialization = initialization;
        Variable loopVar = initialization.getVar();
        this.varUpdate = new Assignment(loopVar, new BinOp(loopVar, "+", new Number(1)));
        this.condition = new Condition(loopVar, "<", maxVal);
        this.body = body;
    }

    /**
     * Executes the for loop in the given environment.
     * 
     * @param env the environment in which to execute the statement
     */
    @Override
    public void exec(Environment env)
    {
        initialization.exec(env);
        while (condition.eval(env) == 1)
        {
            body.exec(env);
            varUpdate.exec(env);
        }
    }
    
}
