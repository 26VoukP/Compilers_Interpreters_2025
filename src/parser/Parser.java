package parser;

import ast.*;
import java.util.*;
import scanner.ScanErrorException;
import scanner.Scanner;

/**
 * The Parser class is responsible for parsing tokens provided by the Scanner.
 * It validates the syntax of the input and provides methods to parse specific constructs.
 * 
 * @author Vouk Praun-Petrovic
 * @version October 2, 2025
 */
public class Parser 
{
    private String lexeme; // The current lexeme being processed
    private String lexemeType; // The type of the current lexeme
    private AbstractMap.SimpleEntry<String, String> token;
    private Scanner scanner; // The Scanner instance providing tokens
    private static final String STATEMENT_TERMINATOR = ";";
    public static final String OPENING_KEYWORD = "BEGIN";
    public static final String CLOSING_KEYWORD = "END";
    public static final String PRINT_KEYWORD = "WRITELN";
    public static final String IF_KEYWORD = "IF";
    public static final String THEN_KEYWORD = "THEN";
    public static final String ELSE_KEYWORD = "ELSE";
    public static final String WHILE_KEYWORD = "WHILE";
    public static final String FOR_KEYWORD = "FOR";
    public static final String TO_KEYWORD = "TO";
    public static final String LOOP_OPENER = "DO";
    public static final String READLN_KEYWORD = "READLN";
    public static final String ASSIGN = ":=";
    public static final String OPEN_ARGS = "(";
    public static final String CLOSE_ARGS = ")";

    /**
     * Constructs a Parser
     * Precondition: The Scanner instance is not null.
     * Postcondition: The Parser is initialized with the provided Scanner.
     * @param s the Scanner instance to use for tokenizing input
     */
    public Parser(Scanner s) 
    {
        this.scanner = s;
        try 
        {
            this.token = scanner.nextToken();
        } 
        catch (ScanErrorException e) 
        {
            System.err.println("ScanErrorException: " + e.getMessage());
            System.exit(1); // Exit the program with a non-zero status
        }
        this.lexeme = token.getKey();
        this.lexemeType = token.getValue();
    }

    /**
     * Returns the current line number.
     *
     * Precondition: None.
     * Postcondition: The current line number is returned.
     *
     * @return the current line number
     */
    public int getLineNumber() 
    {
        return scanner.getCurrentLine();
    }

    /**
     * Consumes the expected lexeme and advances to the next token.
     *
     * Precondition: The expected lexeme is not null.
     * Postcondition: The current lexeme is updated to the next token.
     *
     * @param expectedLexeme the lexeme expected to be consumed
     * @throws ParseErrorException if an error occurs while reading the next token
     */
    private void eat(String expectedLexeme) throws ParseErrorException 
    {
        if (!lexeme.equals(expectedLexeme)) 
        {
            throw new ParseErrorException("Expected: " + expectedLexeme 
                    + ", found: " + lexeme);
        }

        try 
        {
            this.token = scanner.nextToken();
            this.lexeme = token.getKey();
            this.lexemeType = token.getValue();
        } 
        catch (ScanErrorException e) 
        {
            throw new ParseErrorException("ScanErrorException: " + e.getMessage());
        }
    }

    /**
     * Checks if the current lexeme matches the expected lexeme.
     *
     * Precondition: The expected lexeme is not null.
     * Postcondition: Throws an exception if the current lexeme does not match the expected lexeme.
     *
     * @param expectedLexeme the expected lexeme
     * @throws ParseErrorException if the current lexeme does not match the expected lexeme
     */
    public void checkExpectedLexeme(String expectedLexeme) throws ParseErrorException 
    {
        if (!lexeme.equals(expectedLexeme)) 
        {
            throw new ParseErrorException("Unexpected token: " + lexeme + " at line " + getLineNumber());
        }
    }

    /**
     * Checks if the current lexeme satisfies the given condition.
     *
     * Precondition: The condition is not null.
     * Postcondition: Throws an exception if the condition is not satisfied.
     *
     * @param expectedLexeme the expected lexeme
     * @param condition a predicate to test the lexeme
     * @throws ParseErrorException if the condition is not satisfied
     */
    public void checkExpectedLexeme(
            String expectedLexeme, 
            java.util.function.Predicate<String> condition
    ) throws ParseErrorException 
    {
        if (!condition.test(lexeme)) 
        {
            throw new ParseErrorException("Unexpected token: " + lexeme + " at line " + getLineNumber());
        }
    }

