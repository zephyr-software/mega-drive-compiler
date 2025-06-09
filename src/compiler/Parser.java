package compiler;

import compiler.exception.ParserException;
import compiler.model.BinaryOperatorModel;
import compiler.model.Bit16Model;
import compiler.model.BooleanModel;
import compiler.model.ExpressionModel;
import compiler.model.GroupingModel;
import compiler.model.LogicalOperatorModel;
import compiler.model.NodeModel;
import compiler.model.StringModel;
import compiler.model.UnaryOperatorModel;
import java.util.List;

public class Parser {

  private List<Token> tokenList;
  private int cursor;

  public Parser(List<Token> tokenList) {
    this.tokenList = tokenList;
    cursor = 0;
  }

  // recursive descent parser algorithm
  public NodeModel parse() throws ParserException {

    return parseOr();
  }

  // <logical_or> ::= <logical_and> ( "or" <logical_and> )*
  public ExpressionModel parseOr() throws ParserException {
    ExpressionModel expressionModel = parseAnd();

    while (match(TokenType.OR)) {
      Token operator = previousToken();
      int lineNumber = operator.getLine();
      ExpressionModel right = parseAnd();
      expressionModel = new LogicalOperatorModel(operator, expressionModel, right, lineNumber);
    }

    return expressionModel;
  }

  // <logical_and> ::= <equality> ( "and" <equality> )*
  public ExpressionModel parseAnd() throws ParserException {
    ExpressionModel expressionModel = parseEquality();

    while (match(TokenType.AND)) {
      Token operator = previousToken();
      int lineNumber = operator.getLine();
      ExpressionModel right = parseEquality();
      expressionModel = new LogicalOperatorModel(operator, expressionModel, right, lineNumber);
    }

    return expressionModel;
  }

  // <equality> ::= <comparison> ( ( "!=" | "==" ) <comparison> )*
  public ExpressionModel parseEquality() throws ParserException {
    ExpressionModel expressionModel = parseComparison();

    while (match(TokenType.NOT_EQUALS) || match(TokenType.EQUALS)) {
      Token operator = previousToken();
      int lineNumber = operator.getLine();
      ExpressionModel right = parseComparison();
      expressionModel = new BinaryOperatorModel(operator, expressionModel, right, lineNumber);
    }

    return expressionModel;
  }

  // <comparison> ::= <addition> ( ( "<" | "<=" | ">" | ">=" ) <addition> )*
  public ExpressionModel parseComparison() throws ParserException {
    ExpressionModel expressionModel = parseAddition();

    while (match(TokenType.LESS_THAN)
        || match(TokenType.LESS_THAN_OR_EQUALS)
        || match(TokenType.GREATER_THAN)
        || match(TokenType.GREATER_THAN_OR_EQUALS)) {
      Token operator = previousToken();
      int lineNumber = operator.getLine();
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
      int lineNumber = operator.getLine();
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
      int lineNumber = operator.getLine();
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
      int lineNumber = operator.getLine();
      ExpressionModel right = parseUnary();
      expressionModel = new BinaryOperatorModel(operator, expressionModel, right, lineNumber);
    }

    return expressionModel;
  }

  // <unary> ::= ("!"|'-'|'+') <unary> | <primary>
  private ExpressionModel parseUnary() throws ParserException {
    if (match(TokenType.NOT) | match(TokenType.MINUS) | match(TokenType.PLUS)) {
      Token operator = previousToken();
      int lineNumber = operator.getLine();
      ExpressionModel operand = parseUnary();

      return new UnaryOperatorModel(operator, operand, lineNumber);
    }

    return parsePrimary();
  }

  // <primary> ::= <integer> | <float> | <boolean> | <string> | '(' <expr> ')'
  private ExpressionModel parsePrimary() throws ParserException {
    if (match(TokenType.NUMBER)) {
      Token token = previousToken();
      String lexeme = token.getLexeme();
      Integer value = Integer.parseInt(lexeme);
      int lineNumber = token.getLine();

      return new Bit16Model(value, lineNumber);
    }

    if (match(TokenType.BOOLEAN)) {
      Token token = previousToken();
      String lexeme = token.getLexeme();
      Boolean value = Boolean.parseBoolean(lexeme);
      int lineNumber = token.getLine();

      return new BooleanModel(value, lineNumber);
    }

    if (match(TokenType.STRING)) {
      Token token = previousToken();
      String value = token.getLexeme();
      int lineNumber = token.getLine();

      return new StringModel(value, lineNumber);
    }

    if (match(TokenType.LEFT_ROUND_BRACKET)) {
      NodeModel nodeModel = parse();

      if (!match(TokenType.RIGHT_ROUND_BRACKET)) {

        Token token = previousToken();
        throw new ParserException("error: char ')' expected; line: " + token.getLine());
      }

      Token token = previousToken();
      int lineNumber = token.getLine();

      return new GroupingModel(nodeModel, lineNumber);
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
