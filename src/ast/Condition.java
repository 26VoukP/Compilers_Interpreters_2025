package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Represents a condition in the AST.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public class Condition extends Expression
{
    private final Expression exp1;
    private final String op;
    private final Expression exp2;

    /**
     * Constructs a Condition with the given expressions and operator.
     * 
     * @param e1 the first expression
     * @param op the operator (e.g., "=", ">", "<", ">=", "<=", "<>")
     * @param e2 the second expression
     */
    public Condition(Expression e1, String op, Expression e2)
    {
        this.exp1 = e1;
        this.op = op;
        this.exp2 = e2;
    }

    /**
     * Evaluates the condition in the given environment and returns 1 if true, 0 if false.
     * 
     * @param env the environment in which to evaluate the condition
     * @return 1 if the condition is true, 0 if false
     */
    @Override
    public int eval(Environment env)
    {
        switch (op)
        {
            case "=" :
                return exp1.eval(env) == exp2.eval(env) ? 1 : 0;
            case ">" :
                return exp1.eval(env) > exp2.eval(env) ? 1 : 0;
            case "<" :
                return exp1.eval(env) < exp2.eval(env) ? 1 : 0;
            case ">=" :
                return exp1.eval(env) >= exp2.eval(env) ? 1 : 0;
            case "<=" :
                return exp1.eval(env) <= exp2.eval(env) ? 1 : 0;
            case "<>" :
                return exp1.eval(env) != exp2.eval(env) ? 1 : 0;
            default :
                throw new RuntimeException("Unkown operator '" + op + "'");
        }
    }

    /**
     * Compiles the condition into assembly code.
     * Generates code that evaluates the condition and branches to the target label
     * if the condition is true.
     * 
     * @param e the emitter to use to compile the condition
     * @param targetLabel the label to jump to if the condition is true
     * @throws RuntimeException if the operator is unknown
     */
    public void compile(Emitter e, String targetLabel)
    {
        exp1.compile(e);
        e.emit("move $t1, $v0   # Move first evaluated expression in conditional to $t1");
        exp2.compile(e);
        switch (op)
        {
            case "=" :
                e.emit("beq $t1, $v0, " + targetLabel);
                break;
            case ">" :
                e.emit("bgt $t1, $v0, " + targetLabel);
                break;
            case "<" :
                e.emit("blt $t1, $v0, " + targetLabel);
                break;
            case ">=" :
                e.emit("bge $t1, $v0, " + targetLabel);
                break;
            case "<=" :
                e.emit("ble $t1, $v0, " + targetLabel);
                break;
            case "<>" :
                e.emit("bne $t1, $v0, " + targetLabel);
                break;
            default :
                throw new RuntimeException("Unkown operator '" + op + "'");
        }
    }
}