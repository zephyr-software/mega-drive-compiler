package compiler;

import compiler.exception.InterpreterException;
import compiler.model.BinaryOperatorModel;
import compiler.model.Bit16Model;
import compiler.model.ExpressionModel;
import compiler.model.GroupingModel;
import compiler.model.UnaryOperatorModel;

public class Interpreter {

  public Interpreter() {}

  public Integer interpret(ExpressionModel expressionModel) throws InterpreterException {
    ExpressionModel nodeModel = expressionModel;

    if (nodeModel instanceof Bit16Model) {
      Bit16Model bit16Model = (Bit16Model) nodeModel;

      return Integer.valueOf(bit16Model.getValue());
    }

    if (nodeModel instanceof GroupingModel) {
      GroupingModel groupingModel = (GroupingModel) nodeModel;

      return interpret(groupingModel.getValue());
    }

    if (nodeModel instanceof UnaryOperatorModel) {
      UnaryOperatorModel unaryOperatorModel = (UnaryOperatorModel) nodeModel;

      Integer operand = interpret(unaryOperatorModel.getOperand());

      if (unaryOperatorModel.getOperator().getTokenType() == TokenType.PLUS) {

        return +operand;
      }

      if (unaryOperatorModel.getOperator().getTokenType() == TokenType.MINUS) {

        return -operand;
      }
    }

    if (nodeModel instanceof BinaryOperatorModel) {
      BinaryOperatorModel binaryOperatorModel = (BinaryOperatorModel) nodeModel;

      Integer leftValue = interpret(binaryOperatorModel.getLeft());
      Integer rightValue = interpret(binaryOperatorModel.getRight());

      Token operator = binaryOperatorModel.getOperator();
      TokenType operatorTokenType = operator.getTokenType();
      if (operatorTokenType == TokenType.PLUS) {

        return leftValue + rightValue;
      }

      if (operatorTokenType == TokenType.MINUS) {

        return leftValue - rightValue;
      }

      if (operatorTokenType == TokenType.STAR) {

        return leftValue * rightValue;
      }

      if (operatorTokenType == TokenType.SLASH) {

        return leftValue / rightValue;
      }
    }

    throw new InterpreterException("node model is not supported");
  }
}
