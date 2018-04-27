
package test.blocks;

import main.blocks.BlockAddSub;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;

public class BlockAddSubTest {

  @BeforeClass
  public static void PrintClassName() {
    System.out.println("^^^ BlockAddSubTest ^^^");
  }

  private BlockAddSub block;

  @Before
  public void prepare() {
    block = new BlockAddSub();
  }

  @Test
  public void Test_Consturctor_Empty() {
    BlockAddSub block = new BlockAddSub();
    assertEquals(0, block.GetPortsIn().size() );
    assertEquals(0, block.GetPortsOut().size() );

    assertEquals(0, block.GetPositionStart().intValue() );
    assertEquals(0, block.GetPositionEnd().intValue() );
  }

  @Test
  public void Test_Consturctor_Positions() {
    BlockAddSub block = new BlockAddSub(1,10);
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
