//> Classes conyoscript-instance
package com.proglang.conyoscript;

import java.util.HashMap;
import java.util.Map;

class LoxInstance {
  private LoxClass klass;
//> conyoscript-instance-fields
  private final Map<String, Object> fields = new HashMap<>();
//< conyoscript-instance-fields

  LoxInstance(LoxClass klass) {
    this.klass = klass;
  }
//> conyoscript-instance-get-property
  Object get(Token name) {
    if (fields.containsKey(name.lexeme)) {
      return fields.get(name.lexeme);
    }

//> conyoscript-instance-get-method
    LoxFunction method = klass.findMethod(name.lexeme);
/* Classes conyoscript-instance-get-method < Classes conyoscript-instance-bind-method
    if (method != null) return method;
*/
//> conyoscript-instance-bind-method
    if (method != null) return method.bind(this);
//< conyoscript-instance-bind-method

//< conyoscript-instance-get-method
    throw new RuntimeError(name, // [hidden]
        "Undefined property '" + name.lexeme + "'.");
  }
//< conyoscript-instance-get-property
//> conyoscript-instance-set-property
  void set(Token name, Object value) {
    fields.put(name.lexeme, value);
  }
//< conyoscript-instance-set-property
  @Override
  public String toString() {
    return klass.name + " instance";
  }
}
