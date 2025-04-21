public class Token {

  private String tokenType;
  private String lexeme;

  public Token(String tokenType, String lexeme) {
    this.tokenType = tokenType;
    this.lexeme = lexeme;
  }

  @Override
  public String toString() {

    return "token type: " + tokenType + "; lexeme: " + lexeme;
  }
}
