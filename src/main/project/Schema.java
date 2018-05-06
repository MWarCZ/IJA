/**
 * Obsahuje trydu Schema.
 * Trida predstavuje schema s bloky a operace nad schematem.
 *
 * @author Miroslav Válka (xvalka05)
 * @author Jan Trněný (xtrnen03)
 */
package main.project;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import main.blocks.Block;
import main.manipulator.Counter;
import main.manipulator.CycleException;
import main.manipulator.IOperation;
import main.manipulator.MissingValueException;

import java.lang.String;
import java.util.List;

/**
 * Trida predstavuje schema obsahujici bloky a majici nejake jmeno a cestu, kde je ulozeno.
 *
 */
public class Schema {

    //region Variables / Property
    /**
     * Vlastnost nazev schematu.
     */
    public StringProperty nameProperty = new SimpleStringProperty("schema");
    /**
     * Vlastnost cesta k souboru, kde je chema ulozene/kam se ma ulozit.
     */
    public StringProperty pahtProperty = new SimpleStringProperty("");

    /**
     * Bloky ulozene ve schematu. (Sloupecekt paraleni zpracovani/radek seriove.)
     */
    public List<List<IOperation>> blocks;

    /**
     * Citac daneho schematu pro zpracovavani a simulaci vypoctu bloku.
     */
    public Counter counter;

    //endregion

    //region Constructors

    /**
     * Funkce nastavuje hodnoty a vychozi inicializaci internich promennych.
     * Je vyuzivana konstruktory, ktere ji volaji s roznymi parametry.
     *
     * @param name Nazev schematu.
     * @param path Cesta, kde je schema ulozeno. Na tuto hodnotu se hledi pri ukladani a nacitani schematu.
     */
    private void Init(String name, String path) {
        this.SetName(name);
        this.SetPath(path);
        this.counter = new Counter();
        //this.blocks = new ArrayList<List<IOperation>>();
        this.blocks = FXCollections.observableArrayList();

    }

    /**
     * Konstruktor inicializuje vychozi hodnoty.
     *
     * @param name Nazev schematu.
     * @param path Cesta, kde je schema ulozeno. Na tuto hodnotu se hledi pri ukladani a nacitani schematu.
     */
    public Schema(String name, String path) {
        this.Init(name, path);
    }

    /**
     * Konstruktor inicializuje vychozi hodnoty.
     *
     * @param name Nazev schematu.
     */
    public Schema(String name) {
        this.Init(name, null);
    }

    //endregion

    //region Basic Getters and Setters

    /**
     * Vraci nazev schematu.
     *
     * @return Vraci nazev schematu.
     */
    public String GetName() {
        return this.nameProperty.getValue();
    }

    /**
     * Nastavuje novy nazev schematu.
     *
     * @param name Novy nazev pro schema.
     */
    public void SetName(String name) {
        this.nameProperty.setValue(name);
    }

    /**
     * Vraci cestu schematu.
     *
     * @return Vraci cestu schematu.
     */
    public String GetPath() {
        return this.pahtProperty.getValue();
    }

    /**
     * Nastavi nouvou cestu schematu.
     *
     * @param path Nova cesta pro schema.
     */
    public void SetPath(String path) {
        this.pahtProperty.setValue(path);
    }

    /**
     * Vraci aktualni pocet existujicich sloupcu bloku.
     *
     * @return Vraci aktualni pocet existujicich sloupcu bloku.
     */
    public Integer GetCountBlocksColumns() {
        return this.blocks.size();
    }

    /**
     * Funkce zjisti na kterem nejposlednejsim radku se bloky ve schematu nachazi.
     * @return Pocet radku ve schematu.
     */
    public Integer GetCountRows() {
        Integer nowMaxRowIndex = -1;
        for(Integer i=0; i< GetCountBlocksColumns(); i++) {
            for(IOperation io : GetBlocksColumn(i)) {
                if(io instanceof Block) {
                    Integer tmpIndex = ((Block)io).GetPositionEnd();
                    if(tmpIndex > nowMaxRowIndex) nowMaxRowIndex = tmpIndex;
                }
            }
        }
        return nowMaxRowIndex+1;
    }
    //endregion

    //region Functions for work with list of list of block

