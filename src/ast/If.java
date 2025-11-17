package ast;

import emitter.Emitter;
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

    /**
     * Compiles the if statement into assembly code.
     * Generates code that evaluates the condition, branches to the if body if true,
     * and optionally handles an else clause.
     * 
     * @param e the emitter to use to compile the if statement
     */
    @Override
    public void compile(Emitter e)
    {
        int labelID = e.nextLabelID();
        String iftrueLabel = "iftrue" + labelID;
        String endifLabel = "endif" + labelID;
        
        // Compile condition - branch to iftrue if condition is true
        condition.compile(e, iftrueLabel);
        
        // If condition is false, jump to else or endif
        if (elseS != null)
        {
            String elseLabel = "else" + labelID;
            e.emit("j " + elseLabel);
            // Emit iftrue label and compile if body
            e.emit(iftrueLabel + ": # if true body");
            statement.compile(e);
            // Jump past else to endif
            e.emit("j " + endifLabel);
            // Emit else label and compile else body
            e.emit(elseLabel + ": # else body");
            elseS.compile(e);
        }
        else
        {
            e.emit("j " + endifLabel);
        
            e.emit(iftrueLabel + ": # if true body");
            statement.compile(e);
        }
        
        e.emit("");
        e.emit(endifLabel + ": # endif body");
    }
}