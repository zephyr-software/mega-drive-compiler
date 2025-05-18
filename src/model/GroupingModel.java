package model;

public class GroupingModel {

  private ExpressionModel value;

  public GroupingModel(ExpressionModel value) {
    this.value = value;
  }

  @Override
  public String toString() {

    return value.toString();
  }
}
