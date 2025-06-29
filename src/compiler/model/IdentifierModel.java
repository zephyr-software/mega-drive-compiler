package compiler.model;

// examples:
// x, y, _score, level_number
public class IdentifierModel extends ExpressionModel {

  private String name;

  public IdentifierModel(String name, int lineNumber) {
    super(lineNumber);

    this.name = name;
  }

  public String getName() {

    return name;
  }

  @Override
  public String toString() {

    return "^identifier [" + name + "]$";
  }
}
