package main.project;

/**
 * Vyjimka pro oznameni, 6e simulace je jiz na konci.
 */
public class SimulationEndException extends Exception {
  public SimulationEndException() { super(); }
  public SimulationEndException(String message) { super(message); }
  public SimulationEndException(String message, Throwable cause) { super(message, cause); }
  public SimulationEndException(Throwable cause) { super(cause); }
}
