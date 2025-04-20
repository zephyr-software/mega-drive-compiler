import java.io.RandomAccessFile;

public class MegaDriveCompiler {

  public static void main(String[] args) {
    System.out.println("mega drive compiler is running ...");

    for (String arg : args) {
      System.out.println("arg: " + arg);
    }

    if (args.length == 0) {
      System.out.println("file name is required ...");

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
      System.out.println(exception.getMessage());
    }

    System.out.println("mega drive compiler has completed its work");
  }
}
