package compiler;

import static java.lang.String.format;

import compiler.exception.LexerException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

  public static final String ERROR = "error";
  public static final String LINE = "line";
  public static final String ASCII = "ascii / code";
  public static final String UNSUPPORTED_CHAR = "unsupported character";
  public static final String INPUT_DATA = "input data / file character array";
  public static final String IS = "is";
  public static final String NULL = "null";

  private char[] fileChars = null;
  private int cursorStart = 0;
  private int cursorPosition = 0;

  private List<Token> tokenList;

  private int line = 1; // first line in source file

  public Lexer(char[] fileChars) {
    this.fileChars = fileChars;
    tokenList = new ArrayList<Token>();
  }

  public List<Token> tokenize() throws LexerException {
    if (fileChars == null) {

      throw new LexerException(
          format("%s [%s: %s] - %s %s %s", ERROR, LINE, line, INPUT_DATA, IS, NULL));
    }

    while (cursorPosition < fileChars.length) {
      cursorStart = cursorPosition;
      char character = consumeChar();

      // ignored characters

      if (character == '\t' // tab [ascii #9]
          || character == '\r' // carriage return [ascii #13]
          || character == ' ') { // space [ascii #32]

        continue; // ignore character
      }

      if (character == '\n') { // new line [ascii #10]
        line++;

        continue; // ignore character
      }

      if (character == ';') { // semicolon [ascii #59]
        while (viewChar() != '\n') { // skip comment line characters
          consumeChar();
        }

        continue;
      }

      // 1 character token

      if (character == '%') { // modulo [ascii #37]
        Token token = new Token(TokenType.MODULO, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '(') { // left round bracket [ascii #40]
        Token token = new Token(TokenType.LEFT_ROUND_BRACKET, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == ')') { // right round bracket [ascii #41]
        Token token = new Token(TokenType.RIGHT_ROUND_BRACKET, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '*') { // star [ascii #42]
        Token token = new Token(TokenType.STAR, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '+') { // plus [ascii #43]
        Token token = new Token(TokenType.PLUS, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == ',') { // comma [ascii #44]
        Token token = new Token(TokenType.COMMA, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '-') { // minus [ascii #45]
        Token token = new Token(TokenType.MINUS, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '/') { // slash [ascii #47]
        Token token = new Token(TokenType.SLASH, character + "", line);
        tokenList.add(token);

        continue;
      }

      // 2 characters or 1 character token

      if (character == '!') { // exclamation mark / not [ascii #31]
        if (isMatch('=')) { // equals / not equals [ascii #61]
          Token token = new Token(TokenType.NOT_EQUALS, character + "=", line);
          tokenList.add(token);

          continue;
        }

        Token token = new Token(TokenType.NOT, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '<') { // less than [ascii #60]
        if (isMatch('=')) { // less than or equals [ascii #61]
          Token token = new Token(TokenType.LESS_THAN_OR_EQUALS, character + "=", line);
          tokenList.add(token);

          continue;
        }

        Token token = new Token(TokenType.LESS_THAN, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '=') { // equals / assignment [ascii #61]
        if (isMatch('=')) { // equals / equals [ascii #61]
          Token token = new Token(TokenType.EQUALS, character + "=", line);
          tokenList.add(token);

          continue;
        }

        Token token = new Token(TokenType.ASSIGNMENT, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '>') { // greater than [ascii #62]
        if (isMatch('=')) { // greater than or equals [ascii #61]
          Token token = new Token(TokenType.GREATER_THAN_OR_EQUALS, character + "=", line);
          tokenList.add(token);

          continue;
        }

        Token token = new Token(TokenType.GREATER_THAN, character + "", line);
        tokenList.add(token);

        continue;
      }

      // number token

      if (isDigit(character)) {
        String number = character + "";
        while (isDigit(viewChar())) {
          number += consumeChar();
        }

        Token token = new Token(TokenType.NUMBER, number, line);
        tokenList.add(token);

        continue;
      }

      // identifier token

      if (isLetter(character) || character == '_') {
        String identifier = character + "";
        while (isLetter(viewChar()) || isDigit(viewChar()) || viewChar() == '_') {
          identifier += consumeChar();
        }

        // keyword token

        Token token = null;

        if (TokenType.AND.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.AND, identifier, line);
        }

        if (TokenType.OR.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.OR, identifier, line);
        }

        if (TokenType.IF.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.IF, identifier, line);
        }

        if (TokenType.THEN.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.THEN, identifier, line);
        }

        if (TokenType.ELSE.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.ELSE, identifier, line);
        }

        if (TokenType.END.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.END, identifier, line);
        }

        if (TokenType.WHILE.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.WHILE, identifier, line);
        }

        if (TokenType.DO.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.DO, identifier, line);
        }

        if (TokenType.FOR.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.FOR, identifier, line);
        }

        if (TokenType.FUNCTION.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.FUNCTION, identifier, line);
        }

        if (TokenType.RETURN.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.RETURN, identifier, line);
        }

        if (TokenType.FALSE.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.BOOLEAN, identifier, line);
        }

        if (TokenType.TRUE.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.BOOLEAN, identifier, line);
        }

        if (TokenType.BIT8.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.BIT8, identifier, line);
        }

        if (TokenType.BIT16.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.BIT16, identifier, line);
        }

        if (TokenType.BIT32.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.BIT32, identifier, line);
        }

        if (TokenType.SIGN.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.SIGN, identifier, line);
        }

        if (TokenType.DEBUG_PRINT.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.DEBUG_PRINT, identifier, line);
        }

        if (TokenType.DEBUG_PRINT_LINE.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.DEBUG_PRINT_LINE, identifier, line);
        }

        if (token == null) {
          token = new Token(TokenType.IDENTIFIER, identifier, line);
        }

        tokenList.add(token);

        continue;
      }

      // string token

      if (character == '"') { // double quotes
        String string = "";
        while (viewChar() != '"') {
          string += consumeChar();
        }
        consumeChar(); // consume the ending quote

        Token token = new Token(TokenType.STRING, string, line);
        tokenList.add(token);

        continue;
      }

      throw new LexerException(
          format(
              "%s [%s: %s] - %s: %s [%s #%s]",
              ERROR, LINE, line, UNSUPPORTED_CHAR, character, ASCII, (int) character));
    }

    return tokenList;
  }

  // work with character array cursor pointer

  private char consumeChar() {
    char character = fileChars[cursorPosition];
    cursorPosition++;

    return character;
  }

  private char viewChar() {

    return fileChars[cursorPosition];
  }

  private boolean isMatch(char expectedCharacter) {
    if (cursorPosition >= fileChars.length) {

      return false;
    }

    if (fileChars[cursorPosition] != expectedCharacter) {

      return false;
    }

    cursorPosition++; // if it is a match, we also consume that char

    return true;
  }

  // auxiliary methods

  private boolean isLetter(char character) {

    return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
  }

  private boolean isDigit(char character) {

    return character >= '0' && character <= '9';
  }
}
