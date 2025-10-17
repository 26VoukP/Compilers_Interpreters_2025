## Getting Started

Welcome to the Compilers and Interpreters 2025 project! This project is part of coursework for The Harker School's Compilers/Interpreters class. The ultimate goal of this project is to compile Pascal code into assembly. Currently, the Scanner package is implemented as one of the foundational components.

## Folder Structure

The workspace contains the following folders:

- `src`: Contains the source code for the various components of the compiler.
  - `scanner/Scanner.java`: The main class responsible for reading and tokenizing input streams.
  - `scanner/ScannerTester.java`: A tester class to validate the functionality of the Scanner.
- `lib`: The folder to maintain dependencies.
- `bin`: The folder where compiled output files are generated.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Components of the Scanner Package

### Scanner.java
- **Purpose**: Reads an input stream character by character and separates it into tokens.
- **Key Features**:
  - Tokenizes words, numbers, and operators.
  - Handles comments (both single-line `//` and multi-line `/* */`).
  - Skips whitespace and unrecognized characters.
  - Throws `ScanErrorException` for invalid input.

### ScannerTester.java
- **Purpose**: Tests the functionality of the Scanner class.
- **Key Features**:
  - Reads tokens from test files like `scannerTest.txt` and `scannerTestAdvanced.txt`.
  - Prints tokens to the console.
  - Handles errors gracefully, stopping on unrecognized characters.

## Components of the Parser Package

### Parser.java
- **Purpose**: Parses tokens from the Scanner and builds Abstract Syntax Trees (AST) for Pascal-like programs.
- **Key Features**:
  - **Statement Parsing**: Supports multiple statement types including:
    - **Block statements**: `BEGIN ... END` blocks containing multiple statements
    - **Assignment statements**: `variable := expression`
    - **Print statements**: `WRITELN(expression)`
    - **Input statements**: `READLN(variable)`
    - **Conditional statements**: `IF condition THEN statement [ELSE statement]`
    - **Loop statements**: `WHILE condition DO statement` and `FOR variable := expression TO expression DO statement`
  - **Expression Parsing**: Handles arithmetic expressions with proper operator precedence
  - **Condition Parsing**: Supports comparison operators (`=`, `<>`, `<`, `>`, `<=`, `>=`)
  - **Error Handling**: Throws `ParseErrorException` for syntax errors with line number information

### ParserTester.java
- **Purpose**: Tests the functionality of the Parser class.
- **Key Features**:
  - Tests parsing of various Pascal-like constructs
  - Validates syntax error detection
  - Demonstrates AST construction

## Components of the AST Package

The AST (Abstract Syntax Tree) package provides a hierarchical representation of parsed code that can be evaluated to execute programs.

### Statement Nodes
- **Statement.java**: Abstract base class for all statement nodes
- **Block.java**: Represents a block of statements (`BEGIN ... END`)
- **Assignment.java**: Represents variable assignments (`variable := expression`)
- **Writeln.java**: Represents print statements (`WRITELN(expression)`)
- **Readln.java**: Represents input statements (`READLN(variable)`)
- **If.java**: Represents conditional statements with optional else clauses
- **While.java**: Represents while loops (`WHILE condition DO statement`)
- **For.java**: Represents for loops (`FOR variable := expression TO expression DO statement`)

### Expression Nodes
- **Expression.java**: Abstract base class for all expression nodes
- **Number.java**: Represents numeric literals
- **Variable.java**: Represents variable references
- **BinOp.java**: Represents binary operations (`+`, `-`, `*`, `/`)
- **Condition.java**: Represents boolean conditions for comparisons

### Key Features
- **Tree Structure**: Hierarchical representation of program structure
- **Evaluation**: Each node can be evaluated in an environment to execute the program
- **Environment Integration**: Uses the `Environment` class to manage variable storage and retrieval
- **Type Safety**: Strong typing with proper inheritance hierarchy

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
