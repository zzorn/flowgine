package org.skycastle.flowgine.buildlang;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.MemoMismatches;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.support.Var;
import org.skycastle.flowgine.buildlang.ast.*;

/**
 * Parses geometry definition language.
 */
// The language is more or less procedural functional based language, with support for passing functions as arguments
// It also support numerical expressions and boolean expressions (using and, or, not keywords)
// It is provided with a list of builtin functions
// It can work with objects that have methods, but doesn't necessarily support class creating.
// It supports function definition.
// It has list and maps, that are mutable
// When executed, it supports timeout or some other kind of execution counter
// It produces compiled bytecode, with builtin checks and exception throwing for exceeding the instruction count / time.
// Allow statements to optionally be separated with semicolons.
// Possibly allow structs or simple objects.
// Lambda expressions might be nice.
// annotations can be used for configuring ui elements for parameter entry etc.

public class GenLangParser extends BaseParser<Node> {

    // Take string as input

    public Rule program() {
        return Sequence(
                // TODO: Imports
                push(new Prog()),
                ZeroOrMore(
                        varDef(),
                        Optional(SEMI),
                        ((Prog)peek(1)).addVar((Var) pop())
                ),
                ZeroOrMore(
                        functionDef(),
                        Optional(SEMI),
                        ((Prog)peek(1)).addFun((Fun) pop())
                )
                // TODO: Should we have a main body?
        );
    }


    /**
     * Parses:
     * [const] type variableName = expression
     */
    Rule varDef() {
        return Sequence(
                push(new VarDef()),

                // Modifiers (const)
                Optional(
                        Sequence(
                                CONST,
                                ((VarDef) peek()).setConstant(true)
                        )
                ),

                // Type
                typeRef(),
                ((VarDef)peek(1)).setType((TypeRef) pop()),

                // Name
                identifier(),
                ((VarDef)peek()).setName(match()),

                // Value
                ASSIGN, expression(),
                ((VarDef)peek(1)).setExpr((Expr) pop())
        );
    }

    /**
     * Parses:
     * fun ResultType functionName ( (paramType paramName [= defaultExpr], )* )  functionBody
     */
    Rule functionDef() {
        Var<String> name = new Var<String>();
        return Sequence(
                FUN, typeRef(),
                identifier(), name.set(match()),
                paramSequence(),
                functionBody(),
                push(new Fun(name.get(), (TypeRef) pop(2), ((Params) pop(1)).getParams(), (Block) pop()))
        );
    }

    /**
     * Parses:
     * = expression | { (statement [;])* }
     */
    Rule functionBody() {
        return FirstOf(
                Sequence(
                        ASSIGN,
                        expression(),
                        push(new Block(new Return((Expr) pop())))
                ),
                statementBlock()
        );
    }

    Rule paramSequence() {
        return Sequence(
                LPAR,
                push(new Params()),
                Optional(
                        functionParam(),
                        ZeroOrMore(
                                COMMA,
                                functionParam()
                        )
                ),
                RPAR
        );
    }

    Rule functionParam() {
        return Sequence(
                typeRef(),
                identifier(),
                ((Params)peek()).add(new Param((TypeRef) pop(), match())),
                Optional(
                        ASSIGN,
                        expression(),
                        ((Params) peek()).getLast().setDefaultValue((Expr) pop())
                )
        );
    }

    /**
     * Parses:
     * statement[;] | { (statement[;])* }
     */
    Rule statements() {
        return FirstOf(
                statementBlock(),
                Sequence(
                        statement(),
                        push(new Block((Statement) pop()))
                )
        );
    }

    Rule statementBlock() {
        return Sequence(
                push(new Block()), // Push block, each statement peeks it and adds itself
                LCURLY,
                ZeroOrMore(
                        statement(),
                        ((Block) peek(1)).addStatement((Statement) pop())
                ),
                RCURLY
        );
    }

