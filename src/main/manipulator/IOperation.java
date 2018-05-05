
package main.manipulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IOperation {
  /**
   * Funkce vykona operaci nad daty a vrati vysup. 
   * @param  data Vstupni hodnoty. Jako klice jsou pouzity cisla vstupnich portu.
   * @return      Vystupni hodnota.
   */
  public Map<Integer,Double> Operation(Map<Integer,Double> data);

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
