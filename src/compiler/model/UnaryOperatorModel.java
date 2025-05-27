package compiler.model;

import compiler.Token;

// example: -operand
public class UnaryOperatorModel extends ExpressionModel {

  private Token operator;
  private ExpressionModel operand;

  public UnaryOperatorModel(Token operator, ExpressionModel operand, int lineNumber) {
    super(lineNumber);

    this.operator = operator;
    this.operand = operand;
  }

  @Override
  public String toString() {

    return "^unop [" + operator.getLexeme() + "; " + operand + "]$";
  }
}
