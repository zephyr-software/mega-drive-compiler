package compiler.model;

// example:
// "while" <expression> "do" <body_statement_list> "end"
public class WhileStatementModel extends StatementModel {

  private ExpressionModel testExpressionModel;

  private StatementListModel statementListModel;

  public WhileStatementModel(
      ExpressionModel testExpressionModel, StatementListModel statementListModel, int lineNumber) {
    super(lineNumber);

    this.testExpressionModel = testExpressionModel;

    this.statementListModel = statementListModel;
  }

  public ExpressionModel getTestExpressionModel() {

    return testExpressionModel;
  }

  public StatementListModel getStatementListModel() {

    return statementListModel;
  }

  @Override
  public String toString() {

    return "^while_statement [" + testExpressionModel + "; " + statementListModel + "]$";
  }
}
