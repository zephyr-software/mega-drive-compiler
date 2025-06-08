package compiler;

import static compiler.tool.ArgTool.convert;
import static compiler.tool.ArgTool.validate;
import static compiler.tool.FileTool.readFile;
import static compiler.tool.TextTool.printBanner;
import static compiler.tool.TextTool.printFormattedAST;
import static compiler.tool.TextTool.printLine;
import static compiler.tool.TextTool.printShortBanner;

import compiler.exception.FileException;
import compiler.exception.InterpreterException;
import compiler.exception.ParserException;
import compiler.exception.ValidationException;
import compiler.model.NodeModel;
import java.util.List;

public class MegaDriveCompiler {

  public static void main(String[] args) {
    try {
      printBanner("mega drive compiler: start");

      validate(args);
      printLine(convert(args));

      printShortBanner("lexer");
      char[] fileChars = readFile(args[0]);
      Lexer lexer = new Lexer(fileChars);
      List<Token> tokenList = lexer.tokenize();
      List<UnknownChar> unknownCharList = lexer.getUnknownCharList();
      for (UnknownChar unknownChar : unknownCharList) {
        printLine(unknownChar);
      }

      for (Token token : tokenList) {
        printLine(token);
      }

      printShortBanner("parser");
      Parser parser = new Parser(tokenList);
      NodeModel nodeModel = parser.parse();
      printLine(nodeModel);
      printFormattedAST(nodeModel.toString());

      printShortBanner("interpreter");
      Interpreter interpreter = new Interpreter();
      Object value = interpreter.interpret(nodeModel);
      printLine(value);

      printBanner("mega drive compiler: end");
    } catch (FileException
        | ParserException
        | ValidationException
        | InterpreterException exception) {
      printLine(exception.getMessage());
    }
  }
}
