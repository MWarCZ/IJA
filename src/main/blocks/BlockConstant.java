/**
 * Obsahuje tridu BlockConstant, ktera predstavuje blok uchovavajici konstntni hodnotu.
 *
 * @author Miroslav Válka (xvalka05)
 * @author Jan Trněný (xtrnen03)
 */

package main.blocks;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;

/**
 * Blok pro konstantni hodnotu.
 * Propaguje jednu ulozenou hodnotu na vystupy.
 * Nemuze mit zadne vstupy.
 */
public class BlockConstant extends Block {

    /**
     * Vlastnost uchovavajici hodnotu konstanty, kterou blok produkuje.
     */
    public DoubleProperty constValueProperty = new SimpleDoubleProperty(0.0);

    /**
     * Konstruktor inicializuje vychozi hodnoty.
     *
     * @param positionStart Vychozi hodnota pocatecni pozice umisteni bloku.
     * @param positionEnd   Vychozi hodnota konce pozice umisteni bloku.
     */
    public BlockConstant(Integer positionStart, Integer positionEnd) {
        super(positionStart, positionEnd);
    }

    /**
     * Konstruktor inicializuje vychozi hodnoty.
     */
    public BlockConstant() {
        super();
    }

    /**
     * Getter pro ziskani hodnoty ulozeny konstanty.
     *
     * @return Vraci hodnotu ulozene konstanty.
     */
    public Double GetConstValue() {
        return constValueProperty.getValue();
    }

    /**
     * Setter pro nastaveni hodnoty konstanty.
     *
     * @param value Nova hodnota konstanty.
     */
    public void SetConstValue(Double value) {
        constValueProperty.setValue(value);
    }

    @Override
    public boolean IsPossibleGroupIn(Integer group) {
        return false;
    }

    @Override
    public boolean IsPossibleGroupOut(Integer group) {
        return group == 0;
    }

    @Override
    public Map<Integer, Double> Operation(Map<Integer, Double> data) {

//        Map<Integer, Double> results = new HashMap<>();
        Map<Integer, Double> results = FXCollections.observableHashMap();
        for (Integer port : portsOut) {
            results.put(port, GetConstValue());
        }
        return results;
    }

    @Override
    public void LoadFromDomElement(Element parent) {
        super.LoadFromDomElement(parent);

        NodeList portsNode = parent.getChildNodes();
        for (Integer i = 0; i < portsNode.getLength(); i++) {
            Node portNode = portsNode.item(i);
            if (portNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) portNode;
                // <value>0.0</value>

                if (elem.getTagName().equals("value")) {
                    String tmp = elem.getTextContent();
                    SetConstValue(Double.parseDouble(tmp));
                }
            }
        }

    }

    @Override
    public void SaveToDomElement(Element parent, Document dom) {
        super.SaveToDomElement(parent, dom);

        Element value;
        value = dom.createElement("value");
        //value.appendChild(dom.createTextNode(this.constValue.toString()));
        value.appendChild(dom.createTextNode(this.GetConstValue().toString()));
        parent.appendChild(value);

    }
}

