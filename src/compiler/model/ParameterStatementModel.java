package compiler.model;

// a single function parameter
public class ParameterStatementModel extends DeclarationStatementModel {

  private String name;

  public ParameterStatementModel(String name, int lineNumber) {
    super(lineNumber);

    this.name = name;
  }

  public String getName() {

    return name;
  }

  @Override
  public String toString() {

    return "parameter_statement [" + name + "]";
  }
}
