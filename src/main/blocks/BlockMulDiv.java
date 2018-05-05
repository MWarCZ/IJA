package main.blocks;

import javafx.collections.FXCollections;

import java.util.HashMap;
import java.util.Map;

/**
 * Blok pro nasobeni a deleni.
 * Vsechny hodnoty ve skupine 0 se budou nasobyt a ve skupine 1 se budou delit.
 * Vysledek je ulozen na vsechny vystupni porty.
 * pr. 0:{ 10.0, 2.0, 1.0 } 1:{ 5.0, 2.0 }
 * 10.0 * 2.0 * 1.0 / 5.0 / 2.0 = 2.0
 */
public class BlockMulDiv extends Block {

    public BlockMulDiv(Integer positionStart, Integer positionEnd) {
        super(positionStart, positionEnd);
    }

    public BlockMulDiv() {
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
        Double top = 1.0;
        Double down = 1.0;

        for (Integer i = 0; i < portsIn.size(); i++) {
            if (groupIn.get(i).equals(0)) {
                top *= data.get(portsIn.get(i));
            } else {
                down *= data.get(portsIn.get(i));
            }
        }

        Double result = top / down;
//        Map<Integer, Double> results = new HashMap<>();
        Map<Integer, Double> results = FXCollections.observableHashMap();
        for (Integer port : portsOut) {
            results.put(port, result);
        }
        return results;
    }
}

