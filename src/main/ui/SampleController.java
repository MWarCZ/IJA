package main.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import main.blocks.Block;
import main.blocks.BlockAddSub;
import main.blocks.BlockConstant;
import main.ui.component.BlockControl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class SampleController implements Initializable {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollBar cellSizeScrollBar;
    @FXML
    private Label cellSizeLabel;

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
        Node source = (Node) e.getSource();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        System.out.println(String.format("Cell: [%d, %d]", colIndex, rowIndex));
        //System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
    }

    private BlockControl AddCustomControl(GridPane parret, Integer colIndex, Integer rowIndex, Block block) {
        BlockControl cc = new BlockControl();
        //Block block = new Block();
        block.SetPosition(rowIndex, rowIndex);
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
                if (span == null) span = 1;
                GridPane.setRowIndex(cc, GridPane.getRowIndex(cc) - 1);
                cc.setPositionStart(cc.getPositionStart() - 1);
                GridPane.setRowSpan(cc, span + 1);
            }
        });
        cc.menu_revup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer span = GridPane.getRowSpan(cc);
                if (span == null || span < 2) return;
                //if(span == null || span < 2) span = 2;
                GridPane.setRowIndex(cc, GridPane.getRowIndex(cc) + 1);
                cc.setPositionStart(cc.getPositionStart() + 1);
                GridPane.setRowSpan(cc, span - 1);
            }
        });
        //cc.menu_revdown.disableProperty().bind( GridPane.getRowSpan(cc) );
        cc.menu_down.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer span = GridPane.getRowSpan(cc);
                if (span == null) span = 1;
                GridPane.setRowSpan(cc, span + 1);
                cc.setPositionEnd(cc.getPositionEnd() + 1);
            }
        });
        cc.menu_revdown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer span = GridPane.getRowSpan(cc);
                if (span == null || span < 2) return;
                //if(span == null || span < 2) span = 2;
                GridPane.setRowSpan(cc, span - 1);
                cc.setPositionEnd(cc.getPositionEnd() - 1);
            }
        });
        cc.menu_remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parret.getChildren().remove(cc);
            }
        });

        return cc;
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
        if (rows > 0) {
            for (Integer i = 0; i < cols; i++) {
                AddPaneIntoCell(gridPane, i, rows - 1);
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
        if (cols > 0) {
            for (Integer i = 0; i < rows; i++) {
                AddPaneIntoCell(gridPane, cols - 1, i);
            }
        }
    }

    private void AddPaneIntoCell(GridPane gridPane, Integer colIndex, Integer rowIndex) {
        // Vytvoreni prvniho panelu v bunce.
        Pane pane = new Pane();
        gridPane.add(pane, colIndex, rowIndex);

        // Vytvareni kotextoveho menu daneho panelu
        ContextMenu menu = new ContextMenu();

        MenuItem itemBlockConstant = new MenuItem();
        itemBlockConstant.setText("new Constant Block");
        menu.getItems().add(itemBlockConstant);

        MenuItem itemBlockAddSub = new MenuItem();
        itemBlockAddSub.setText("new AddSub Block");
        menu.getItems().add(itemBlockAddSub);

        MenuItem itemBlockMulDiv = new MenuItem();
        itemBlockMulDiv.setText("new MulDiv Block");
        menu.getItems().add(itemBlockMulDiv);

        MenuItem itemBlockSwitch = new MenuItem();
        itemBlockSwitch.setText("new Switch Block");
        menu.getItems().add(itemBlockSwitch);

        pane.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                menu.show(pane, event.getScreenX(), event.getScreenY());
            }
        });

        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                PositionCell_Click(event);
            }
        });
//        pane.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
//            @Override
//            public void handle(ContextMenuEvent event) {
//                Node source = (Node)event.getSource() ;
//                Integer colIndex = GridPane.getColumnIndex(source);
//                Integer rowIndex = GridPane.getRowIndex(source);
//                AddCustomControl(gridPane, colIndex, rowIndex, new Block() );
//            }
//        });

        itemBlockConstant.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BlockConstant block = new BlockConstant();
                block.SetConstValue(10.0);

                //region Dialog ktery prijma hodnoty typu Double
                TextInputDialog tid = new TextInputDialog("1.0");
                tid.setTitle("Double");
                tid.setHeaderText("Zadejte hodnotu bloku:");

                Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

                UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>() {
                    @Override
                    public TextFormatter.Change apply(TextFormatter.Change change) {
                        String text = change.getControlNewText();
                        if (validEditingState.matcher(text).matches()) {
                            return change ;
                        } else {
                            return null ;
                        }
                    }
                };
                StringConverter<Double> converter = new StringConverter<Double>() {
                    @Override
                    public Double fromString(String s) {
                        if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                            return 0.0 ;
                        } else {
                            return Double.valueOf(s);
                        }
                    }
                    @Override
                    public String toString(Double d) {
                        return d.toString();
                    }
                };

                TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.0, filter);
                tid.getEditor().setTextFormatter(textFormatter);

                //endregion

                Optional<String> result = tid.showAndWait();
                if (result.isPresent()) {
                    // ok
                    Double value = Double.parseDouble(tid.getEditor().getText());
                    block.SetConstValue(value);
                    Integer colIndex = GridPane.getColumnIndex(pane);
                    Integer rowIndex = GridPane.getRowIndex(pane);
                    BlockControl cc = AddCustomControl(gridPane, colIndex, rowIndex, block);

                    Bindings.bindBidirectional(cc.textProperty,
                        block.constValueProperty, new NumberStringConverter());

                    System.out.println(String.format("Create new BlockConstant."));
                } else {
                    // cancel
                    System.out.println(String.format("Canceled new BlockConstant."));
                }

                System.out.println(String.format("Cell: [%d, %d]", colIndex, rowIndex));
            }
        });

        itemBlockAddSub.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BlockAddSub block = new BlockAddSub();
                Integer colIndex = GridPane.getColumnIndex(pane);
                Integer rowIndex = GridPane.getRowIndex(pane);
                BlockControl cc = AddCustomControl(gridPane, colIndex, rowIndex, block);

                System.out.println(String.format("Cell: [%d, %d]", colIndex, rowIndex));
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
        for (Integer i = 0; i < 5; i++) {
            AddCol(gridPane);
        }
        for (Integer i = 0; i < 5; i++) {
            AddRow(gridPane);
        }

        ArrayList<String> texty = new ArrayList<>();
        ObservableList observeTexty;

        texty.add("ahoj");
        texty.add("caw");
        texty.add("ahojky");

        observeTexty = FXCollections.observableArrayList(texty);

        observeTexty.add("hello");
        observeTexty.add("hi");

    }
}
