package compiler;

public enum TokenType {

  // 1 character tokens
  SEMICOLON, // ;

  LEFT_ROUND_BRACKET, // (
  RIGHT_ROUND_BRACKET, // (

  ASSIGNMENT, // =
  PLUS, // +
  MINUS, // -
  STAR, // *
  SLASH, // /

  // 2 characters token
  EQUALS, // ==
  NOT_EQUALS, // !=

  NUMBER, // number
  STRING, // ASCII string
  IDENTIFIER, // 0-9, a-z, A-Z, _ [must start with a letter or underscore]

  // keyword
  BIT8,
  BIT16,
  BIT32,
  SIGN,
  FUNCTION
}
