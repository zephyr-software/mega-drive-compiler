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

  private int line;

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
        case '-':
          token = new Token(TokenType.MINUS, character);
          tokenList.add(token);
          break;

        case '+':
          token = new Token(TokenType.PLUS, character);
          tokenList.add(token);
          break;

        case '*':
          token = new Token(TokenType.STAR, character);
          tokenList.add(token);
          break;

        case '/':
          token = new Token(TokenType.SLASH, character);
          tokenList.add(token);
          break;

        default:
          System.out.println("lexer warning - unknown character");
          break;
      }
    }

    return tokenList;
  }

  // advance the cursor pointer
  // consumes the character
  private char advance() {
    char character = fileChars[cursorPosition];
    cursorPosition++;

    return character;
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
