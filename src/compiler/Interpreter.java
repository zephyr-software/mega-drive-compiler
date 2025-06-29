package compiler;

import compiler.exception.InterpreterException;
import compiler.model.AssignmentStatementModel;
import compiler.model.BinaryOperatorModel;
import compiler.model.Bit16Model;
import compiler.model.BooleanModel;
import compiler.model.DebugPrintLineStatementModel;
import compiler.model.DebugPrintStatementModel;
import compiler.model.ExpressionModel;
import compiler.model.GroupingModel;
import compiler.model.IdentifierModel;
import compiler.model.IfStatementModel;
import compiler.model.LogicalOperatorModel;
import compiler.model.NodeModel;
import compiler.model.StatementListModel;
import compiler.model.StatementModel;
import compiler.model.StringModel;
import compiler.model.UnaryOperatorModel;
import java.util.ArrayList;
import java.util.List;

public class Interpreter {

  public Interpreter() {}

  public Object interpret(NodeModel nodeModel, Environment environment)
      throws InterpreterException {

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

      return interpret(value, environment);
    }

    if (nodeModel instanceof IdentifierModel) {
      IdentifierModel identifierModel = (IdentifierModel) nodeModel;
      String name = identifierModel.getName();
      int lineNumber = identifierModel.getLineNumber();

      Object value = environment.getVariable(name);
      if (value == null) {

        throw new InterpreterException("undeclared identifier: " + name + " line:" + lineNumber);
      }

      return value;
    }

    if (nodeModel instanceof AssignmentStatementModel) {
      AssignmentStatementModel assignmentStatementModel = (AssignmentStatementModel) nodeModel;

      ExpressionModel right = assignmentStatementModel.getRight();
      Object value = interpret(right, environment);

      ExpressionModel left = assignmentStatementModel.getLeft();
      if (left instanceof IdentifierModel) {
        IdentifierModel identifierModel = (IdentifierModel) left;
        String name = identifierModel.getName();

        environment.setVariable(name, value);
      }

      return value;
    }

    if (nodeModel instanceof UnaryOperatorModel) {
      UnaryOperatorModel unaryOperatorModel = (UnaryOperatorModel) nodeModel;

      Token operator = unaryOperatorModel.getOperator();
      TokenType tokenType = operator.getTokenType();

      Object operand = interpret(unaryOperatorModel.getOperand(), environment);
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

      Object leftValue = interpret(binaryOperatorModel.getLeft(), environment);
      Object rightValue = interpret(binaryOperatorModel.getRight(), environment);

      Token operator = binaryOperatorModel.getOperator();
      TokenType tokenType = operator.getTokenType();
      if (tokenType == TokenType.PLUS) {
        if ((leftValue instanceof Integer) && (rightValue instanceof Integer)) {

          return (Integer) leftValue + (Integer) rightValue;
        }

        if ((leftValue instanceof String) && (rightValue instanceof Integer)) {

          return (String) leftValue + (Integer) rightValue;
        }

        if ((leftValue instanceof Integer) && (rightValue instanceof String)) {

          return (Integer) leftValue + (String) rightValue;
        }

        if ((leftValue instanceof String) && (rightValue instanceof String)) {

          return (String) leftValue + (String) rightValue;
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

      Object leftValue = interpret(logicalOperatorModel.getLeft(), environment);
      Object rightValue = interpret(logicalOperatorModel.getRight(), environment);

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

    if (nodeModel instanceof StatementListModel) {
      StatementListModel statementListModel = (StatementListModel) nodeModel;
      List<StatementModel> statementModelList = statementListModel.getStatementModelList();

      List<Object> objectList = new ArrayList<Object>();
      for (StatementModel statementModel : statementModelList) {
        Object object = interpret(statementModel, environment);
        objectList.add(object);
      }

      return objectList;
    }

    if (nodeModel instanceof DebugPrintStatementModel) {
      DebugPrintStatementModel debugPrintStatementModel = (DebugPrintStatementModel) nodeModel;

      Object object = interpret(debugPrintStatementModel.getExpressionModel(), environment);
      System.out.print(object);

      return object;
    }

    if (nodeModel instanceof DebugPrintLineStatementModel) {
      DebugPrintLineStatementModel debugPrintLineStatementModel =
          (DebugPrintLineStatementModel) nodeModel;

      Object object = interpret(debugPrintLineStatementModel.getExpressionModel(), environment);
      System.out.println(object);

      return object;
    }

    if (nodeModel instanceof IfStatementModel) {
      IfStatementModel ifStatementModel = (IfStatementModel) nodeModel;

      ExpressionModel testExpressionModel = ifStatementModel.getTestExpressionModel();
      Boolean isConditionAccepted = (Boolean) interpret(testExpressionModel, environment);

      if (isConditionAccepted) {
        StatementListModel thenStatementListModel = ifStatementModel.getThenStatementListModel();

        return interpret(thenStatementListModel, new Environment(environment));
      } else {
        StatementListModel elseStatementListModel = ifStatementModel.getElseStatementListModel();

        return interpret(elseStatementListModel, new Environment(environment));
      }
    }

    if (nodeModel == null) {

      return null;
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
