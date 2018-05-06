/**
 * Obsahuje tridu SimulationEndException.
 * Jedna se o vyjimku, ktera muze byt vyvolana pokud probehla jiz cela simulace obvodu.
 *
 * @author Miroslav Válka (xvalka05)
 * @author Jan Trněný (xtrnen03)
 */
package main.project;

/**
 * Vyjimka pro oznameni, 6e simulace je jiz na konci.
 */
public class SimulationEndException extends Exception {
    public SimulationEndException() {
        super();
    }

    public SimulationEndException(String message) {
        super(message);
    }

    public SimulationEndException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimulationEndException(Throwable cause) {
        super(cause);
    }
}
