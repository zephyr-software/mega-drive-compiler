package compiler.model;

public class DebugPrintLineStatementModel extends StatementModel {

  private ExpressionModel expressionModel;

  public DebugPrintLineStatementModel(ExpressionModel expressionModel, int lineNumber) {
    super(lineNumber);

    this.expressionModel = expressionModel;
  }

  public ExpressionModel getExpressionModel() {

    return expressionModel;
  }

  @Override
  public String toString() {

    return "^debug_print_line_statement [" + expressionModel + "]$";
  }
}
