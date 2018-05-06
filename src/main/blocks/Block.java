/**
 * Obsahuje trydu Block, jedna se o zakladni trydu ze ktere vychazi ostatni bloky.
 *
 * @author Miroslav Válka (xvalka05)
 * @author Jan Trněný (xtrnen03)
 */
package main.blocks;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
import main.manipulator.IOperation;
import main.project.IDomSaveLoad;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Zakladni implementace bloku.
 */
public class Block implements IOperation, IDomSaveLoad {
    /**
     * Seznam Indexu propoju na ktery jsou napojeny vstupni porty.
     */
    protected List<Integer> portsIn;
    /**
     * Seznam indexu propoju na ktery jsou napojeny vystupni porty.
     */
    protected List<Integer> portsOut;
    /**
     * Seznam skupin pridelenych vstupnim portum.
     */
    protected List<Integer> groupIn;
    /**
     * Seznam skupin pridelenych vystupnim portum.
     */
    protected List<Integer> groupOut;

//    protected Integer positionStart;
//    protected Integer positionEnd;

    /**
     * Vlastnost urcujici pocatecni pozice bloku.
     */
    public IntegerProperty positionStartProperty = new SimpleIntegerProperty(0);
    /**
     * Vlastnost urcujici konec pozice bloku.
     */
    public IntegerProperty positionEndProperty = new SimpleIntegerProperty(0);
    /**
     * Vlastnost uchovavajici velikost bloku.
     */
    public IntegerProperty sizeProperty = new SimpleIntegerProperty(1);

    /**
     * Konstruktor inicializuje vychozi hodnoty.
     *
     * @param positionStart Vychozi hodnota pocatecni pozice umisteni bloku.
     * @param positionEnd   Vychozi hodnota konce pozice umisteni bloku.
     */
    public Block(Integer positionStart, Integer positionEnd) {
//        portsIn = new ArrayList<Integer>();
//        portsOut = new ArrayList<Integer>();
//        groupIn = new ArrayList<Integer>();
//        groupOut = new ArrayList<Integer>();

        portsIn = FXCollections.observableArrayList();
        portsOut = FXCollections.observableArrayList();
        groupIn = FXCollections.observableArrayList();
        groupOut = FXCollections.observableArrayList();

        this.SetPosition(positionStart, positionEnd);
    }

    /**
     * Konstruktor inicializuje vychozi hodnoty.
     */
    public Block() {
//        portsIn = new ArrayList<Integer>();
//        portsOut = new ArrayList<Integer>();
//        groupIn = new ArrayList<Integer>();
//        groupOut = new ArrayList<Integer>();

        portsIn = FXCollections.observableArrayList();
        portsOut = FXCollections.observableArrayList();
        groupIn = FXCollections.observableArrayList();
        groupOut = FXCollections.observableArrayList();
        this.SetPosition(0, 0);
    }

    /**
     * Setter pro nastaveni hodnoty velikosti. Hodnota se ulozi do sizeProperty.
     *
     * @param value Hodnota velikosti.
     */
    protected void SetSize(Integer value) {
        if (value > 0) {
            this.sizeProperty.setValue(value);
        } else {
            this.sizeProperty.setValue(1);
        }
    }

    /**
     * Getter pro vraceni hodnoty velikosti bloku.
     *
     * @return Vraci velikost bloku.
     */
    public Integer GetSize() {
        return this.sizeProperty.getValue();
    }

    /**
     * Setter pro nastaveni zacatku a konce bloku.
     * Hodnoty jsou automaticky ulozeny tak,
     * aby nejmensi hodnota byla positionStart a nejvedsi byla positionEnd.
     * Dojde take k prepocitani velikosti bloku.
     *
     * @param value1 Hodnota umisteni bloku od.
     * @param value2 Hodnota umisteni bloku do.
     */
    public void SetPosition(Integer value1, Integer value2) {
        if (value1 < value2) {
            positionStartProperty.setValue(value1);
            positionEndProperty.setValue(value2);
            this.SetSize(value2 - value1 + 1);
        } else {
            positionStartProperty.setValue(value2);
            positionEndProperty.setValue(value1);
            this.SetSize(value1 - value2 + 1);
        }
    }

