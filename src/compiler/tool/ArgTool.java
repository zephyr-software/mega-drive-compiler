package compiler.tool;

import static compiler.tool.TextTool.printLine;

import compiler.exception.ValidationException;

public class ArgTool {

  private ArgTool() {

  }

  public static String convert(String[] args) {

    return "command line argument list: " + String.join(" ", args);
  }

  public static void validate(String[] args) throws ValidationException {
    if (args.length < 1) {

       throw new ValidationException("file name required");
    }
  }
}
