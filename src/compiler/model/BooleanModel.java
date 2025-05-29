package compiler.model;

// example: true
public class BooleanModel extends ExpressionModel {

  private boolean value;

  public BooleanModel(boolean value, int lineNumber) {
    super(lineNumber);

    this.value = value;
  }

  public boolean getValue() {

    return value;
  }

  @Override
  public String toString() {

    return "^boolean [" + value + "]$";
  }
}
