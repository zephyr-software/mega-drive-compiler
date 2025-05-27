package compiler;

import compiler.exception.ParserException;
import compiler.model.BinaryOperatorModel;
import compiler.model.Bit16Model;
import compiler.model.ExpressionModel;
import compiler.model.GroupingModel;
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
  public ExpressionModel parse() throws ParserException {

    return parseExpression();
  }

  // <expr> ::= <term> ( ('+'|'-') <term> )*
  private ExpressionModel parseExpression() throws ParserException {
    ExpressionModel expressionModel = parseTerm();

    while (match(TokenType.PLUS) || match(TokenType.MINUS)) {
      Token operator = previousToken();
      ExpressionModel right = parseTerm();
      expressionModel = new BinaryOperatorModel(operator, expressionModel, right);
    }

    return expressionModel;
  }

  // <term> ::= <factor> ( ('*'|'/') <factor> )*
  private ExpressionModel parseTerm() throws ParserException {
    ExpressionModel expressionModel = parseFactor();

    while (match(TokenType.STAR) || match(TokenType.SLASH)) {
      Token operator = previousToken();
      ExpressionModel right = parseFactor();
      expressionModel = new BinaryOperatorModel(operator, expressionModel, right);
    }

    return expressionModel;
  }

  // <factor> ::= <unary>
  private ExpressionModel parseFactor() throws ParserException {

    return parseUnary();
  }

  // <unary> ::= ('+'|'-') <unary> | <primary>
  private ExpressionModel parseUnary() throws ParserException {
    if (match(TokenType.MINUS) | match(TokenType.PLUS)) {
      Token operator = previousToken();
      ExpressionModel operand = parseUnary();

      return new UnaryOperatorModel(operator, operand);
    }

    return parsePrimary();
  }

  // <primary> ::= <integer> | <float> | '(' <expr> ')'
  private ExpressionModel parsePrimary() throws ParserException {
    if (match(TokenType.NUMBER)) {
      Token token = previousToken();
      String lexeme = token.getLexeme();
      Integer value = Integer.parseInt(lexeme);

      return new Bit16Model(value);
    }

    if (match(TokenType.LEFT_ROUND_BRACKET)) {
      ExpressionModel expressionModel = parseExpression();

      if (!match(TokenType.RIGHT_ROUND_BRACKET)) {

        Token token = previousToken();
        throw new ParserException("error: char ')' expected; line: " + token.getLine());
      }

      return new GroupingModel(expressionModel);
    }

    throw new ParserException("not supported model");
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
