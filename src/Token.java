public class Token {

  private TokenType tokenType;
  private String lexeme;
  private int line;

  public Token(TokenType tokenType, String lexeme, int line) {
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
