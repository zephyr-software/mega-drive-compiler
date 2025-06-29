package compiler.model;

// examples:
//
// var = 1
// name = "knuckles"
public class AssignmentStatementModel extends StatementModel {

  private ExpressionModel left;
  private ExpressionModel right;

  public AssignmentStatementModel(ExpressionModel left, ExpressionModel right, int lineNumber) {
    super(lineNumber);

    this.left = left;
    this.right = right;
  }

  public ExpressionModel getLeft() {

    return left;
  }

  public ExpressionModel getRight() {

    return right;
  }

  @Override
  public String toString() {

    return "^assignment_statement [" + left + "; " + right + "]$";
  }
}
