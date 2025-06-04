package compiler.model;

// example: "this is a string"
public class StringModel extends ExpressionModel {

  private String value;

  public StringModel(String value, int lineNumber) {
    super(lineNumber);

    this.value = value;
  }

  @Override
  public String getValue() {

    return value;
  }

  @Override
  public String toString() {

    return "^string [" + value + "]$";
  }
}
