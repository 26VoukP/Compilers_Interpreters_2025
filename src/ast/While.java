package ast;

import emitter.Emitter;
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

    /**
     * Compiles the while loop into assembly code.
     * Generates a loop that checks the condition at the start of each iteration
     * and exits when the condition is false.
     * 
     * @param e the emitter to use to compile the while loop
     */
    @Override
    public void compile(Emitter e)
    {
        int labelID = e.nextLabelID();
        String loopStartLabel = "loopStart" + labelID;
        String loopEndLabel = "loopEnd" + labelID;
        String bodyLabel = "loopBody" + labelID;
        e.emit(loopStartLabel + ":"); // Compile condition - if true, jump to body; if false, fall through to end
        
        condition.compile(e, bodyLabel);// If condition is false, jump to end
        
        e.emit("j " + loopEndLabel);// Emit body label and compile body
        e.emit(bodyLabel + ":");
        body.compile(e);
        e.emit("j " + loopStartLabel);
        e.emit(loopEndLabel + ":");
    }
}