package compiler.model;

import java.util.List;

public class StatementListModel extends StatementModel {

  private List<StatementModel> statementModelList;

  public StatementListModel(List<StatementModel> statementModelList, int lineNumber) {
    super(lineNumber);

    this.statementModelList = statementModelList;
  }

  public List<StatementModel> getStatementModelList() {

    return statementModelList;
  }

  @Override
  public String toString() {

    return "^statement_list_model [" + statementModelList + "]$";
  }
}
