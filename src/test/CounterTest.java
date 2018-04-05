
package test;

import main.manipulator.Counter;

import static org.junit.Assert.*;

import main.manipulator.IOperation;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;

public class CounterTest {
  private Counter counter;
  private IOperation blokGetOne;
  private IOperation blokPlus;

  @Before
  public void prepare() {
    counter = new Counter();

    blokGetOne = new IOperation() {
      ArrayList<Integer> outPorts = new ArrayList<Integer>(Arrays.asList(1,3));
      @Override
      public Double Operation(ArrayList<Double> data) {
        return 1.0;
      }
      @Override
      public ArrayList<Integer> GetPortsIn() {
        return new ArrayList<Integer>();
      }
      @Override
      public ArrayList<Integer> GetPortsOut() {
        return outPorts;
      }
    };

    blokPlus = new IOperation() {
      ArrayList<Integer> portsIn = new ArrayList<Integer>(Arrays.asList(1, 3));
      ArrayList<Integer> portsOut = new ArrayList<Integer>(Arrays.asList(1,2,3));

      @Override
      public Double Operation(ArrayList<Double> data) {
        Double sum = 0.0;
        for(Double d: data) {
          sum += d;
        }
        return sum;
      }

      @Override
      public ArrayList<Integer> GetPortsIn() {
        return portsIn;
      }

      @Override
      public ArrayList<Integer> GetPortsOut() {
        return portsOut;
      }
    };
  }

  @Test
  public void Test_Sets_and_Get_one_time() {
    counter.SetValueOut(1, 1.1);
    counter.SetOut2In();
    Double d = counter.GetValueIn(1);
    assertTrue( 1.1==d);
  }
  @Test
  public void Test_Sets_and_Get_more_time() {
    Double[] values = {1.1, 2.2, 3.3, 4.4};
    for(int i = 0; i < values.length; i++) {
      counter.SetValueOut(i, values[i]);
    }
    counter.SetOut2In();
    for(int i=0; i<values.length; i++) {
      Double d = counter.GetValueIn(i);
      assertEquals( d, values[i]);
    }
  }
  @Test
  public void Test_Sets_and_Get_() {
    Double value = 1.1;
    int position = 8;
    counter.SetValueOut(position, value);
    counter.SetOut2In();
    for(int i=0; i<position; i++) {
      Double d = counter.GetValueIn(i);
      assertNull( d);
    }
    Double d = counter.GetValueIn(position);
    assertEquals( d, value);
  }

  @Test
  public void Test_Step_one_block() {
    assertEquals(counter.counter, (Integer)(-1));
    counter.Step(blokGetOne);
    assertEquals(counter.counter, (Integer)0);
    counter.SetOut2In();
    Double d;
    d = counter.GetValueIn(1);
    assertEquals((Double)1.0, d);
    d = counter.GetValueIn(3);
    assertEquals((Double)1.0, d);
  }

  @Test
  public void Test_Step_counter_two_Step() {
    assertEquals(counter.counter, (Integer)(-1));
    counter.Step(blokGetOne);
    assertEquals(counter.counter, (Integer)0);
    counter.Step(blokGetOne);
    assertEquals(counter.counter, (Integer)1);
  }

  @Test
  public void Test_Step_two_block() {
    assertEquals(counter.counter, (Integer)(-1));
    counter.Step(blokGetOne);
    assertEquals(counter.counter, (Integer)0);
    counter.Step(blokPlus);
    assertEquals(counter.counter, (Integer)1);
    counter.SetOut2In();
    Double d;
    d = counter.GetValueIn(1);
    assertEquals((Double)2.0, d);
    d = counter.GetValueIn(2);
    assertEquals((Double)2.0, d);
    d = counter.GetValueIn(3);
    assertEquals((Double)2.0, d);
  }

}