    /**
     * Funkce vrati pozadovany sloupec s bloky. Pokud je index sloupce vetsi
     * nez aktualni pocet sloupcu je vracena hodnota 'null'.
     * Pokud je index sloupce zaporny bude vyhozena vyjimka 'ArrayIndexOutOfBoundsException'.
     *
     * @param index Index pozadovaneho sloupce (Indexovano od 0).
     * @return Funkce vrati pozadovany sloupec s bloky nebo null pokud sloupec zatim neexistuje.
     */
    public List<IOperation> GetBlocksColumn(int index) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Zaporny index sloupce.");
        }
        if (this.blocks.size() <= index) {
            return null;
        }
        return this.blocks.get(index);
    }

    /**
     * Funkce vlozi novy sloupec na dany index. Pokud je index vetsi nez velikost listu se sloupci,
     * tak volna mista mezi poslednim sloupcem a novym sloupcem budou zaplnena prazdnymi sloupci.
     * Pokud je index sloupce zaporny bude vyhozena vyjimka 'ArrayIndexOutOfBoundsException'.
     *
     * @param index  Index pozice sloupce na ktery se ma novy sloupec vlozit (Indexovano od 0).
     * @param column Sloupec s bloky, ktery bude vlozen.
     */
    public void InsertColumnOfBlocks(int index, List<IOperation> column) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Zaporny index sloupce.");
        }
        while (this.blocks.size() < index) {
            //this.blocks.add(new ArrayList<IOperation>());
            this.blocks.add(FXCollections.observableArrayList());
        }
        this.blocks.add(index, column);
    }

    /**
     * Funkce vlozi novy prazdny sloupec na dany index.
     * Pokud je index vetsi nez velikost listu se sloupci, tak volna mista
     * mezi poslednim sloupcem a novym sloupcem budou zaplnena prazdnymi sloupci.
     * Pokud je index sloupce zaporny bude vyhozena vyjimka 'ArrayIndexOutOfBoundsException'.
     *
     * @param index Index pozice sloupce na ktery se ma novy prazdny sloupec vlozit (Indexovano od 0).
     */
    public void AddEmptyColumnOfBlocks(int index) {
        //InsertColumnOfBlocks(index, new ArrayList<IOperation>());
        InsertColumnOfBlocks(index, FXCollections.observableArrayList());
    }

    /**
     * Funkce prida blok do sloupce s danym indexem.
     * Pokud je index sloupce vetsi nez velikost listu se sloupci, tak volna mista
     * mezi poslednim sloupcem a novym sloupcem kam bude blok vlozen budou zaplnena prazdnymi sloupci.
     * Pokud je index sloupce zaporny bude vyhozena vyjimka 'ArrayIndexOutOfBoundsException'.
     *
     * @param columnIndex Index sloupce do ktereho bude blok vlozen.
     * @param block       Blok ktery bude vlozen do sloupce.
     */
    public void AddBlock(int columnIndex, IOperation block) {
        if (columnIndex < 0) {
            throw new ArrayIndexOutOfBoundsException("Zaporny index sloupce.");
        }
        if (this.blocks.size() <= columnIndex) {
            this.AddEmptyColumnOfBlocks(columnIndex);
        }
        this.blocks.get(columnIndex).add(block);
    }

    /**
     * Funkce odstrani blok ze schematu v danem sloupecku s danzm indexem.
     * Pokud je index sloupce zaporny bude vyhozena vyjimka 'ArrayIndexOutOfBoundsException'.
     *
     * @param columnIndex Index sloupce ze ktereho bude blok odstranen.
     * @param block       Blok ktery bude odstranen ze sloupce.
     */
    public void RemoveBlock(int columnIndex, IOperation block) {
        if (columnIndex < 0) {
            throw new ArrayIndexOutOfBoundsException("Zaporny index sloupce.");
        }
        if (this.blocks.size() <= columnIndex) {
            return;
        }
        if (this.blocks.get(columnIndex).contains(block)) {
            this.blocks.get(columnIndex).remove(block);
        }

    }

    //endregion

    //region Functions for run simulation

    /**
     * Funkce pro restartovani simulace vypocku pro obvod zapojenych bloku.
     */
    public void SimulationReset() {
        counter.SetOut2In();
        counter.SetOut2In();
        counter.SetCounter(-1);
        //counter.counter = -1;
    }

    /**
     * Funkce pro vykonani jednoho kroku simulace vypoctu obvodu.
     *
     * @throws CycleException        Doslo ke kolizi bloku ve schematu - necekana smycka na portech.
     * @throws MissingValueException Na vstupnim portu nektereho z bloku chybi vstupni hodnota.
     * @throws SimulationEndException Simulace jiz skoncila a neni mozne dale pokracovat.
     */
    public void SimulationStep() throws CycleException, MissingValueException, SimulationEndException {
        List<IOperation> list = GetBlocksColumn(counter.GetCounter() + 1);
        if (list != null) {
            counter.SetOut2In();
            counter.Step(list, false);
        }
        else {
            throw new SimulationEndException("Simulace jiz skoncila.");
        }
    }

    /**
     * Funkce pro vykonani vsech kroku simulace vypoctu obvodu.
     *
     * @throws CycleException        Doslo ke kolizi bloku ve schematu - necekana smycka na portech.
     * @throws MissingValueException Na vstupnim portu nektereho z bloku chybi vstupni hodnota.
     * @throws SimulationEndException Simulace jiz skoncila a neni mozne dale pokracovat.
     */
    public void SimulationRun() throws CycleException, MissingValueException, SimulationEndException {
        List<IOperation> list = GetBlocksColumn(0);
        while (list != null) {
            counter.SetOut2In();
            counter.Step(list, false);
            list = GetBlocksColumn(counter.GetCounter());
        }
    }

    //endregion

}
