package compiler.model;

// example: true
public class BooleanModel extends ExpressionModel {

  private Boolean value;

  public BooleanModel(Boolean value, int lineNumber) {
    super(lineNumber);

    this.value = value;
  }

  @Override
  public Boolean getValue() {

    return value;
  }

  @Override
  public String toString() {

    return "^boolean [" + value + "]$";
  }
}
