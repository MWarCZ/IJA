
package test.blocks;

import main.manipulator.CycleException;
import main.blocks.Block;
import main.blocks.PortException;
import main.blocks.PortGroupException;

import static org.junit.Assert.*;

import main.manipulator.MissingValueException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BlockTest {

  @BeforeClass
  public static void PrintClassName() {
    System.out.println("^^^ BlockTest ^^^");
  }

  private Block block;

  @Before
  public void prepare() {
    block = new Block();
  }

  @Test
  public void Test_IsPossibleGroupIn() {
    for(Integer i=0; i<10; i++) {
      assertTrue( block.IsPossibleGroupIn(i) );
    }
  }
  @Test(expected = PortGroupException.class)
  public void Test_IsPossibleGroupIn_Failed_1() {
    assertTrue( block.IsPossibleGroupIn(-1) );
  }
  @Test(expected = PortGroupException.class)
  public void Test_IsPossibleGroupIn_Failed_2() {
    assertTrue( block.IsPossibleGroupIn(10) );
  }

  @Test(expected = CycleException.class)
  public void Test_Step_Throw_Exception() throws CycleException, MissingValueException {

  }


}
