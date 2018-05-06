/**
 * Obsahuje tridu PortException. Jedna se o vyjimku, ktera muze byt vyvolana bloky pri praci s porty.
 *
 * @author Miroslav Válka (xvalka05)
 * @author Jan Trněný (xtrnen03)
 */

package main.blocks;

/**
 * Vyjimka pro chyby zpusobene pri praci s porty bloku.
 */
public class PortException extends Exception {
  public PortException() { super(); }
  public PortException(String message) { super(message); }
  public PortException(String message, Throwable cause) { super(message, cause); }
  public PortException(Throwable cause) { super(cause); }
}
