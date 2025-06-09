package compiler.model;

import compiler.Token;

// x and y
// x or y
public class LogicalOperatorModel extends ExpressionModel {

  private Token operator;

  private ExpressionModel left;
  private ExpressionModel right;

  public LogicalOperatorModel(
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

    return "^logop [" + operator.getLexeme() + "; " + left + "; " + right + "]$";
  }
}
