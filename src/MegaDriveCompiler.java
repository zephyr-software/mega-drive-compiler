import static tool.ArgTool.convert;
import static tool.ArgTool.validate;
import static tool.FileTool.readFile;
import static tool.TextTool.printBanner;
import static tool.TextTool.printLine;
import static tool.TextTool.printShortBanner;

import exception.FileException;
import exception.ValidationException;
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
      parser.parse();

      printBanner("mega drive compiler: end");
    } catch (FileException | ValidationException exception) {
      printLine(exception.getMessage());
    }
  }
}
