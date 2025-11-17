package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Represents a binary operation expression node in the AST.
 * Stores two operand expressions and an operator.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public class BinOp extends Expression
{

    private final Expression exp1;
    private final Expression exp2;
    private final String op;

    /**
     * Constructs a BinOp with the given left and right expressions and operator.
     * 
     * @param exp1 the left operand expression
     * @param op the operator
     * @param exp2 the right operand expression
     */
    public BinOp(Expression exp1, String op, Expression exp2)
    {
        this.exp1 = exp1;
        this.op = op;
        this.exp2 = exp2;
    }

    /**
     * Returns the left operand expression.
     * @return the left operand expression
     */
    public Expression getExp1() 
    {
        return exp1;
    }

    /**
     * Returns the right operand expression.
     * @return the right operand expression
     */
    public Expression getExp2() 
    {
        return exp2;
    }

    /**
     * Returns the operator as a string.
     * @return the operator
     */
    public String getOp() 
    {
        return op;
    }

    /**
     * Evaluates the binary operation in the given environment and returns its result.
     * 
     * @param env the environment in which to evaluate the expression
     * @return the integer result of the binary operation
     * @throws RuntimeException if the operator is unknown
     */
    @Override
    public int eval(Environment env)
    {
        int l = exp1.eval(env);
        int r = exp2.eval(env);
        return switch (op)
        {
            case "+" -> l + r;
            case "-" -> l - r;
            case "*" -> l * r;
            case "/" -> l / r;
            default -> throw new RuntimeException("Unknown operator '" + op + "'");
        };
    }

    /**
     * Compiles the binary operation into assembly code.
     * 
     * @param e the emitter to use to compile the binary operation
     * @throws RuntimeException if the operator is unknown
     */
    @Override
    public void compile(Emitter e)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        switch (op)
        {
            case "+" -> 
            {
                e.emit("addu $v0, $v0, $t0");
                e.emit(""); // adds a new line
            }
            case "-" -> 
            {
                e.emit("subu $v0, $v0, $t0");
                e.emit(""); // adds a new line
            }
            case "*" -> 
            {
                e.emit("mult $v0, $t0");
                e.emit("mflo $v0");
                e.emit(""); // adds a new line
            }
            case "/" -> 
            {
                e.emit("div $v0, $t0");
                e.emit("mflo $v0");
                e.emit(""); // adds a new line
            }
            default -> throw new RuntimeException("Unknown operator '" + op + "'");
        }
    }
    
}
