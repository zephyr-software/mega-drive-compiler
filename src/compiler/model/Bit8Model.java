package compiler.model;

public class Bit8Model extends ExpressionModel {

  private Integer value;

  public Bit8Model(Integer value) {
    this.value = value;
  }

  @Override
  public String toString() {

    return value.toString();
  }
}
