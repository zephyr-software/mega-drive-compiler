public enum TokenType {

  // 1 character tokens
  SEMICOLON, // ;

  PLUS, // +
  MINUS, // -
  STAR, // *
  SLASH, // /

  // 2 characters token
  EQUALS, // ==
  NOT_EQUALS, // !=

  NUMBER, // 32 bit number
  STRING, // ASCII string
  IDENTIFIER, // 0-9, a-z, A-Z, _ [must start with a letter or underscore]

  // keyword
  FUNCTION // function
}
