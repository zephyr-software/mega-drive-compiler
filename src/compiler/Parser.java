package compiler;

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

public class Parser {

  private static final int PARAMETERS_MAX_NUMBER = 255;

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

  // <program> ::= <statement>*
  private NodeModel parseProgram() throws ParserException {

    return parseStatementList();
  }

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

    // assignment
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

    throw new ParserException("bad statement; token:" + token);
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

  // <for_statement> ::= "for" <identifier> "=" <start> "," <end> ("," <step>)? "do" <body_stmts>
  // "end"
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
          "number of function parameters: "
              + parametersNumber
              + " is greater then maximum number: "
              + PARAMETERS_MAX_NUMBER);
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
      StatementModel statementModel = new DebugPrintStatementModel(expressionModel, lineNumber);

      return statementModel;
    }

    if (match(TokenType.DEBUG_PRINT_LINE)) {
      ExpressionModel expressionModel = parseExpression();
      int lineNumber = expressionModel.getLineNumber();
      StatementModel statementModel = new DebugPrintLineStatementModel(expressionModel, lineNumber);

      return statementModel;
    }

    throw new ParserException("no debug print statement found");
  }

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

  // <primary> ::= <integer> | <boolean> | <string> | <identifier> '(' <expr> ')'
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
        throw new ParserException("error: char ')' expected; line: " + token.getLineNumber());
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
    throw new ParserException("not supported model; token: " + token);
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
