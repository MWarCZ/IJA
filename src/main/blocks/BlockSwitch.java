/**
 * Obsahuje tridu BlockSwitch, ktera predstavuje prok pro prepinani hodnot mezi radky.
 *
 * @author Miroslav Válka (xvalka05)
 * @author Jan Trněný (xtrnen03)
 */

package main.blocks;

import javafx.collections.FXCollections;
import java.util.Map;

/**
 * Blok pro krizovani.
 * Kazdy vstup je samostatna skupina. Vystupy mohou patrit jen
 * do skupin schodnych s vstupnimi skupinami.
 */
public class BlockSwitch extends Block {

    /**
     * Konstruktor inicializuje vychozi hodnoty.
     *
     * @param positionStart Vychozi hodnota pocatecni pozice umisteni bloku.
     * @param positionEnd   Vychozi hodnota konce pozice umisteni bloku.
     */
    public BlockSwitch(Integer positionStart, Integer positionEnd) {
        super(positionStart, positionEnd);
    }

    /**
     * Konstruktor inicializuje vychozi hodnoty.
     */
    public BlockSwitch() {
        super();
    }

    @Override
    public boolean IsPossibleGroupIn(Integer group) {
        for (Integer existGroup : this.groupIn) {
            if (group.equals(existGroup)) {
                return false;
            }
        }
        return (group >= 0);
    }

    @Override
    public boolean IsPossibleGroupOut(Integer group) {
        for (Integer existGroup : this.groupIn) {
            if (group.equals(existGroup)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer GetNextGroupOut(Integer group) throws PortGroupException {
        Integer indexGroup = groupIn.indexOf(group);
        indexGroup = indexGroup + 1;
        if (indexGroup < groupIn.size()) {
            return groupIn.get(indexGroup);
        } else {
            for (Integer g : groupIn) {
                return g;
            }
        }
        throw new PortGroupException("Neexistuje zadna mozna skupina.");

    }

    @Override
    public Map<Integer, Double> Operation(Map<Integer, Double> data) {
//      Map<Integer, Double> results = new HashMap<>();
        Map<Integer, Double> results = FXCollections.observableHashMap();

        for (Integer i = 0; i < portsIn.size(); i++) {
            Integer group = groupIn.get(i);
            for (Integer j = 0; j < portsOut.size(); j++) {
                if (groupOut.get(j).equals(group)) {
                    results.put(portsOut.get(j), data.get(portsIn.get(i)));
                }
            }
        }

        return results;
    }
}

