package compiler;

import compiler.exception.InterpreterException;
import compiler.model.BinaryOperatorModel;
import compiler.model.Bit16Model;
import compiler.model.BooleanModel;
import compiler.model.GroupingModel;
import compiler.model.LogicalOperatorModel;
import compiler.model.NodeModel;
import compiler.model.StringModel;
import compiler.model.UnaryOperatorModel;

public class Interpreter {

  public Interpreter() {}

  public Object interpret(NodeModel nodeModel) throws InterpreterException {

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
      NodeModel value = groupingModel.getValue();

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

      if (operand instanceof Boolean) {
        Boolean value = (Boolean) operand;

        if (tokenType == TokenType.NOT) {

          return !value;
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

      if (tokenType == TokenType.LESS_THAN_OR_EQUALS) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return (Integer) leftValue <= (Integer) rightValue;
        }
      }

      if (tokenType == TokenType.GREATER_THAN_OR_EQUALS) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return (Integer) leftValue >= (Integer) rightValue;
        }
      }

      if (tokenType == TokenType.MODULO) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return (Integer) leftValue % (Integer) rightValue;
        }
      }

      if (tokenType == TokenType.EQUALS) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return (Integer) leftValue == (Integer) rightValue;
        }

        if ((leftValue instanceof Boolean) && (rightValue instanceof Boolean)) {

          return (Boolean) leftValue == (Boolean) rightValue;
        }
      }

      if (tokenType == TokenType.NOT_EQUALS) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return (Integer) leftValue != (Integer) rightValue;
        }

        if ((leftValue instanceof Boolean) && (rightValue instanceof Boolean)) {

          return (Boolean) leftValue == (Boolean) rightValue;
        }
      }
    }

    if (nodeModel instanceof LogicalOperatorModel) {
      LogicalOperatorModel logicalOperatorModel = (LogicalOperatorModel) nodeModel;

      Object leftValue = interpret(logicalOperatorModel.getLeft());
      Object rightValue = interpret(logicalOperatorModel.getRight());

      Token operator = logicalOperatorModel.getOperator();
      TokenType tokenType = operator.getTokenType();
      if (tokenType == TokenType.AND) {
        if ((leftValue instanceof Boolean) && (rightValue instanceof Boolean)) {

          return (Boolean) leftValue && (Boolean) rightValue;
        }
      }

      if (tokenType == TokenType.OR) {
        if ((leftValue instanceof Boolean) && (rightValue instanceof Boolean)) {

          return (Boolean) leftValue || (Boolean) rightValue;
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
