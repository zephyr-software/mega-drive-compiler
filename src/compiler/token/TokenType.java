package compiler.token;

public enum TokenType {

  // 1 character tokens
  NOT, // ! [ascii 33]
  MODULO, // % [ascii #37]
  LEFT_ROUND_BRACKET, // ( [ascii #40]
  RIGHT_ROUND_BRACKET, // ) [ascii #41]
  STAR, // * [ascii #42]
  PLUS, // + [ascii #43]
  COMMA, // , [ascii #44]
  MINUS, // - [ascii #45]
  SLASH, // / [ascii #47]
  SEMICOLON, // ; [ascii #59]
  LESS_THAN, // < [ascii #60]
  ASSIGNMENT, // = [ascii #61]
  GREATER_THAN, // > [ascii #62]

  // 2 characters token
  EQUALS, // ==
  NOT_EQUALS, // !=
  LESS_THAN_OR_EQUALS, // <=
  GREATER_THAN_OR_EQUALS, // >=

  AND, // and
  OR, // or

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
  FUNCTION,

  IF,
  THEN,
  ELSE,
  END,

  WHILE,
  DO,
  FOR,

  RETURN,

  DEBUG_PRINT,
  DEBUG_PRINT_LINE
}
