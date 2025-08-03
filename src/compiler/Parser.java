package compiler;

import static java.lang.String.format;

import compiler.exception.ParserException;
import compiler.model.AssignmentStatementModel;
import compiler.model.BinaryOperatorModel;
import compiler.model.Bit16Model;
import compiler.model.BooleanModel;
import compiler.model.DebugPrintLineStatementModel;
import compiler.model.DebugPrintStatementModel;
import compiler.model.ExpressionModel;
import compiler.model.ForStatementModel;
import compiler.model.FunctionCallModel;
import compiler.model.FunctionCallStatementModel;
import compiler.model.FunctionDeclarationStatementModel;
import compiler.model.GroupingModel;
import compiler.model.IdentifierModel;
import compiler.model.IfStatementModel;
import compiler.model.LogicalOperatorModel;
import compiler.model.NodeModel;
import compiler.model.ParameterStatementModel;
import compiler.model.ReturnStatementModel;
import compiler.model.StatementListModel;
import compiler.model.StatementModel;
import compiler.model.StringModel;
import compiler.model.UnaryOperatorModel;
import compiler.model.WhileStatementModel;
import compiler.token.Token;
import compiler.token.TokenType;
import java.util.ArrayList;
import java.util.List;

/*
syntax
backus–naur form - production rules

<digit> ::= "0"|"1"|"2"|"3"|"4"|"5"|"6"|"7"|"8"|"9"
<lowercase_letter> ::= "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m" |
                       "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"
<uppercase_letter> ::= "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K" | "L" | "M" |
                       "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z"
<alphabet> ::= <lowercase_letter> | <uppercase_letter> | "_"
<alphanumeric> ::= <alphabet> | <digit>

<boolean> ::= "true" | "false"
<number> ::= <digit>+
<string> ::= '"' <alphanumeric>+ '"'

<literal> ::= <boolean> | <number> | <string>
<identifier> ::= <alphabet> <alphanumeric>*

<primary> ::= <literal> | <identifier> | <grouping>
<unary> ::= ( "!" | "-" | "+" ) <unary> | <primary>
<modulo> ::= <unary> ( "%" <unary> )*
<multiplication> ::= <modulo> ( ( "/" | "*" ) <modulo> )*
<addition> ::= <multiplication> ( ("-" | "+" ) <multiplication> )*
<comparison> ::= <addition> ( ( "<" | "<=" | ">" | ">=" ) <addition> )*
<equality> ::= <comparison> ( ( "!=" | "==" ) <comparison> )*
<logical_and> ::= <equality> ( "and" <equality> )*
<logical_or> ::= <logical_and> ( "or" <logical_and> )*
<expression> ::= <logical_or>

<grouping> ::= "(" <expression> ")"

<argument_list> = expression ( "," expression)*
<parameter_list> ::= <identifier> ("," <identifier> )*

<debug_print_statement> ::= "debug_print" | "debug_print_line"  <expression>

<return_statement> ::= "return" <expression>
<function_call_statement> ::= identifier "("argument_list* ")"
<function_declaration_statement> ::= "function" identifier "(" <parameter_list>* ")" <statement_list> "end"
<for statement> ::= "for" <assignment_statement> "," <expression> ( "," <expression> )?
                    "do" <statement_list> "end"
<while_statement> ::= "while" <expression> "do" <statement_list> "end"
<if_statement> ::= "if" <expression> "then" <statement_list> ( "else" <statement_list> )? "end
<assignment_statement> ::= identifier "=" <experession>
<expression_statement> ::= <expression>
<statement> ::= <expression_statement> | <assignment_statement> | <if_statement> | <while_Statement> |
                <for_statement> | <function_declaration_statement> | <function_call_statement> |
                <return_statement> | <debug_print_statement>
<statement_list> ::= <statement>+

<program> ::= <statement_list>
*/

public class Parser {

  private static final int PARAMETERS_MAX_NUMBER = 255;

