package ast;

import environment.Environment;

/**
 * Represents a procedure declaration in the AST.
 * A procedure declaration defines a named procedure with parameters and a body.
 * 
 * @author Vouk Praun-Petrovic
 * @version October 2, 2025
 */
public class ProcedureDeclaration extends Statement
{
    private final String name;
    private final Statement body;
    private final String[] args;

    /**
     * Constructs a ProcedureDeclaration with the given name, arguments, and body.
     *
     * Precondition: The name, args, and body parameters are not null.
     * Postcondition: A new ProcedureDeclaration is created with the specified name,
     * arguments, and body.
     *
     * @param name the name of the procedure
     * @param args the parameter names for the procedure
     * @param body the body of the procedure
     */
    public ProcedureDeclaration(String name, String[] args, Statement body) 
    {
        this.name = name;
        this.body = body;
        this.args = args;
    }

    /**
     * Sets the argument values for the procedure in the new environment.
     *
     * Precondition: The argVals, oldEnv, and newEnv parameters are not null.
     * Postcondition: The argument values are evaluated and stored in the new environment
     * as variables with the parameter names.
     *
     * @param argVals the argument values to set
     * @param oldEnv the environment to evaluate the argument values in
     * @param newEnv the environment to store the argument values in
     * @throws RuntimeException if the number of argument values doesn't match the number of 
     * parameters
     */
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
     * Runs the body of the procedure with the given argument values.
     *
     * Precondition: The argVals and env parameters are not null.
     * Postcondition: The procedure is executed in a new local environment with the
     * argument values set, and the return value is retrieved.
     *
     * @param argVals the argument values to pass to the procedure
     * @param env the environment in which to run the procedure
     * @return the value of the procedure variable after execution
     */
    public int runProcedure(Expression[] argVals, Environment env)
    {
        Environment localEnv = new Environment(env.getRoot()); // createing child of root env
        setArgs(argVals, env, localEnv);
        body.exec(localEnv);
        return localEnv.getVariable(name);
    }

    /**
     * Executes the procedure declaration in the given environment.
     * This registers the procedure in the environment so it can be called later.
     *
     * Precondition: The environment is not null.
     * Postcondition: The procedure is registered in the environment with its name.
     *
     * @param env the environment in which to execute the procedure declaration
     */
    @Override
    public void exec(Environment env) 
    {
        env.setProcedure(name, this);
    }
}
