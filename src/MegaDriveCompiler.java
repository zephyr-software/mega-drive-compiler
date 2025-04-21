import java.util.List;

public class MegaDriveCompiler {

  public static void main(String[] args) {
    println("================================================================================");
    println("mega drive compiler: start");
    println("================================================================================");

    validateArgs(args);

    println("----------------------------------------");
    println("lexer");
    println("----------------------------------------");

    Lexer lexer = new Lexer(args[0]);
    List<Token> tokenList = lexer.tokenize();
    for (Token token : tokenList) {
      println(token);
    }

    println("================================================================================");
    println("mega drive compiler: done");
    println("================================================================================");
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

  public static void println(Object object) {
    System.out.println(object);
  }
}
