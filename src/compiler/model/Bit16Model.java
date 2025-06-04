package compiler.model;

// example: 16
public class Bit16Model extends ExpressionModel {

  private Integer value;

  public Bit16Model(Integer value, int lineNumber) {
    super(lineNumber);

    this.value = value;
  }

  @Override
  public Integer getValue() {

    return value;
  }

  @Override
  public String toString() {

    return "^bit16 [" + value + "]$";
  }
}
