package environment;

import ast.ProcedureDeclaration;
import java.util.*;

/**
 * Represents the environment in which variables are stored and retrieved.
 * 
 * @author Vouk
 * @version October 10, 2025
 */
public class Environment 
{
    private final Environment parent;
    private final Map<String, Integer> variables;
    private final Map<String, ProcedureDeclaration> procedures;

    /** 
     * Constructs a new Environment. 
     */
    public Environment()
    {
        parent = null;
        variables = new HashMap<>();
        procedures = new HashMap<>();
    }

    /**
     * Constructs an environemnt with the specified parent.
     * 
     * @param p the parent of the current environment.
     */
    public Environment(Environment p)
    {
        parent = p;
        variables = new HashMap<>();
        procedures = null;
    }

    /**
     * Goes up the tree of environments to get a reference to the root environment
     * 
     * @return the root environment
     */
    public Environment getRoot()
    {
        if (parent == null)
        {
            return this;
        }
        return parent.getRoot();
    }

    /** 
     * Declares a variable in the environment.
     * If it is already declared, it will be replaced.
     * 
     * @param n the name of the variable
     * @param v the value to set the variable to
     */
    public void declareVariable(String n, int v)
    {
        if (variables.containsKey(n))
        {
            variables.replace(n, v);
            return;
        }
        variables.put(n, v);
    }

    /** 
     * Sets the value of a variable in the environment if it is already declared. 
     * If it is not, it will be declared in the root environment.
     * 
     * @param n the name of the variable
     * @param v the value to set the variable to
     */
    public void setVariable(String n, int v)
    {
        if (variables.containsKey(n))
        {
            declareVariable(n, v);
        }
        else if (getRoot().variables.containsKey(n))
        {
            getRoot().declareVariable(n, v);
        }
        else
        {
            declareVariable(n, v);
        }
    }

    /** 
     * Gets the value of a variable in the environment.
     * 
     * @param n the name of the variable
     * @return the value of the variable
     */
    public int getVariable(String n)
    {
        if (!variables.containsKey(n))
        {
            Environment root = getRoot();
            if (!root.variables.containsKey(n))
            {
                this.setVariable(n, 0); // sets undefined variable to 0 automatically
                return 0;
            }
            return root.getVariable(n);
        }
        return variables.get(n);
    }

    /** 
     * Sets the procedure in the environment.
     * If it is already declared, it will be replaced.
     * 
     * @param n the name of the procedure
     * @param p the procedure to set
     */
    public void setProcedure(String n, ProcedureDeclaration p)
    {
        Map<String, ProcedureDeclaration> rootProcedures = getRoot().procedures;
        if (rootProcedures.containsKey(n))
        {
            rootProcedures.replace(n, p);
            return;
        }
        rootProcedures.put(n, p);
    }

    /** 
     * Gets the procedure in the environment.
     * 
     * @param n the name of the procedure
     * @return the procedure
     */
    public ProcedureDeclaration getProcedure(String n)
    {
        Map<String, ProcedureDeclaration> rootProcedures = getRoot().procedures;
        if (!rootProcedures.containsKey(n))
            throw new NoSuchElementException("Procedure " + n + " not found.");
        return rootProcedures.get(n);
    }
}
