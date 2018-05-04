package main.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.util.converter.NumberStringConverter;
import main.blocks.Block;
import main.ui.component.BlockControl;

import java.net.URL;
import java.util.ResourceBundle;

public class SampleController implements Initializable {

    @FXML private ScrollPane scrollPane;
    @FXML private GridPane gridPane;
    @FXML private ScrollBar cellSizeScrollBar;
    @FXML private Label cellSizeLabel;

    protected DoubleProperty CellSizeProperty = new SimpleDoubleProperty();


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
                GridPane.setRowIndex(cc, GridPane.getRowIndex(cc)-1);
                cc.setPositionStart(cc.getPositionStart()-1);
                GridPane.setRowSpan(cc,  span + 1);
            }
        });
        cc.menu_revup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer span = GridPane.getRowSpan(cc);
                if(span == null || span < 2) return;
                //if(span == null || span < 2) span = 2;
                GridPane.setRowIndex(cc, GridPane.getRowIndex(cc)+1);
                cc.setPositionStart(cc.getPositionStart()+1);
                GridPane.setRowSpan(cc,  span - 1);
            }
        });
        //cc.menu_revdown.disableProperty().bind( GridPane.getRowSpan(cc) );
        cc.menu_down.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer span = GridPane.getRowSpan(cc);
                if(span == null) span = 1;
                GridPane.setRowSpan(cc,  span + 1);
                cc.setPositionEnd(cc.getPositionEnd()+1);
            }
        });
        cc.menu_revdown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer span = GridPane.getRowSpan(cc);
                if(span == null || span < 2) return;
                //if(span == null || span < 2) span = 2;
                GridPane.setRowSpan(cc,  span - 1);
                cc.setPositionEnd(cc.getPositionEnd()-1);
            }
        });
        cc.menu_remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parret.getChildren().remove(cc);
            }
        });
    }

    //------------

    private void AddRow(GridPane gridPane) {
        RowConstraints rc = new RowConstraints();
        rc.setPrefHeight(100.0);
        rc.prefHeightProperty().bind(CellSizeProperty);
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
        cc.prefWidthProperty().bind(CellSizeProperty);
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
      // Velikost bunky pro blok
        CellSizeProperty.bind(cellSizeScrollBar.valueProperty());
        cellSizeScrollBar.valueProperty().setValue(100);
        Bindings.bindBidirectional(cellSizeLabel.textProperty(), CellSizeProperty, new NumberStringConverter());

        // vytvoreni mrizky 5x5 pro bloky
        for(Integer i = 0; i<5; i++) {
            AddCol(gridPane);
        }
        for(Integer i = 0; i<5; i++) {
            AddRow(gridPane);
        }
    }
}
