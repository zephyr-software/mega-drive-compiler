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

  LESS_THAN, // <
  GREATER_THAN, // >

  // 2 characters token
  EQUALS, // ==
  NOT_EQUALS, // !=

  BOOLEAN, // true or false
  TRUE, // true
  FALSE, // false
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
