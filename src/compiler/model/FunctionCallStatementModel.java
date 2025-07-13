package compiler.model;

// a special type of statement used to wrap function call model
public class FunctionCallStatementModel extends StatementModel {

  private FunctionCallModel functionCallModel;

  public FunctionCallStatementModel(FunctionCallModel functionCallModel, int lineNumber) {
    super(lineNumber);

    this.functionCallModel = functionCallModel;
  }

  public FunctionCallModel getFunctionCallModel() {

    return functionCallModel;
  }

  @Override
  public String toString() {

    return "^function_call_statement [" + functionCallModel + "]$";
  }
}
