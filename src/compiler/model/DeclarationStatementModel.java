package compiler.model;

// declaration is statement to declare a new name (function)
public class DeclarationStatementModel extends StatementModel {

  public DeclarationStatementModel(int lineNumber) {
    super(lineNumber);
  }

  @Override
  public String toString() {

    return "^declaration_statement []$";
  }
}
