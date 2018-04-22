package main.manipulator;

/**
 * Vyjimka pro chybu zpusobenou chybejici vstupni hodnoty.
 */
public class MissingValueException extends Exception {
  public MissingValueException() { super(); }
  public MissingValueException(String message) { super(message); }
  public MissingValueException(String message, Throwable cause) { super(message, cause); }
  public MissingValueException(Throwable cause) { super(cause); }
}
