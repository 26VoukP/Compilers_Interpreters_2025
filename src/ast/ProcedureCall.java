package ast;

import environment.Environment;

/**
 * Represents a procedure call statement in the AST.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public class ProcedureCall extends Expression
{
    private final String name;
    private final Expression[] argVals;

    /**
     * Constructs a ProcedureCall with the given environment and body.
     * 
     * @param name the name of the procedure being called.
     */
    public ProcedureCall(String n, Expression[] aV)
    {
        this.name = n;
        this.argVals = aV;
    }

    /**
     * Executes the procedure call in the given environment.
     * 
     * @param env the environment in which to execute the procedure call
     */
    @Override
    public int eval(Environment env)
    {
        return env.getProcedure(name).runProcedure(argVals, env);
    }
}