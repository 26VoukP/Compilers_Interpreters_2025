package ast;

import environment.Environment;

public class Program extends Statement {
    private final ProcedureDeclaration procedure;
    private final Statement childProgram;

    public Program(ProcedureDeclaration procedure, Program childProgram) 
    {
        this.procedure = procedure;
        this.childProgram = childProgram;
    }

    public Program(Statement mainBody)
    {
        childProgram = mainBody;
        procedure = null;
    }
    
    /**
     * Executes the program in the given environment.
     * 
     * @param env the environment in which to execute the program
     */
    @Override
    public void exec(Environment env) 
    {
        if (procedure != null)
        {
            procedure.exec(env);
        }
        childProgram.exec(env);
        
    }
    
}
