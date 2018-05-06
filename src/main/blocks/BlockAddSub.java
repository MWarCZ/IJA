/**
 * Obsahuje tridu BlockAddSub, ktera predstavuje blok, ktery je schopen scitat i odcitat.
 *
 * @author Miroslav Válka (xvalka05)
 * @author Jan Trněný (xtrnen03)
 */

package main.blocks;

import javafx.collections.FXCollections;

import java.util.Map;

/**
 * Blok pro scitani a odecitani.
 * Vsechny hodnoty ve skupine 0 se budou pricitat a ve skupine 1 se budou odecitat.
 * Vysledek je ulozen na vsechny vystupni porty.
 * pr. 0:{ 2.0, 3.0, 1.0 } 1:{ 5.0, 1.0 }
 * 2.0 + 3.0 + 1.0 - 5.0 - 1.0 = 0.0
 */
public class BlockAddSub extends Block {

    /**
     * Konstruktor inicializuje vychozi hodnoty.
     *
     * @param positionStart Vychozi hodnota pocatecni pozice umisteni bloku.
     * @param positionEnd   Vychozi hodnota konce pozice umisteni bloku.
     */
    public BlockAddSub(Integer positionStart, Integer positionEnd) {
        super(positionStart, positionEnd);
    }

    /**
     * Konstruktor inicializuje vychozi hodnoty.
     */
    public BlockAddSub() {
        super();
    }

    @Override
    public boolean IsPossibleGroupIn(Integer group) {
        return (group >= 0 && group <= 1);
    }

    @Override
    public boolean IsPossibleGroupOut(Integer group) {
        return group == 0;
    }

    @Override
    public Map<Integer, Double> Operation(Map<Integer, Double> data) {
        Double sum = 0.0;

        for (Integer i = 0; i < portsIn.size(); i++) {
            if (groupIn.get(i).equals(0)) {
                sum += data.get(portsIn.get(i));
            } else {
                sum -= data.get(portsIn.get(i));
            }
        }

//        Map<Integer, Double> results = new HashMap<>();
        Map<Integer, Double> results = FXCollections.observableHashMap();
        for (Integer port : portsOut) {
            results.put(port, sum);
        }
        return results;
    }
}

