package compiler;

import static java.lang.String.format;

import compiler.exception.LexerException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

  public static final String ASCII = "ascii / code";
  public static final String ERROR = "error";
  public static final String LINE = "line";
  public static final String UNSUPPORTED_CHAR = "unsupported character";

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
    while (cursorPosition < fileChars.length) {
      cursorStart = cursorPosition;
      char character = advance();

      if (isUnsupportedCharacter(character)) {

        throw new LexerException(
            format(
                "%s [%s: %s] - %s: %s [%s #%s]",
                ERROR, LINE, line, UNSUPPORTED_CHAR, character, ASCII, (int) character));
      }

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
        while (peek() != '\n') { // skip comment line characters
          advance();
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
        if (match('=')) { // equals / not equals [ascii #61]
          Token token = new Token(TokenType.NOT_EQUALS, character + "=", line);
          tokenList.add(token);

          continue;
        }

        Token token = new Token(TokenType.NOT, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '<') { // less than [ascii #60]
        if (match('=')) { // less than or equals [ascii #61]
          Token token = new Token(TokenType.LESS_THAN_OR_EQUALS, character + "=", line);
          tokenList.add(token);

          continue;
        }

        Token token = new Token(TokenType.LESS_THAN, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '=') { // equals / assignment [ascii #61]
        if (match('=')) { // equals / equals [ascii #61]
          Token token = new Token(TokenType.EQUALS, character + "=", line);
          tokenList.add(token);

          continue;
        }

        Token token = new Token(TokenType.ASSIGNMENT, character + "", line);
        tokenList.add(token);

        continue;
      }

      if (character == '>') { // greater than [ascii #62]
        if (match('=')) { // greater than or equals [ascii #61]
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
        while (isDigit(peek())) {
          number += advance();
        }

        Token token = new Token(TokenType.NUMBER, number, line);
        tokenList.add(token);

        continue;
      }

      // identifier token

      if (isLetter(character) || character == '_') {
        String identifier = character + "";
        while (isLetter(peek()) || isDigit(peek()) || peek() == '_') {
          identifier += advance();
        }

        // keyword token

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

      // string token

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

      throw new LexerException(
          format(
              "%s [%s: %s] - %s: %s [%s #%s]",
              ERROR, LINE, line, UNSUPPORTED_CHAR, character, ASCII, (int) character));
    }

    return tokenList;
  }

  // auxiliary methods

  private boolean isUnsupportedCharacter(char character) throws LexerException {
    int asciiNumber = (int) character;
    if ((asciiNumber >= 0 && asciiNumber < 9)
        || (asciiNumber == 11 || asciiNumber == 12)
        || (asciiNumber >= 14 && asciiNumber < 32)
        || (asciiNumber > 127)) {

      return true;
    }

    return false;
  }

  private boolean isLetter(char character) {

    return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
  }

  private boolean isDigit(char character) {

    return character >= '0' && character <= '9';
  }

  // work with character array cursor pointer

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
