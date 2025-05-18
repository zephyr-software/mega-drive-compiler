package model;

public class UnaryOperatorModel {

  private Token operator;

  private ExpressionModel operand;

  public UnaryOperatorModel(Token operator, ExpressionModel operand) {
    this.operator = operator;

    this.opreand = operand
  }

  @Override
  public String toString() {

    return operator.toString() + " " + operand.toString();
  }
}
