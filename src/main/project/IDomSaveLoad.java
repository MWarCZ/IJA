/**
 * Obsahuje rozhrani IDomSaveLoad.
 * Rozhrani definuje funkce pro ulozeni a nacteni objektu do xml souboru.
 *
 * @author Miroslav Válka (xvalka05)
 * @author Jan Trněný (xtrnen03)
 */

package main.project;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Rozhrani definuje funkce pro ulozeni a nacteni objektu do xml souboru.
 */
public interface IDomSaveLoad {
  /**
   * Objekt se nacte z dodaneho elementu.
   * @param parent Element, ktery by mel obsahovat vse potrebne pro inicializaci objektu.
   */
  public void LoadFromDomElement(Element parent);

  /**
   * Objekt se ulozi do daneho elementu.
   * @param parent Element do nehoz se objekt ulozi.
   * @param dom Document pomoci ktereho je mozne generovat nove elementy.
   */
  public void SaveToDomElement(Element parent, Document dom);
}
