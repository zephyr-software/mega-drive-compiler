package compiler.exception;

public class ReturnException extends Exception {

  private Object value;

  public ReturnException(Object value) {
    super();

    this.value = value;
  }

  public Object getValue() {

    return value;
  }
}
