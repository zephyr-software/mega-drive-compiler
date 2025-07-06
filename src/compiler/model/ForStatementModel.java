package compiler.model;

// example:
// "for" <identifier> "=" <start> "," <end> ("," <step>)? "do" <body_stmts> "end"
public class ForStatementModel extends StatementModel {

  private IdentifierModel identifierModel;

  private ExpressionModel startExpressionModel;
  private ExpressionModel endExpressionModel;
  private ExpressionModel stepExpressionModel;

  private StatementListModel statementListModel;

  public ForStatementModel(
      IdentifierModel identifierModel,
      ExpressionModel startExpressionModel,
      ExpressionModel endExpressionModel,
      ExpressionModel stepExpressionModel,
      StatementListModel statementListModel,
      int lineNumber) {
    super(lineNumber);

    this.identifierModel = identifierModel;

    this.startExpressionModel = startExpressionModel;
    this.endExpressionModel = endExpressionModel;
    this.stepExpressionModel = stepExpressionModel;

    this.statementListModel = statementListModel;
  }

  public IdentifierModel getIdentifierModel() {

    return identifierModel;
  }

  public ExpressionModel getStartExpressionModel() {

    return startExpressionModel;
  }

  public ExpressionModel getEndExpressionModel() {

    return endExpressionModel;
  }

  public ExpressionModel getStepExpressionModel() {

    return stepExpressionModel;
  }

  public StatementListModel getStatementListModel() {

    return statementListModel;
  }

  @Override
  public String toString() {

    return "^for_statement ["
        + identifierModel
        + "; "
        + startExpressionModel
        + "; "
        + endExpressionModel
        + "; "
        + stepExpressionModel
        + "; "
        + statementListModel
        + "]$";
  }
}
