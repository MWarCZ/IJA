
package main.project;

import main.manipulator.IOperation;
import main.project.IDomSaveLoad;
import main.project.Schema;

import java.lang.String;
import java.util.ArrayList;


import java.io.FileOutputStream;
import java.io.IOException;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;


public class SaveLoader {

    public SaveLoader() { }

  /**
   * Funkce pro ulozeni schematu do souboru XML.
   * @param filename Nazev XML souboru. (Cesta k souboru.)
   * @param schema Schema ktere se bude ukladat.
   */
    public void WriteXML3(String filename, Schema schema) {
      Document dom;
      Element e = null;

      // instance of a DocumentBuilderFactory
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      try {
        // use factory to get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();
        // create instance of DOM
        dom = db.newDocument();

        // create the root element
        Element rootElement = dom.createElement("schema");
        rootElement.setAttribute("name", schema.GetName());

        // Prochazeni sloupecku - jednotlivych kroku.
        for(Integer i=0; i<schema.blocks.size(); i++) {
          Element stepElement = dom.createElement("step");
          stepElement.setAttribute("order", i.toString() );

          // Prochazeni bloku ve sloupecku - v jednom kroku.
          ArrayList<IOperation> blocks = schema.blocks.get(i);
          for(Integer j=0; j<blocks.size(); j++) {
            IOperation block = blocks.get(j);
            IDomSaveLoad saveBlock = (IDomSaveLoad)block;
            Element blockElement = dom.createElement("block");

            blockElement.setAttribute("class", saveBlock.getClass().getCanonicalName() );
            saveBlock.SaveToDomElement(blockElement, dom);

            stepElement.appendChild(blockElement);
          }
          rootElement.appendChild(stepElement);
        }

        dom.appendChild(rootElement);

        try {
          Transformer tr = TransformerFactory.newInstance().newTransformer();
          tr.setOutputProperty(OutputKeys.INDENT, "yes");
          tr.setOutputProperty(OutputKeys.METHOD, "xml");
          tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

          // send DOM to file
          tr.transform(new DOMSource(dom),
            new StreamResult(new FileOutputStream(filename)));

        } catch (TransformerException te) {
          System.out.println(te.getMessage());
        } catch (IOException ioe) {
          System.out.println(ioe.getMessage());
        }
      } catch (ParserConfigurationException pce) {
        System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
      }
    }

  /**
   * Funkce pro nacteni schematu ze souboru XML.
   * @param filename Nazev XML souboru. (Cesta k souboru.)
   * @return Vraci nactene schema.
   */
    public Schema ReadXML3(String filename) {
      // Vytvareni noveho schematu.
      Schema schema = new Schema("tmp");
      ArrayList<ArrayList<IOperation>> blocks = schema.blocks;

      Document dom;
      // Vytvori instanci DocumentBuilderFactory
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      try {
        // Vytvori instanci DocumentBuilder
        DocumentBuilder db = dbf.newDocumentBuilder();
        // Nacte a zparsuje xml soubor.
        dom = db.parse(filename);

        // nacteni root elementu = schema
        Element rootElement = dom.getDocumentElement();
        if(! rootElement.getTagName().equals("schema") ) {
          return null; // Nejedna se o tag schema.
        }
        if(! rootElement.hasAttribute("name") ) {
          return null; // Neobsahuje atribut name.
        }
        schema.SetName( rootElement.getAttribute("name") );

        NodeList stepsListNode = rootElement.getChildNodes();

        // Prochzeni elementu step
        Node tmpNode;
        Element elem;
        for(Integer i=0; i<stepsListNode.getLength();i++) {
          tmpNode = stepsListNode.item(i);
          if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
            elem = (Element) tmpNode;
            if(! elem.getTagName().equals("step") ) {
              return null; // Nejedna se o tag step.
            }
            if(! elem.hasAttribute("order") ) {
              return null; // Neobsahuje atribut class.
            }
            String stepOrder = elem.getAttribute("order");
            Integer indexStepOrder = Integer.parseInt(stepOrder);
            schema.AddEmptyColumnOfBlocks(indexStepOrder);

            // Prochazeni elemuntu block.
            NodeList blocksListNode = elem.getChildNodes();
            for(Integer j=0; j<blocksListNode.getLength(); j++) {
              tmpNode = blocksListNode.item(j);
              if(tmpNode.getNodeType() == Node.ELEMENT_NODE) {
                elem = (Element) tmpNode;
                if(! elem.getTagName().equals("block") ) {
                  return null; // Nejedna se o tag block.
                }
                if(! elem.hasAttribute("class") ) {
                  return null; // Neobsahuje atribut class.
                }
                String blockClass = elem.getAttribute("class");
                Class ccc = Class.forName(blockClass);
                Object ooo = ccc.newInstance();

                IDomSaveLoad loadBlock = (IDomSaveLoad)ooo;
                loadBlock.LoadFromDomElement(elem);

                IOperation block = (IOperation)ooo;
                schema.AddBlock(indexStepOrder,block);

                //System.out.println( elem.getTagName() );
                //System.out.println( blockClass );
                //System.out.println( elem.getTextContent() );
              }
            }

          }
        }

        return schema;

      } catch (ParserConfigurationException pce) {
        System.out.println(pce.getMessage());
      } catch (SAXException se) {
        System.out.println(se.getMessage());
      } catch (IOException ioe) {
        System.err.println(ioe.getMessage());
      } catch (ClassNotFoundException e) {
        e.printStackTrace(); // class bloku nebyla nalezena
      } catch (IllegalAccessException e) {
        e.printStackTrace(); // nelze vytvorit instanci tridy
      } catch (InstantiationException e) {
        e.printStackTrace(); // nelze vytvorit instanci tridy
      }

      return null;
    }

}

