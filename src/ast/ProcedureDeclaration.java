package ast;

import environment.Environment;

/**
 * Represents a procedure declaration in the AST.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public class ProcedureDeclaration extends Statement
{
    private final String name;
    private final Statement body;
    private final String[] args;

    /**
     * Constructs a ProcedureDeclaration with the given name, arguments, and body.
     * 
     * @param name the name of the procedure
     * @param body the body of the procedure
     */
    public ProcedureDeclaration(String name, String[] args, Statement body) 
    {
        this.name = name;
        this.body = body;
        this.args = args;
    }

    public void setArgs(Expression[] argVals, Environment oldEnv, Environment newEnv)
    {
        int l = args.length;
        if (argVals.length != l)
        {
            throw new RuntimeException("Invalid number of arguments passed to procedure " + name);
        }
        for (int i = 0; i < l; i++)
        {
            newEnv.declareVariable(args[i], argVals[i].eval(oldEnv));
        }
        newEnv.declareVariable(name, 0);
    }

    /**
     * Runs the body of the procedure.
     */
    public int runProcedure(Expression[] argVals, Environment env)
    {
        Environment localEnv = new Environment(env.getRoot()); // creates a child of root environment
        setArgs(argVals, env, localEnv);
        body.exec(localEnv);
        return localEnv.getVariable(name);
    }

    /**
     * Executes the procedure declaration in the given environment.
     * 
     * @param env the environment in which to execute the procedure declaration
     */
    @Override
    public void exec(Environment env) 
    {
        env.setProcedure(name, this);
    }
}
