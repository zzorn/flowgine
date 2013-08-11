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

public class GenLangParser extends BaseParser<Node> {

    // Take string as input

    public Rule program() {
        return Sequence(
                ZeroOrMore(constantDef()),
                ZeroOrMore(functionDef())
        );
    }


    public Rule constantDef() {
        Var<String> name = new Var<String>();
        return Sequence(
                CONST, typeName(),
                identifier(), name.set(match()),
                ASSIGN, expression(),
                push(new Const((TypeRef) pop(1), name.get(), (Expr) pop()))
        );
    }

    /**
     * Parses: fun ResultType functionName ( (paramType paramName [= defaultExpr], )* )  { (statement [;])* }
     */
    public Rule functionDef() {
        Var<String> name = new Var<String>();
        return Sequence(
                FUN, typeName(),
                identifier(), name.set(match()),
                paramSequence(),
                statementBlock(),
                push(new Fun(name.get(), (TypeRef) pop(2),  ((Params)pop(1)).getParams(), (Block) pop()))
        );
    }

    public Rule paramSequence() {
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
                typeName(),
                identifier(),
                ((Params)peek()).add(new Param((TypeRef) pop(), match())),
                Optional(
                        ASSIGN,
                        expression(),
                        ((Params) peek()).getLast().setDefaultValue((Expr) pop())
                )
        );
    }

    Rule statementBlock() {
        return Sequence(
                push(new Block()), // Push block, each statement peeks it and adds itself
                LCURLY,
                ZeroOrMore(
                        statement(),
                        ((Block) peek(1)).addStatement((Statement) pop()),
                        Optional(SEMI)
                ),
                RCURLY
        );
    }

    Rule statement() {
        return FirstOf(
                variableDefinition(),
                expression(),
                forStatement()
                // TODO Add more
        );
    }

    /**
     * Variable definition of the form:
     * type variableName = expression
     */
    Rule variableDefinition() {
        return Sequence(
                typeName(),
                identifier(),
                ASSIGN,
                expression()
                // TODO
        );
    }

    Rule forStatement() {
        return null; // TODO: Implement
    }

    // TODO: Add function definition statement
    // TODO: Add assignment statement
    // TODO: Add expression statement
    // TODO: Add for statement (or should for be an expression as well?)
    // TODO: Add while statement?  Is it needed at all?

    public Rule expression() {
        return mathExpression();
    }

    public Rule typeName() {
        return Sequence(
                identifier(),
                push(new TypeRef(match()))
        );
    }

    public Rule mathExpression() {
        return operatorRule(term(), FirstOf(PLUS, MINUS));
    }

    public Rule term() {
        return operatorRule(factor(), FirstOf(MUL, DIV));
    }

    public Rule factor() {
        return operatorRule(atom(), POWER);
    }

    // TODO: Add function call, either direct or on some object or value (not supported on number or boolean const)

    // TODO: Add object creation

    // TODO: Add list / map creation

    // TODO: Add list/map access (looks like function call?)

    // TODO: Add if expression

    // TODO: Add function expression?  Maybe later

    public Rule atom() {
        return FirstOf(number(), parens());
    }

    public Rule parens() {
        return Sequence(LPAR, expression(), RPAR);
    }

    public Rule number() {
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


    public Rule operatorRule(Rule part, Rule operator) {
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

    public Rule Digit() {
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
                        "if", "else",
                        "for", "while",
                        "or", "and", "not", "xor", "nor", "nand"),
                TestNot(letterOrDigit())
        );
    }

    public final Rule IMPORT = keyword("import");
    public final Rule CONST = keyword("const");
    public final Rule FUN = keyword("fun");
    public final Rule NEW = keyword("new");
    public final Rule RETURN = keyword("return");
    public final Rule THIS = keyword("this");
    public final Rule IF = keyword("if");
    public final Rule ELSE = keyword("else");
    public final Rule FOR = keyword("for");
    public final Rule WHILE = keyword("while");
    public final Rule OR = keyword("or");
    public final Rule AND = keyword("and");
    public final Rule NOT = keyword("not");
    public final Rule XOR = keyword("xor");
    public final Rule NOR = keyword("nor");
    public final Rule NAND = keyword("nand");

    @SuppressNode
    @DontLabel
    Rule keyword(String keyword) {
        return Terminal(keyword, letterOrDigit());
    }

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
