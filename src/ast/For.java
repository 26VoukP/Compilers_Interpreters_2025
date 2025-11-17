package ast;

import emitter.Emitter;
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
    
    /**
     * Compiles the for loop into assembly code.
     * Generates a loop that initializes the loop variable, checks the condition,
     * executes the body, updates the loop variable, and repeats.
     * 
     * @param e the emitter to use to compile the for loop
     */
    @Override
    public void compile(Emitter e)
    {
        int labelID = e.nextLabelID();
        String loopStartLabel = "loopStart" + labelID;
        String loopEndLabel = "loopEnd" + labelID;
        String bodyLabel = "loopBody" + labelID;
        initialization.compile(e);
        e.emit(loopStartLabel + ": # start of for loop");
        condition.compile(e, bodyLabel);
        e.emit("j " + loopEndLabel);
        e.emit(bodyLabel + ": # body of for loop");
        body.compile(e);
        varUpdate.compile(e);
        e.emit("j " + loopStartLabel);
        e.emit("");
        e.emit(loopEndLabel + ": # end of for loop");
    }
}
