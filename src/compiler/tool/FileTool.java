package compiler.tool;

import compiler.exception.FileException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class FileTool {

  private FileTool() {}

  public static final char[] readFile(String fileName) throws FileException {
    RandomAccessFile file = null;
    char[] fileChars = null;

    try {
      file = new RandomAccessFile(fileName, "r");

      byte[] fileBytes = new byte[(int) file.length()];
      file.readFully(fileBytes);
      fileChars = new String(fileBytes, StandardCharsets.UTF_8).toCharArray();
    } catch (Exception exception) {

      throw new FileException(exception.getMessage());
    } finally {
      if (file != null) {
        try {
          file.close();
        } catch (IOException exception) {

          throw new FileException(exception.getMessage());
        }
      }
    }

    return fileChars;
  }
}
