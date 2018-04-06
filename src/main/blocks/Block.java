
package main.blocks;

import main.manipulator.IOperation;

import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;

public class Block implements IOperation {
  protected ArrayList<Integer> portsIn;
  protected ArrayList<Integer> portsOut;

  public Block(ArrayList<Integer> portsIn, ArrayList<Integer> portsOut) {
    this.portsIn = portsIn;
    this.portsOut = portsOut;
  }
  public Block() {
    portsIn =  new ArrayList<Integer>();
    portsOut =  new ArrayList<Integer>();
  }

  public Double Operation(HashMap<Integer,Double> data) {
    return 0.0;
  }
  public ArrayList<Integer> GetPortsIn() {
    return this.portsIn;
  }
  public ArrayList<Integer> GetPortsOut() {
    return this.portsOut;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Block){
      Block block = (Block)obj;
      return ( (this.portsIn.equals(block.portsIn)) && (this.portsOut.equals(block.portsOut)));
    }
    return false;
  }

  @Override
  public int hashCode() {
    return portsIn.hashCode() + portsOut.hashCode();
  }
}