    Rule statement() {
        return Sequence(
                FirstOf(
                        varDef(),
                        functionDef(),
                        assignmentStatement(),
                        expressionStatement(),
                        forStatement(),
                        whileStatement(),
                        returnStatement()
                        // TODO Add others
                ),
                Optional(SEMI)
        );
    }

    /**
     * Parses:
     * return expression
     */
    Rule returnStatement() {
        return Sequence(
                RETURN,
                expression(),
                push(new Return((Expr) pop()))
        );
    }

    /**
     * Parses:
     * variableName = expression
     */
    Rule assignmentStatement() {
        Var<String> name = new Var<String>();
        return Sequence(
                identifier(), name.set(match()),
                ASSIGN,
                expression(),
                push(new Assign(name.get(), (Expr) pop()))
        );
    }


    /**
     * Parses:
     * expression
     */
    Rule expressionStatement() {
        return Sequence(
                expression(),
                push(new ExprStatement((Expr) pop()))
        );
    }

    /**
     * Parses:
     * for type variableName in expr do statements
     * where expr results in a collection or range or other iterable.
     */
    Rule forStatement() {
        Var<String> name = new Var<String>();
        return Sequence(
                FOR,
                typeRef(),
                identifier(), name.set(match()),
                IN,
                expression(),
                DO,
                statements(),
                push(new For((TypeRef) pop(2), name.get(), (Expr) pop(1), (Block) pop()))
        );
    }

    /**
     * Parses:
     * while booleanExpression do statements
     */
    Rule whileStatement() {
        return Sequence(
                WHILE,
                booleanExpr(),
                DO,
                statements(),
                push(new While((BoolExpr) pop(1), (Block) pop()))
        );
    }

    /**
     * Parses any expression returning a value.
     */
    Rule expression() {
        return FirstOf(
                booleanExpr(),
                range(),
                map(),
                list(),
                mathExpression(),
                ifExpression(),
                functionCall(),
                update(),
                apply(),
                create(),
                functionExpr()
                // TODO: Add others
        );
    }

    /**
     * Parses:
     * if booleanExpression then statements [else statements ]
     */
    Rule ifExpression() {
        return Sequence(
                IF,
                booleanExpr(),
                THEN,
                statements(),
                push(new If((BoolExpr) pop(1), (Block) pop())),
                Optional(
                        ELSE,
                        statements(),
                        ((If) peek()).setElseBlock((Block) pop())
                )
        );
    }


    Rule booleanExpr() {
        return orExpr();
    }

    Rule orExpr() {
        return operatorRule(andExpr(), OR);
    }

    Rule andExpr() {
        return operatorRule(boolAtom(), AND);
    }

    Rule boolAtom() {
        return null; // TODO: Support NOT, support true false constants, support comparsion ops
    }

    Rule typeRef() {
        // TODO: Add support for list and map types?
        return Sequence(
                identifier(),
                push(new TypeRef(match()))
        );
    }

    Rule mathExpression() {
        return operatorRule(term(), FirstOf(PLUS, MINUS));
    }

    Rule term() {
        return operatorRule(factor(), FirstOf(MUL, DIV));
    }

    Rule factor() {
        return operatorRule(atom(), POWER);
    }


    /**
     * Parses:
     * [ expr, * ]
     */
    Rule list() {
        return Sequence(
                LSQUARE,
                push(new ListExpr()),
                Optional(
                  expression(),
                  ((ListExpr)peek(1)).add((Expr) pop()),
                  ZeroOrMore(
                          COMMA,
                          expression(),
                          ((ListExpr)peek(1)).add((Expr) pop())
                  )
                ),
                RSQUARE
        );
    }

    /**
     * Parses:
     * [ expr : expr, * ]
     * Empty map is coded as [:]
     */
    Rule map() {
        return FirstOf(
                Sequence(
                        LSQUARE,
                        COLON,
                        RSQUARE,
                        push(new MapExpr())
                ),
                Sequence(
                        LSQUARE,
                        push(new MapExpr()),
                        Optional(
                                mapEntry(),
                                ZeroOrMore(
                                        COMMA,
                                        mapEntry()
                                )
                        ),
                        RSQUARE
                ));
    }

