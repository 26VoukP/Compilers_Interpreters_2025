package ast;

import environment.Environment;
import java.util.*;

/**
 * Represents a block of statements in the AST.
 * 
 * @author Vouk
 * @version October 15, 2025
 */
public class Block extends Statement
{
    /**
     * The list of statements in this block.
     */
    private final List<Statement> statements;

    /**
     * Constructs an empty block.
     */
    public Block()
    {
        this.statements = new ArrayList<>();
    }

    /**
     * Returns the list of statements in this block.
     *
     * @return the list of statements
     */
    public List<Statement> getStatements() 
    {
        return statements;
    }

    /**
     * Adds a statement to this block.
     *
     * @param stmt the statement to add
     */
    public void addStatement(Statement stmt)
    {
        statements.add(stmt);
    }

    /**
     * Executes all statements in this Block
     * 
     * @param env the envirnoment in which the code is being run
     */
    @Override
    public void exec(Environment env)
    {
        for (Statement stmt : statements)
        {
            stmt.exec(env);
        }
    }
}