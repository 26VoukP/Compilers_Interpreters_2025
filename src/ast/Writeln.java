package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Represents a writeln statement in the AST.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public class Writeln extends Statement
{
    private final Expression expr;

    /**
     * Constructs a Writeln statement with the given expression.
     * 
     * @param expr the expression to be printed
     */
    public Writeln(Expression expr)
    {
        this.expr = expr;
    }

    /**
     * Returns the expression to be printed.
     * 
     * @return the expression
     */
    public Expression getExpr() 
    {
        return expr;
    }

    /**
     * Executes the writeln statement by evaluating the expression and printing its value.
     * 
     * @param env the environment in which to execute the statement
     */
    @Override
    public void exec(Environment env)
    {
        System.out.println(expr.eval(env));
    }

    /**
     * Compiles the writeln statement into assembly code.
     * @param e the emitter to use to compile the writeln statement
     */
    public void compile(Emitter e)
    {
        expr.compile(e);
        e.emit("move $a0, $v0");
        e.emit("li $v0, 1");
        e.emit("syscall");
        e.emit("la $a0, newline");
        e.emit("li $v0, 4");
        e.emit("syscall");
    }
}