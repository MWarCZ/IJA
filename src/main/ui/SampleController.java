package main.ui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import main.blocks.*;
import main.manipulator.IOperation;
import main.project.SaveLoader;
import main.project.Schema;
import main.ui.component.BlockControl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class SampleController implements Initializable {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private BorderPane firstBorder;
    @FXML
    private GridPane gridPane;
    @FXML
    private MenuItem createScheme;
    @FXML
    private MenuItem openScheme;
    @FXML
    private MenuItem saveScheme;
    @FXML
    private MenuItem exitScheme;
    @FXML
    private MenuItem runScheme;
    @FXML
    private MenuItem resetScheme;
    @FXML
    private MenuItem debugScheme;
    @FXML
    private MenuItem stopDebug;

    @FXML
    private ScrollBar cellSizeScrollBar;
    @FXML
    private Label cellSizeLabel;

    private DoubleProperty CellSizeProperty = new SimpleDoubleProperty();

    private File file;
    private FileChooser fileChooser = new FileChooser();

    private SaveLoader saveloader = new SaveLoader();
    private Schema schema;

    @FXML
    public void AddRow_Click(MouseEvent mouseEvent) {
        AddRow(gridPane);
    }

    @FXML
    public void AddCol_Click(MouseEvent mouseEvent) {
        AddCol(gridPane);
    }
    // ---------------

    private void ShowErrorDialog(String headerTest, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(headerTest);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    /**
     * Vypsani pozice Radku a Sloupecku bunky do konzole.
     *
     * @param e Parametry udalosti mysi.
     */
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
        cc.setBlock(block);

        gridPane.add(cc, colIndex, rowIndex, 1, block.GetSize());
        cc.toFront();

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
                if (cc.getPositionStart() == 0) return;
                GridPane.setRowIndex(cc, GridPane.getRowIndex(cc) - 1);
                cc.setPositionStart(cc.getPositionStart() - 1);
                GridPane.setRowSpan(cc, span + 1);
                /*adding cabel to end of list*/
                if (cc.cabel != null) {
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
                if (span == null || span < 2) return;
                GridPane.setRowIndex(cc, GridPane.getRowIndex(cc) + 1);
                cc.setPositionStart(cc.getPositionStart() + 1);
                GridPane.setRowSpan(cc, span - 1);
                if (cc.cabel != null) {
                    Line tmp = cc.returnLine(GridPane.getRowIndex(cc) - 1);   //Null??
                    parret.getChildren().remove(tmp);
                    cc.lineList.remove(tmp);
                    if (cc.lineList.isEmpty()) {
                        cc.rightButton.setVisible(true);
                    }
                }
            }
        });
        cc.menu_down.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer span = GridPane.getRowSpan(cc);
                if (span == null) span = 1;
                GridPane.setRowSpan(cc, span + 1);
                cc.setPositionEnd(cc.getPositionEnd() + 1);
                if (cc.cabel != null) {
                    cc.genNewLine();
                    parret.add(cc.cabel, colIndex, GridPane.getRowIndex(cc) + 1);
                    cc.lineList.add(cc.cabel);
                }
            }
        });
        cc.menu_revdown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer span = GridPane.getRowSpan(cc);
                if (span == null || span < 2) return;
                GridPane.setRowSpan(cc, span - 1);
                cc.setPositionEnd(cc.getPositionEnd() - 1);
                if (cc.cabel != null) {
                    Line tmp = cc.returnLine(GridPane.getRowIndex(cc) + 1);   //null?
                    parret.getChildren().remove(tmp);
                    cc.lineList.remove(tmp);
                    if (cc.lineList.isEmpty()) {
                        cc.rightButton.setVisible(true);
                    }
                }
            }
        });
        cc.menu_remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                schema.RemoveBlock(GridPane.getColumnIndex(cc), cc.block);
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
                if (!cc.checkNextBlock(parret, rowIndex, colIndex + 1)) return;
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

        return cc;
    }

    //------------
    @FXML
    private void ExitProgram() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Do you want to exit SchemeBuilder?");
        alert.setContentText("Choose your option");

        ButtonType buttonExit = new ButtonType("Exit");
        ButtonType buttonSaveExit = new ButtonType("Save & Exit");
        ButtonType buttonNo = new ButtonType("Nevermind");

        alert.getButtonTypes().setAll(buttonExit, buttonSaveExit, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonExit) {
            Platform.exit();
        } else if (result.get() == buttonSaveExit) {
            SaveProgram();
            Platform.exit();
        }
    }

    @FXML
    private void SaveProgram() {
        //Ulozeni programu!!
        fileChooser.setTitle("Save schema to file");

        // Pokud ve schematu je jeho umisteni, tak ho nacte.
        if(schema.GetPath() != null && schema.GetPath() != ""){
            File tmpFile = new File(schema.GetPath());
            fileChooser.setInitialFileName(tmpFile.getName());

            tmpFile = new File(tmpFile.getParent());
            fileChooser.setInitialDirectory(tmpFile);
        }
        else {
            fileChooser.setInitialFileName(schema.GetName());
        }

        file = fileChooser.showSaveDialog(gridPane.getScene().getWindow());

        if(file != null) {
            try {
                System.out.println(String.format("Start saving file '%s'", file.getPath()));

                saveloader.WriteXML3(file.getPath(), schema);
                schema.SetPath(file.getPath());
                System.out.println(String.format("File '%s' is saved.", file.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
                ShowErrorDialog("Problem with save file.", "Ooops, there was an error! So sorry.");
            }
        }
    }

    @FXML
    private void LoadFile() {
        fileChooser.setTitle("Open XML file");
        file = fileChooser.showOpenDialog((Stage) gridPane.getScene().getWindow());

        if(file != null) {
            try {
                System.out.println(String.format("Start loading file '%s'", file.getPath()));

                schema = saveloader.ReadXML3(file.getPath());
                System.out.println(String.format("File '%s' is loaded.", file.getPath()));

                ReDrawSchema(gridPane, schema);

            } catch (IOException e) {
                e.printStackTrace();
                ShowErrorDialog("Problem with open file.", "Ooops, there was an error! So sorry.");
            }
        }

    }

    // Znovuvytvori form ze schematu
    private void ReDrawSchema(GridPane gridPane, Schema schema){
        // Vycisteni formu - gridu
        Node settingNode = gridPane.getChildren().get(0);
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getChildren().add(settingNode);

        // Znovunaplneni formu (gridu) ze schematu
        Integer maxRowIndex = 0;
        Integer rowIndexNow = 0;
        for(Integer i=0; i<schema.GetCountBlocksColumns(); i++) {
            AddCol(gridPane);
            for(IOperation operation :schema.GetBlocksColumn(i)) {
                // Zjistovani poctu radku
                if(operation instanceof Block) {
                    Integer tmpIndex = ((Block)operation).GetPositionEnd();
                    if(tmpIndex > maxRowIndex) maxRowIndex = tmpIndex;

                }
                // Pridani bloku do formu
                if(operation instanceof BlockConstant) {
                    BlockConstant block = (BlockConstant)operation;
                    AddBlockConstant(i, block.GetPositionStart(), block );
                }
                else if(operation instanceof BlockAddSub) {
                    BlockAddSub block = (BlockAddSub)operation;
                    AddBlockAddSub(i, block.GetPositionStart(), block );
                }
                else if(operation instanceof BlockMulDiv) {
                    BlockMulDiv block = (BlockMulDiv)operation;
                    AddBlockMulDiv(i, block.GetPositionStart(), block );
                }
                else if(operation instanceof BlockSwitch) {
                    BlockSwitch block = (BlockSwitch)operation;
                    AddBlockSwitch(i, block.GetPositionStart(), block );
                }
                // \todo else ...
            }
        }
        // Doplneni radku
        for (Integer i = 0; i <= maxRowIndex; i++) {
            AddRow(gridPane);
        }
    }

    @FXML
    private void Reload() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New scheme");
        alert.setHeaderText("Do you want new scheme? This one is going to be lost.");
        alert.setContentText("Choose your option");

        ButtonType buttonOk = new ButtonType("Create");
        ButtonType buttonSave = new ButtonType("Save & Create");
        ButtonType buttonNo = new ButtonType("Nevermind");

        alert.getButtonTypes().setAll(buttonOk, buttonSave, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonNo) {
            //Reload aplikace
        } else if (result.get() == buttonSave) {
            //SaveProgram();
            //Reload aplikace
        }
        //??
    }

    @FXML
    private void RunScheme() {
        //vykonání bloků
        //zvýraznění bloku při jeho výpočtu cc.centerButton.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3px;")
    }

    @FXML
    private void ResetScheme() {
        //všechny výpočty se vrátí do původního stavu, tzn. všechny bloky nastavit na původní hodnoty
    }

    @FXML
    private void DebugScheme() {
        //Přidání buttonu pro krokování
        Button ccNext = new Button("Next");
        firstBorder.setLeft(ccNext);
        //ccNext.setOnMouseClicked((event) -> System.out.println("HELO"));
        //při kliknutí zvýraznění bloku + nad blokem dialog s hodnotamy před výpočtem a po výpočtu cc.centerButton.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3px;")
    }

    @FXML
    private void StopDebug() {
        if (firstBorder.getLeft() == null) return;
        firstBorder.setLeft(null);
    }

    //----------------------------

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
        Node node = gridPane.getChildren().get(0);
        pane.toBack();
        node.toBack();

        pane.setStyle("-fx-background-color: #00b8ff; -fx-opacity: 0.5;");

        // Vytvareni kotextoveho menu daneho panelu
        ContextMenu menu = new ContextMenu();

        // Munu obsahuje rozne varianty, ktere bloky pridat do schematu
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

        // Pridavani bloku do schema a do formu
        itemBlockConstant.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

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
                            return change;
                        } else {
                            return null;
                        }
                    }
                };
                StringConverter<Double> converter = new StringConverter<Double>() {
                    @Override
                    public Double fromString(String s) {
                        if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                            return 0.0;
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

                    Integer colIndex = GridPane.getColumnIndex(pane);
                    Integer rowIndex = GridPane.getRowIndex(pane);

                    AddNewBlockConstant(colIndex, rowIndex, value);

                } else {
                    // cancel
                    System.out.println("Canceled BlockConstant.");
                }

                System.out.println(String.format("Cell: [%d, %d]", colIndex, rowIndex));
            }
        });

        itemBlockAddSub.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer colIndex = GridPane.getColumnIndex(pane);
                Integer rowIndex = GridPane.getRowIndex(pane);

                AddNewBlockAddSub(colIndex, rowIndex);

                System.out.println(String.format("Cell: [%d, %d]", colIndex, rowIndex));
            }
        });

        itemBlockMulDiv.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer colIndex = GridPane.getColumnIndex(pane);
                Integer rowIndex = GridPane.getRowIndex(pane);

                AddNewBlockMulDiv(colIndex, rowIndex);

                System.out.println(String.format("Cell: [%d, %d]", colIndex, rowIndex));
            }
        });

        itemBlockSwitch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer colIndex = GridPane.getColumnIndex(pane);
                Integer rowIndex = GridPane.getRowIndex(pane);

                AddNewBlockSwitch(colIndex, rowIndex);

                System.out.println(String.format("Cell: [%d, %d]", colIndex, rowIndex));
            }
        });
    }


    // Prida blok do formu
    private void AddBlockConstant(Integer colIndex, Integer rowIndex, BlockConstant block) {
        BlockControl cc = AddCustomControl(gridPane, colIndex, rowIndex, block);
        Bindings.bindBidirectional(cc.textProperty,
            block.constValueProperty, new NumberStringConverter());

        System.out.println("Create new BlockConstant.");
    }
    // Vytvori blok a prida do formu
    private void AddNewBlockConstant(Integer colIndex, Integer rowIndex, Double value) {
        BlockConstant block = new BlockConstant();
        block.SetPosition(rowIndex, rowIndex);
        block.SetConstValue(value);
        schema.AddBlock(colIndex, block);

        this.AddBlockConstant(colIndex, rowIndex, block);
    }

    // Prida blok do formu
    private void AddBlockAddSub(Integer colIndex, Integer rowIndex, BlockAddSub block) {
        BlockControl cc = AddCustomControl(gridPane, colIndex, rowIndex, block);
        //cc.textProperty.setValue("0[+-]X");
        cc.textProperty.setValue("+-");
        System.out.println("Create new BlockAddSub.");
    }
    // Vytvori blok a prida do formu
    private void AddNewBlockAddSub(Integer colIndex, Integer rowIndex) {
        BlockAddSub block = new BlockAddSub();
        block.SetPosition(rowIndex, rowIndex);
        schema.AddBlock(colIndex, block);

        this.AddBlockAddSub(colIndex, rowIndex, block);
    }

    // Prida blok do formu
    private void AddBlockMulDiv(Integer colIndex, Integer rowIndex, BlockMulDiv block) {
        BlockControl cc = AddCustomControl(gridPane, colIndex, rowIndex, block);
        //cc.textProperty.setValue("0[*/]X");
        cc.textProperty.setValue("*/");
        System.out.println("Create new BlockMulDiv.");
    }
    // Vytvori blok a prida do formu
    private void AddNewBlockMulDiv(Integer colIndex, Integer rowIndex) {
        BlockMulDiv block = new BlockMulDiv();
        block.SetPosition(rowIndex, rowIndex);
        schema.AddBlock(colIndex, block);

        this.AddBlockMulDiv(colIndex, rowIndex, block);
    }

    // Prida blok do formu
    private void AddBlockSwitch(Integer colIndex, Integer rowIndex, BlockSwitch block) {
        BlockControl cc = AddCustomControl(gridPane, colIndex, rowIndex, block);
        cc.textProperty.setValue("Switch");
        System.out.println("Create new BlockSwitch.");
    }
    // Vytvori blok a prida do formu
    private void AddNewBlockSwitch(Integer colIndex, Integer rowIndex) {
        BlockSwitch block = new BlockSwitch();
        block.SetPosition(rowIndex, rowIndex);
        schema.AddBlock(colIndex, block);

        this.AddBlockSwitch(colIndex, rowIndex, block);
    }

    //----------------------------

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.schema = new Schema("noname");

        // Nastaveni filtru pro FileChooser dialog.
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("XIJA", "*.xija"),
            new FileChooser.ExtensionFilter("XML", "*.xml"),
            new FileChooser.ExtensionFilter("All file", "*.*")
        );

        // Velikost bunky pro blok
        CellSizeProperty.bind(cellSizeScrollBar.valueProperty());
        cellSizeScrollBar.valueProperty().setValue(100);
        Bindings.bindBidirectional(cellSizeLabel.textProperty(), CellSizeProperty, new NumberStringConverter());

        for (Integer i = 0; i < 5; i++) {
            AddCol(gridPane);
        }
        for (Integer i = 0; i < 5; i++) {
            AddRow(gridPane);
        }
    }
}
