
package main.manipulator;

import main.manipulator.IOperation;
import java.util.ArrayList;

public class Counter {
  private ArrayList<Double> valueIn;
  private ArrayList<Double> valueOut;
  private Integer counter;

  public Counter() {
    this.valueIn =  new ArrayList<Double>();
    this.valueOut =  new ArrayList<Double>();
    this.counter = -1;
  }

  public Double GetValueIn(Integer index) {
    if(this.valueIn.size() <= index) {
      throw new ArithmeticException("GetValueIn");
    }
    return this.valueIn.get(index);
  }

  public void SetValueOut(Integer index, Double value) {
    while(this.valueOut.size() <= index) {
      this.valueOut.add(0.0); 
    }
    this.valueOut.set(index, value);
  }

  public void SetOut2In() {
    ArrayList<Double> tmp = this.valueIn;
    this.valueIn = this.valueOut;
    this.valueOut = tmp;
    this.valueOut.clear();
  }
  public void Step(IOperation operator) {
    SetOut2In();

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
  public void Step(ArrayList<IOperation> operators) {
    for(IOperation operator: operators) {
      this.Step(operator);
    }
  }

}