    /**
     * Getter pro ziskani hodnoty pocatecni pozice bloku.
     *
     * @return Hodnota pocatecni pozice bloku.
     */
    public Integer GetPositionStart() {
        return positionStartProperty.getValue();
    }

    /**
     * Setter pro nastaveni pocatecni pozice bloku.
     * Pokud je pocatecni pozice vedsi nez koncova pozice, tak dojde k prohozeni techto hodnot.
     *
     * @param value Nova hodnota umisteni.
     */
    public void SetPositionStart(Integer value) {
        this.SetPosition(value, this.GetPositionEnd());
    }

    /**
     * Getter pro ziskani hodnoty koncove pozice bloku.
     *
     * @return Hodnota koncove pozice bloku.
     */
    public Integer GetPositionEnd() {
        return positionEndProperty.getValue();
    }

    /**
     * Setter pro nastaveni koncove pozice bloku.
     * Pokud je koncova pozice mensi nez pocatecni pozice, tak dojde k prohozeni techto hodnot.
     *
     * @param value Nova hodnota umisteni.
     */
    public void SetPositionEnd(Integer value) {
        this.SetPosition(this.GetPositionStart(), value);
    }

    /**
     * Funkce pro kontrolu jestli dana skupina muze existovat.
     * Ve vychozim nastaveni muze existovat 10 skupin (0-9).
     *
     * @param group Stupina
     * @return Vraci true pokud muze skupina existovat jinak false.
     */
    public boolean IsPossibleGroupIn(Integer group) {
        return (group >= 0 && group < 10);
    }

    /**
     * Funkce pro kontrolu jestli dana skupina muze existovat.
     * Ve vychozim nastaveni muze existovat 10 skupin (0-9).
     *
     * @param group Stupina
     * @return Vraci true pokud muze skupina existovat jinak false.
     */
    public boolean IsPossibleGroupOut(Integer group) {
        return (group >= 0 && group < 10);
    }

    /**
     * Prida novy vstupni port.
     *
     * @param port  Na ktery port se napojuje.
     * @param group Skupina do ktere port bude patrit.
     * @throws PortGroupException Neexistujici skupina pro vstupni porty.
     * @throws PortException      Chyba pri praci s porty.
     */
    public void AddPortIn(Integer port, Integer group) throws PortGroupException, PortException {
        if (!this.IsPossibleGroupIn(group)) {
            // err skupina nemuze existovat
            throw new PortGroupException(String.format(
                "Neexistujici skupina vstupnich portu '%d' pro blok '%s'. ",
                group, this.getClass().getSimpleName()));
        }
        if (this.portsIn.contains(port)) {
            throw new PortException(String.format(
                "Duplicitni pripojeni k jednomu vstupnimu portu '%d' pro blok '%s'. ",
                port, this.getClass().getSimpleName()));
        }
        this.portsIn.add(port);
        this.groupIn.add(group);
    }

    /**
     * Odstrani vstupni port.
     *
     * @param port Na ktery port je napojen.
     */
    public void RemovePortIn(Integer port) {
        Integer index = this.portsIn.indexOf(port);
        if (index > -1) {
            this.portsIn.remove(index.intValue());
            this.groupIn.remove(index.intValue());
        }
    }

    /**
     * Funkce pro ziskani do ktere skupiny vstupnich portu patri dany port.
     *
     * @param port Port pro ktery se zjistuje jeho skupina.
     * @return Vraci se skupina portu do ktere port patri.
     * @throws PortException Pokud dany port v bloku neexituje dojde k vyjimce.
     */
    public Integer GetGroupIn(Integer port) throws PortException {
        Integer index = this.portsIn.indexOf(port);
        if (index < 0) {
            // err port neexistuje
            throw new PortException(String.format(
                "Blok '%s' neni pripojen k vstupnimu portu '%d'. ",
                this.getClass().getSimpleName(), port));
        }
        return groupIn.get(index);
    }

