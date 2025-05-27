package compiler.model;

// expressions evaluate to a result, like x + (3 * y) >= 6
public class ExpressionModel extends NodeModel {

  public ExpressionModel(int lineNumber) {

    super(lineNumber);
  }
}
