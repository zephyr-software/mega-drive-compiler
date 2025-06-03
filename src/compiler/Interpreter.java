package compiler;

import compiler.exception.InterpreterException;
import compiler.model.BinaryOperatorModel;
import compiler.model.Bit16Model;
import compiler.model.ExpressionModel;
import compiler.model.GroupingModel;
import compiler.model.StringModel;
import compiler.model.UnaryOperatorModel;

public class Interpreter {

  public Interpreter() {}

  public Object interpret(ExpressionModel expressionModel) throws InterpreterException {
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

      Integer operand = (Integer) interpret(unaryOperatorModel.getOperand());

      if (unaryOperatorModel.getOperator().getTokenType() == TokenType.PLUS) {

        return +operand;
      }

      if (unaryOperatorModel.getOperator().getTokenType() == TokenType.MINUS) {

        return -operand;
      }
    }

    if (nodeModel instanceof BinaryOperatorModel) {
      BinaryOperatorModel binaryOperatorModel = (BinaryOperatorModel) nodeModel;

      Object leftValue = interpret(binaryOperatorModel.getLeft());
      Object rightValue = interpret(binaryOperatorModel.getRight());

      Token operator = binaryOperatorModel.getOperator();
      TokenType operatorTokenType = operator.getTokenType();
      if (operatorTokenType == TokenType.PLUS) {

        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return ((Integer) leftValue) + ((Integer) rightValue);
        }

        if ((leftValue instanceof String) && (rightValue instanceof Integer)) {

          return ((String) leftValue) + ((Integer) rightValue);
        }

        if ((leftValue instanceof Integer) || (rightValue instanceof String)) {

          return ((Integer) leftValue) + ((String) rightValue);
        }
      }

      if (operatorTokenType == TokenType.MINUS) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return ((Integer) leftValue) - ((Integer) rightValue);
        }
      }

      if (operatorTokenType == TokenType.STAR) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return ((Integer) leftValue) * ((Integer) rightValue);
        }
      }

      if (operatorTokenType == TokenType.SLASH) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return ((Integer) leftValue) / ((Integer) rightValue);
        }
      }
    }

    if (nodeModel instanceof StringModel) {
      StringModel stringModel = (StringModel) nodeModel;

      return String.valueOf(stringModel.getValue());
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
