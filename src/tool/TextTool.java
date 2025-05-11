package tool;

public class TextTool {

  private static final String BANNER_LINE = "================================================================================";
  private static final String SHORT_BANNER_LINE = "----------------------------------------";

  private TextTool() {

  }

  public static void println(Object object) {
    System.out.println(object);
  }

  public static void printBanner(String message) {
    println(BANNER_LINE);
    println(message);
    println(BANNER_LINE);
  }

  public static void printShortBanner(String message) {
    println(SHORT_BANNER_LINE);
    println(message);
    println(SHORT_BANNER_LINE);
  }
}