    /**
     * Zmeni skupinu vstupniho portu pro dany port v bloku.
     *
     * @param port  Port pro ktery bude nastavena nova skupina.
     * @param group Skupina ktera bude nastavena.
     * @throws PortGroupException Neexistujici skupina pro vstupni porty.
     * @throws PortException      Chyba pri praci s porty.
     */
    public void ChangeGroupIn(Integer port, Integer group) throws PortGroupException, PortException {
        if (!this.IsPossibleGroupIn(group)) {
            // err skupina nemuze existovat
            throw new PortGroupException(String.format(
                "Neexistujici skupina vstupnich portu '%d' pro blok '%s'. ",
                group, this.getClass().getSimpleName()));
        }
        Integer index = this.portsIn.indexOf(port);
        if (index < 0) {
            // err port neexistuje
            throw new PortException(String.format(
                "Blok '%s' neni pripojen k vstupnimu portu '%d'. ",
                this.getClass().getSimpleName(), port));
        }
        this.groupIn.set(index, group);
    }

    /**
     * Funkce vrati cislo skupiny, ktera je jako dalsi v poradi za zadanou skupinou.
     *
     * @param group Aktualni skupina portu.
     * @return Vraci cislo skupiny portu, ktera nasleduje za zadanou skupinou.
     * @throws PortGroupException Pokud dode k chybe pri praci se skupinami portu.
     */
    public Integer GetNextGroupIn(Integer group) throws PortGroupException {
        // maximalni hodnota vstupni skupiny
        Integer maxGroup = 0;
        for (Integer g : groupIn) {
            if (g > maxGroup) {
                maxGroup = g;
            }
        }
        // Hledani dalsi v poradi mozne vstupni skupiny.
        Integer newGroup = -1;
        for (Integer i = group + 1; i <= maxGroup; i++) {
            if (IsPossibleGroupIn(i)) {
                newGroup = i;
                break;
            }
        }
        if (newGroup.equals(-1)) {
            for (Integer i = 0; i < group; i++) {
                if (IsPossibleGroupIn(i)) {
                    newGroup = i;
                    break;
                }
            }
        }
        // Vsechny skupiny jsou obsazeny - vytvori se nova.
        if (newGroup.equals(-1)) {
            newGroup = maxGroup + 1;
        }
        return newGroup;
    }


    /**
     * Nastavy pro dany port jinou skupinu. Skupinu dalsi v poradi.
     *
     * @param port Port pro ktery bude nastavena nova skupina.
     * @return Vraci cislo skupiny, ktera byla nastavena pro dany port.
     * @throws PortException Chyba pri praci s porty.
     */
    public Integer NextGroupIn(Integer port) throws PortException {
        Integer index = this.portsIn.indexOf(port);
        if (index < 0) {
            // err port neexistuje
            throw new PortException(String.format(
                "Blok '%s' neni pripojen k vstupnimu portu '%d'. ",
                this.getClass().getSimpleName(), port));
        }

        Integer group = GetNextGroupIn(groupIn.get(index));
//    if(! IsPossibleGroupIn(group) ) {
//      group = 0;
//    }
        ChangeGroupIn(port, group);
        return group;
    }

    /**
     * Prida novy vystupni port.
     *
     * @param port  Na ktery port se napojuje.
     * @param group Skupina do ktere port bude patrit.
     * @throws PortGroupException Neexistujici skupina pro vstupni porty.
     * @throws PortException      Chyba pri praci s porty.
     */
    public void AddPortOut(Integer port, Integer group) throws PortGroupException, PortException {
        if (!this.IsPossibleGroupOut(group)) {
            // err skupina nemuze existovat
            throw new PortGroupException(String.format(
                "Neexistujici skupina vystupnich portu '%d' pro blok '%s'. ",
                group, this.getClass().getSimpleName()));
        }
        if (this.portsOut.contains(port)) {
            // err duplicita
            throw new PortException(String.format(
                "Duplicitni pripojeni k jednomu vystupnimu portu '%d' pro blok '%s'. ",
                port, this.getClass().getSimpleName()));
        }
        this.portsOut.add(port);
        this.groupOut.add(group);
    }

