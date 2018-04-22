package main.blocks;

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

public class Block implements IOperation,IDomSaveLoad{
  protected ArrayList<Integer> portsIn;
  protected ArrayList<Integer> portsOut;
  protected ArrayList<Integer> groupIn;
  protected ArrayList<Integer> groupOut;
  /**
   *Constructor
   */
  public Block(ArrayList<Integer> portsIn, ArrayList<Integer> portsOut){
    this.portsIn = portsIn;
    this.portsOut = portsOut;
    this.groupIn = new ArrayList<Integer>(portsIn.size());
    this.groupOut = new ArrayList<Integer>(portsOut.size());
  }
  public Block(){
    portsIn = new ArrayList<Integer>();
    portsOut = new ArrayList<Integer>();
    groupIn = new ArrayList<Integer>();
    groupOut = new ArrayList<Integer>();
  }

  /**
   * Funkce pro kontrolu jestli dana skupina muze existovat.
   * Ve vychozim nastaveni muze existovat 10 skupin (0-9).
   * @param group Stupina
   * @return Vraci true pokud muze skupina existovat jinak false.
   */
  public boolean IsPossibleGroupIn(Integer group) {
    return (group >= 0 && group < 10);
  }

  /**
   * Funkce pro kontrolu jestli dana skupina muze existovat.
   * Ve vychozim nastaveni muze existovat 10 skupin (0-9).
   * @param group Stupina
   * @return Vraci true pokud muze skupina existovat jinak false.
   */
  public boolean IsPossibleGroupOut(Integer group) {
    return (group >= 0 && group < 10);
  }

  /**
   * Prida novy vstupni port.
   * @param port Na ktery port se napojuje.
   * @param group Skupina do ktere port bude patrit.
   * @throws PortGroupException Neexistujici skupina pro vstupni porty.
   * @throws PortException Chyba pri praci s porty.
   */
  public void AddPortIn(Integer port, Integer group) throws PortGroupException, PortException {
    if(! this.IsPossibleGroupIn(group) ) {
      // err skupina nemuze existovat
      throw new PortGroupException(String.format(
        "Neexistujici skupina vstupnich portu '%d' pro blok '%s'. ",
        group, this.getClass().getSimpleName()));
    }
    if( this.portsIn.contains(port) ) {
      throw new PortException(String.format(
        "Duplicitni pripojeni k jednomu vstupnimu portu '%d' pro blok '%s'. ",
        port, this.getClass().getSimpleName()));
    }
    this.portsIn.add(port);
    this.groupIn.add(group);
  }

  /**
   * Odstrani vstupni port.
   * @param port Na ktery port je napojen.
   */
  public void RemovePortIn(Integer port) {
    Integer index = this.portsIn.indexOf(port);
    if(index > -1) {
      this.portsIn.remove(index.intValue() );
      this.groupIn.remove(index.intValue() );
    }
  }

  public Integer GetGroupIn(Integer port) throws PortException {
    Integer index = this.portsIn.indexOf(port);
    if( index < 0 ) {
      // err port neexistuje
      throw new PortException(String.format(
        "Blok '%s' neni pripojen k vstupnimu portu '%d'. ",
        this.getClass().getSimpleName(), port ));
    }
    return groupIn.get(port);
  }

  /**
   * Zmeni skupinu vstupniho portu pro dany port v bloku.
   * @param port Port pro ktery bude nastavena nova skupina.
   * @param group Skupina ktera bude nastavena.
   * @throws PortGroupException Neexistujici skupina pro vstupni porty.
   * @throws PortException Chyba pri praci s porty.
   */
  public void ChangeGroupIn(Integer port, Integer group) throws PortGroupException, PortException {
    if(! this.IsPossibleGroupIn(group) ) {
      // err skupina nemuze existovat
      throw new PortGroupException(String.format(
        "Neexistujici skupina vstupnich portu '%d' pro blok '%s'. ",
        group, this.getClass().getSimpleName()));
    }
    Integer index = this.portsIn.indexOf(port);
    if( index < 0 ) {
      // err port neexistuje
      throw new PortException(String.format(
        "Blok '%s' neni pripojen k vstupnimu portu '%d'. ",
        this.getClass().getSimpleName(), port ));
    }
    this.groupIn.set(index, group);
  }


