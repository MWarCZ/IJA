package main.blocks;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.manipulator.IOperation;
import main.project.IDomSaveLoad;

//import main.blocks.PortException;
//import main.blocks.PortGroupException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Block implements IOperation, IDomSaveLoad {
    protected List<Integer> portsIn;
    protected List<Integer> portsOut;
    protected List<Integer> groupIn;
    protected List<Integer> groupOut;

//    protected Integer positionStart;
//    protected Integer positionEnd;

    public IntegerProperty positionStartProperty = new SimpleIntegerProperty(0);
    public IntegerProperty positionEndProperty = new SimpleIntegerProperty(0);
    public IntegerProperty sizeProperty = new SimpleIntegerProperty(1);

    /**
     * Constructor
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

    protected void SetSize(Integer value) {
        if(value>0){
            this.sizeProperty.setValue(value);
        }
        else {
            this.sizeProperty.setValue(1);
        }
    }
    public Integer GetSize() {
        return this.sizeProperty.getValue();
    }

    public void SetPosition(Integer value1, Integer value2) {
        if (value1 < value2) {
            positionStartProperty.setValue(value1);
            positionEndProperty.setValue(value2);
            this.SetSize(value2-value1+1);
        } else {
            positionStartProperty.setValue(value2);
            positionEndProperty.setValue(value1);
            this.SetSize(value1-value2+1);
        }
    }
//    public void SetPosition(Integer value1, Integer value2) {
//        if (value1 < value2) {
//            this.positionStart = value1;
//            this.positionEnd = value2;
//        } else {
//            this.positionStart = value2;
//            this.positionEnd = value1;
//        }
//    }

    public Integer GetPositionStart() {
        return positionStartProperty.getValue();
    }

    public void SetPositionStart(Integer value) {
        this.SetPosition(value, this.GetPositionEnd());
    }

    public Integer GetPositionEnd() {
        return positionEndProperty.getValue();
    }

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


    public Integer GetNextGroupIn(Integer group) throws PortException {
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


    public Integer GetNextGroupOut(Integer group) throws PortException {
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

