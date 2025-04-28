public class Token {

  private TokenType tokenType;
  private char lexeme;
  private int line;

  public Token(TokenType tokenType, char lexeme, int line) {
    this.tokenType = tokenType;
    this.lexeme = lexeme;
    this.line = line;
  }

  @Override
  public String toString() {

    return "token [token type: "
        + tokenType.name().toLowerCase()
        + "; lexeme: "
        + lexeme
        + "; line: "
        + line
        + "]";
  }
}