    /**
     * Odstrani vystupni port.
     *
     * @param port Na ktery port je napojen.
     */
    public void RemovePortOut(Integer port) {
        Integer index = this.portsOut.indexOf(port);
        if (index > -1) {
            this.portsOut.remove(index.intValue());
            this.groupOut.remove(index.intValue());
        }
    }

    /**
     * Funkce pro ziskani do ktere skupiny vystupnich portu patri dany port.
     *
     * @param port Port pro ktery se zjistuje jeho skupina.
     * @return Vraci se skupina portu do ktere port patri.
     * @throws PortException Pokud dany port v bloku neexituje dojde k vyjimce.
     */
    public Integer GetGroupOut(Integer port) throws PortException {
        Integer index = this.portsOut.indexOf(port);
        if (index < 0) {
            // err port neexistuje
            throw new PortException(String.format(
                "Blok '%s' neni pripojen k vstupnimu portu '%d'. ",
                this.getClass().getSimpleName(), port));
        }
        return groupOut.get(index);
    }

    /**
     * Zmeni skupinu vystupniho portu pro dany port v bloku.
     *
     * @param port  Port pro ktery bude nastavena nova skupina.
     * @param group Skupina ktera bude nastavena.
     * @throws PortGroupException Neexistujici skupina pro vystupni porty.
     * @throws PortException      Chyba pri praci s porty.
     */
    public void ChangeGroupOut(Integer port, Integer group) throws PortGroupException, PortException {
        if (!this.IsPossibleGroupOut(group)) {
            // err skupina nemuze existovat
            throw new PortGroupException(String.format(
                "Neexistujici skupina vstupnich portu '%d' pro blok '%s'. ",
                group, this.getClass().getSimpleName()));
        }
        Integer index = this.portsOut.indexOf(port);
        if (index < 0) {
            // err port neexistuje
            throw new PortException(String.format(
                "Blok '%s' neni pripojen k vstupnimu portu '%d'. ",
                this.getClass().getSimpleName(), port));
        }
        this.groupOut.set(index, group);
    }


    /**
     * Funkce vrati cislo skupiny, ktera je jako dalsi v poradi za zadanou skupinou.
     *
     * @param group Aktualni skupina portu.
     * @return Vraci cislo skupiny portu, ktera nasleduje za zadanou skupinou.
     * @throws PortGroupException Pokud dode k chybe pri praci se skupinami portu.
     */
    public Integer GetNextGroupOut(Integer group) throws PortGroupException {
        // maximalni hodnota vstupni skupiny
        Integer maxGroup = 0;
        for (Integer g : groupOut) {
            if (g > maxGroup) {
                maxGroup = g;
            }
        }
        // Hledani dalsi v poradi mozne vstupni skupiny.
        Integer newGroup = -1;
        for (Integer i = group; i <= maxGroup; i++) {
            if (IsPossibleGroupOut(i)) {
                newGroup = i;
                break;
            }
        }
        if (newGroup.equals(-1)) {
            for (Integer i = 0; i < group; i++) {
                if (IsPossibleGroupOut(i)) {
                    newGroup = i;
                    break;
                }
            }
        }
        // Vsechny skupiny jsou obsazeny - vytvori se nova.
        if (newGroup.equals(-1)) {
            newGroup = maxGroup + 1;
        }
        return newGroup;
    }

