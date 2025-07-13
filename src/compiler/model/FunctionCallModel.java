package compiler.model;

import java.util.List;

//  <func_call> ::= <name> "(" <args>? ")"
//  <args> ::= <expr> ( ',' <expr> )*
public class FunctionCallModel extends ExpressionModel {

  private String name;
  private List<ExpressionModel> argumentList;

  public FunctionCallModel(String name, List<ExpressionModel> argumentList, int lineNumber) {
    super(lineNumber);

    this.name = name;
    this.argumentList = argumentList;
  }

  public String getName() {

    return name;
  }

  public List<ExpressionModel> getArgumentList() {

    return argumentList;
  }

  @Override
  public String toString() {

    return "^function_call [" + name + "; " + argumentList + " ]$";
  }
}
