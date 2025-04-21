import java.io.RandomAccessFile;

public class MegaDriveCompiler {

  public static void main(String[] args) {
    println("mega drive compiler: start");

    for (String arg : args) {
      println("arg: " + arg);
    }

    if (args.length == 0) {
      println("file name is required ...");

      System.exit(0);
    }

    try {
      RandomAccessFile file = new RandomAccessFile(args[0], "r");

      String str;
      while ((str = file.readLine()) != null) {
        System.out.println(str);
      }
      file.close();
    } catch (Exception exception) {
      println(exception.getMessage());
    }

    println("mega drive compiler: done");
  }

  public static void println(Object object) {
    System.out.println(object);
  }
}