    /**
     * Nastavy pro dany port jinou skupinu. Skupinu dalsi v poradi.
     *
     * @param port Port pro ktery bude nastavena nova skupina.
     * @return Vraci cislo skupiny, ktera byla nastavena pro dany port.
     * @throws PortException Chyba pri praci s porty.
     */
    public Integer NextGroupOut(Integer port) throws PortException {
        Integer index = this.portsOut.indexOf(port);
        if (index < 0) {
            // err port neexistuje
            throw new PortException(String.format(
                "Blok '%s' neni pripojen k vstupnimu portu '%d'. ",
                this.getClass().getSimpleName(), port));
        }

        Integer group = GetNextGroupOut(groupOut.get(index));
//    if(! IsPossibleGroupIn(group) ) {
//      group = 0;
//    }
        ChangeGroupOut(port, group);
        return group;
    }


    @Override
    public List<Integer> GetPortsIn() {
        return this.portsIn;
    }

    @Override
    public List<Integer> GetPortsOut() {
        return this.portsOut;
    }

    @Override
    public Map<Integer, Double> Operation(Map<Integer, Double> data) {
//        Map<Integer, Double> results = new HashMap<>();
        Map<Integer, Double> results = FXCollections.observableHashMap();
        for (Integer port : portsOut) {
            results.put(port, 1.0);
        }
        return results;
    }

    @Override
    public void LoadFromDomElement(Element parent) {
        // inicializace listu pro porty.
        ArrayList<Integer> portsIn = new ArrayList<>();
        ArrayList<Integer> portsOut = new ArrayList<>();
        ArrayList<Integer> groupIn = new ArrayList<>();
        ArrayList<Integer> groupOut = new ArrayList<>();

        // nacitani portu.
        NodeList portsNode = parent.getChildNodes();
        for (Integer i = 0; i < portsNode.getLength(); i++) {
            Node portNode = portsNode.item(i);
            if (portNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) portNode;
                // <in group="0">0</in>
                // <out group="0">0</out>

                Integer group = 0;
                if (elem.hasAttribute("group")) {
                    group = Integer.parseInt(elem.getAttribute("group"));
                }

                if (elem.getTagName().equals("in")) {
                    String tmp = elem.getTextContent();
                    portsIn.add(Integer.parseInt(tmp));
                    groupIn.add(group);
                } else if (elem.getTagName().equals("out")) {
                    String tmp = elem.getTextContent();
                    portsOut.add(Integer.parseInt(tmp));
                    groupOut.add(group);
                } else if (elem.getTagName().equals("position")) {

                    Integer tmpStart = 0, tmpEnd = 0;
                    if (elem.hasAttribute("start")) {
                        tmpStart = Integer.parseInt(elem.getAttribute("start"));
                    }
                    if (elem.hasAttribute("end")) {
                        tmpEnd = Integer.parseInt(elem.getAttribute("end"));
                    }
                    this.SetPosition(tmpStart, tmpEnd);
                }
            }
        }

        // Nove nactene porty ulozi.
        this.portsIn = portsIn;
        this.portsOut = portsOut;
        this.groupIn = groupIn;
        this.groupOut = groupOut;
    }

    @Override
    public void SaveToDomElement(Element parent, Document dom) {
        Element port;

        port = dom.createElement("position");
//        port.setAttribute("start", this.positionStart.toString());
//        port.setAttribute("end", this.positionEnd.toString());
        port.setAttribute("start", this.GetPositionStart().toString());
        port.setAttribute("end", this.GetPositionEnd().toString());
        parent.appendChild(port);

        for (Integer i = 0; i < this.portsIn.size(); i++) {
            port = dom.createElement("in");
            port.setAttribute("group", this.groupIn.get(i).toString());
            port.appendChild(dom.createTextNode(this.portsIn.get(i).toString()));
            parent.appendChild(port);
        }
        for (Integer i = 0; i < this.portsOut.size(); i++) {
            port = dom.createElement("out");
            port.setAttribute("group", this.groupOut.get(i).toString());
            port.appendChild(dom.createTextNode(this.portsOut.get(i).toString()));
            parent.appendChild(port);
        }
    }
}

