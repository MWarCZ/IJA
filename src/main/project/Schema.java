
package main.project;

//import main.blocks.Block;
import main.manipulator.Counter;
import main.manipulator.IOperation;

import java.lang.String;
import java.util.ArrayList;

public class Schema {

  //region Variables / Property

  /**
   * Nazev schematu.
   */
  private String name;
  /**
   * Cesta, kde je schema ulozeno / kam se bude ukladat.
   */
  private String path;
  /**
   * Seznam obsahujici sloupce s jednotlivymi bloky.
   */
  private ArrayList<ArrayList<IOperation>> blocks;
  /**
   * Citac daneho schematu pro zpracovavani a simulaci vypoctu bloku.
   */
  public Counter counter;

  //endregion

  //region Constructors

  /**
   * Funkce nastavuje hodnoty a vychozi inicializaci internich promennych.
   * Je vyuzivana konstruktory, ktere ji volaji s roznymi parametry.
   * @param name Nazev schematu.
   * @param path Cesta, kde je schema ulozeno. Na tuto hodnotu se hledi pri ukladani a nacitani schematu.
    */
  private void Init(String name, String path) {
    this.path = path;
    this.name = name;
    this.counter = new Counter();
    this.blocks = new ArrayList<ArrayList<IOperation>>();

  }

  /**
   * Konstruktor
   * @param name Nazev schematu.
   * @param path Cesta, kde je schema ulozeno. Na tuto hodnotu se hledi pri ukladani a nacitani schematu.
   */
  public Schema(String name, String path) {
    this.Init(name, path);
  }

  /**
   * Konstruktor
   * @param name Nazev schematu.
   */
  public Schema(String name) {
    this.Init(name, null);
  }

  //endregion

  //region Basic Getters and Setters

  /**
   * Vraci nazev schematu.
   * @return Vraci nazev schematu.
   */
  public String GetName() {
    return this.name;
  }

  /**
   * Nastavuje novy nazev schematu.
   * @param name Novy nazev pro schema.
   */
  public void SetName(String name) {
    this.name = name;
  }

  /**
   * Vraci cestu schematu.
   * @return Vraci cestu schematu.
   */
  public String GetPath() {
    return this.path;
  }

  /**
   * Nastavi nouvou cestu schematu.
   * @param path Nova cesta pro schema.
   */
  public void SetPath(String path) {
    this.path = path;
  }

  /**
   * Vraci aktualni pocet existujicich sloupcu bloku.
   * @return Vraci aktualni pocet existujicich sloupcu bloku.
   */
  public Integer GetCountBlocksColumns() {
    return this.blocks.size();
  }


  //endregion

  //region Function for work with list of list of block

  /**
   * Funkce vrati pozadovany sloupec s bloky. Pokud je index sloupce vetsi
   * nez aktualni pocet sloupcu je vracena hodnota 'null'.
   * Pokud je index sloupce zaporny bude vyhozena vyjimka 'ArrayIndexOutOfBoundsException'.
   * @param index Index pozadovaneho sloupce (Indexovano od 0).
   * @return Funkce vrati pozadovany sloupec s bloky nebo null pokud sloupec zatim neexistuje.
   */
  public ArrayList<IOperation> GetBlocksColumn( int index) {
    if(index < 0) {
      throw new ArrayIndexOutOfBoundsException("Zaporny index sloupce.");
    }
    if(this.blocks.size() <= index) {
      return null;
    }
    return this.blocks.get(index);
  }

  /**
   * Funkce vlozi novy sloupec na dany index. Pokud je index vetsi nez velikost listu se sloupci,
   * tak volna mista mezi poslednim sloupcem a novym sloupcem budou zaplnena prazdnymi sloupci.
   * Pokud je index sloupce zaporny bude vyhozena vyjimka 'ArrayIndexOutOfBoundsException'.
   * @param index Index pozice sloupce na ktery se ma novy sloupec vlozit (Indexovano od 0).
   * @param column Sloupec s bloky, ktery bude vlozen.
   */
  public void InsertColumnOfBlocks(int index, ArrayList<IOperation> column) {
    if(index < 0) {
      throw new ArrayIndexOutOfBoundsException("Zaporny index sloupce.");
    }
    while(this.blocks.size() < index) {
      this.blocks.add(new ArrayList<IOperation>());
    }
    this.blocks.add(index, column);
  }

  /**
   * Funkce vlozi novy prazdny sloupec na dany index.
   * Pokud je index vetsi nez velikost listu se sloupci, tak volna mista
   * mezi poslednim sloupcem a novym sloupcem budou zaplnena prazdnymi sloupci.
   * Pokud je index sloupce zaporny bude vyhozena vyjimka 'ArrayIndexOutOfBoundsException'.
   * @param index Index pozice sloupce na ktery se ma novy prazdny sloupec vlozit (Indexovano od 0).
   */
  public void AddEmptyColumnOfBlocks(int index) {
    InsertColumnOfBlocks(index, new ArrayList<IOperation>());
  }

  /**
   * Funkce prida blok do sloupce s danym indexem.
   * Pokud je index sloupce vetsi nez velikost listu se sloupci, tak volna mista
   * mezi poslednim sloupcem a novym sloupcem kam bude blok vlozen budou zaplnena prazdnymi sloupci.
   * Pokud je index sloupce zaporny bude vyhozena vyjimka 'ArrayIndexOutOfBoundsException'.
   * @param columnIndex Index sloupce do ktereho bude blok vlozen.
   * @param blok Blok ktery bude vlozen do sloupce.
   */
  public void AddBlock(int columnIndex, IOperation blok) {
    if(columnIndex < 0) {
      throw new ArrayIndexOutOfBoundsException("Zaporny index sloupce.");
    }
    if(this.blocks.size() <= columnIndex) {
      this.AddEmptyColumnOfBlocks(columnIndex);
    }
    this.blocks.get(columnIndex).add(blok);
  }

  //endregion

}
