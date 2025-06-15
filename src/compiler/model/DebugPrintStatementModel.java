package compiler.model;

public class DebugPrintStatementModel extends StatementModel {

  private ExpressionModel expressionModel;

  public DebugPrintStatementModel(ExpressionModel expressionModel, int lineNumber) {
    super(lineNumber);

    this.expressionModel = expressionModel;
  }

  public ExpressionModel getExpressionModel() {

    return expressionModel;
  }

  @Override
  public String toString() {

    return "^debug_print_statement [" + expressionModel + "]$";
  }
}
