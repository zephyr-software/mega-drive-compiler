package compiler.model;

// "return" <expression>
public class ReturnStatementModel extends StatementModel {

  private ExpressionModel value;

  public ReturnStatementModel(ExpressionModel value, int lineNumber) {
    super(lineNumber);

    this.value = value;
  }

  public ExpressionModel getValue() {

    return value;
  }

  @Override
  public String toString() {

    return "^return_statement [" + value + "]$";
  }
}
