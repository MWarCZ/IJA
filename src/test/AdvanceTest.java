
package test;

import main.blocks.*;
import main.manipulator.CycleException;
import main.manipulator.IOperation;
import main.manipulator.MissingValueException;
import main.project.SaveLoader;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import main.project.Schema;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AdvanceTest {

  private SaveLoader saveloader;
  private Schema schema;

  @BeforeClass
  public static void PrintClassName() {
    System.out.println("^^^ AdvanceTest ^^^");
  }

  @Before
  public void prepare() {
    saveloader = new SaveLoader();
    schema = new Schema("Schema Advance");
  }

  @Test
  public void Test_GroupIn() throws PortException {
    Block block = new BlockSwitch();
    Integer group;
    group = block.GetNextGroupIn(-1);
    assertEquals(0, group.intValue());

    block.AddPortIn(1,group);
    block.AddPortIn(2,1);
    block.AddPortIn(3,2);
    group = block.GetNextGroupIn(0);
    assertEquals(3, group.intValue());

  }
  @Test
  public void Test_GroupOut() throws PortException {
    Block block = new BlockSwitch();
    Integer group;

    block.AddPortIn(1,0);
    block.AddPortIn(2,1);
    block.AddPortIn(3,2);

    group = block.GetNextGroupOut(0);
    assertEquals(1, group.intValue());

    group = block.GetNextGroupOut(2);
    assertEquals(0, group.intValue());


  }
  @Test
  public void Test_Schema_SimulationStep() throws PortException, MissingValueException, CycleException, IOException {
    Block block;

    block = new BlockConstant();
    ((BlockConstant) block).SetConstValue(1.0);
    block.AddPortOut(0, 0);
    block.AddPortOut(1, 0);
    block.AddPortOut(2, 0);
    schema.AddBlock(0, block);

    block = new BlockConstant();
    ((BlockConstant) block).SetConstValue(2.0);
    block.AddPortOut(3, 0);
    block.AddPortOut(4, 0);
    schema.AddBlock(0, block);

    block = new BlockAddSub();
    block.AddPortIn(0, 0);
    block.AddPortIn(1, 0);
    block.AddPortIn(2, 1);
    block.AddPortOut(0, 0);
    block.AddPortOut(1, 0);
    block.NextGroupIn(2);
    schema.AddBlock(1, block);

    block = new BlockMulDiv();
    block.AddPortIn(3, 0);
    block.AddPortIn(4, 0);
    block.AddPortOut(2, 0);
    block.NextGroupIn(3);
    schema.AddBlock(1, block);

    block = new BlockSwitch();
    block.AddPortIn(0, 0);
    block.AddPortIn(1, 1);
    block.AddPortIn(2, 2);
    block.AddPortOut(0, 2);
    block.AddPortOut(1, 2);
    block.AddPortOut(2, 1);
    block.AddPortOut(3, 1);
    Integer test;
    test = block.NextGroupIn(1);
    test = block.NextGroupIn(1);
    test = block.NextGroupOut(0);
    test = block.NextGroupOut(0);
    test = block.NextGroupOut(0);
    test = block.NextGroupOut(0);
    schema.AddBlock(2, block);

    schema.SimulationReset();
    schema.SimulationStep();
    schema.SimulationStep();
    schema.SimulationStep();

    //Integer c1 = schema.counter.counter;
    Integer c1 = schema.counter.GetCounter();

    saveloader.WriteXML3("bbb.xija", schema);

    Schema schema2 = saveloader.ReadXML3("bbb.xija");

    schema2.SimulationReset();
    schema2.SimulationRun();

    Integer c2 = schema.counter.GetCounter();

  }

}
