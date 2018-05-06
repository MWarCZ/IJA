
package main.manipulator;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import main.manipulator.IOperation;
import main.manipulator.CycleException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Counter {
    private Map<Integer, Double> valueIn;
    private Map<Integer, Double> valueOut;
    //  public Integer counter;
    public IntegerProperty counterProperty = new SimpleIntegerProperty();

    public Counter() {
//        this.valueIn = new HashMap<Integer, Double>();
//        this.valueOut = new HashMap<Integer, Double>();
        this.valueIn = FXCollections.observableHashMap();
        this.valueOut = FXCollections.observableHashMap();
        this.SetCounter(-1);
        //this.counter = -1;
    }

    public Map GetValueIn() {
        return this.valueIn;
    }
    public Map GetValueOut() {
        return this.valueOut;
    }

    public void SetCounter(Integer value) {
        counterProperty.setValue(value);
    }

    public Integer GetCounter() {
        return counterProperty.getValue();
    }

    /**
     * Vrati hodnotu, ktera je pripravena pro vstupni port s indexem 'index'.
     *
     * @param index Index na kterem je ocekavana hodnota.
     * @return Vraci nactenou hodnotu nebo null, pokud neni k dispozici hodnota.
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
     *
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
     *
     * @param index Index na kterem je ocekavana hodnota.
     * @return Vraci nactenou hodnotu nebo null, pokud neni k dispozici hodnota.
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
        SetOut2In(false);
    }

    /**
     * Funkce predohi vstupni hodnoty na vstupni a vystup vycisti a inkrementuje citac.
     * Pokud je nastaveno memory na true, tak se zachovaji hodnoty z in pokud v out nebyli prepsany.
     *
     * @param memory Urcuje zda se budou pamatovat i hodnoty, ktere nebyly zmeneny.
     *               (tj. pokud v out je null tak se tam nahraje hodnota z in)
     */
    public void SetOut2In(boolean memory) {
//        Map<Integer, Double> tmp = this.valueIn;
        if (!memory) {
            this.valueIn.clear();
            this.valueIn.putAll(this.valueOut);
            this.valueOut.clear();
//            this.valueIn = this.valueOut;
//            this.valueOut = tmp;
        } else {
            for (Map.Entry<Integer, Double> pair : this.valueIn.entrySet()) {
                //if()
            }
        }
//        this.valueOut.clear();
        this.SetCounter(GetCounter() + 1);
        //this.counter++;
    }

    /**
     * Pokud 'swap' je true tak se nejprve vykona prehozeni vystupu na vstupy a inkrementuje se citac.
     * Funkce zjisti jake vstupy ocekava 'operator' a pripravi mu je z aktualnich vstupu.
     * Nasledne zavola operaci operatoru a
     * vracenou hodnotu ulozi do vystupu na pozice dane vystupnimi porty operatoru.
     *
     * @param operator Operator s jehoz vstupy, vystupy a operaci se pracuje.
     * @param swap     Urcuje zda se zmeni vystupy na vstupy.
     * @throws CycleException Pokud operace ma bit ulozena na stejnou pozici jako jiz existujici hodnota.
     */
    public void Step(IOperation operator, boolean swap) throws CycleException, MissingValueException {
        if (swap) {
            SetOut2In(false);
        }

        List<Integer> portsIn = operator.GetPortsIn();
//        Map<Integer, Double> valuesInput = new HashMap<Integer, Double>();
        Map<Integer, Double> valuesInput = FXCollections.observableHashMap();

        for (Integer portIndex : portsIn) {
            Double tmp = this.GetValueIn(portIndex);
            if (tmp == null) {
                throw new MissingValueException("Chyby vstupni hodota pro blok.");
            }
            valuesInput.put(portIndex, this.GetValueIn(portIndex));
        }

        Map<Integer, Double> valuesOut = operator.Operation(valuesInput);

        List<Integer> portsOut = operator.GetPortsOut();

        for (Integer portIndex : portsOut) {
            if (this.valueOut.containsKey(portIndex)) {
                throw new CycleException();
            }
            this.SetValueOut(portIndex, valuesOut.get(portIndex));
        }

    }

    /**
     * Provadi stejne ukony jako Step(IOperation operator, boolean swap),
     * ale postupne pro vsechny bloky v listu.
     *
     * @param operators List operatoru.
     * @param swap      Urcuje zda se budou menit vystupy na vstupy.
     * @throws CycleException Pokud operace ma bit ulozena na stejnou pozici jako jiz existujici hodnota.
     */
    public void Step(List<IOperation> operators, boolean swap) throws CycleException, MissingValueException {
        for (IOperation operator : operators) {
            this.Step(operator, swap);
        }
    }

}
