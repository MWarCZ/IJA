
package main.project;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
