
package test;

import main.manipulator.Counter;
import main.manipulator.CycleException;
import main.manipulator.IOperation;

import static org.junit.Assert.*;

import main.manipulator.MissingValueException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

import java.util.*;

public class CounterTest {

  @BeforeClass
  public static void PrintClassName() {
    System.out.println("^^^ CounterTest ^^^");
  }

  private Counter counter;
  private IOperation blokGetOne;
  private IOperation blokPlus;

  @Before
  public void prepare() {
    counter = new Counter();

    blokGetOne = new IOperation() {
      ArrayList<Integer> outPorts = new ArrayList<Integer>(Arrays.asList(1,3));
      @Override
      public Map<Integer,Double> Operation(Map<Integer,Double> data) {
        Map<Integer,Double> resut = new HashMap<Integer,Double>();
        List<Integer> portsOut = GetPortsOut();
        for(Integer portIndex: portsOut) {
          resut.put(portIndex, 1.0);
        }
        return resut;
      }
      @Override
      public List<Integer> GetPortsIn() {
        return new ArrayList<Integer>();
      }
      @Override
      public List<Integer> GetPortsOut() {
        return outPorts;
      }
    };
    blokPlus = new IOperation() {
      List<Integer> portsIn = new ArrayList<Integer>(Arrays.asList(1, 3));
      List<Integer> portsOut = new ArrayList<Integer>(Arrays.asList(1,2,3));

      @Override
      public Map<Integer,Double> Operation(Map<Integer,Double> data) {
        Double sum = 0.0;
        Map<Integer,Double> resut = new HashMap<Integer,Double>();
        for(Integer i: portsIn) {
          sum += data.get(i);
        }
        for(Integer portIndex: portsOut) {
          resut.put(portIndex, sum);
        }
        return resut;
      }

      @Override
      public List<Integer> GetPortsIn() {
        return portsIn;
      }

      @Override
      public List<Integer> GetPortsOut() {
        return portsOut;
      }
    };
  }

  @Test
  public void Test_Sets_and_Get_one_time() {
    Integer i = 1;
    counter.SetValueOut(i, 1.1);
    Double value1 = counter.GetValueOut(i);
    counter.SetOut2In();
    Double value2 = counter.GetValueIn(1);
    assertEquals(value1, value2);
    assertEquals((Double)1.1, value2);
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
      //assertNull( d);
    }
    Double d = counter.GetValueIn(position);
    assertEquals( value, d );
  }

  @Test
  public void Test_Step_one_block() throws CycleException, MissingValueException {
    assertEquals(counter.GetCounter(), (Integer)(-1));
    counter.Step(blokGetOne, true);
    assertEquals(counter.GetCounter(), (Integer)0);
    Double d;
    d = counter.GetValueOut(1);
    assertEquals((Double)1.0, d);
    d = counter.GetValueOut(3);
    assertEquals((Double)1.0, d);
  }

  @Test
  public void Test_Step_counter_two_Step() throws CycleException, MissingValueException {
    assertEquals(counter.GetCounter(), (Integer)(-1));
    counter.Step(blokGetOne, true);
    assertEquals(counter.GetCounter(), (Integer)0);
    counter.Step(blokGetOne, true);
    assertEquals(counter.GetCounter(), (Integer)1);
  }

  @Test
  public void Test_Step_two_block() throws CycleException, MissingValueException {
    assertEquals(counter.GetCounter(), (Integer)(-1));
    counter.Step(blokGetOne, true);
    assertEquals(counter.GetCounter(), (Integer)0);
    counter.Step(blokPlus, true);
    assertEquals(counter.GetCounter(), (Integer)1);
    Double d;
    d = counter.GetValueOut(1);
    assertEquals((Double)2.0, d);
    d = counter.GetValueOut(2);
    assertEquals((Double)2.0, d);
    d = counter.GetValueOut(3);
    assertEquals((Double)2.0, d);
  }

  @Test(expected = CycleException.class)
  public void Test_Step_Throw_Exception() throws CycleException, MissingValueException {
    counter.Step(blokGetOne, false);
    counter.Step(blokGetOne, false);
  }


}
