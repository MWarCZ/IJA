package main.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import main.blocks.Block;
import main.ui.component.BlockControl;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SampleController implements Initializable {

    @FXML private ScrollPane scrollPane;
    @FXML private GridPane gridPane;
    @FXML private MenuItem createScheme;
    @FXML private MenuItem openScheme;
    @FXML private MenuItem saveScheme;
    @FXML private MenuItem exitScheme;
    @FXML private MenuItem runScheme;
    @FXML private MenuItem resetScheme;
    @FXML private MenuItem debugScheme;

    @FXML
    public void AddRow_Click(MouseEvent mouseEvent) {
        AddRow(gridPane);
    }

    @FXML
    public void AddCol_Click(MouseEvent mouseEvent) {
        AddCol(gridPane);
    }
    // ---------------

    @FXML
    private void PositionCell_Click(MouseEvent e) {
        Node source = (Node)e.getSource() ;
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        System.out.println(String.format("Cell: [%d, %d]", colIndex, rowIndex));
        //System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
    }

    private void AddCustomControl(GridPane parret, Integer colIndex, Integer rowIndex) {
        BlockControl cc = new BlockControl();
        Block block = new Block();
        block.SetPosition(rowIndex,rowIndex);
        cc.setBlock(block);

        gridPane.add(cc, colIndex, rowIndex);
        /*Input ITEM*/
        if(colIndex == 0){
            cc.centerButton.setText("In");
            //input values dialog
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Set of values");
            inputDialog.setHeaderText("Example: '0,1,2,3'");
            inputDialog.setContentText("Insert values here:");
            cc.disableForInputBlock();  //set ContextMenu for input block
            cc.centerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        Optional<String> set = inputDialog.showAndWait();
                        if (set.isPresent()) {
                            //Input
                            System.out.println("Values: " + set.get());
                        }
                    }
                }
            });
        }
        cc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                PositionCell_Click(event);
            }
        });
        cc.menu_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer span = GridPane.getRowSpan(cc);
                if(span == null) span = 1;
                if(cc.getPositionStart() == 0)  return;
                GridPane.setRowIndex(cc, GridPane.getRowIndex(cc)-1);
                cc.setPositionStart(cc.getPositionStart()-1);
                GridPane.setRowSpan(cc,  span + 1);
                /*adding cabel to end of list*/
                if(cc.cabel != null){
                    //new line on new position
                    cc.genNewLine();
                    parret.add(cc.cabel, colIndex, GridPane.getRowIndex(cc));
                    cc.lineList.add(cc.cabel);
                }
            }
        });
        cc.menu_revup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer span = GridPane.getRowSpan(cc);
                if(span == null || span < 2) span = 2;
                GridPane.setRowIndex(cc, GridPane.getRowIndex(cc)+1);
                cc.setPositionStart(cc.getPositionStart()+1);
                GridPane.setRowSpan(cc,  span - 1);
                if(cc.cabel != null){
                    Line tmp = cc.returnLine(GridPane.getRowIndex(cc)-1);   //Null??
                    parret.getChildren().remove(tmp);
                    cc.lineList.remove(tmp);
                    if(cc.lineList.isEmpty()){
                        cc.rightButton.setVisible(true);
                    }
                }
            }
        });
        cc.menu_down.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer span = GridPane.getRowSpan(cc);
                if(span == null) span = 1;
                GridPane.setRowSpan(cc,  span + 1);
                cc.setPositionEnd(cc.getPositionEnd()+1);
                if(cc.cabel !=null){
                    cc.genNewLine();
                    parret.add(cc.cabel, colIndex, GridPane.getRowIndex(cc)+1);
                    cc.lineList.add(cc.cabel);
                }
            }
        });
        cc.menu_revdown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer span = GridPane.getRowSpan(cc);
                if(span == null || span < 2) span = 2;
                GridPane.setRowSpan(cc,  span - 1);
                cc.setPositionEnd(cc.getPositionEnd()-1);
                if(cc.cabel != null){
                    Line tmp = cc.returnLine(GridPane.getRowIndex(cc)+1);   //null?
                    parret.getChildren().remove(tmp);
                    cc.lineList.remove(tmp);
                    if(cc.lineList.isEmpty()){
                        cc.rightButton.setVisible(true);
                    }
                }
            }
        });
        cc.menu_remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parret.getChildren().remove(cc.cabel);
                parret.getChildren().remove(cc);
            }
        });
        cc.remove_line.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cc.removeLine(parret);
            }
        });
        cc.rightButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!cc.checkNextBlock(parret, rowIndex, colIndex+1))  return;
                cc.remove_line.setDisable(false);
                cc.genNewLine();
                cc.rightButton.setVisible(false);

                cc.info = new Tooltip();
                cc.info.setText("Hello");   //vypis aktualnich hodnot
                Tooltip.install(cc.cabel, cc.info);

                parret.add(cc.cabel, GridPane.getColumnIndex(cc), GridPane.getRowIndex(cc));
                cc.lineList.add(cc.cabel);
            }
        });
        cc.block_add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //back-end block for add
                cc.centerButton.setText("+");
            }
        });
        cc.block_sub.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //back-end block for add
                cc.centerButton.setText("-");
            }
        });
        cc.block_mul.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //back-end block for add
                cc.centerButton.setText("*");
            }
        });
        cc.block_div.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //back-end block for add
                cc.centerButton.setText("/");
            }
        });
        cc.block_constant.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //back-end block for add
                cc.centerButton.setText("c");
            }
        });
        cc.block_switch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //back-end block for add
                cc.centerButton.setText("<>");
            }
        });
    }

    //------------
    @FXML
    private void ExitProgram(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Do you want to exit SchemeBuilder?");
        alert.setContentText("Choose your option");

        ButtonType buttonExit = new ButtonType("Exit");
        ButtonType buttonSaveExit = new ButtonType("Save & Exit");
        ButtonType buttonNo = new ButtonType("Nevermind");

        alert.getButtonTypes().setAll(buttonExit, buttonSaveExit, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonExit){
            Platform.exit();
        }
        else if(result.get() == buttonSaveExit){
            //Save scheme
            //Platform.exit();
        }
    }

    private void AddRow(GridPane gridPane) {
        RowConstraints rc = new RowConstraints();
        rc.setPrefHeight(100.0);
        rc.setVgrow(Priority.SOMETIMES);
        gridPane.getRowConstraints().add(rc);

        Integer rows = gridPane.getRowConstraints().size();
        Integer cols = gridPane.getColumnConstraints().size();
        if(rows>0) {
            for (Integer i = 0; i < cols; i++) {
                AddPaneIntoCell(gridPane, i, rows-1);
            }
        }
    }

    private void AddCol(GridPane gridPane) {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPrefWidth(100.0);
        cc.setHgrow(Priority.SOMETIMES);
        gridPane.getColumnConstraints().add(cc);

        Integer rows = gridPane.getRowConstraints().size();
        Integer cols = gridPane.getColumnConstraints().size();
        if(cols>0) {
            for (Integer i = 0; i < rows; i++) {
                AddPaneIntoCell(gridPane, cols-1, i);
            }
        }
    }

    private void AddPaneIntoCell(GridPane gridPane, Integer colIndex, Integer rowIndex) {
        Pane pane = new Pane();
        gridPane.add(pane, colIndex, rowIndex);
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                PositionCell_Click(event);
            }
        });
        pane.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                Node source = (Node)event.getSource() ;
                Integer colIndex = GridPane.getColumnIndex(source);
                Integer rowIndex = GridPane.getRowIndex(source);
                AddCustomControl(gridPane, colIndex, rowIndex );
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(Integer i = 0; i<5; i++) {
            AddCol(gridPane);
        }
        for(Integer i = 0; i<5; i++) {
            AddRow(gridPane);
        }
    }
}
