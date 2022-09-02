//> Classes conyoscript-class
package com.proglang.conyoscript;

import java.util.List;
import java.util.Map;

/* Classes conyoscript-class < Classes conyoscript-class-callable
class LoxClass {
*/
//> conyoscript-class-callable
class LoxClass implements LoxCallable {
//< conyoscript-class-callable
  final String name;
//> Inheritance conyoscript-class-superclass-field
  final LoxClass superclass;
//< Inheritance conyoscript-class-superclass-field
/* Classes conyoscript-class < Classes conyoscript-class-methods

  LoxClass(String name) {
    this.name = name;
  }
*/
//> conyoscript-class-methods
  private final Map<String, LoxFunction> methods;

/* Classes conyoscript-class-methods < Inheritance conyoscript-class-constructor
  LoxClass(String name, Map<String, LoxFunction> methods) {
*/
//> Inheritance conyoscript-class-constructor
  LoxClass(String name, LoxClass superclass,
           Map<String, LoxFunction> methods) {
    this.superclass = superclass;
//< Inheritance conyoscript-class-constructor
    this.name = name;
    this.methods = methods;
  }
//< conyoscript-class-methods
//> conyoscript-class-find-method
  LoxFunction findMethod(String name) {
    if (methods.containsKey(name)) {
      return methods.get(name);
    }

//> Inheritance find-method-recurse-superclass
    if (superclass != null) {
      return superclass.findMethod(name);
    }

//< Inheritance find-method-recurse-superclass
    return null;
  }
//< conyoscript-class-find-method

  @Override
  public String toString() {
    return name;
  }
//> conyoscript-class-call-arity
  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    LoxInstance instance = new LoxInstance(this);
//> conyoscript-class-call-initializer
    LoxFunction initializer = findMethod("init");
    if (initializer != null) {
      initializer.bind(instance).call(interpreter, arguments);
    }

//< conyoscript-class-call-initializer
    return instance;
  }

  @Override
  public int arity() {
/* Classes conyoscript-class-call-arity < Classes conyoscript-initializer-arity
    return 0;
*/
//> conyoscript-initializer-arity
    LoxFunction initializer = findMethod("init");
    if (initializer == null) return 0;
    return initializer.arity();
//< conyoscript-initializer-arity
  }
//< conyoscript-class-call-arity
}
