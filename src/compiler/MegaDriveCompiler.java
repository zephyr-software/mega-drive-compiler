package compiler;

import static compiler.tool.FileTool.readFile;
import static compiler.tool.TextTool.printBanner;
import static compiler.tool.TextTool.printFormattedAST;
import static compiler.tool.TextTool.printLine;
import static compiler.tool.TextTool.printShortBanner;
import static java.lang.String.format;
import static java.lang.String.join;

import compiler.exception.ValidationException;
import compiler.model.NodeModel;
import java.util.List;

public class MegaDriveCompiler {

  public static final String PROGRAM_NAME = "mega drive compiler";

  public static final String COMMAND_LINE_ARGUMENT_LIST = "command line argument list";

  public static final String LEXER = "lexer";
  public static final String PARSER = "parser";
  public static final String INTERPRETER = "interpreter";
  public static final String COMPILER = "compiler";

  public static final String SECTION = "section";

  public static final String START = "start";
  public static final String END = "end";

  public static final String ERROR = "error";

  public static final String FILE_NAME = "file name";

  public static final String REQUIRED = "required";

  public static void main(String[] args) {
    try {
      printBanner(format("%s: %s", PROGRAM_NAME, START));

      validateArgs(args);
      printLine(format("%s: %s", COMMAND_LINE_ARGUMENT_LIST, join(" ", args)));

      // lexer section
      printShortBanner(format("%s %s", LEXER, SECTION));

      char[] fileChars = readFile(args[0]);
      Lexer lexer = new Lexer(fileChars);
      List<Token> tokenList = lexer.tokenize();
      for (Token token : tokenList) {
        printLine(token);
      }

      // parser section
      printShortBanner(format("%s %s", PARSER, SECTION));

      Parser parser = new Parser(tokenList);
      NodeModel nodeModel = parser.parse();
      printLine(nodeModel);
      printFormattedAST(nodeModel.toString());

      // interpreter section
      printShortBanner(format("%s %s", INTERPRETER, SECTION));

      Interpreter interpreter = new Interpreter();
      Object value = interpreter.interpret(nodeModel, new Environment());
      printLine(value);

      // compiler section
      printShortBanner(format("%s %s", COMPILER, SECTION));

      Compiler compiler = new Compiler();
      List<String> codeLineList = compiler.compile(nodeModel);
      for (String codeLine : codeLineList) {
        printLine(codeLine);
      }
    } catch (Exception exception) {
      printBanner(exception.getMessage());
    } finally {
      printBanner(format("%s: %s", PROGRAM_NAME, END));
    }
  }

  private static void validateArgs(String[] args) throws ValidationException {
    if (args.length < 1) {

      throw new ValidationException(format("%s: %s %s", ERROR, FILE_NAME, REQUIRED));
    }
  }
}
