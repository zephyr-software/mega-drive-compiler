import static tool.TextTool.*;

import java.util.List;

public class MegaDriveCompiler {

  public static void main(String[] args) {
    printBanner("mega drive compiler: start");

    validateArgs(args);

    printShortBanner("lexer");

    Lexer lexer = new Lexer(args[0]);
    List<Token> tokenList = lexer.tokenize();
    for (Token token : tokenList) {
      println(token);
    }

    printBanner("mega drive compiler: end");
  }

  public static void validateArgs(String[] args) {
    for (String arg : args) {
      println("arg: " + arg);
    }

    if (args.length == 0) {
      println("file name is required ...");

      System.exit(0);
    }
  }
}
