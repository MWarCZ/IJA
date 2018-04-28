
package test.blocks;

import main.manipulator.CycleException;
import main.blocks.Block;
import main.blocks.PortException;
import main.blocks.PortGroupException;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;

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
  public void Test_Consturctor_Empty() {
    Block block = new Block();
    assertEquals(0, block.GetPortsIn().size() );
    assertEquals(0, block.GetPortsOut().size() );

    assertEquals(0, block.GetPositionStart().intValue() );
    assertEquals(0, block.GetPositionEnd().intValue() );
  }

  @Test
  public void Test_Consturctor_Positions() {
    Block block = new Block(1,10);
    assertEquals(0, block.GetPortsIn().size() );
    assertEquals(0, block.GetPortsOut().size() );

    assertEquals(1, block.GetPositionStart().intValue() );
    assertEquals(10, block.GetPositionEnd().intValue() );
  }

  @Test
  public void Test_SetPosition() {
    block.SetPosition(3,5);
    assertEquals(3, block.GetPositionStart().intValue() );
    assertEquals(5, block.GetPositionEnd().intValue() );

    block.SetPosition(4,4);
    assertEquals(4, block.GetPositionStart().intValue() );
    assertEquals(4, block.GetPositionEnd().intValue() );

    block.SetPosition(10,2);
    assertEquals(2, block.GetPositionStart().intValue() );
    assertEquals(10, block.GetPositionEnd().intValue() );
  }

  @Test
  public void Test_Getters_Setters_Position(){
    block.SetPosition(1,5);
    assertEquals(1, block.GetPositionStart().intValue() );
    assertEquals(5, block.GetPositionEnd().intValue() );

    block.SetPositionStart(0);
    assertEquals(0, block.GetPositionStart().intValue() );
    assertEquals(5, block.GetPositionEnd().intValue() );

    block.SetPositionStart(3);
    assertEquals(3, block.GetPositionStart().intValue() );
    assertEquals(5, block.GetPositionEnd().intValue() );

    block.SetPositionStart(7);
    assertEquals(5, block.GetPositionStart().intValue() );
    assertEquals(7, block.GetPositionEnd().intValue() );

    // ------------------

    block.SetPosition(1,5);
    assertEquals(1, block.GetPositionStart().intValue() );
    assertEquals(5, block.GetPositionEnd().intValue() );

    block.SetPositionEnd(8);
    assertEquals(1, block.GetPositionStart().intValue() );
    assertEquals(8, block.GetPositionEnd().intValue() );

    block.SetPositionEnd(10);
    assertEquals(1, block.GetPositionStart().intValue() );
    assertEquals(10, block.GetPositionEnd().intValue() );

    block.SetPositionEnd(0);
    assertEquals(0, block.GetPositionStart().intValue() );
    assertEquals(1, block.GetPositionEnd().intValue() );

  }

  @Test
  public void Test_IsPossibleGroupIn() {
    for(Integer i=0; i<10; i++) {
      assertTrue( block.IsPossibleGroupIn(i) );
    }
  }

  @Test
  public void Test_IsPossibleGroupIn_Failed_1() {
    for(Integer i=-1; i>-11; i--) {
      assertFalse( block.IsPossibleGroupIn(i) );
    }
  }

  @Test
  public void Test_IsPossibleGroupIn_Failed_2() {
    for(Integer i=10; i<20; i++) {
      assertFalse( block.IsPossibleGroupIn(i) );
    }
  }

  @Test
  public void Test_AddPortIn() throws PortException {
    for(int i = 0; i<10; i++) {
      block.AddPortIn(i, 0);
    }
    ArrayList<Integer> tmp = block.GetPortsIn();
    assertEquals(10, tmp.size() );

    Integer check = 0;
    for(Integer i : tmp) {
      assertEquals(check, i);
      check++;
    }
  }

  @Test(expected = PortException.class)
  public void Test_AddPortIn_Exception_DuplicitPort() throws PortException {
    block.AddPortIn(1, 0);
    block.AddPortIn(1, 1);
  }

  @Test(expected = PortGroupException.class)
  public void Test_AddPortIn_Exception_UnexistGroup() throws PortException {
    block.AddPortIn(1, -5);
  }

  @Test
  public void Test_RemovePortIn() throws PortException {
    for(int i = 0; i<10; i++) {
      block.AddPortIn(i, 0);
    }
    for(int i = 0; i<10; i+=2) {
      block.RemovePortIn(i);
    }
    ArrayList<Integer> tmp = block.GetPortsIn();
    assertEquals(5, tmp.size() );

    Integer check = 1;
    for(Integer i : tmp) {
      assertEquals(check, i);
      check+=2;
    }
  }
  @Test
  public void Test_RemovePortIn_Unexist() {
    block.RemovePortIn(1);
    block.RemovePortIn(8);
    block.RemovePortIn(-5);
  }

  @Test
  public void Test_GroupIn() throws PortException {
    for(int i = 0; i<10; i++) {
      block.AddPortIn(i, i%3);
    }
    for(int i = 0; i<10; i++) {
      assertEquals(i%3, block.GetGroupIn(i).intValue() );
    }

    for(int i = 0; i<10; i+=2) {
      block.RemovePortIn(i);
    }
    for(int i = 1; i<10; i+=2) {
      assertEquals(i % 3, block.GetGroupIn(i).intValue());
    }
  }

  @Test
  public void Test_ChangeGroupIn() throws PortException {
    for(int i = 0; i<10; i++) {
      block.AddPortIn(i,0);
      assertEquals(0, block.GetGroupIn(i).intValue());
    }
    for(int i = 0; i<10; i++) {
      block.ChangeGroupIn(i,2);
      assertEquals(2, block.GetGroupIn(i).intValue());
    }
    for(int i = 0; i<10; i+=2) {
      block.ChangeGroupIn(i,i);
      assertEquals(i, block.GetGroupIn(i).intValue());
    }
    for(int i = 0; i<10; i++) {
      if(i%2 == 1) {
        assertEquals(2, block.GetGroupIn(i).intValue());
      } else {
        assertEquals(i, block.GetGroupIn(i).intValue());
      }
    }
  }

  @Test(expected = PortException.class)
  public void Test_ChangeGroupIn_Exception_UnexistPort() throws PortException {
    block.ChangeGroupIn(1,0);
  }

  @Test(expected = PortGroupException.class)
  public void Test_ChangeGroupIn_Exception_UnexistGroup() throws PortException {
    block.AddPortIn(1,0);
    block.ChangeGroupIn(1,-10);
  }

  //------------

  @Test
  public void Test_AddPortOut() throws PortException {
    for(int i = 0; i<10; i++) {
      block.AddPortOut(i, 0);
    }
    ArrayList<Integer> tmp = block.GetPortsOut();
    assertEquals(10, tmp.size() );

    Integer check = 0;
    for(Integer i : tmp) {
      assertEquals(check, i);
      check++;
    }
  }

  @Test(expected = PortException.class)
  public void Test_AddPortOut_Exception_DuplicitPort() throws PortException {
    block.AddPortOut(10, 0);
    block.AddPortOut(10, 0);
  }

  @Test(expected = PortGroupException.class)
  public void Test_AddPortOut_Exception_UnexistGroup() throws PortException {
    block.AddPortOut(0, 20);
  }

  @Test
  public void Test_RemovePortOut() throws PortException {
    for(int i = 0; i<10; i++) {
      block.AddPortOut(i, 0);
    }
    for(int i = 0; i<10; i+=2) {
      block.RemovePortOut(i);
    }
    ArrayList<Integer> tmp = block.GetPortsOut();
    assertEquals(5, tmp.size() );

    Integer check = 1;
    for(Integer i : tmp) {
      assertEquals(check, i);
      check+=2;
    }
  }

  @Test
  public void Test_RemovePortOut_Unexist() {
    block.RemovePortOut(1);
    block.RemovePortOut(8);
    block.RemovePortOut(-5);
  }

  @Test
  public void Test_GroupOut() throws PortException {
    for(int i = 0; i<10; i++) {
      block.AddPortOut(i, i%3);
    }
    for(int i = 0; i<10; i++) {
      assertEquals(i%3, block.GetGroupOut(i).intValue() );
    }

    for(int i = 0; i<10; i+=2) {
      block.RemovePortOut(i);
    }
    for(int i = 1; i<10; i+=2) {
      assertEquals(i % 3, block.GetGroupOut(i).intValue());
    }
  }

  @Test
  public void Test_ChangeGroupOut() throws PortException {
    for(int i = 0; i<10; i++) {
      block.AddPortOut(i,0);
      assertEquals(0, block.GetGroupOut(i).intValue());
    }
    for(int i = 0; i<10; i++) {
      block.ChangeGroupOut(i,2);
      assertEquals(2, block.GetGroupOut(i).intValue());
    }
    for(int i = 0; i<10; i+=2) {
      block.ChangeGroupOut(i,i);
      assertEquals(i, block.GetGroupOut(i).intValue());
    }
    for(int i = 0; i<10; i++) {
      if(i%2 == 1) {
        assertEquals(2, block.GetGroupOut(i).intValue());
      } else {
        assertEquals(i, block.GetGroupOut(i).intValue());
      }
    }
  }

  @Test(expected = PortException.class)
  public void Test_ChangeGroupOut_Exception_UnexistPort() throws PortException {
    block.ChangeGroupOut(1,0);
  }

  @Test(expected = PortGroupException.class)
  public void Test_ChangeGroupOut_Exception_UnexistGroup() throws PortException {
    block.AddPortOut(1,0);
    block.ChangeGroupOut(1,-10);
  }

  // ----------

  @Test
  public void Test_GetNextGroupIn() throws PortException {
    Integer group;
    for(int i = -5; i<0; i++) {
      group = block.GetNextGroupIn(i);
      assertEquals(0, group.intValue());
    }
    group = block.GetNextGroupIn(0);
    assertEquals(1, group.intValue());

    group = block.GetNextGroupIn(1);
    assertEquals(0, group.intValue());
    group = block.GetNextGroupIn(6);
    assertEquals(0, group.intValue());
    group = block.GetNextGroupIn(50);
    assertEquals(0, group.intValue());

  }

}
