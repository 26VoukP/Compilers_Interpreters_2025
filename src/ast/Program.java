package ast;

import emitter.Emitter;
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
    private final Variable[] globals;
    private final ProcedureDeclaration[] procedures;
    private final Statement mainBody;

    /**
     * Constructs a Program with the specified global variables, procedure declarations, and program body.
     *
     * Precondition: The vars, procDecs, and body parameters are not null.
     * Postcondition: A new Program is created with the specified global variables, procedure declarations, and program body.
     *
     * @param vars the global variables declared at the top of the program
     * @param procDecs the procedure declarations to include in this program
     * @param body the program body that follows the procedures and global declarations
     */
    public Program(Variable[] vars, ProcedureDeclaration[] procDecs, Statement body) 
    {
        this.globals = vars;
        this.procedures = procDecs;
        this.mainBody = body;
    }

    /**
     * Executes the program in the given environment.
     * 
     * @param env the environment in which to execute the program
     */
    public void exec(Environment env) 
    {
        if (procedures != null)
        {
            for (ProcedureDeclaration proc : procedures)
            {
                proc.exec(env);
            }
        }
        mainBody.exec(env);
    }

    /**
     * Compiles the program into assembly code.
     * Generates the data section with global variables, the text section with
     * the main function, and includes the main body code.
     * 
     * @param e the emitter to use to compile the program
     */
    public void compile(Emitter e)
    {
        e.emit(".data");
        e.emit("newline: .asciiz \"\\n\"");
        if (globals != null)
        {
            for (Variable dec : globals)
            {
                e.emit("var" + dec.getName() + ": .word 0");
            }
        }
        e.emit(".text");
        e.emit(".globl main");
        e.emit("main:");
        mainBody.compile(e);
        e.emit(""); // adds a new line
        e.emit("li $v0, 10");
        e.emit("syscall");
        e.close();
    }
}
