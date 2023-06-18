# Microlexer

## About the project

I have always wanted to understand what happens behind the scene every time I type my code in C, Python, Java, and
many other programming languages. The only way to do so is to build a small compiler(OK but maybe not from scratch :)).
The fun of engineering is to construct things and see how far they can take you. That is exactly what this project is
about. It reflects my journey in learning about compilers. Although my knowledge on the topic is still shallow, I will
continue to update it, and any feedback will be much appreciated.

## References

* My book of
  choice: [The Dragon Book](https://www.amazon.com/Compilers-Principles-Techniques-Tools-2nd/dp/0321486811).
* I also borrowed some ideas and read a lot of code from these excellent resources:
    * [DoctorWkt on Github](https://github.com/DoctorWkt/acwj)
    * [Bob Nystrom's blog on Pratt's Parser](https://journal.stuffwithstuff.com/2011/03/19/pratt-parsers-expression-parsing-made-easy/)
    * [Matklad's Pratt parser implementation in Rust](https://matklad.github.io/2020/04/13/simple-but-powerful-pratt-parsing.html)
* You cannot write a compiler without looking at some other compilers! So I chose the following list of compilers for
  references(mostly on grammar):
    * [C's grammar](https://learn.microsoft.com/en-us/cpp/c-language/c-language-syntax-summary?view=msvc-170)
    * [Swift's grammar](https://docs.swift.org/swift-book/documentation/the-swift-programming-language/summaryofthegrammar#app-top)
    * [Swift's compiler](https://www.swift.org/swift-compiler/)
    * [Kotlin's grammar](https://kotlinlang.org/docs/reference/grammar.html)

## Computer system basics

There are several phases in the compilation process:

* **Preprocessing**: modifies source code by processing include statements, directives and macros.
* **Compiling**: compiles preprocessed source code to assembly.
* **Assembling**: turns assembly instructions into relocatable machine code.
* **Linking**: links relocatable machine code with code from other object files to produce executables.

## Compiler v.s Interpreter

* **Compiler**: compiles the source language to a target language.
* **Interpreter**: executes the source code to produce some output.
* **Hybrid**: this combines a compiler with an interpreter. For example, Java Virtual Machine(JVM) first compiles Java
  source code to an assembly-like language called bytecodes. JVM's interpreter then executes bytecode instructions to
  produce some output.

## Compilation phases

* **Lexing (or tokenizing, aka lexical analysis)**: tokenizes the code and splits it into small units called
  tokens which is composed of a type, a string value, and a line number that the token is on.
* **Parsing**: We can divide parsing into two smaller phases.
    * **Syntax analysis**: consumes the tokens and "stitches" them together by following some rules, or
      grammar. The result produced by parser is an abstract syntax tree, or AST.
    * **Semantic analysis**: figures out what the code is trying to do. Some things to do in this phase are
      type checking and type conversion.

## Lexer

The code for lexer is in the package `lexers`. It includes:

* **CharReader**: has an internal buffer which holds characters read from the input stream (usually a file).
* **AlnumUnderscoreLexer**: reads alphanumeric and underscore characters to form a lexeme.
* **KeywordLexer**: inherits from AlnumUnderscoreLexer and reads a keyword lexeme using the KeywordTable object.
* **TypeLexer**: inherits from AlnumUnderscoreLexer and reads a data type lexeme using the TypeTable object.
* **OpLexer**: reads an operator lexeme using the OperatorTable object.
* **NumLexer**: reads a number(integer or floating-point) lexeme.
* **Lexer**: uses one of the component lexers above to read a lexeme in a switch-case or if-else fashion.

## Tables

### Keyword table

* The code for the keyword table is in the package `keywords`.
* The table is handcoded so values are predetermined.
* It stores all keywords that have been reserved for the language.

### Operator table

* The code for the operator table is in the package `operators`.
* Similar to the keyword table, it is also handcoded.
* It is a collection of tables that store operators and their properties: one for prefix, one for infix, one for postfix
  , one for precedences, one for associativities, and two for type compatibilities between operands.

### Type table

* The code for the type table is in the package `types`.
* Like other tables, it is handcoded.
* There are two tables:
    * One maps strings as type IDs to objects that store type information.
    * One maps a data type to another data type if the first one can be cast to the second(explicitly or implicitly).