    /**
     * Parses a statement, which can be a program, print statement, or variable definition.
     * 
     * Precondition: The current lexeme is the start of a valid statement.
     * Postcondition: The statement is parsed, and the lexeme is advanced past the statement
     *
     * @throws ParseErrorException if the syntax of the statement is invalid
     */
    public Statement parseStatement() throws ParseErrorException 
    {
        if (lexeme.equals(OPENING_KEYWORD)) 
        {
            return parseBlock();
        } 
        else if (lexeme.equals(PRINT_KEYWORD)) 
        {
            return parsePrintStatement();
        }
        else if (lexeme.equals(IF_KEYWORD))
        {
            return parseIfStatement();
        }
        else if (lexeme.equals(WHILE_KEYWORD))
        {
            return parseWhileStatement();
        }
        else if (lexeme.equals(FOR_KEYWORD))
        {
            return parseForStatement();
        }
        else if (lexeme.equals(READLN_KEYWORD))
        {
            return parseReadlnStatement();
        }
        else if (lexemeType.equals(Scanner.IDENTIFIER))
        {
            return parseDefinition();
        }
        else if (lexeme.equals(Scanner.EOF))
        {
            throw new ParseErrorException("Unexpected end of file at line " + getLineNumber());
        }
        else 
        {
            throw new ParseErrorException("Unexpected token: " + lexeme + " at line " + getLineNumber());
        }
    }

    /**
     * Parses a program enclosed between BEGIN and END.
     *
     * Precondition: The current lexeme is "BEGIN".
     * Postcondition: The program is parsed, and the lexeme is advanced past 
     * "END" and the statement terminator.
     *
     * @throws ParseErrorException if the syntax of the program is invalid
     */
    private Block parseBlock() throws ParseErrorException
    {
        eat(OPENING_KEYWORD);
        Block block = new Block();
        while (!lexeme.equals(CLOSING_KEYWORD))
        {
            block.addStatement(parseStatement());
        }
        eat(CLOSING_KEYWORD);
        eat(STATEMENT_TERMINATOR);
        return block;
    }

    /**
     * Parses a print statement of the form WRITELN(expression).
     *
     * Precondition: The current lexeme is "WRITELN".
     * Postcondition: The print statement is parsed, and the lexeme is 
     * advanced past the statement terminator.
     *
     * @throws ParseErrorException if the syntax of the print statement is invalid
     */
    private Writeln parsePrintStatement() throws ParseErrorException
    {
        eat(PRINT_KEYWORD);
        eat(OPEN_ARGS);
        Expression expr = parseTerm();
        eat(CLOSE_ARGS);
        eat(STATEMENT_TERMINATOR);
        return new Writeln(expr);
    }

    /**
     * Parses an if statement of the form IF condition THEN statement [ELSE statement].
     *
     * Precondition: The current lexeme is "IF".
     * Postcondition: The if statement is parsed, and the lexeme is advanced 
     * past the statement terminator.
     *
     * @throws ParseErrorException if the syntax of the if statement is invalid
     */
    private If parseIfStatement() throws ParseErrorException
    {
        eat(IF_KEYWORD);
        Condition i = parseCondition();
        eat(THEN_KEYWORD);
        Statement t = parseStatement();
        if (!lexeme.equals(ELSE_KEYWORD))
        {
            return new If(i, t);
        }
        eat(ELSE_KEYWORD);
        Statement e = parseStatement();
        return new If(i, t, e);
    }

    /**
     * Parses a condition of the form expression operator expression.
     *
     * Precondition: The current lexeme is the start of a valid condition.
     * Postcondition: The condition is parsed, and the lexeme is advanced 
     * past the condition.
     *
     * @throws ParseErrorException if the syntax of the condition is invalid
     */
    private Condition parseCondition() throws ParseErrorException
    {
        List<String> validOperators = Arrays.asList("=", "<>", "<", ">", "<=", ">=");
        Expression e1 = parseTerm();
        if (!lexemeType.equals(Scanner.OPERATOR))
        {
            throw new ParseErrorException("'" +lexeme + "' is not an operator.");
        }
        else if (!validOperators.contains(lexeme))
        {
            throw new ParseErrorException("'" + lexeme + "' is not a valid boolean operator.");
        }
        String op = lexeme;
        eat(lexeme);
        Expression e2 = parseTerm();
        return new Condition(e1, op, e2);
    }

    /**
     * Parses a while statement of the form WHILE condition DO statement.
     *
     * Precondition: The current lexeme is "WHILE".
     * Postcondition: The while statement is parsed, and the lexeme is advanced 
     * past the statement terminator.
     *
     * @throws ParseErrorException if the syntax of the while statement is invalid
     */
    private While parseWhileStatement() throws ParseErrorException
    {
        eat(WHILE_KEYWORD);
        Condition c = parseCondition();
        eat(LOOP_OPENER);
        Statement s = parseStatement();
        return new While(c, s);
    }

    /**
     * Parses a for statement of the form FOR variable := expression TO expression DO statement.
     *
     * Precondition: The current lexeme is "FOR".
     * Postcondition: The for statement is parsed, and the lexeme is advanced 
     * past the statement terminator.
     *
     * @throws ParseErrorException if the syntax of the for statement is invalid
     */
    private For parseForStatement() throws ParseErrorException
    {
        eat(FOR_KEYWORD);
        Assignment initialization = parseDefinition();
        eat(TO_KEYWORD);
        Expression maxVal = parseTerm();
        eat(LOOP_OPENER);
        Statement body = parseStatement();
        return new For(initialization, maxVal, body);
    }