  private static final String BAD_STATEMENT = "bad statement";
  public static final String ERROR = "error";
  public static final String TOKEN = "token";
  public static final String FUNCTION_MAX_PARAMETER_EXCEEDED =
      "maximum number of function parameters exceeded";
  public static final String PARAMETERS_NUMBER = "parameters number";
  public static final String MAXIMUM = "maximum";
  public static final String DEBUG_PRINT = "debug print";
  public static final String NOT_FOUND = "not found";
  public static final String MODEL_NOT_SUPPORTED = "model is not supported";
  public static final String CHARACTER_EXPECTED = "character expected";
  public static final String LINE = "line";

  private List<Token> tokenList;
  private int cursor;

  public Parser(List<Token> tokenList) {
    this.tokenList = tokenList;
    cursor = 0;
  }

  // recursive descent parser algorithm
  public NodeModel parse() throws ParserException {

    return parseProgram();
  }

  // <program> ::= <statement_list>
  private NodeModel parseProgram() throws ParserException {

    return parseStatementList();
  }

  // <statement_list> ::= <statement>+
  private StatementListModel parseStatementList() throws ParserException {
    List<StatementModel> statementModelList = new ArrayList<StatementModel>();

    int lineNumber = 1;
    while (cursor < tokenList.size() && !isNext(TokenType.ELSE) && !isNext(TokenType.END)) {
      StatementModel statementModel = parseStatement();
      statementModelList.add(statementModel);

      lineNumber = statementModel.getLineNumber();
    }

    return new StatementListModel(statementModelList, lineNumber);
  }

  // predictive parsing
  // <statement> ::= <expression_statement> | <assignment_statement> | <if_statement> |
  //                 <while_Statement> | <for_statement> | <function_declaration_statement> |
  //                 <function_call_statement> |  <return_statement> | <debug_print_statement>
  private StatementModel parseStatement() throws ParserException {
    Token token = peek();
    TokenType tokenType = token.getTokenType();
    if (tokenType == TokenType.DEBUG_PRINT || tokenType == TokenType.DEBUG_PRINT_LINE) {

      return parseDebugPrint();
    }

    if (tokenType == TokenType.IF) {

      return parseIf();
    }

    if (tokenType == TokenType.WHILE) {

      return parseWhile();
    }

    if (tokenType == TokenType.FOR) {

      return parseFor();
    }

    if (tokenType == TokenType.FUNCTION) {

      return parseFunctionDeclaration();
    }

    if (tokenType == TokenType.RETURN) {

      return parseReturn();
    }

    ExpressionModel left = parseExpression();
    if (match(TokenType.ASSIGNMENT)) {
      ExpressionModel right = parseExpression();
      token = previousToken();
      int lineNumber = token.getLineNumber();

      return new AssignmentStatementModel(left, right, lineNumber);
    }

    if (left instanceof FunctionCallModel) {
      FunctionCallModel functionCallModel = (FunctionCallModel) left;
      token = previousToken();
      int lineNumber = token.getLineNumber();

      return new FunctionCallStatementModel(functionCallModel, lineNumber);
    }

    throw new ParserException(format("%s: %s; %s: %s", ERROR, BAD_STATEMENT, TOKEN, token));
  }

  // <if_statement> ::= "if" <expression> "then" <statement_list> ( "else" <statement_list> )? "end"
  private StatementModel parseIf() throws ParserException {
    expect(TokenType.IF);
    ExpressionModel testExpressionModel = parseExpression();

    expect(TokenType.THEN);
    StatementListModel thenStatementListModel = parseStatementList();

    StatementListModel elseStatementListModel = null;
    if (isNext(TokenType.ELSE)) {
      advance();
      elseStatementListModel = parseStatementList();
    }

    Token token = expect(TokenType.END);
    int lineNumber = token.getLineNumber();

    return new IfStatementModel(
        testExpressionModel, thenStatementListModel, elseStatementListModel, lineNumber);
  }

  // <while_statement> ::= "while" <expression> "do" <statement_list> "end"
  private StatementModel parseWhile() throws ParserException {
    expect(TokenType.WHILE);
    ExpressionModel testExpressionModel = parseExpression();

    expect(TokenType.DO);
    StatementListModel statementListModel = parseStatementList();

    Token token = expect(TokenType.END);
    int lineNumber = token.getLineNumber();

    return new WhileStatementModel(testExpressionModel, statementListModel, lineNumber);
  }

