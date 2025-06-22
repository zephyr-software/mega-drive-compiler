package compiler.model;

// example:
// "if" <expression> <then_statement_list> ("else" <else_statement_list>)? "end"
public class IfStatementModel extends StatementModel {

  private ExpressionModel testExpressionModel;

  private StatementListModel thenStatementListModel;
  private StatementListModel elseStatementListModel;

  public IfStatementModel(
      ExpressionModel testExpressionModel,
      StatementListModel thenStatementListModel,
      StatementListModel elseStatementListModel,
      int lineNumber) {
    super(lineNumber);

    this.testExpressionModel = testExpressionModel;

    this.thenStatementListModel = thenStatementListModel;
    this.elseStatementListModel = elseStatementListModel;
  }

  public ExpressionModel getTestExpressionModel() {

    return testExpressionModel;
  }

  public StatementListModel getThenStatementListModel() {

    return thenStatementListModel;
  }

  public StatementListModel getElseStatementListModel() {

    return elseStatementListModel;
  }

  @Override
  public String toString() {

    return "if_statement ["
        + testExpressionModel
        + "; "
        + thenStatementListModel
        + "; "
        + elseStatementListModel
        + "]$";
  }
}