    Rule mapEntry() {
        return Sequence(
                expression(),
                COLON,
                expression(),
                ((MapExpr)peek(2)).add((Expr) pop(1), (Expr) pop())
        );
    }


    /**
     * Expression used to create iterable range objects for for loops, possibly other things too.
     * Parses:
     * [ expr ..|... expr [step expr] ]
     * .. denotes non inclusive end, ... denotes inclusive end.
     */
    public Rule range() {
        Var<Boolean> inclusive = new Var<Boolean>();
        return Sequence(
                LSQUARE,
                expression(),
                FirstOf(
                        Sequence(
                                DOTDOTDOT,
                                inclusive.set(true)
                        ),
                        Sequence(
                                DOTDOT,
                                inclusive.set(false)
                        )
                ),
                expression(),
                push(new RangeExpr((Expr) pop(1), (Expr) pop(), inclusive.get())),
                Optional(
                  STEP,
                  expression(),
                  ((RangeExpr)peek(1)).setStep((Expr) pop())
                ),
                RSQUARE
        );
    }

    /**
     * Used for calling a default setter type value for an object.
     * Translates to a method call object.update(param, value)
     * Parses:
     * objectExpression ( expression ) = expression
     */
    Rule update() {
        return null; // TODO
    }

    /**
     * Parses:
     * [objectExpression . ] functionName ( [expression, *] [argumentName = expression, *] ) [lambdaExpr]
     */
    Rule functionCall() {
        return null; // TODO: also extract argument list
    }

    /**
     * Used for calling a default getter type value for an object.
     * Translates to a method call object.apply(param)
     * Parses:
     * objectExpression ( expression )
     */
    Rule apply() {
        return null; // TODO
    }

    /**
     * Creates a new object.  In effect a function call to a constructor.
     * Parses:
     * new type ( argumentList )
     */
    Rule create() {
        return null; // TODO
    }

    /**
     * Creates a new anonymous function with a closure from the current block
     * Parses:
     * fun resultType ( paramList ) [= expression | statementBlock ]
     */
    Rule functionExpr() {
        return Sequence(
                FUN, typeRef(),
                paramSequence(),
                functionBody(),
                push(new FunExpr((TypeRef) pop(2), ((Params) pop(1)).getParams(), (Block) pop()))
        );
    }



    Rule atom() {
        return FirstOf(number(), parens());
    }

    Rule parens() {
        return Sequence(LPAR, expression(), RPAR);
    }

    Rule number() {
        return Sequence(
                Sequence(
                        Optional(Ch('-')),
                        OneOrMore(Digit()),
                        Optional(Ch('.'), OneOrMore(Digit()))
                ),
                // The action uses a default string in case it is run during error recovery (resynchronization)
                push(new Num(Double.parseDouble(matchOrDefault("0")))),
                whiteSpace()
        );
    }


    Rule operatorRule(Rule part, Rule operator) {
        Var<String> op = new Var<String>();
        return Sequence(
                part,
                ZeroOrMore(
                        operator, op.set(match()),
                        part,
                        push(new OperationNode((Expr) pop(1), op.get(), (Expr) pop()))
                )
        );
    }

    Rule Digit() {
        return CharRange('0', '9');
    }





    @SuppressSubnodes
    @MemoMismatches
    Rule identifier() {
        return Sequence(TestNot(keyword()), letter(), ZeroOrMore(letterOrDigit()), whiteSpace());
    }

