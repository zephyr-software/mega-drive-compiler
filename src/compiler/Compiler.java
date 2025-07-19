package compiler;

import compiler.model.BinaryOperatorModel;
import compiler.model.Bit16Model;
import compiler.model.DebugPrintLineStatementModel;
import compiler.model.NodeModel;
import compiler.model.StatementListModel;
import compiler.model.StatementModel;
import java.util.ArrayList;
import java.util.List;

public class Compiler {

  private List<String> codeLineList = new ArrayList<String>();

  public Compiler() {}

  public List<String> compile(NodeModel nodeModel) {

    if (nodeModel instanceof Bit16Model) {
      Bit16Model bit16Model = (Bit16Model) nodeModel;
      Integer value = bit16Model.getValue();

      String codeLine = "move.w #" + value + ", -(sp)" + " ; store a value on the stack";
      codeLineList.add(codeLine);
    }

    if (nodeModel instanceof BinaryOperatorModel) {
      BinaryOperatorModel binaryOperatorModel = (BinaryOperatorModel) nodeModel;

      compile(binaryOperatorModel.getLeft());
      compile(binaryOperatorModel.getRight());

      Token operator = binaryOperatorModel.getOperator();
      TokenType tokenType = operator.getTokenType();
      if (tokenType == TokenType.PLUS) {
        String codeLine = "move.w (sp)+, d0" + " ; load a value from the stack to d0";
        codeLineList.add(codeLine);

        codeLine = "move.w (sp)+, d1" + " ; load a value from the stack to d1";
        codeLineList.add(codeLine);

        codeLine = "add.w d0, d1" + " ; add source: d0 to destination: d1";
        codeLineList.add(codeLine);

        codeLine = "move.w d1, -(sp)" + " ; store a value on the stack";
        codeLineList.add(codeLine);
      }
    }

    if (nodeModel instanceof DebugPrintLineStatementModel) {
      DebugPrintLineStatementModel debugPrintLineStatementModel =
          (DebugPrintLineStatementModel) nodeModel;
      compile(debugPrintLineStatementModel.getExpressionModel());
    }

    if (nodeModel instanceof StatementListModel) {
      StatementListModel statementListModel = (StatementListModel) nodeModel;
      List<StatementModel> statementModelList = statementListModel.getStatementModelList();

      List<Object> objectList = new ArrayList<Object>();
      for (StatementModel statementModel : statementModelList) {
        compile(statementModel);
      }
    }

    return codeLineList;
  }
}
