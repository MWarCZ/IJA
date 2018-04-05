
package main.manipulator;

import java.util.ArrayList;

public interface IOperation {
  /**
   * Funkce vykona operaci nad daty a vrati vysup. 
   * @param  data Vstupni hodnoty.
   * @return      Vystupni hodnota.
   */
  public Double Operation(ArrayList<Double> data);

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
