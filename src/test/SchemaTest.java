package test;

import main.project.Schema;
import main.manipulator.IOperation;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;

public class SchemaTest {

  @BeforeClass
  public static void PrintClassName() {
    System.out.println("^^^ SchemaTest ^^^");
  }

  private Schema schema;
  private IOperation blokGetOne;

  @Before
  public void prepare() {
    schema = new Schema("schema");

    blokGetOne = new IOperation() {
      ArrayList<Integer> outPorts = new ArrayList<Integer>(Arrays.asList(1,3));
      ArrayList<Integer> inPorts = new ArrayList<>();
      @Override
      public Double Operation(ArrayList<Double> data) {
        return 1.0;
      }
      @Override
      public ArrayList<Integer> GetPortsIn() {
        return inPorts;
      }
      @Override
      public ArrayList<Integer> GetPortsOut() {
        return outPorts;
      }
    };
  }

  @Test
  public void Test_Test() {
    /* Vzorek */
    assertEquals(5,5);
  }

  @Test
  public void Test_Constructors() {
    /* Test konstruktoru.
     * Vyzkousi konstruktory zda pravne nastavuji hodnoty.
     */
    Schema schema = new Schema("jmeno","cesta");
    assertEquals("jmeno", schema.GetName());
    assertEquals("cesta", schema.GetPath());

    schema = new Schema("jmeno");
    assertEquals("jmeno", schema.GetName());
    assertEquals("", schema.GetPath());
  }

  @Test
  public void Test_Getters_and_Setters() {
    /* Test zakladnich getteru a setteru.
     * Otestuje se ulozeni a ziskani nazvu a cesty.
     */
    schema.SetName("name");
    assertEquals("name", schema.GetName());
    schema.SetName("schema1");
    assertEquals("schema1", schema.GetName());

    schema.SetPath("path");
    assertEquals("path", schema.GetPath());
    schema.SetPath("schema1.xija");
    assertEquals("schema1.xija", schema.GetPath());
  }

  @Test
  public void Test_GetBlocksColumn_unexist_column() {
    /* Test ziskani neexistujicich sloupcu s bloky.
     * Z prazdneho schematu chceme ziskat neexistujici sloupce.
     * Ocekava se, 6e bude vracen typ null nebot sloupce neexistuji.
     */
    assertNull(schema.GetBlocksColumn(0));
    assertNull(schema.GetBlocksColumn(1));
    assertNull(schema.GetBlocksColumn(2));
    assertNull(schema.GetBlocksColumn(30));
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void Test_GetBlocksColumn_Exception() {
    /* Test ziskani sloupce se zapornym indexem.
     * Je ocekavano, ze dojde k vyjimce pri pristupu mimo pole.
     */
    schema.GetBlocksColumn(-1);
  }

  @Test
  public void Test_InsertBlocksColumn() {
    /* Test vlozeni sloupce do schematu.
     * Do schematu jsou vlozeny sloupce a testuje se
     * zda doslo k ulozeni sloupce na danou pozici.
     */
    ArrayList<IOperation> list0 = new ArrayList<>();

    schema.InsertColumnOfBlocks(0, list0);
    assertSame(list0, schema.GetBlocksColumn(0));

    ArrayList<IOperation> list1 = new ArrayList<>();
    schema.InsertColumnOfBlocks(1, list1);
    assertSame(list0, schema.GetBlocksColumn(0));
    assertSame(list1, schema.GetBlocksColumn(1));

    ArrayList<IOperation> list2 = new ArrayList<>();
    schema.InsertColumnOfBlocks(0, list2);
    assertSame(list2, schema.GetBlocksColumn(0));
    assertSame(list0, schema.GetBlocksColumn(1));
    assertSame(list1, schema.GetBlocksColumn(2));
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void Test_InsertBlocksColumn_Exception() {
    /* Test vlozeni sloupce na zaporny index.
     * Je ocekavano, ze dojde k vyjimce pri pristupu mimo pole.
     */
    ArrayList<IOperation> list = new ArrayList<>();
    schema.InsertColumnOfBlocks(-1, list);
  }

  @Test
  public void Test_AddEmptyBlocksColumn() {
    /* Test vlozeni prazdnych sloupcu do schematu.
     * Do schematu jsou vlozeny prazne sloupce a kontroluje se pocet sloupcu,
     * zda je sloupce prazdny a zda se sloupce spravne posouvaji.
     */
    assertEquals((Integer)0, schema.GetCountBlocksColumns());

    schema.AddEmptyColumnOfBlocks(0);
    ArrayList<IOperation> list0 = schema.GetBlocksColumn(0);
    assertNotNull(list0);
    assertEquals(0, list0.size());
    assertEquals((Integer)1, schema.GetCountBlocksColumns());

    schema.AddEmptyColumnOfBlocks(1);
    ArrayList<IOperation> list1 = schema.GetBlocksColumn(1);
    assertNotNull(list1);
    assertEquals(0, list1.size());
    assertEquals((Integer)2, schema.GetCountBlocksColumns());

    assertSame(list0, schema.GetBlocksColumn(0));
    assertSame(list1, schema.GetBlocksColumn(1));

    schema.AddEmptyColumnOfBlocks(1);
    ArrayList<IOperation> list2 = schema.GetBlocksColumn(1);
    assertNotNull(list2);
    assertEquals(0, list2.size());
    assertEquals((Integer)3, schema.GetCountBlocksColumns());

    assertSame(list0, schema.GetBlocksColumn(0));
    assertSame(list2, schema.GetBlocksColumn(1));
    assertSame(list1, schema.GetBlocksColumn(2));

    schema.AddEmptyColumnOfBlocks(5);
    ArrayList<IOperation> list3 = schema.GetBlocksColumn(5);
    assertNotNull(list3);
    assertEquals(0, list3.size());
    assertEquals((Integer)6, schema.GetCountBlocksColumns());

    assertSame(list0, schema.GetBlocksColumn(0));
    assertSame(list2, schema.GetBlocksColumn(1));
    assertSame(list1, schema.GetBlocksColumn(2));
    assertSame(list3, schema.GetBlocksColumn(5));
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void Test_AddEmptyBlocksColumn_Exception() {
    /* Test vlozeni sloupce na zaporny index.
     * Je ocekavano, ze dojde k vyjimce pri pristupu mimo pole.
     */
    schema.AddEmptyColumnOfBlocks(-2);
  }

  @Test
  public void Test_AddBlock() {
    /* Test vlozeni bloku do schematu do daneho slopce.
     * Vlozi se blok do daneho sloupce a nasledne se zkontroluje
     * novy pocet sloupcu, existence vlozeneho bloku, identyta bloku.
     */
    schema.AddBlock(1,blokGetOne);
    assertEquals((Integer)2, schema.GetCountBlocksColumns());

    ArrayList<IOperation> blocks;
    blocks = schema.GetBlocksColumn(1);
    assertEquals(1, blocks.size());
    assertSame(blokGetOne, blocks.get(0) );

  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void Test_AddBlock_Exception() {
    /* Test vlozeni bloku do sloupce se zapornym indexem.
     * Je ocekavano, ze dojde k vyjimce pri pristupu mimo pole.
     */
    schema.AddBlock(-1,blokGetOne);
  }


}
