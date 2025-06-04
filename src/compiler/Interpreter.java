package compiler;

import compiler.exception.InterpreterException;
import compiler.model.BinaryOperatorModel;
import compiler.model.Bit16Model;
import compiler.model.BooleanModel;
import compiler.model.ExpressionModel;
import compiler.model.GroupingModel;
import compiler.model.StringModel;
import compiler.model.UnaryOperatorModel;

public class Interpreter {

  public Interpreter() {}

  public Object interpret(ExpressionModel expressionModel) throws InterpreterException {
    ExpressionModel nodeModel = expressionModel;

    if (nodeModel instanceof BooleanModel) {
      BooleanModel booleanModel = (BooleanModel) nodeModel;
      Boolean value = booleanModel.getValue();

      return value;
    }

    if (nodeModel instanceof Bit16Model) {
      Bit16Model bit16Model = (Bit16Model) nodeModel;
      Integer value = bit16Model.getValue();

      return value;
    }

    if (nodeModel instanceof StringModel) {
      StringModel stringModel = (StringModel) nodeModel;
      String value = stringModel.getValue();

      return value;
    }

    if (nodeModel instanceof GroupingModel) {
      GroupingModel groupingModel = (GroupingModel) nodeModel;
      ExpressionModel value = groupingModel.getValue();

      return interpret(value);
    }

    if (nodeModel instanceof UnaryOperatorModel) {
      UnaryOperatorModel unaryOperatorModel = (UnaryOperatorModel) nodeModel;

      Token operator = unaryOperatorModel.getOperator();
      TokenType tokenType = operator.getTokenType();

      Object operand = interpret(unaryOperatorModel.getOperand());
      if (operand instanceof Integer) {
        Integer value = (Integer) operand;

        if (tokenType == TokenType.PLUS) {

          return value;
        }

        if (tokenType == TokenType.MINUS) {

          return -value;
        }
      }
    }

    if (nodeModel instanceof BinaryOperatorModel) {
      BinaryOperatorModel binaryOperatorModel = (BinaryOperatorModel) nodeModel;

      Object leftValue = interpret(binaryOperatorModel.getLeft());
      Object rightValue = interpret(binaryOperatorModel.getRight());

      Token operator = binaryOperatorModel.getOperator();
      TokenType tokenType = operator.getTokenType();
      if (tokenType == TokenType.PLUS) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return (Integer) leftValue + (Integer) rightValue;
        }

        if ((leftValue instanceof String) && (rightValue instanceof Integer)) {

          return (String) leftValue + (Integer) rightValue;
        }

        if ((leftValue instanceof Integer) || (rightValue instanceof String)) {

          return (Integer) leftValue + (String) rightValue;
        }
      }

      if (tokenType == TokenType.MINUS) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return (Integer) leftValue - (Integer) rightValue;
        }
      }

      if (tokenType == TokenType.STAR) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return (Integer) leftValue * (Integer) rightValue;
        }
      }

      if (tokenType == TokenType.SLASH) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return (Integer) leftValue / (Integer) rightValue;
        }
      }

      if (tokenType == TokenType.LESS_THAN) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return (Integer) leftValue < (Integer) rightValue;
        }
      }

      if (tokenType == TokenType.GREATER_THAN) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return (Integer) leftValue > (Integer) rightValue;
        }
      }
    }

    int lineNumber = nodeModel.getLineNumber();
    Class aClass = nodeModel.getClass();
    Object value = nodeModel.getValue();

    throw new InterpreterException(
        "node model is not supported; line: "
            + lineNumber
            + "; class: "
            + aClass.getSimpleName()
            + "; value: "
            + value);
  }
}
