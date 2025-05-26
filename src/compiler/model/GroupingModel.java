package compiler.model;

// example: ( <expr> )
public class GroupingModel extends ExpressionModel {

  private ExpressionModel value;

  public GroupingModel(ExpressionModel value) {
    this.value = value;
  }

  @Override
  public String toString() {

    return "group [" + value.toString() + "]";
  }
}
