/**
 * Obsahuje tridu PortGroupException.
 * Jedna se o vyjimku, ktera muze byt vyvolana bloky pri praci se skupinami porty.
 *
 * @author Miroslav Válka (xvalka05)
 * @author Jan Trněný (xtrnen03)
 */

package main.blocks;

import main.blocks.PortException;
/**
 * Vyjimka pro chybu spusobena pri manipulaci se skupinou u portu.
 */
public class PortGroupException extends PortException {
  public PortGroupException() { super(); }
  public PortGroupException(String message) { super(message); }
  public PortGroupException(String message, Throwable cause) { super(message, cause); }
  public PortGroupException(Throwable cause) { super(cause); }
}
