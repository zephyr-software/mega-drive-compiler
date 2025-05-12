package tool;

public class TextTool {

  private static final String BANNER_LINE = "================================================================================";
  private static final String SHORT_BANNER_LINE = "----------------------------------------";

  private TextTool() {

  }

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
}
