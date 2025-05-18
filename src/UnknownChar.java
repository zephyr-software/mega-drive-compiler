public class UnknownChar {

  private String character;
  private int line;

  public UnknownChar(String character, int line) {
    this.character = character;
    this.line = line;
  }

  @Override
  public String toString() {

    return "unknown char [character: " + character + "; line: " + line + "]";
  }
}
