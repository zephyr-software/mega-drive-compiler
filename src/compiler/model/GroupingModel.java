package compiler.model;

// example: ( <expr> )
public class GroupingModel extends ExpressionModel {

  private NodeModel value;

  public GroupingModel(NodeModel value, int lineNumber) {
    super(lineNumber);

    this.value = value;
  }

  @Override
  public NodeModel getValue() {

    return value;
  }

  @Override
  public String toString() {

    return "^group [" + value.toString() + "]$";
  }
}
