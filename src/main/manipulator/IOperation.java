
package main.manipulator;

import java.util.ArrayList;
import java.util.HashMap;

public interface IOperation {
  /**
   * Funkce vykona operaci nad daty a vrati vysup. 
   * @param  data Vstupni hodnoty. Jako klice jsou pouzity cisla vstupnich portu.
   * @return      Vystupni hodnota.
   */
  public HashMap<Integer,Double> Operation(HashMap<Integer,Double> data);

  /**
   * Funkce pro ziskani pole s indexy vstupnich portu.
   * (Index udava na jakou pozici je vede dany port.)
   * @return Vraci pole s indexy.
   */
  public ArrayList<Integer> GetPortsIn();

  /**
   * Funkce pro ziskani pole s indexy vystupnich portu.
   * (Index udava na jakou pozici je vede dany port.)
   * @return Vraci pole s indexy.
   */
  public ArrayList<Integer> GetPortsOut();

}