    Rule letter() {
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '_');
    }

    @MemoMismatches
    Rule letterOrDigit() {
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), CharRange('0', '9'), '_');
    }

    @MemoMismatches
    Rule keyword() {
        return Sequence(
                FirstOf("import",
                        "const", "fun",
                        "new", "return", "this",
                        "if", "then", "else",
                        "for", "in", "while", "do",
                        "step",
                        "or", "and", "not", "xor", "nor", "nand"),
                TestNot(letterOrDigit())
        );
    }

    final Rule IMPORT = keyword("import");
    final Rule CONST = keyword("const");
    final Rule FUN = keyword("fun");
    final Rule NEW = keyword("new");
    final Rule RETURN = keyword("return");
    final Rule THIS = keyword("this");
    final Rule THEN = keyword("then");
    final Rule IF = keyword("if");
    final Rule ELSE = keyword("else");
    final Rule FOR = keyword("for");
    final Rule IN = keyword("in");
    final Rule WHILE = keyword("while");
    final Rule DO = keyword("do");
    final Rule STEP = keyword("step");
    final Rule OR = keyword("or");
    final Rule AND = keyword("and");
    final Rule NOT = keyword("not");
    final Rule XOR = keyword("xor");
    final Rule NOR = keyword("nor");
    final Rule NAND = keyword("nand");

    @SuppressNode
    @DontLabel
    Rule keyword(String keyword) {
        return Terminal(keyword, letterOrDigit());
    }

    final Rule DOT = Terminal(".", Ch('.'));
    final Rule DOTDOT = Terminal("..", Ch('.'));
    final Rule DOTDOTDOT = Terminal("...", Ch('.'));
    final Rule COMMA = Terminal(",");
    final Rule COLON = Terminal(":");
    final Rule SEMI  = Terminal(";");
    final Rule ASSIGN = Terminal("=", Ch('='));
    final Rule EQUAL = Terminal("==");
    final Rule GT = Terminal(">", Ch('='));
    final Rule GE = Terminal(">=");
    final Rule LT = Terminal("<", Ch('='));
    final Rule LE = Terminal("<=");
    final Rule PLUS = Terminal("+", AnyOf("=+"));
    final Rule MINUS = Terminal("-", AnyOf("=-"));
    final Rule MUL = Terminal("*", AnyOf("=*"));
    final Rule DIV = Terminal("/", AnyOf("=/"));
    final Rule POWER = Terminal("^", Ch('='));
    final Rule BANG = Terminal("!");
    final Rule QUERY = Terminal("?");
    final Rule PERCENT = Terminal("%");
    final Rule LPAR = Terminal("(");
    final Rule RPAR = Terminal(")");
    final Rule LCURLY = Terminal("{");
    final Rule RCURLY = Terminal("}");
    final Rule LSQUARE = Terminal("[");
    final Rule RSQUARE = Terminal("]");

    @SuppressNode
    Rule whiteSpace() {
        return ZeroOrMore(FirstOf(

                // Whitespace
                OneOrMore(AnyOf(" \t\r\n\f").label("Whitespace")),

                // Traditional comment
                Sequence("/*", ZeroOrMore(TestNot("*/"), ANY), "*/"),

                // End of line comment
                Sequence(
                        "//",
                        ZeroOrMore(TestNot(AnyOf("\r\n")), ANY),
                        FirstOf("\r\n", '\r', '\n', EOI)
                )
        ));
    }

    // We redefine the rule creation for string literals to automatically match trailing whitespace if the string
    // literal ends with a space character, this way we don't have to insert extra whitespace() rules after each
    // character or string literal
    @Override
    protected Rule fromStringLiteral(String string) {
        return string.endsWith(" ") ?
               Sequence(String(string.substring(0, string.length() - 1)), whiteSpace()) :
               String(string);
    }


    @Override
    protected Rule fromCharLiteral(char c) {
        // turn of creation of parse tree nodes for single characters
        return super.fromCharLiteral(c).suppressNode();
    }

    @SuppressNode
    @DontLabel
    Rule Terminal(String string) {
        return Sequence(string, whiteSpace()).label('\'' + string + '\'');
    }

    @SuppressNode
    @DontLabel
    Rule Terminal(String string, Rule mustNotFollow) {
        return Sequence(string, TestNot(mustNotFollow), whiteSpace()).label('\'' + string + '\'');
    }



}
