package compiler.model;

import java.util.List;

// "func" <name> "(" <params>? ")" <body_Statement_list> "end"
public class FunctionDeclarationStatementModel extends DeclarationStatementModel {

  private String name;
  private List<ParameterStatementModel> parameterStatementModelList;
  private StatementListModel statementListModel;

  public FunctionDeclarationStatementModel(
      String name,
      List<ParameterStatementModel> parameterStatementModelList,
      StatementListModel statementListModel,
      int lineNumber) {
    super(lineNumber);

    this.name = name;
    this.parameterStatementModelList = parameterStatementModelList;
    this.statementListModel = statementListModel;
  }

  public String getName() {

    return name;
  }

  public List<ParameterStatementModel> getParameterStatementModelList() {

    return parameterStatementModelList;
  }

  public StatementListModel getstatementListModel() {

    return statementListModel;
  }

  @Override
  public String toString() {

    return "^function_declaration_statement ["
        + name
        + "; "
        + parameterStatementModelList
        + "; "
        + statementListModel
        + "]$";
  }
}
