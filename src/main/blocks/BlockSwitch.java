package main.blocks;

import java.util.HashMap;

/**
 * Blok pro krizovani.
 * Kazdy vstup je samostatna skupina. Vystupy mohou patrit jen
 * do skupin schodnych s vstupnimi skupinami.
 */
public class BlockSwitch extends Block {

  public BlockSwitch(Integer positionStart, Integer positionEnd) {
    super(positionStart, positionEnd);
  }
  public BlockSwitch() {
    super();
  }

  @Override
  public boolean IsPossibleGroupIn(Integer group) {
    for(Integer existGroup : this.groupIn) {
      if(group.equals(existGroup)) {
        return false;
      }
    }
    return true;
  }
  @Override
  public boolean IsPossibleGroupOut(Integer group) {
    for(Integer existGroup : this.groupIn) {
      if(group.equals(existGroup)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Integer GetNextGroupOut(Integer group) throws PortException {
    Integer indexGroup = groupIn.indexOf(group);
    indexGroup = indexGroup+1;
    if(indexGroup<groupIn.size()) {
      return groupIn.get(indexGroup);
    }
    else {
      for(Integer g : groupIn) {
        return g;
      }
    }
    throw new PortGroupException("Neexistuje zadna mozna skupina.");

  }

  @Override
  public HashMap<Integer, Double> Operation(HashMap<Integer, Double> data) {
    HashMap<Integer, Double> results = new HashMap<>();

    for(Integer i=0; i<portsIn.size(); i++) {
      Integer group = groupIn.get(i);
      for(Integer j=0; j<portsOut.size(); j++) {
        if(groupOut.get(j).equals(group)) {
          results.put( portsOut.get(j), data.get(portsIn.get(i)) );
        }
      }
    }

    return results;
  }
}

