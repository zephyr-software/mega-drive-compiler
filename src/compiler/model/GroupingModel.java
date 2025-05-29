package compiler.model;

// example: ( <expr> )
public class GroupingModel extends ExpressionModel {

  private ExpressionModel value;

  public GroupingModel(ExpressionModel value, int lineNumber) {
    super(lineNumber);

    this.value = value;
  }

  public ExpressionModel getValue() {

    return value;
  }

  @Override
  public String toString() {

    return "^group [" + value.toString() + "]$";
  }
}
