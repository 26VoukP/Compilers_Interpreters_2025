package ast;

import environment.Environment;

/**
 * Represents a program that can contain procedure declarations and a main body.
 * A program can either be a procedure declaration followed by another program,
 * or just a main body statement.
 * 
 * @author Vouk Praun-Petrovic
 * @version October 2, 2025
 */
public class Program 
{
    private final ProcedureDeclaration procedure;
    private final Program childProgram;
    private final Statement mainBody;

    /**
     * Constructs a Program with a procedure declaration and a child program.
     *
     * Precondition: The procedure and childProgram parameters are not null.
     * Postcondition: A new Program is created with the specified procedure and child program.
     *
     * @param procedure the procedure declaration to include in this program
     * @param childProgram the child program that follows the procedure
     */
    public Program(ProcedureDeclaration procedure, Program childProgram) 
    {
        this.procedure = procedure;
        this.childProgram = childProgram;
        this.mainBody = null;
    }

    /**
     * Constructs a Program with only a main body (no procedure declaration).
     *
     * Precondition: The mainBody parameter is not null.
     * Postcondition: A new Program is created with only the main body.
     *
     * @param mainBody the main body of the program
     */
    public Program(Statement mainBody)
    {
        this.mainBody = mainBody;
        this.childProgram = null;
        this.procedure = null;
    }
    
    /**
     * Executes the program in the given environment.
     * 
     * Precondition: The environment is not null.
     * Postcondition: If the program contains a procedure declaration, it executes
     * the procedure and then the child program. Otherwise, it executes the main body.
     *
     * @param env the environment in which to execute the program
     */
    public void exec(Environment env) 
    {
        if (procedure != null)
        {
            procedure.exec(env);
            childProgram.exec(env);
        }
        else
        {
            mainBody.exec(env);
        }
    }
}