  // <for statement> ::= "for" <assignment_statement> "," <expression> ( "," <expression> )?
  //                     "do" <statement_list> "end"
  private StatementModel parseFor() throws ParserException {
    expect(TokenType.FOR);
    IdentifierModel identifierModel = (IdentifierModel) parsePrimary();
    expect(TokenType.ASSIGNMENT);
    ExpressionModel startExpressionModel = parseExpression();

    expect(TokenType.COMMA);
    ExpressionModel endExpressionModel = parseExpression();

    ExpressionModel stepExpressionModel = null;
    if (isNext(TokenType.COMMA)) {
      advance();
      stepExpressionModel = parseExpression();
    }

    expect(TokenType.DO);
    StatementListModel statementListModel = parseStatementList();

    Token token = expect(TokenType.END);
    int lineNumber = token.getLineNumber();

    return new ForStatementModel(
        identifierModel,
        startExpressionModel,
        endExpressionModel,
        stepExpressionModel,
        statementListModel,
        lineNumber);
  }

  // <func_decl> ::= "func" <name> "(" <parameter_list>? ")" <body_statement_list> "end"
  private StatementModel parseFunctionDeclaration() throws ParserException {
    expect(TokenType.FUNCTION);
    Token nameToken = expect(TokenType.IDENTIFIER);
    String name = nameToken.getLexeme();

    expect(TokenType.LEFT_ROUND_BRACKET);

    List<ParameterStatementModel> parameterStatementModelList = parseParameterList();
    int parametersNumber = parameterStatementModelList.size();

    if (parametersNumber > PARAMETERS_MAX_NUMBER) {

      throw new ParserException(
          format(
              "%s: %s; %s: %s; %s %s: %s",
              ERROR,
              FUNCTION_MAX_PARAMETER_EXCEEDED,
              PARAMETERS_NUMBER,
              parametersNumber,
              MAXIMUM,
              PARAMETERS_NUMBER,
              PARAMETERS_MAX_NUMBER));
    }

    expect(TokenType.RIGHT_ROUND_BRACKET);

    StatementListModel statementListModel = parseStatementList();

    Token endToken = expect(TokenType.END);
    int lineNumber = endToken.getLineNumber();

    return new FunctionDeclarationStatementModel(
        name, parameterStatementModelList, statementListModel, lineNumber);
  }

  // <parameter_list> ::= <identifier> ("," <identifier> )*
  private List<ParameterStatementModel> parseParameterList() throws ParserException {
    List<ParameterStatementModel> result = new ArrayList<>();

    while (!isNext(TokenType.RIGHT_ROUND_BRACKET)) {
      Token nameToken = expect(TokenType.IDENTIFIER);
      String name = nameToken.getLexeme();
      int lineNumber = nameToken.getLineNumber();

      ParameterStatementModel parameterStatementModel =
          new ParameterStatementModel(name, lineNumber);
      result.add(parameterStatementModel);

      if (!isNext(TokenType.RIGHT_ROUND_BRACKET)) {
        expect(TokenType.COMMA);
      }
    }

    return result;
  }

  // <argument_list> = expression ( "," expression)*
  private List<ExpressionModel> parseArgumentList() throws ParserException {
    List<ExpressionModel> result = new ArrayList<>();

    while (!isNext(TokenType.RIGHT_ROUND_BRACKET)) {
      ExpressionModel expressionModel = parseExpression();
      result.add(expressionModel);

      if (!isNext(TokenType.RIGHT_ROUND_BRACKET)) {
        expect(TokenType.COMMA);
      }
    }

    return result;
  }

  // <return_statement> ::= "return" <expression>
  private StatementModel parseReturn() throws ParserException {
    expect(TokenType.RETURN);
    ExpressionModel expressionModel = parseExpression();
    int lineNumber = expressionModel.getLineNumber();

    return new ReturnStatementModel(expressionModel, lineNumber);
  }

