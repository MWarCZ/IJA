/**
 * Obsahuje tridu CycleException.
 * Jedna se o vyjimku, ktera muze byt vyvolana pokud dojde k cyklu nebo kolizi signalu/dat.
 *
 * @author Miroslav Válka (xvalka05)
 * @author Jan Trněný (xtrnen03)
 */
package main.manipulator;

/**
 * Vyjimka pro detekovani cyklu nebo kolize.
 */
public class CycleException extends Exception {
    public CycleException() {
        super();
    }

    public CycleException(String message) {
        super(message);
    }

    public CycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public CycleException(Throwable cause) {
        super(cause);
    }
}
