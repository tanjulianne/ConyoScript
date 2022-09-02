//> Scanning token-type
package com.proglang.conyoscript;

enum TokenType {
  // Single-character tokens.
  LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
  COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR, MODULO,
  PLUS_EQUAL, MINUS_EQUAL, STAR_EQUAL, SLASH_EQUAL,

  // One or two character tokens.
  BANG, BANG_EQUAL,
  EQUAL, EQUAL_EQUAL,
  GREATER, GREATER_EQUAL,
  LESS, LESS_EQUAL,

  // Literals.
  IDENTIFIER, STRING, NUMBER,

  // Keywords.
  AND, CLASS, DO, ELSE, FALSE, FUN, FINAL, FOR, IF, NIL, OR,
  PRINT, RETURN, SCAN, SUPER, THIS, TRUE, VAR, WHILE, ABSTRACT,

  EOF
}