# AI Coding Agent Instructions for Compilers and Interpreters 2025

Welcome to the Compilers and Interpreters 2025 project! This document provides essential guidelines for AI coding agents to be productive in this codebase.

## Project Overview
This project is part of coursework for The Harker School's Compilers/Interpreters class. The ultimate goal is to compile Pascal code into assembly. Currently, the Scanner package is implemented as a foundational component.

### Key Components
- **Scanner** (`src/scanner/Scanner.java`):
  - Reads input streams and tokenizes them into words, numbers, operators, etc.
  - Handles comments (`//` and `/* */`) and skips whitespace.
  - Throws `ScanErrorException` for invalid input.
- **Scanner Tester** (`src/scanner/ScannerTester.java`):
  - Validates the functionality of the Scanner.
  - Reads tokens from test files like `scannerTest.txt` and `scannerTestAdvanced.txt`.
  - Prints tokens to the console and handles errors gracefully.

## Developer Workflows
### Building the Project
- Compile the Java files using the following command:
  ```powershell
  javac -d bin src/**/*.java
  ```
  This will place the compiled `.class` files in the `bin` directory.

### Running Tests
- Execute the `ScannerTester` class to validate the Scanner:
  ```powershell
  java -cp bin scanner.ScannerTester
  ```

### Debugging
- Use the `scannerTestAdvanced.txt` file for advanced test cases.
- Modify `ScannerTester.java` to add custom test cases.

## Project-Specific Conventions
- **Error Handling**: Use `ScanErrorException` for invalid input scenarios.
- **Tokenization Rules**: Follow the rules defined in `Scanner.java` for handling comments, whitespace, and unrecognized characters.
- **Testing**: Place test files in the `src/scanner/` directory. Use descriptive names like `scannerTest.txt`.

## Integration Points
- **JFlex**: The `JFlexContactsScanner.flex` file in `src/jflex/` suggests potential integration with JFlex for lexical analysis. However, this is not yet fully integrated.
- **Dependencies**: The `lib/` folder is reserved for external dependencies, though none are currently specified.

## Examples
### Adding a New Token Type
1. Modify the `Scanner.java` class to include the new token type.
2. Update the `ScannerTester.java` to test the new token.
3. Add a test case in `scannerTestAdvanced.txt`.

### Handling Errors
- Example: Throwing `ScanErrorException` for invalid characters.
  ```java
  if (!isValidCharacter(c)) {
      throw new ScanErrorException("Invalid character: " + c);
  }
  ```

---

Feel free to update this document as the project evolves!