  // <debug_print_statement> ::= "debug_print" | "debug_print_line"  <expression>
  private StatementModel parseDebugPrint() throws ParserException {
    if (match(TokenType.DEBUG_PRINT)) {
      ExpressionModel expressionModel = parseExpression();
      int lineNumber = expressionModel.getLineNumber();

      return new DebugPrintStatementModel(expressionModel, lineNumber);
    }

    if (match(TokenType.DEBUG_PRINT_LINE)) {
      ExpressionModel expressionModel = parseExpression();
      int lineNumber = expressionModel.getLineNumber();

      return new DebugPrintLineStatementModel(expressionModel, lineNumber);
    }

    throw new ParserException(format("%s: %s %s - %s", ERROR, TOKEN, NOT_FOUND, DEBUG_PRINT));
  }

  // <expression> ::= <logical_or>
  private ExpressionModel parseExpression() throws ParserException {

    return parseOr();
  }

  // <logical_or> ::= <logical_and> ( "or" <logical_and> )*
  private ExpressionModel parseOr() throws ParserException {
    ExpressionModel expressionModel = parseAnd();

    while (match(TokenType.OR)) {
      Token operator = previousToken();
      int lineNumber = operator.getLineNumber();
      ExpressionModel right = parseAnd();
      expressionModel = new LogicalOperatorModel(operator, expressionModel, right, lineNumber);
    }

    return expressionModel;
  }

  // <logical_and> ::= <equality> ( "and" <equality> )*
  private ExpressionModel parseAnd() throws ParserException {
    ExpressionModel expressionModel = parseEquality();

    while (match(TokenType.AND)) {
      Token operator = previousToken();
      int lineNumber = operator.getLineNumber();
      ExpressionModel right = parseEquality();
      expressionModel = new LogicalOperatorModel(operator, expressionModel, right, lineNumber);
    }

    return expressionModel;
  }

  // <equality> ::= <comparison> ( ( "!=" | "==" ) <comparison> )*
  private ExpressionModel parseEquality() throws ParserException {
    ExpressionModel expressionModel = parseComparison();

    while (match(TokenType.NOT_EQUALS) || match(TokenType.EQUALS)) {
      Token operator = previousToken();
      int lineNumber = operator.getLineNumber();
      ExpressionModel right = parseComparison();
      expressionModel = new BinaryOperatorModel(operator, expressionModel, right, lineNumber);
    }

    return expressionModel;
  }

  // <comparison> ::= <addition> ( ( "<" | "<=" | ">" | ">=" ) <addition> )*
  private ExpressionModel parseComparison() throws ParserException {
    ExpressionModel expressionModel = parseAddition();

    while (match(TokenType.LESS_THAN)
        || match(TokenType.LESS_THAN_OR_EQUALS)
        || match(TokenType.GREATER_THAN)
        || match(TokenType.GREATER_THAN_OR_EQUALS)) {
      Token operator = previousToken();
      int lineNumber = operator.getLineNumber();
      ExpressionModel right = parseAddition();
      expressionModel = new BinaryOperatorModel(operator, expressionModel, right, lineNumber);
    }

    return expressionModel;
  }

  // <addition> ::= <multiplication> ( ( "-" | "+" ) <multiplication> )*
  private ExpressionModel parseAddition() throws ParserException {
    ExpressionModel expressionModel = parseMultiplication();

    while (match(TokenType.MINUS) || match(TokenType.PLUS)) {
      Token operator = previousToken();
      int lineNumber = operator.getLineNumber();
      ExpressionModel right = parseMultiplication();
      expressionModel = new BinaryOperatorModel(operator, expressionModel, right, lineNumber);
    }

    return expressionModel;
  }

  // <multiplication> ::= <modulo> ( ( "/" | "*" ) <modulo> )*
  private ExpressionModel parseMultiplication() throws ParserException {
    ExpressionModel expressionModel = parseModulo();

    while (match(TokenType.SLASH) || match(TokenType.STAR)) {
      Token operator = previousToken();
      int lineNumber = operator.getLineNumber();
      ExpressionModel right = parseModulo();
      expressionModel = new BinaryOperatorModel(operator, expressionModel, right, lineNumber);
    }

    return expressionModel;
  }

  // <modulo> ::= <unary> ( "%" <unary> )*
  private ExpressionModel parseModulo() throws ParserException {
    ExpressionModel expressionModel = parseUnary();

    while (match(TokenType.MODULO)) {
      Token operator = previousToken();
      int lineNumber = operator.getLineNumber();
      ExpressionModel right = parseUnary();
      expressionModel = new BinaryOperatorModel(operator, expressionModel, right, lineNumber);
    }

    return expressionModel;
  }

