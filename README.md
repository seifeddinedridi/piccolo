## Piccolo: experimental toy compiler
Piccolo is a work-in-progress toy programming language aimed to be my educational playground for computer language parsing and machine code generation.

In its current state, the compiler can recognize:

* Basic arithmetic expressions (+,-,*,/)
* Variable declarations
* Functions

As output, the compiler only generates an AST (Abstract Syntax Tree) if the input program is valid, so there is no interpreter or bytecode generation module yet. 