  public Integer GetNextGroupIn(Integer group) throws PortException {
    // maximalni hodnota vstupni skupiny
    Integer maxGroup = 0;
    for(Integer g : groupIn) {
      if(g > maxGroup) {
        maxGroup = g;
      }
    }
    // Hledani dalsi v poradi mozne vstupni skupiny.
    Integer newGroup = -1;
    for(Integer i=group+1; i<=maxGroup; i++) {
      if( IsPossibleGroupIn(i) ){
        newGroup = i;
        break;
      }
    }
    if(newGroup.equals(-1)) {
      for(Integer i=0; i<group; i++) {
        if( IsPossibleGroupIn(i) ){
          newGroup = i;
          break;
        }
      }
    }
    // Vsechny skupiny jsou obsazeny - vytvori se nova.
    if(newGroup.equals(-1)) {
      newGroup = maxGroup+1;
    }
    return newGroup;
  }


  /**
   * Nastavy pro dany port jinou skupinu. Skupinu dalsi v poradi.
   * @param port Port pro ktery bude nastavena nova skupina.
   * @return Vraci cislo skupiny, ktera byla nastavena pro dany port.
   * @throws PortException Chyba pri praci s porty.
   */
  public Integer NextGroupIn(Integer port) throws PortException {
    Integer index = this.portsIn.indexOf(port);
    if( index < 0 ) {
      // err port neexistuje
      throw new PortException(String.format(
        "Blok '%s' neni pripojen k vstupnimu portu '%d'. ",
        this.getClass().getSimpleName(), port ));
    }

    Integer group = GetNextGroupIn( groupIn.get(index) );
//    if(! IsPossibleGroupIn(group) ) {
//      group = 0;
//    }
    ChangeGroupIn(port,group);
    return group;
  }

