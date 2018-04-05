
package main.blocks;

import main.manipulator.IOperation;

import java.lang.String;
import java.util.ArrayList;

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

  public Double Operation(ArrayList<Double> data) {
    return 0.0;
  }
  public ArrayList<Integer> GetPortsIn() {
    return this.portsIn;
  }
  public ArrayList<Integer> GetPortsOut() {
    return this.portsOut;
  }

}
