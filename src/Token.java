public class Token {

  private TokenType tokenType;
  private char lexeme;

  public Token(TokenType tokenType, char lexeme) {
    this.tokenType = tokenType;
    this.lexeme = lexeme;
  }

  @Override
  public String toString() {

    return "token [token type: " + tokenType + "; lexeme: " + lexeme + "]";
  }
}
