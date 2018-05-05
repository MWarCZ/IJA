
package main.ui.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import main.blocks.Block;
import main.blocks.PortException;

/**
 * Sample custom control hosting a text field and a button.
 */
public class BlockControl extends GridPane {
    public Block block;
    public Line cabel;
    public Tooltip info;
    public ArrayList<Line> lineList = new ArrayList<>();
    protected Integer rows = 1;

    public List<Button> portsInButtons;
    public List<Button> portsOutButtons;

    //@FXML private TextField textField;
    @FXML
    public MenuItem menu_up;
    @FXML
    public MenuItem menu_down;
    @FXML
    public MenuItem menu_revup;
    @FXML
    public MenuItem menu_revdown;
    @FXML
    public MenuItem menu_remove;
    @FXML
    public MenuItem remove_line;

    @FXML
    public Button centerButton;
    @FXML
    public Button rightButton;


    @FXML public StringProperty textProperty = new SimpleStringProperty("?");

    public BlockControl() {
        this.portsInButtons = FXCollections.observableArrayList();
        this.portsOutButtons = FXCollections.observableArrayList();

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

        centerButton.textProperty().bind(textProperty);
    }
    // Zmeni velikost bloku - o kolikatiradkovy blok se bude jednat
    //protected void ReSizeByBlock() {
    protected void ChangeRows(Integer start, Integer end) {
        rows = block.GetSize();
        if (rows <= 0) rows = 1;
        Button centerButton = this.centerButton;

        this.getChildren().clear();
        this.getRowConstraints().clear();


        for (int i = 0; i < rows * 3; i += 3) {
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

        //GridPane.setRowSpan(centerButton, rows * 3);
        this.add(centerButton, 1, 0, 1, rows*3 );

        this.portsInButtons.clear();
        this.portsOutButtons.clear();
        for(Integer i = 0; i<rows; i++) {
            Button portInButton = new Button("<");
            portInButton.setFocusTraversable(false);
            portInButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            portInButton.setMinSize(0.0,0.0);

            // pozice kde se port nachazi ve schematu.
            Integer portInPosition = block.GetPositionStart()+i;
            if(block.GetPortsIn().contains(portInPosition)) {
                portInButton.getStyleClass().add("port");
                try {
                    portInButton.setText(
                        block.GetGroupIn(portInPosition).toString() );
                } catch (PortException e) {
                    //e.printStackTrace();
                    System.out.println("Chyba pri bindovani portu. ");
                }
            }
            else {
                portInButton.getStyleClass().add("addport");
            }
            portInButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Boolean ExistPort = portInButton.getStyleClass().contains("port");
                    if(! ExistPort) {
                        try {
                            Integer group = block.GetNextGroupIn(-1);
                            block.AddPortIn(portInPosition, group);
                            portInButton.getStyleClass().remove("addport");
                            portInButton.getStyleClass().add("port");
                            try {
                                portInButton.setText(
                                    block.GetGroupIn(portInPosition).toString() );
                            } catch (PortException e) {
                                //e.printStackTrace();
                                System.out.println("Chyba pri bindovani portu. ");
                            }
                        } catch (PortException e) {
                            //e.printStackTrace();
                            System.out.println("Chyba pri vytvareni portu.");
                        }
                    }
                    else {
                        try {
                            Integer groupNow = block.GetGroupIn(portInPosition);
                            Integer group = block.GetNextGroupIn(groupNow);
                            block.ChangeGroupIn(portInPosition, group);

                            portInButton.setText(
                                block.GetGroupIn(portInPosition).toString() );
                        } catch (PortException e) {
                            //e.printStackTrace();
                            System.out.println("Chyba pri zmene skupiny portu.");
                        }
                    }
                    System.out.println("Button portOut onAction");
                }
            });

            this.portsInButtons.add(portInButton);
            this.add(portInButton, 0, i*3+1);

            Button portOutButton = new Button( ">");
            portOutButton.setFocusTraversable(false);
            portOutButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            portOutButton.setMinSize(0.0,0.0);

