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

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