    /**
     * Parses a readln statement of the form READLN(variable).
     *
     * Precondition: The current lexeme is "READLN".
     * Postcondition: The readln statement is parsed, and the lexeme is advanced 
     * past the statement terminator.
     *
     * @throws ParseErrorException if the syntax of the readln statement is invalid
     */
    private Readln parseReadlnStatement() throws ParseErrorException
    {
        eat(READLN_KEYWORD);
        eat(OPEN_ARGS);
        if (!lexemeType.equals(Scanner.IDENTIFIER))
        {
            throw new ParseErrorException("Expected an identifier, found: " + lexeme);
        }
        Variable var = new Variable(lexeme);
        eat(lexeme);
        eat(CLOSE_ARGS);
        eat(STATEMENT_TERMINATOR);
        return new Readln(var);
    }

    /**
     * Parses a variable definition of the form identifier := expression.
     *
     * Precondition: The current lexeme is an identifier.
     * Postcondition: The variable is added to the variable table with its value, and the lexeme is
     * advanced past the statement terminator.
     *
     * @throws ParseErrorException if the syntax of the variable definition is invalid
     */
    private Assignment parseDefinition() throws ParseErrorException
    {
        int originalLine = getLineNumber();
        String varName = lexeme;
        eat(lexeme);
        eat(ASSIGN);
        Expression value = parseTerm();
        if (lexeme.equals(STATEMENT_TERMINATOR))
        {
            eat(STATEMENT_TERMINATOR);
        }
        else if (getLineNumber() != originalLine)
        {
            throw new ParseErrorException("Missing semicolon after definition at line " + originalLine);
        }
        return new Assignment(new Variable(varName), value);
    }

    /**
     * Parses a term, which is a factor followed by zero or more multiplication or 
     * division operators and factors.
     *
     * Precondition: The current lexeme is the start of a valid term.
     * Postcondition: The term is parsed, and the lexeme is advanced past the term.
     *
     * @return the parsed term
     * @throws ParseErrorException if the syntax of the term is invalid
     */
    public Expression parseTerm() throws ParseErrorException
    {
        Expression result = parseFactor();
        String op;
        while (true)
        {
            switch (lexeme)
            {
                case "*" -> 
                {
                    op = lexeme;
                    eat(lexeme);
                    result = new BinOp(result, op, parseFactor());
                }
                case "/" -> 
                {
                    op = lexeme;
                    eat(lexeme);
                    result = new BinOp(result, op, parseFactor());
                }
                case "+" -> 
                {
                    op = lexeme;
                    eat(lexeme);
                    result = new BinOp(result, op, parseTerm());
                }
                case "-" -> 
                {
                    op = lexeme;
                    eat(lexeme);
                    result = new BinOp(result, op, parseTerm());
                }
                default -> 
                {
                    return result;
                }
            }
        }
    }

    /**
     * Parses a factor, which can be a number, an identifier, or a parenthesized expression.
     *
     * Precondition: The current lexeme is the start of a valid factor.
     * Postcondition: The factor is parsed, and the lexeme is advanced past the factor.
     *
     * @return the parsed factor
     * @throws ParseErrorException if the syntax of the factor is invalid
     */
    public Expression parseFactor() throws ParseErrorException
    {
        if (lexeme.equals(OPEN_ARGS))
        {
            // It's a valid opening parenthesis
            eat(lexeme);
            // Parse the expression inside the parentheses
            Expression parsedTerm = parseTerm();
            eat(CLOSE_ARGS);
            return parsedTerm;
        }

        if (lexemeType.equals(Scanner.IDENTIFIER))
        {
            String varName = lexeme;
            eat(lexeme);
            return new Variable(varName);
        }
        else if (lexemeType.equals(Scanner.NUMBER))
        {
            int num;
            try
            {
                num = Integer.parseInt(lexeme);
            }
            catch (NumberFormatException e)
            {
                throw new ParseErrorException("Invalid number '" + lexeme + "'");
            }
            eat(lexeme);
            return new ast.Number(num);
        }
        else if (lexeme.equals("-"))
        {
            eat("-"); // unary minus
            return new BinOp(new ast.Number(-1), "*", parseFactor());
        }
        else
        {
            throw new ParseErrorException("Unexpected token: " + lexeme + " at line " + getLineNumber());
        }
    }

    /**
     * Checks if there are more tokens to parse.
     *
     * Precondition: None.
     * Postcondition: Returns true if the current lexeme is not EOF, false otherwise.
     *
     * @return true if there are more tokens, false otherwise
     */
    public boolean hasMoreTokens()
    {
        return !lexeme.equals(Scanner.EOF);
    }

    /**
     * Parses the entire file by repeatedly parsing statements until EOF is reached.
     *
     * Precondition: The file contains valid syntax.
     * Postcondition: All statements in the file are parsed.
     *
     * @throws ParseErrorException if the syntax of any statement is invalid
     */
    public Block parseFile() throws ParseErrorException
    {
        Block block = new Block();
        while (hasMoreTokens())
        {
            block.addStatement(parseStatement());
        }
        return block;
    }
}