  // <unary> ::= ("!"|'-'|'+') <unary> | <primary>
  private ExpressionModel parseUnary() throws ParserException {
    if (match(TokenType.NOT) | match(TokenType.MINUS) | match(TokenType.PLUS)) {
      Token operator = previousToken();
      int lineNumber = operator.getLineNumber();
      ExpressionModel operand = parseUnary();

      return new UnaryOperatorModel(operator, operand, lineNumber);
    }

    return parsePrimary();
  }

  // <primary> ::= <number> | <boolean> | <string> | <identifier> '(' <expr> ')'
  private ExpressionModel parsePrimary() throws ParserException {
    if (match(TokenType.NUMBER)) {
      Token token = previousToken();
      String lexeme = token.getLexeme();
      Integer value = Integer.parseInt(lexeme);
      int lineNumber = token.getLineNumber();

      return new Bit16Model(value, lineNumber);
    }

    if (match(TokenType.BOOLEAN)) {
      Token token = previousToken();
      String lexeme = token.getLexeme();
      Boolean value = Boolean.parseBoolean(lexeme);
      int lineNumber = token.getLineNumber();

      return new BooleanModel(value, lineNumber);
    }

    if (match(TokenType.STRING)) {
      Token token = previousToken();
      String value = token.getLexeme();
      int lineNumber = token.getLineNumber();

      return new StringModel(value, lineNumber);
    }

    if (match(TokenType.LEFT_ROUND_BRACKET)) {
      NodeModel nodeModel = parseExpression();

      if (!match(TokenType.RIGHT_ROUND_BRACKET)) {

        Token token = previousToken();
        int lineNumber = token.getLineNumber();
        throw new ParserException(
            format(
                "%s: %s '%s'; %s: %s; %s: %s",
                ERROR, CHARACTER_EXPECTED, "(", TOKEN, token, LINE, lineNumber));
      }

      Token token = previousToken();
      int lineNumber = token.getLineNumber();

      return new GroupingModel(nodeModel, lineNumber);
    }

    if (match(TokenType.IDENTIFIER)) {
      Token token = previousToken();
      String name = token.getLexeme();
      int lineNumber = token.getLineNumber();

      if (match(TokenType.LEFT_ROUND_BRACKET)) {
        List<ExpressionModel> argumentList = parseArgumentList();
        expect(TokenType.RIGHT_ROUND_BRACKET);
        return new FunctionCallModel(name, argumentList, lineNumber);
      }

      return new IdentifierModel(name, lineNumber);
    }

    Token token = peek();
    int lineNumber = token.getLineNumber();
    throw new ParserException(
        format(
            "%s: %s; %s: %s; %s: %s", ERROR, MODEL_NOT_SUPPORTED, TOKEN, token, LINE, lineNumber));
  }

  // work with token list

  private Token advance() {
    Token token = tokenList.get(cursor);
    cursor++;

    return token;
  }

  private Token peek() {

    return tokenList.get(cursor);
  }

  private boolean isNext(TokenType expectedTokenType) {
    if (cursor >= tokenList.size()) {

      return false;
    }

    Token token = peek();
    TokenType tokenType = token.getTokenType();

    return tokenType == expectedTokenType;
  }

  private Token expect(TokenType expectedTokenType) throws ParserException {
    if (cursor >= tokenList.size()) {

      throw new ParserException(
          "expected token: " + expectedTokenType + "is outside the token list");
    }

    Token token = peek();
    TokenType tokenType = token.getTokenType();

    if (tokenType == expectedTokenType) {

      return advance();
    }

    throw new ParserException(
        "expected token type: " + expectedTokenType + "; token type: " + tokenType);
  }

  private Token previousToken() {

    return tokenList.get(cursor - 1);
  }

  private boolean match(TokenType expectedTokenType) {
    if (cursor >= tokenList.size()) {

      return false;
    }

    Token token = peek();
    TokenType tokenType = token.getTokenType();

    boolean result = false;
    if (tokenType == expectedTokenType) {
      cursor++;

      result = true;
    }

    return result;
  }
}