  /**
   * Prida novy vystupni port.
   * @param port Na ktery port se napojuje.
   * @param group Skupina do ktere port bude patrit.
   * @throws PortGroupException Neexistujici skupina pro vstupni porty.
   * @throws PortException Chyba pri praci s porty.
   */
  public void AddPortOut(Integer port, Integer group) throws PortGroupException, PortException {
    if(! this.IsPossibleGroupOut(group) ) {
      // err skupina nemuze existovat
      throw new PortGroupException(String.format(
        "Neexistujici skupina vystupnich portu '%d' pro blok '%s'. ",
        group, this.getClass().getSimpleName()));
    }
    if( this.portsOut.contains(port) ) {
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
   * @param port Na ktery port je napojen.
   */
  public void RemovePortOut(Integer port) {
    Integer index = this.portsOut.indexOf(port);
    if(index > -1) {
      this.portsOut.remove(index.intValue() );
      this.groupOut.remove(index.intValue() );
    }
  }

  public Integer GetGroupOut(Integer port) throws PortException {
    Integer index = this.portsOut.indexOf(port);
    if( index < 0 ) {
      // err port neexistuje
      throw new PortException(String.format(
        "Blok '%s' neni pripojen k vstupnimu portu '%d'. ",
        this.getClass().getSimpleName(), port ));
    }
    return groupOut.get(port);
  }

  /**
   * Zmeni skupinu vystupniho portu pro dany port v bloku.
   * @param port Port pro ktery bude nastavena nova skupina.
   * @param group Skupina ktera bude nastavena.
   * @throws PortGroupException Neexistujici skupina pro vystupni porty.
   * @throws PortException Chyba pri praci s porty.
   */
  public void ChangeGroupOut(Integer port, Integer group) throws PortGroupException, PortException {
    if(! this.IsPossibleGroupOut(group) ) {
      // err skupina nemuze existovat
      throw new PortGroupException(String.format(
        "Neexistujici skupina vstupnich portu '%d' pro blok '%s'. ",
        group, this.getClass().getSimpleName()));
    }
    Integer index = this.portsOut.indexOf(port);
    if( index < 0 ) {
      // err port neexistuje
      throw new PortException(String.format(
        "Blok '%s' neni pripojen k vstupnimu portu '%d'. ",
        this.getClass().getSimpleName(), port ));
    }
    this.groupOut.set(index, group);
  }


  public Integer GetNextGroupOut(Integer group) throws PortException {
    // maximalni hodnota vstupni skupiny
    Integer maxGroup = 0;
    for(Integer g : groupOut) {
      if(g > maxGroup) {
        maxGroup = g;
      }
    }
    // Hledani dalsi v poradi mozne vstupni skupiny.
    Integer newGroup = -1;
    for(Integer i=group; i<=maxGroup; i++) {
      if( IsPossibleGroupOut(i) ){
        newGroup = i;
        break;
      }
    }
    if(newGroup.equals(-1)) {
      for(Integer i=0; i<group; i++) {
        if( IsPossibleGroupOut(i) ){
          newGroup = i;
          break;
        }
      }
    }
    // Vsechny skupiny jsou obsazeny - vytvori se nova.
    if(newGroup.equals(-1)) {
      newGroup = maxGroup+1;
    }
    return newGroup;
  }

  /**
   * Nastavy pro dany port jinou skupinu. Skupinu dalsi v poradi.
   * @param port Port pro ktery bude nastavena nova skupina.
   * @return Vraci cislo skupiny, ktera byla nastavena pro dany port.
   * @throws PortException Chyba pri praci s porty.
   */
  public Integer NextGroupOut(Integer port) throws PortException {
    Integer index = this.portsOut.indexOf(port);
    if( index < 0 ) {
      // err port neexistuje
      throw new PortException(String.format(
        "Blok '%s' neni pripojen k vstupnimu portu '%d'. ",
        this.getClass().getSimpleName(), port ));
    }

    Integer group = GetNextGroupOut(groupOut.get(index) );
//    if(! IsPossibleGroupIn(group) ) {
//      group = 0;
//    }
    ChangeGroupOut(port,group);
    return group;
  }


  @Override
  public ArrayList<Integer> GetPortsIn() {    return this.portsIn;    }
  @Override
  public ArrayList<Integer> GetPortsOut() {   return this.portsOut;   }
  @Override
  public HashMap<Integer, Double> Operation(HashMap<Integer, Double> data) {
    HashMap<Integer, Double> results = new HashMap<>();
    for(Integer port : portsOut) {
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
    for(Integer i=0; i<portsNode.getLength(); i++) {
      Node portNode = portsNode.item(i);
      if(portNode.getNodeType() == Node.ELEMENT_NODE) {
        Element elem = (Element) portNode;
        // <in group="0">0</in>
        // <out group="0">0</out>

        Integer group = 0;
        if(elem.hasAttribute("group") ) {
          group = Integer.parseInt( elem.getAttribute("group") );
        }

        if(elem.getTagName().equals("in") ) {
          String tmp = elem.getTextContent();
          portsIn.add(Integer.parseInt(tmp));
          groupIn.add(group);
        }
        else if(elem.getTagName().equals("out") ) {
          String tmp = elem.getTextContent();
          portsOut.add(Integer.parseInt(tmp));
          groupOut.add(group);
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
    for(Integer i=0; i<this.portsIn.size(); i++) {
      port = dom.createElement("in");
      port.setAttribute("group", this.groupIn.get(i).toString() );
      port.appendChild(dom.createTextNode( this.portsIn.get(i).toString() ));
      parent.appendChild(port);
    }
    for(Integer i=0; i<this.portsOut.size(); i++){
      port = dom.createElement("out");
      port.setAttribute("group", this.groupOut.get(i).toString() );
      port.appendChild(dom.createTextNode( this.portsOut.get(i).toString() ));
      parent.appendChild(port);
    }
  }
}

