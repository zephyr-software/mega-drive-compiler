package compiler.model;

import compiler.Token;

// example: x + y
public class BinaryOperatorModel extends ExpressionModel {

  private Token operator;

  private ExpressionModel left;
  private ExpressionModel right;

  public BinaryOperatorModel(
      Token operator, ExpressionModel left, ExpressionModel right, int lineNumber) {
    super(lineNumber);

    this.operator = operator;

    this.left = left;
    this.right = right;
  }

  public Token getOperator() {

    return operator;
  }

  public ExpressionModel getLeft() {

    return left;
  }

  public ExpressionModel getRight() {

    return right;
  }

  @Override
  public String toString() {

    return "^binop [" + operator.getLexeme() + "; " + left + "; " + right + "]$";
  }
}
