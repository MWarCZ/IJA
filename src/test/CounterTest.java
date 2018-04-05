
package test;

import main.manipulator.Counter;

import static org.junit.Assert.*;
import org.junit.Test;

public class CounterTest {
  @Test
  public void TestIt() {
    Counter counter = new Counter();
    counter.SetValueOut(1, 1.1);
    counter.SetOut2In();
    Double d = counter.GetValueIn(1);
    assertTrue( 1.1==d);
  }
}
