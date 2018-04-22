
package test.blocks;

import main.manipulator.Counter;
import main.manipulator.CycleException;
import main.manipulator.IOperation;

import static org.junit.Assert.*;

import main.manipulator.MissingValueException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BlockAddSubTest {

  @BeforeClass
  public static void BlockAddSubTest() {
    System.out.println("^^^ BlockAddSubTest ^^^");
  }

  @Before
  public void prepare() {

  }

  @Test
  public void Test_Sets_and_Get_one_time() {
    
  }

  @Test(expected = CycleException.class)
  public void Test_Step_Throw_Exception() throws CycleException, MissingValueException {

  }


}
