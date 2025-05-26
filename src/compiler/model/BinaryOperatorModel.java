package compiler.model;

import compiler.Token;

// example: x + y
public class BinaryOperatorModel extends ExpressionModel {

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

    return "^binop [" + operator.getLexeme() + "; " + left + "; " + right + "]$";
  }
}
