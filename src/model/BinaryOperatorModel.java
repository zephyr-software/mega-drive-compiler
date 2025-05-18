package model;

public class BinaryOperatorModel {

  private Token operator;

  private ExpressionModel left;
  private ExpressionModel right;

  public BinaryOperatorModel(Token operator, ExpressionModel left, ExpressionModel right) {
    this.operator = operator;

    this.left = left;
    this.right = right;
  }

  @Override
  public String toString() {

    return operator.toString() + " " + left.toString() + " " right.toString();
  }
}
