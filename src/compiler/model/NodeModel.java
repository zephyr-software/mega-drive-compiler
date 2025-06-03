package compiler.model;

public class NodeModel {

  private int lineNumber;

  public NodeModel(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public int getLineNumber() {

    return lineNumber;
  }

  public Object getValue() {

    return this;
  }
}
