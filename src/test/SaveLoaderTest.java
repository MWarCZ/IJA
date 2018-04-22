
package test;

import main.blocks.Block;
import main.blocks.PortException;
import main.manipulator.IOperation;
import main.project.SaveLoader;
import main.blocks.BlockMulDiv;

import static org.junit.Assert.*;

import main.project.Schema;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SaveLoaderTest {

  SaveLoader saveloader;

  @BeforeClass
  public static void PrintClassName() {
    System.out.println("^^^ SaveLoaderTest ^^^");
  }

  @Before
  public void prepare() {
    saveloader = new SaveLoader();

  }

  @Test
  public void Test_Write() {
    String s = "xxx.xml";
    //saveloader.WriteXML3(s);
    System.out.println(s);
  }
  @Test
  public void TestRead() {
    String s = "xxx.xml";
    saveloader.ReadXML3(s);
    System.out.println(s);
  }
  @Test
  public void TestRead2() {
    Schema schema = null;
    String s = "aaa.xml";
    schema = saveloader.ReadXML3(s);
  }
  @Test
  public void TestReadWrite() {
    Schema schema = null;
    String s = "aaa.xml";
    schema = saveloader.ReadXML3(s);
    saveloader.WriteXML3("bbb.xml", schema);
    Schema schema2 = saveloader.ReadXML3("bbb.xml");
    System.out.println("-------");
  }
  @Test
  public void Test_Read_BlockMulDiv() {
    Schema schema = saveloader.ReadXML3("bbb.xml");
    HashMap<Integer,Double> out;
    HashMap<Integer,Double> data = new HashMap<>();
    data.put(0,12.0);
    data.put(1,2.0);
    data.put(2,3.0);
    out = schema.blocks.get(0).get(0).Operation(data);
    System.out.println(out.toString());
  }
  @Test
  public void Test_Write_BlockMulDiv() throws PortException {
    Schema schema = new Schema("Pokus X");

    Block block = new BlockMulDiv();
    block.AddPortIn(2,0);
    block.AddPortIn(5,1);
    block.AddPortIn(1,0);
    block.AddPortOut(1,0);
    block.AddPortOut(0,0);

    schema.AddBlock(0,block);

    block = new BlockMulDiv();
    block.AddPortIn(2,1);
    block.AddPortIn(3,0);
    block.AddPortIn(4,1);
    block.AddPortOut(2,0);
    block.AddPortOut(3,0);
    block.RemovePortIn(2);

    schema.AddBlock(0,block);

    saveloader.WriteXML3("ccc.xml",schema);

  }

}
