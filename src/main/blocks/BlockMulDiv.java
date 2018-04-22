package main.blocks;

import java.util.HashMap;

/**
 * Blok pro nasobeni a deleni.
 * Vsechny hodnoty ve skupine 0 se budou nasobyt a ve skupine 1 se budou delit.
 * Vysledek je ulozen na vsechny vystupni porty.
 * pr. 0:{ 10.0, 2.0, 1.0 } 1:{ 5.0, 2.0 }
 * 10.0 * 2.0 * 1.0 / 5.0 / 2.0 = 2.0
 */
public class BlockMulDiv extends Block {

  @Override
  public boolean IsPossibleGroupIn(Integer group) {
    return ( group >= 0 && group <=1 );
  }
  @Override
  public boolean IsPossibleGroupOut(Integer group) {
    return group == 0;
  }

  @Override
  public HashMap<Integer, Double> Operation(HashMap<Integer, Double> data) {
    Double top = 1.0;
    Double down = 1.0;

    for(Integer i=0; i<portsIn.size(); i++) {
      if(groupIn.get(i).equals(0)) {
        top *= data.get(portsIn.get(i));
      }
      else {
        down *= data.get(portsIn.get(i));
      }
    }

    Double result = top / down;
    HashMap<Integer, Double> results = new HashMap<>();
    for(Integer port : portsOut) {
      results.put(port, result);
    }
    return results;
  }
}