            // pozice kde se port nachazi ve schematu.
            Integer portOutPosition = block.GetPositionStart()+i;
            if(block.GetPortsOut().contains(portOutPosition)) {
                portOutButton.getStyleClass().add("port");
                try {
                    portOutButton.setText(
                        block.GetGroupOut(portOutPosition).toString() );
                } catch (PortException e) {
                    //e.printStackTrace();
                    System.out.println("Chyba pri bindovani portu.");
                }
            }
            else {
                portOutButton.getStyleClass().add("addport");
            }
            portOutButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Boolean ExistPort = portOutButton.getStyleClass().contains("port");
                    if(! ExistPort) {
                        try {
                            Integer group = block.GetNextGroupOut(-1);
                            block.AddPortOut(portOutPosition, group);
                            portOutButton.getStyleClass().remove("addport");
                            portOutButton.getStyleClass().add("port");
                            try {
                                portOutButton.setText(
                                    block.GetGroupOut(portOutPosition).toString() );
                            } catch (PortException e) {
                                //e.printStackTrace();
                                System.out.println("Chyba pri bindovani portu.");
                            }
                        } catch (PortException e) {
                            //e.printStackTrace();
                            System.out.println("Chyba pri vytvareni portu.");
                        }
                    }
                    else {
                        try {
                            Integer groupNow = block.GetGroupOut(portOutPosition);
                            Integer group = block.GetNextGroupOut(groupNow);
                            block.ChangeGroupOut(portOutPosition, group);

                            portOutButton.setText(
                                block.GetGroupOut(portOutPosition).toString() );
                        } catch (PortException e) {
                            //e.printStackTrace();
                            System.out.println("Chyba pri zmene skupiny portu.");
                        }
                    }
                    System.out.println("Button portOut onAction");
                }
            });

            this.portsOutButtons.add(portOutButton);
            this.add(portOutButton, 2, i*3+1);
        }
    }
    // Zmeni velikost bloku - o kolikatiradkovy blok se bude jednat
    protected void ChangeRows1(Integer start, Integer end) {
        rows = end - start + 1;
        if (rows <= 0) rows = 1;
        Integer nowRows = this.getRowConstraints().size();
        for (int i = rows * 3; i < nowRows; i++) {
            // (nowRows > rows*3)
            this.getRowConstraints().remove(0);
        }
        for (int i = nowRows; i < rows * 3; i += 3) {
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
        GridPane.setRowSpan(centerButton, rows * 3);
    }

// <Button focusTraversable="false"
// maxHeight="1.7976931348623157E308"
// maxWidth="1.7976931348623157E308"
// GridPane.columnIndex="2" GridPane.rowIndex="1" />
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

    public void disableForInputBlock() {
//        this.block_add.setDisable(true);
//        this.block_sub.setDisable(true);
//        this.block_mul.setDisable(true);
//        this.block_div.setDisable(true);
//        this.block_constant.setDisable(true);
//        this.block_switch.setDisable(true);
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

    /**
     * Funkce prohledá pole, zda-li existuje blok navazující na blok volající tuto metodu
     *
     * @param gridPane
     * @param row
     * @param col
     * @return Boolean
     */
    public boolean checkNextBlock(GridPane gridPane, int row, int col) {
        int tmpRow;
        int tmpCol;
        for (Node next : gridPane.getChildren()) {
            if (GridPane.getRowIndex(next) == null) tmpRow = 0;
            else tmpRow = GridPane.getRowIndex(next);
            if (GridPane.getColumnIndex(next) == null) tmpCol = 0;
            else tmpCol = GridPane.getColumnIndex(next);

            if (tmpRow == row && tmpCol == col) return true;
        }
        return false;
    }

    /**
     * Vytvoření nového propoje do hodnoty cabel daného bloku
     */
    public void genNewLine() {
        this.cabel = new Line(this.rightButton.getLayoutX(), 0, this.rightButton.getLayoutX() * 2 - 16, 0);  //magical numbers B)
        this.cabel.setTranslateX(this.rightButton.getLayoutX());    //move to center
        this.cabel.setStrokeWidth(7);
    }

    /**
     * Projde list spojů a najde spoj pro daný blok na daném řádku
     *
     * @param row
     * @return Vrací spoj při úspěšném nalezení. V opačném případě null.
     */
    public Line returnLine(int row) {
        for (Line line : this.lineList) {
            if (GridPane.getRowIndex(line) == row) {
                return line;
            }
        }
        return null;
    }

    /**
     * Funkce odebere všechny propoje bloku. Vyčistí arrayList + všechny spoje v Gridpane
     *
     * @param gridpane
     */
    public void removeLine(GridPane gridpane) {
        for (Line each : this.lineList) {
            gridpane.getChildren().remove(each);
        }
        this.lineList.clear();
    }
}
