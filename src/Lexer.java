import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

  private String fileName;
  private RandomAccessFile file;

  private char[] fileChars = null;
  private int cursorStart = 0;
  private int cursorPosition = 0;

  private List<Token> tokenList;

  private int line = 1; // first line in source file

  public Lexer(String fileName) {
    this.fileName = fileName;
    tokenList = new ArrayList<Token>();
  }

  public List<Token> tokenize() {
    char[] fileChars = readFile();

    while (cursorPosition < fileChars.length) {
      cursorStart = cursorPosition;
      char character = advance();
      Token token;
      switch (character) {
        // ignored characters
        case ' ':
          break;

        case '\t': // tab
          break;

        case '\n': // new line
          line++;
          break;

        case '\r': // carriage return
          break;

        case ';': // semicolon
          while (peek() != '\n') {
            advance();
          }
          break;

        // 1 character
        case '-':
          token = new Token(TokenType.MINUS, character + "", line);
          tokenList.add(token);
          break;

        case '+':
          token = new Token(TokenType.PLUS, character + "", line);
          tokenList.add(token);
          break;

        case '*':
          token = new Token(TokenType.STAR, character + "", line);
          tokenList.add(token);
          break;

        case '/':
          token = new Token(TokenType.SLASH, character + "", line);
          tokenList.add(token);
          break;

        // 2 characters
        case '=':
          if (match('=')) {
            token = new Token(TokenType.EQUALS, character + "=", line);
            tokenList.add(token);
            break;
          }

          token = new Token(TokenType.ASSIGNMENT, character + "", line);
          tokenList.add(token);
          break;

        case '!':
          if (match('=')) {
            token = new Token(TokenType.NOT_EQUALS, character + "=", line);
            tokenList.add(token);
          }
          break;

        // numbers
        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
          String number = character + "";
          while (isDigit(peek())) {
            number += advance();
          }
          token = new Token(TokenType.NUMBER, number, line);
          tokenList.add(token);
          break;

        // identifiers
        case 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm':
        case 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z':
        case 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M':
        case 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z':
        case '_':
          String identifier = character + "";
          while (isLetterOrDigit(peek()) || peek() == '_') {
            identifier += advance();
          }

          // keywords
          if (isKeyword(identifier)) {
            token = new Token(TokenType.FUNCTION, identifier, line);
          } else {
            token = new Token(TokenType.IDENTIFIER, identifier, line);
          }

          tokenList.add(token);
          break;

        // strings
        case '"':
          String string = "";
          while (peek() != '"') {
            string += advance();
          }
          advance(); // consume the ending quote
          token = new Token(TokenType.STRING, string, line);
          tokenList.add(token);
          break;

        default:
          System.out.println("lexer warning - unknown character: " + character);
          break;
      }
    }

    return tokenList;
  }

  private boolean isKeyword(String identifier) {
    if (TokenType.FUNCTION.name().toLowerCase().equals(identifier)) {

      return true;
    }

    return false;
  }

  private boolean isLetterOrDigit(char character) {
    return (character >= 'a' && character <= 'z')
        || (character >= 'A' && character <= 'Z')
        || (character >= '0' && character <= '9');
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

  private char[] readFile() {
    try {
      file = new RandomAccessFile(fileName, "r");

      byte[] fileBytes = new byte[(int) file.length()];
      file.readFully(fileBytes);
      fileChars = new String(fileBytes, StandardCharsets.UTF_8).toCharArray();

      file.close();

      return fileChars;
    } catch (Exception exception) {
      System.out.println(exception.getMessage());

      System.exit(0);
    }

    return fileChars;
  }
}
