
package main.ui.component;

import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import main.blocks.Block;

/**
 * Sample custom control hosting a text field and a button.
 */
public class BlockControl extends GridPane {
    public Block block;
    protected Integer rows = 1;

    //@FXML private TextField textField;
    @FXML public MenuItem menu_up;
    @FXML public MenuItem menu_down;
    @FXML public MenuItem menu_revup;
    @FXML public MenuItem menu_revdown;
    @FXML public MenuItem menu_remove;
    @FXML public MenuItem block_add;   //Block types list
    @FXML public MenuItem block_sub;
    @FXML public MenuItem block_constant;
  @FXML public MenuItem block_mul;
  @FXML public MenuItem block_div;
  @FXML public MenuItem block_switch;

    @FXML public Button centerButton;
    @FXML public Button rightButton;


  public BlockControl() {
    FXMLLoader fxmlLoader = new FXMLLoader(
      getClass().getResource(
              //"custom_control.fxml"
              //"custom_my.fxml"
              "BlockControl.fxml"
      ));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  protected void ChangeRows(Integer start, Integer end) {
    rows = end - start + 1;
    if(rows <= 0) rows = 1;
    Integer nowRows = this.getRowConstraints().size();
    for(int i = rows*3; i < nowRows; i++) {
      // (nowRows > rows*3)
      this.getRowConstraints().remove(0);
    }
    for(int i = nowRows; i < rows*3; i+=3) {
      // (nowRows < rows*3)
      //<RowConstraints minHeight="0.0" percentHeight="25.0" vgrow="SOMETIMES" />
      RowConstraints rc = new RowConstraints();
      rc.setMinHeight(0.0);
      rc.setPercentHeight(25.0);
      this.getRowConstraints().add(rc);
      rc = new RowConstraints();
      rc.setMinHeight(0.0);
      rc.setPercentHeight(50.0);
      this.getRowConstraints().add(rc);
      rc = new RowConstraints();
      rc.setMinHeight(0.0);
      rc.setPercentHeight(25.0);
      this.getRowConstraints().add(rc);
    }
    GridPane.setRowSpan(centerButton,rows*3);
  }

  public Block getBlock() {
      return this.block;
  }
  public void setBlock(Block block) {
    this.block = block;
    ChangeRows(block.GetPositionStart(), block.GetPositionEnd());
  }

  public Integer getPositionStart() {
      return block.GetPositionStart();
  }
  public void setPositionStart(Integer value) {
    block.SetPositionStart(value);
    ChangeRows(block.GetPositionStart(), block.GetPositionEnd());
  }

  public Integer getPositionEnd() {
      return block.GetPositionEnd();
  }
  public void setPositionEnd(Integer value) {
    block.SetPositionEnd(value);
    ChangeRows(block.GetPositionStart(), block.GetPositionEnd());
  }

//    public String getText() {
//        return textProperty().get();
//    }
//
//    public void setText(String value) {
//        textProperty().set(value);
//    }

//    public StringProperty textProperty() {
//        return textField.textProperty();
//    }

  @FXML
  protected void doSomething() {
        System.out.println("The button was clicked!");
    }


}
