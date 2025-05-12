import static tool.ArgTool.convert;
import static tool.ArgTool.validate;
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

      Lexer lexer = new Lexer(args[0]);
      List<Token> tokenList = lexer.tokenize();
      for (Token token : tokenList) {
        printLine(token);
      }

      printBanner("mega drive compiler: end");
    } catch (FileException | ValidationException exception) {
      printLine(exception.getMessage());
    }
  }
}
