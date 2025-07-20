package compiler.tool;

public class TextTool {

  private static final String BANNER_LINE =
      "================================================================================";
  private static final String SHORT_BANNER_LINE = "----------------------------------------";

  private static final int AST_FORMAT_OFFSET = 4;

  private TextTool() {}

  public static void print(Object object) {
    System.out.print(object);
  }

  public static void printLine(Object object) {
    System.out.println(object);
  }

  public static void printBanner(String message) {
    printLine(BANNER_LINE);
    printLine(message);
    printLine(BANNER_LINE);
  }

  public static void printShortBanner(String message) {
    printLine(SHORT_BANNER_LINE);
    printLine(message);
    printLine(SHORT_BANNER_LINE);
  }

  public static void printFormattedAST(String ast) {
    String result = "";

    int ast_depth = 0;
    for (int i = 0; i < ast.length(); i++) {
      char character = ast.charAt(i);

      if (character == '^') {
        result += "\n";

        for (int j = 0; j < ast_depth; j++) {
          result += " ";
        }

        ast_depth += AST_FORMAT_OFFSET;
      } else if (character == '$') {
        result += "\n";

        ast_depth -= AST_FORMAT_OFFSET;
      } else {
        result += character;
      }
    }

    printLine(result);
  }
}
