
package test.blocks;

import main.blocks.BlockMulDiv;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

public class BlockMulDivTest {

  @BeforeClass
  public static void PrintClassName() {
    System.out.println("^^^ BlockMulDivTest ^^^");
  }

  private BlockMulDiv block;

  @Before
  public void prepare() {
    block = new BlockMulDiv();
  }

  @Test
  public void Test_Consturctor_Empty() {
    BlockMulDiv block = new BlockMulDiv();
    assertEquals(0, block.GetPortsIn().size() );
    assertEquals(0, block.GetPortsOut().size() );

    assertEquals(0, block.GetPositionStart().intValue() );
    assertEquals(0, block.GetPositionEnd().intValue() );
  }

  @Test
  public void Test_Consturctor_Positions() {
    BlockMulDiv block = new BlockMulDiv(1,10);
    assertEquals(0, block.GetPortsIn().size() );
    assertEquals(0, block.GetPortsOut().size() );

    assertEquals(1, block.GetPositionStart().intValue() );
    assertEquals(10, block.GetPositionEnd().intValue() );
  }

  @Test
  public void Test_IsPossibleGroupIn() {
    assertTrue( block.IsPossibleGroupIn(0) );
    assertTrue( block.IsPossibleGroupIn(1) );
  }

  @Test
  public void Test_IsPossibleGroupIn_Failed_1() {
    for(Integer i=-1; i>-11; i--) {
      assertFalse( block.IsPossibleGroupIn(i) );
    }
  }

  @Test
  public void Test_IsPossibleGroupIn_Failed_2() {
    for(Integer i=2; i<20; i++) {
      assertFalse( block.IsPossibleGroupIn(i) );
    }
  }

}
