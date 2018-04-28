
package test.blocks;

import main.blocks.BlockConstant;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

public class BlockConstantTest {

  @BeforeClass
  public static void PrintClassName() {
    System.out.println("^^^ BlockConstantTest ^^^");
  }

  private BlockConstant block;

  @Before
  public void prepare() {
    block = new BlockConstant();
  }

  @Test
  public void Test_Consturctor_Empty() {
    BlockConstant block = new BlockConstant();
    assertEquals(0, block.GetPortsIn().size() );
    assertEquals(0, block.GetPortsOut().size() );

    assertEquals(0, block.GetPositionStart().intValue() );
    assertEquals(0, block.GetPositionEnd().intValue() );
  }

  @Test
  public void Test_Consturctor_Positions() {
    BlockConstant block = new BlockConstant(1,10);
    assertEquals(0, block.GetPortsIn().size() );
    assertEquals(0, block.GetPortsOut().size() );

    assertEquals(1, block.GetPositionStart().intValue() );
    assertEquals(10, block.GetPositionEnd().intValue() );
  }

  @Test
  public void Test_IsPossibleGroupIn_Failed_1() {
    for(Integer i=-11; i<20; i++) {
      assertFalse( block.IsPossibleGroupIn(i) );
    }
  }
}
