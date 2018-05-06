/**
 * Obsahuje rozhrani IOperation.
 * Rozhrani definuje jake funkce jsou potreba pro provadeni vypoctu.
 *
 * @author Miroslav Válka (xvalka05)
 * @author Jan Trněný (xtrnen03)
 */
package main.manipulator;

import java.util.List;
import java.util.Map;

/**
 * Rozhrani definuje jake funkce jsou potreba pro provadeni vypoctu.
 */
public interface IOperation {
    /**
     * Funkce vykona operaci nad daty a vrati vysup.
     * @param  data Vstupni hodnoty. Jako klice jsou pouzity cisla vstupnich portu.
     * @return Vystupni hodnota.
     */
    public Map<Integer, Double> Operation(Map<Integer, Double> data);

    /**
     * Funkce pro ziskani pole s indexy vstupnich portu.
     * (Index udava na jakou pozici je vede dany port.)
     * @return Vraci pole s indexy.
     */
    public List<Integer> GetPortsIn();

    /**
     * Funkce pro ziskani pole s indexy vystupnich portu.
     * (Index udava na jakou pozici je vede dany port.)
     * @return Vraci pole s indexy.
     */
    public List<Integer> GetPortsOut();

}
