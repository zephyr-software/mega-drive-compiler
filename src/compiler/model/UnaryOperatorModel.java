package compiler.model;

import compiler.token.Token;

// example: -operand
public class UnaryOperatorModel extends ExpressionModel {

  private Token operator;
  private NodeModel operand;

  public UnaryOperatorModel(Token operator, NodeModel operand, int lineNumber) {
    super(lineNumber);

    this.operator = operator;
    this.operand = operand;
  }

  public Token getOperator() {

    return operator;
  }

  public NodeModel getOperand() {

    return operand;
  }

  @Override
  public String toString() {

    return "^unop [" + operator.getLexeme() + "; " + operand + "]$";
  }
}
