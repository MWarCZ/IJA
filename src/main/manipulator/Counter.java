
package main.manipulator;

import main.manipulator.IOperation;
import java.util.ArrayList;
import java.util.HashMap;

public class Counter {
  private HashMap<Integer,Double> valueIn;
  private HashMap<Integer,Double> valueOut;
  public Integer counter;

  public Counter() {
    this.valueIn = new HashMap<Integer,Double>();
    this.valueOut = new HashMap<Integer,Double>();
    this.counter = 0;
  }

  /**
   * Vrati hodnotu, ktera je pripravena pro vstupni port s indexem 'index'.
   * @param index Index na kterem je ocekavana hodnota.
   * @return  Vraci nactenou hodnotu nebo null, pokud neni k dispozici hodnota.
   */
  public Double GetValueIn(Integer index) {
//    if(this.valueIn.size() <= index) {
//      //throw new ArithmeticException("GetValueIn");
//      return null;
//    }
    return this.valueIn.get(index);
  }

  /**
   * Nastavi hodnotu na vystup na ocekavany dany index.
   * @param index Index na kery bude ulozena hodnota.
   * @param value Hodnota, ktera bude ulozena.
   */
  public void SetValueOut(Integer index, Double value) {
//    while(this.valueOut.size() <= index) {
//      this.valueOut.add(null);
//    }
    this.valueOut.put(index, value);
  }

  /**
   * Vrati hodnotu, ktera je nyni na vystupu pro port s indexem 'index'.
   * @param index Index na kterem je ocekavana hodnota.
   * @return  Vraci nactenou hodnotu nebo null, pokud neni k dispozici hodnota.
   */
  public Double GetValueOut(Integer index) {
//    if(this.valueOut.size() <= index) {
//      return null;
//    }
    return this.valueOut.get(index);
  }

  /**
   * Funkce prehodi vystupni hodnoty na vstupni a vystup vycisti a inkrementuje citac.
   */
  public void SetOut2In() {
    HashMap<Integer,Double> tmp = this.valueIn;
    this.valueIn = this.valueOut;
    this.valueOut = tmp;
    this.valueOut.clear();
    this.counter++;
  }

  /**
   * Pokud 'swap' je true tak se nejprve vykona prehozeni vystupu na vstupy a inkrementuje se citac.
   * Funkce zjisti jake vstupy ocekava 'operator' a pripravi mu je z aktualnich vstupu.
   * Nasledne zavola operaci operatoru a
   * vracenou hodnotu ulozi do vystupu na pozice dane vystupnimi porty operatoru.
   * @param operator Operator s jehoz vstupy, vystupy a operaci se pracuje.
   * @param swap Urcuje zda se zmeni vystupy na vstupy.
   */
  public void Step(IOperation operator, boolean swap) {
    if(swap) {
      SetOut2In();
    }

    ArrayList<Integer> portsIn = operator.GetPortsIn();
    ArrayList<Double> valuesInput = new ArrayList<Double>();

    for(Integer portIndex: portsIn) {
      valuesInput.add( this.GetValueIn(portIndex) );
    }

    Double valueOut = operator.Operation(valuesInput); 

    ArrayList<Integer> portsOut = operator.GetPortsOut();

    for(Integer portIndex: portsOut) {
      this.SetValueOut(portIndex, valueOut);
    }

  }

  /**
   * Provadi stejne ukony jako Step(IOperation operator, boolean swap),
   * ale postupne pro vsechny bloky v listu.
   * @param operators List operatoru.
   * @param swap Urcuje zda se budou menit vystupy na vstupy.
   */
  public void Step(ArrayList<IOperation> operators, boolean swap) {
    for(IOperation operator: operators) {
      this.Step(operator, swap);
    }
  }

}
