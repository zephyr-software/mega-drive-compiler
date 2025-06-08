package compiler.model;

import compiler.Token;

// example: x + y
public class BinaryOperatorModel extends ExpressionModel {

  private Token operator;

  private NodeModel left;
  private NodeModel right;

  public BinaryOperatorModel(Token operator, NodeModel left, NodeModel right, int lineNumber) {
    super(lineNumber);

    this.operator = operator;

    this.left = left;
    this.right = right;
  }

  public Token getOperator() {

    return operator;
  }

  public NodeModel getLeft() {

    return left;
  }

  public NodeModel getRight() {

    return right;
  }

  @Override
  public String toString() {

    return "^binop [" + operator.getLexeme() + "; " + left + "; " + right + "]$";
  }
}
