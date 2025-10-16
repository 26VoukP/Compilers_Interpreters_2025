package ast;

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
}