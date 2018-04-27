
package test.blocks;

import main.blocks.BlockSwitch;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

public class BlockSwitchTest {

  @BeforeClass
  public static void PrintClassName() {
    System.out.println("^^^ BlockSwitchTest ^^^");
  }

  private BlockSwitch block;

  @Before
  public void prepare() {
    block = new BlockSwitch();
  }

  @Test
  public void Test_Consturctor_Empty() {
    BlockSwitch block = new BlockSwitch();
    assertEquals(0, block.GetPortsIn().size() );
    assertEquals(0, block.GetPortsOut().size() );

    assertEquals(0, block.GetPositionStart().intValue() );
    assertEquals(0, block.GetPositionEnd().intValue() );
  }

  @Test
  public void Test_Consturctor_Positions() {
    BlockSwitch block = new BlockSwitch(1,10);
    assertEquals(0, block.GetPortsIn().size() );
    assertEquals(0, block.GetPortsOut().size() );

    assertEquals(1, block.GetPositionStart().intValue() );
    assertEquals(10, block.GetPositionEnd().intValue() );
  }

  @Test
  public void Test_IsPossibleGroupIn() {
    for(Integer i=0; i<20; i++) {
      assertTrue( block.IsPossibleGroupIn(i) );
    }
  }

  @Test
  public void Test_IsPossibleGroupIn_Failed_1() {
    for(Integer i=-11; i<0; i++) {
      assertFalse( block.IsPossibleGroupIn(i) );
    }
  }

}
