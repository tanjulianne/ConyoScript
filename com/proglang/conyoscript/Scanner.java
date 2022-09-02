//> Scanning scanner-class
package com.proglang.conyoscript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.proglang.conyoscript.TokenType.*; // [static-import]

class Scanner {
//> keyword-map
  private static final Map<String, TokenType> keywords;

  static {
    keywords = new HashMap<>();
    keywords.put("and",    				AND);
    keywords.put("abstract", 				ABSTRACT);
    keywords.put("class",  				CLASS);
    keywords.put("gawa_like",  				DO);
    keywords.put("else",   				ELSE);
    keywords.put("deins",  				FALSE); //
    keywords.put("like_habang",    				FOR); //
    keywords.put("final",    				FINAL); //
    keywords.put("make_gawa",    			FUN); //
    keywords.put("if_like",     			IF); //
    keywords.put("nil",    				NIL);
    keywords.put("or",     				OR);
    keywords.put("make_sabi",  			PRINT); //
    keywords.put("make_balik", 			RETURN); //
    keywords.put("make_basa",     			SCAN); //
    keywords.put("super",  				SUPER);
    keywords.put("this",   				THIS);
    keywords.put("true",   				TRUE);
    keywords.put("so_like",    			VAR); //
    keywords.put("habang_like",  			WHILE); //
  }
//< keyword-map
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
//> scan-state
  private int start = 0;
  private int current = 0;
  private int line = 1;
//< scan-state

  Scanner(String source) {
    this.source = source;
  }
//> scan-tokens
  List<Token> scanTokens() {
    while (!isAtEnd()) {
      // We are at the beginning of the next lexeme.
      start = current;
      scanToken();
    }

    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }
//< scan-tokens
//> scan-token
private void scanToken() {
  char c = advance();
  switch(c) {
    case '(': addToken(LEFT_PAREN); break;
    case ')': addToken(RIGHT_PAREN); break;
    case '{': addToken(LEFT_BRACE); break;
    case '}': addToken(RIGHT_BRACE); break;
    case ',': addToken(COMMA); break;
    case '.': addToken(DOT); break;
    //case '-': addToken(MINUS); break;
    //case '+': addToken(PLUS); break;
    case ';': addToken(SEMICOLON); break;
    //case '*': addToken(STAR); break;
    case '%': addToken(MODULO); break;

    case '!': addToken(match('=') ? BANG_EQUAL : BANG); break;
    case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
    case '<': addToken(match('=') ? LESS_EQUAL : LESS); break;
    case '>': addToken(match('=') ? GREATER_EQUAL : GREATER); break;

    case '/':
      if (match('/')) {
        // A comment goes until the end of the line.
        while (peek() != '\n' && !isAtEnd()) advance();
      } else if (match('=')) {
        addToken(SLASH_EQUAL);
      } else {
        addToken(SLASH);
      }
      break;

    case '+':
      if (match('=')) {
        addToken(PLUS_EQUAL);
      } else {
        addToken(PLUS);
      }
      break;

    case '-':
      if (match('=')) {
        addToken(MINUS_EQUAL);
      } else {
        addToken(MINUS);
      }
      break;

    case '*':
      if (match('=')) {
        addToken(STAR_EQUAL);
      } else {
        addToken(STAR);
      }
      break;

    case ' ':
    case '\r':
    case '\t':
      // Ignore whitespace.
      break;

    case '\n':
      line++;
      break;

    case '"': string(); break;

    default:
      if (isDigit(c)) {
        number();
      } else if (isAlpha(c)) {
        identifier();

      } else {
        Lox.error(line, "Unexpected character.");
      }

      break;
  }
}
//< scan-token
//> identifier
  private void identifier() {
    while (isAlphaNumeric(peek())) advance();

/* Scanning identifier < Scanning keyword-type
    addToken(IDENTIFIER);
*/
//> keyword-type
    // See if the identifier is a reserved word.
    String text = source.substring(start, current);

    TokenType type = keywords.get(text);
    if (type == null) type = IDENTIFIER;
    addToken(type);
//< keyword-type
  }
//< identifier
//> number
  private void number() {
    while (isDigit(peek())) advance();

    // Look for a fractional part.
    if (peek() == '.' && isDigit(peekNext())) {
      // Consume the "."
      advance();

      while (isDigit(peek())) advance();
    }

    addToken(NUMBER,
        Double.parseDouble(source.substring(start, current)));
  }
//< number
//> string
  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n') line++;
      advance();
    }

    // Unterminated string.
    if (isAtEnd()) {
      Lox.error(line, "Unterminated string.");
      return;
    }

    // The closing ".
    advance();

    // Trim the surrounding quotes.
    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
  }
//< string
//> match
  private boolean match(char expected) {
    if (isAtEnd()) return false;
    if (source.charAt(current) != expected) return false;

    current++;
    return true;
  }
//< match
//> peek
  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }
//< peek
//> peek-next
  private char peekNext() {
    if (current + 1 >= source.length()) return '\0';
    return source.charAt(current + 1);
  } // [peek-next]
//< peek-next
//> is-alpha
  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') ||
           (c >= 'A' && c <= 'Z') ||
            c == '_';
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }
//< is-alpha
//> is-digit
  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  } // [is-digit]
//< is-digit
//> is-at-end
  private boolean isAtEnd() {
    return current >= source.length();
  }
//< is-at-end
//> advance-and-add-token
  private char advance() {
    current++;
    return source.charAt(current - 1);
  }

  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }
//< advance-and-add-token
}
