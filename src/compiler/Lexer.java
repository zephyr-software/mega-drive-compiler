package compiler;

import compiler.exception.FileException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

  private char[] fileChars = null;
  private int cursorStart = 0;
  private int cursorPosition = 0;

  private List<Token> tokenList;
  private List<UnknownChar> unknownCharList;

  private int line = 1; // first line in source file

  public Lexer(char[] fileChars) {
    this.fileChars = fileChars;

    tokenList = new ArrayList<Token>();
    unknownCharList = new ArrayList<UnknownChar>();
  }

  public List<Token> tokenize() throws FileException {
    while (cursorPosition < fileChars.length) {
      cursorStart = cursorPosition;
      char character = advance();

      // ignored characters

      if (character == ' ') { // whitespace

        continue; // ignore character
      }

      if (character == '\t') { // tab

        continue; // ignore character
      }

      if (character == '\n') { // new line
        line++;

        continue;
      }

      if (character == '\r') { // carriage return

        continue; // ignore character
      }

      if (character == ';') { // semicolon
        while (peek() != '\n') { // skip comment line characters
          advance();
        }

        continue;
      }

      // 1 character

      if (character == '(') { // left round bracket
        Token token = new Token(TokenType.LEFT_ROUND_BRACKET, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == ')') { // right round bracket
        Token token = new Token(TokenType.RIGHT_ROUND_BRACKET, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '-') { // minus
        Token token = new Token(TokenType.MINUS, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '+') { // plus
        Token token = new Token(TokenType.PLUS, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '*') { // star
        Token token = new Token(TokenType.STAR, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '/') { // slash
        Token token = new Token(TokenType.SLASH, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '%') { // modulo
        Token token = new Token(TokenType.MODULO, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == ',') { // comma
        Token token = new Token(TokenType.COMMA, character + "", line);
        tokenList.add(token);

        continue;
      }

      // 2 characters

      if (character == '<') { // less than
        if (match('=')) { // less than or equals
          Token token = new Token(TokenType.LESS_THAN_OR_EQUALS, character + "=", line);
          tokenList.add(token);

          continue;
        }

        Token token = new Token(TokenType.LESS_THAN, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '>') { // greater than
        if (match('=')) { // greater than or equals
          Token token = new Token(TokenType.GREATER_THAN_OR_EQUALS, character + "=", line);
          tokenList.add(token);

          continue;
        }

        Token token = new Token(TokenType.GREATER_THAN, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '=') { // equals / assignment
        if (match('=')) { // equals / equals
          Token token = new Token(TokenType.EQUALS, character + "=", line);
          tokenList.add(token);

          continue;
        }

        Token token = new Token(TokenType.ASSIGNMENT, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '!') { // exclamation mark / not
        if (match('=')) { // equals / not equals
          Token token = new Token(TokenType.NOT_EQUALS, character + "=", line);
          tokenList.add(token);

          continue;
        }

        Token token = new Token(TokenType.NOT, character + "", line);
        tokenList.add(token);

        continue;
      }

      // numbers

      if (isDigit(character)) {
        String number = character + "";
        while (isDigit(peek())) {
          number += advance();
        }

        Token token = new Token(TokenType.NUMBER, number, line);
        tokenList.add(token);

        continue;
      }

      // identifiers

      if (isLetter(character) || character == '_') {
        String identifier = character + "";
        while (isLetter(peek()) || isDigit(peek()) || peek() == '_') {
          identifier += advance();
        }

        // keywords

        Token token;
        if (TokenType.AND.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.AND, identifier, line);
        } else if (TokenType.OR.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.OR, identifier, line);

        } else if (TokenType.FALSE.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.BOOLEAN, identifier, line);
        } else if (TokenType.TRUE.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.BOOLEAN, identifier, line);

        } else if (TokenType.BIT8.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.BIT8, identifier, line);
        } else if (TokenType.BIT16.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.BIT16, identifier, line);
        } else if (TokenType.BIT32.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.BIT32, identifier, line);
        } else if (TokenType.SIGN.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.SIGN, identifier, line);

        } else if (TokenType.DEBUG_PRINT.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.DEBUG_PRINT, identifier, line);
        } else if (TokenType.DEBUG_PRINT_LINE.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.DEBUG_PRINT_LINE, identifier, line);

        } else if (TokenType.IF.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.IF, identifier, line);
        } else if (TokenType.THEN.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.THEN, identifier, line);
        } else if (TokenType.ELSE.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.ELSE, identifier, line);
        } else if (TokenType.END.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.END, identifier, line);

        } else if (TokenType.WHILE.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.WHILE, identifier, line);
        } else if (TokenType.DO.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.DO, identifier, line);

        } else if (TokenType.FOR.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.FOR, identifier, line);

        } else if (TokenType.FUNCTION.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.FUNCTION, identifier, line);

        } else if (TokenType.RETURN.name().toLowerCase().equals(identifier)) {
          token = new Token(TokenType.RETURN, identifier, line);

        } else {
          token = new Token(TokenType.IDENTIFIER, identifier, line);
        }
        tokenList.add(token);

        continue;
      }

      // strings

      if (character == '"') { // double quotes
        String string = "";
        while (peek() != '"') {
          string += advance();
        }
        advance(); // consume the ending quote

        Token token = new Token(TokenType.STRING, string, line);
        tokenList.add(token);

        continue;
      }

      UnknownChar unknownChar = new UnknownChar(character + "", line);
      unknownCharList.add(unknownChar);
    }

    return tokenList;
  }

  public List<UnknownChar> getUnknownCharList() {

    return unknownCharList;
  }

  private boolean isLetter(char character) {

    return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
  }

  private boolean isDigit(char character) {

    return character >= '0' && character <= '9';
  }

  // advance the cursor pointer
  // consumes the character
  private char advance() {
    char character = fileChars[cursorPosition];
    cursorPosition++;

    return character;
  }

  // just takes a peek at the current character
  // does not consume the character
  private char peek() {
    char character = fileChars[cursorPosition];

    return character;
  }

  // looks at the next character in the source
  // does not consume the character
  private char lookAhead() {
    char character = fileChars[cursorPosition + 1];

    return character;
  }

  // check if current character matches an expectation
  // consumes the character only if match is true
  private boolean match(char expectedCharacter) {
    if (cursorPosition >= fileChars.length) {

      return false;
    }

    char character = fileChars[cursorPosition];
    if (character != expectedCharacter) {

      return false;
    }

    cursorPosition++; // if it is a match, we also consume that char

    return true;
  }
}
