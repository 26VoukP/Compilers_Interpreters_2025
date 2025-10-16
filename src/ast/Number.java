package ast;

import environment.Environment;

/**
 * Represents a numeric literal in the AST.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public class Number extends Expression
{
    private final int value;

    /**
     * Constructs a Number with the given integer value.
     * @param value the integer value of this numeric literal
     */
    public Number(int value) 
    {
        this.value = value;
    }

    /**
     * Returns the integer value of this numeric literal.
     * @return the integer value
     */
    public int getValue() 
    {
        return value;
    }

    /**
     * Performs addition with another Number and returns a new Number.
     * @param other the other Number to add
     * @return a new Number representing the sum
     */
    public Number add(Number other)
    {
        return new Number(this.value + other.value);
    }

    /**
     * Performs subtraction with another Number and returns a new Number.
     * @param other the other Number to subtract
     * @return a new Number representing the difference
     */
    public Number subtract(Number other)
    {
        return new Number(this.value - other.value);
    }

    /**
     * Performs multiplication with another Number and returns a new Number.
     * @param other the other Number to multiply
     * @return a new Number representing the product
     */
    public Number multiply(Number other)
    {
        return new Number(this.value * other.value);
    }

    /**
     * Performs division with another Number and returns a new Number.
     * @param other the other Number to divide by
     * @return a new Number representing the quotient
     */
    public Number divide(Number other)
    {
        if (other.value == 0) 
        {
            throw new ArithmeticException("Division by zero");
        }
        return new Number(this.value / other.value);
    }

    @Override
    public int eval(Environment env) 
    {
        return value;
    }
}