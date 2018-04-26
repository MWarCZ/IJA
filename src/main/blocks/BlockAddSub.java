package main.blocks;

import java.util.HashMap;

/**
 * Blok pro scitani a odecitani.
 * Vsechny hodnoty ve skupine 0 se budou pricitat a ve skupine 1 se budou odecitat.
 * Vysledek je ulozen na vsechny vystupni porty.
 * pr. 0:{ 2.0, 3.0, 1.0 } 1:{ 5.0, 1.0 }
 * 2.0 + 3.0 + 1.0 - 5.0 - 1.0 = 0.0
 */
public class BlockAddSub extends Block {

  public BlockAddSub(Integer positionStart, Integer positionEnd) {
    super(positionStart, positionEnd);
  }
  public BlockAddSub() {
    super();
  }

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
    Double sum = 0.0;

    for(Integer i=0; i<portsIn.size(); i++) {
      if(groupIn.get(i).equals(0)) {
        sum += data.get(portsIn.get(i));
      }
      else {
        sum -= data.get(portsIn.get(i));
      }
    }

    HashMap<Integer, Double> results = new HashMap<>();
    for(Integer port : portsOut) {
      results.put(port, sum);
    }
    return results;
  }
}

