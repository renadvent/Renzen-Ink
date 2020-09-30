package io.renzen.ink.Views;

import io.renzen.ink.CommandObjectsDomain.ActionPanelAccountInfoCO;
import io.renzen.ink.Controllers.ActionPanelController;
import io.renzen.ink.Controllers.CanvasPanelController;
import io.renzen.ink.InkClass;
import io.renzen.ink.Links.ActionPanelControllerToCanvasPanelViewLink;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.io.File;

public class JavaFXPanel {

    private static final Style STYLE = Style.DARK;
    final SplitPane mainSplitPane = new SplitPane();
    final SplitPane horizontalSplitPane = new SplitPane();
    final ScrollPane scrollPane = new ScrollPane();
    final BorderPane borderPane = new BorderPane();
    final BorderPane root = new BorderPane(borderPane);
    final Scene scene = new Scene(root, 1000, 1000);
    final VBox fileBox = new VBox();
    final VBox menuPane = new VBox();
    final VBox accountBox = new VBox();
    final VBox profileBox = new VBox();
    final VBox uploadBox = new VBox();
    final VBox canvasBox = new VBox();
    final VBox toolSelectionBox = new VBox();
    final VBox casterToolOptionsBox = new VBox();
    final VBox brushBox = new VBox();
    final TitledPane filePane = new TitledPane("File", fileBox);
    final TitledPane loginPane = new TitledPane("Login", profileBox);
    final TitledPane profilePane = new TitledPane("Renzen.io Profile", uploadBox);
    final TitledPane canvasPane = new TitledPane("Canvas", canvasBox);
    final TitledPane toolSelectionPane = new TitledPane("Tool Selection", toolSelectionBox);
    final TitledPane toolOptionsPane = new TitledPane("Caster Options", casterToolOptionsBox);
    final TitledPane brushOptionsPane = new TitledPane("Brush Options", brushBox);
    final Pane fxCanvasPane = new Pane();
    final Pane oldActionPane = new Pane();
    final SwingNode FXCanvasNode = new SwingNode();
    final SwingNode OldToolNode = new SwingNode();
    final Button openFileButton = new Button("Open File");
    final Button saveFileButton = new Button("Save File");
    final Button openWebsiteButton = new Button("View Profile Online");
    final Button uploadOnlineButton = new Button("Upload Screenshot to Profile");
    final Button loginButton = new Button("Login");
    final Button flipCasterButton = new Button("Flip Caster");
    final Button createNewCasterButton = new Button("Create New Caster");
    final Button deleteSelectedCasterButton = new Button ("Delete Caster");
    final Accordion uploadAcc = new Accordion(profilePane);
    final Accordion fileAcc = new Accordion(filePane);
    final Accordion loginAcc = new Accordion();
    final Accordion canvasAcc = new Accordion((canvasPane));
    final Accordion toolSelectionAcc = new Accordion(toolSelectionPane);
    final Accordion toolOptionsAcc = new Accordion(toolOptionsPane);
    final TextField usernameField = new TextField();
    final PasswordField passwordField = new PasswordField();
    final RadioButton casterToolButton = new RadioButton("Caster");
    final RadioButton brushToolButton = new RadioButton("Brush");
    final ToggleGroup toolGroup = new ToggleGroup();
    final Label toolOptionsLabel = new Label("Tool Options");
    final Label raysLabel = new Label("Rays");
    final Label toleranceLabel = new Label("Tolerance");
    final Label brushSizeLabel = new Label("Brush Size");
    final Label densityLabel = new Label("Brush Density");
    final Slider raysSlider = new Slider();
    final Slider toleranceSlider = new Slider();
    final Slider brushSizeSlider = new Slider();
    final Slider densitySlider = new Slider();
    final CheckBox castThroughCheckbox = new CheckBox("Cast Through");
    final CheckBox showCasterCheckbox = new CheckBox("Show Caster");
    final CheckBox showStrokesCheckbox = new CheckBox("Show Strokes");
    final CheckBox showLoadedImageCheckbox = new CheckBox("Show Loaded Image");
    final Separator casterToolSeparatorTop = new Separator();
    final Separator brushSeparator = new Separator();
    final Separator casterToolSeparatorBottom = new Separator();
    final ColorPicker colorPicker = new ColorPicker();
    final FileChooser fileChooser = new FileChooser();
    final int B_WIDTH = 300;
    ConfigurableApplicationContext springContext = null;
    ActionPanelController actionPanelController = null;
    CanvasPanelController canvasPanelController = null;
    Stage stage = null;
    int createCasterCounter =0;

    ActionPanelControllerToCanvasPanelViewLink actionPanelControllerToCanvasPanelViewLink = null;


    public JavaFXPanel(Stage primaryStage, ConfigurableApplicationContext springContext) {
        System.setProperty("prism.lcdtext", "false");

        this.stage = primaryStage;
        this.springContext = springContext;
        this.actionPanelController = springContext.getBean(ActionPanelController.class);
        this.canvasPanelController = springContext.getBean(CanvasPanelController.class);
        this.actionPanelControllerToCanvasPanelViewLink=springContext.getBean(
                ActionPanelControllerToCanvasPanelViewLink.class
        );

        buildMenu();
        setUpJPanelCanvasInJavaFX();
        //setUpOldToolPanelInJavaFX();
        linkButtonsToActionController();

        stage.setTitle("Renzen Ink");
        stage.setScene(scene);
        stage.show();
    }


    final ListView<Button> list = new ListView<>();
    //final ObservableList<String> items =

//            FXCollections.observableArrayList (
//            "Single", "Double", "Suite", "Family App");
//list.setItems(items);

    private void linkButtonsToActionController() {

        openWebsiteButton.setOnMouseClicked(mouseEvent -> actionPanelController.openRenzen());
        raysSlider.valueProperty().addListener((observableValue, number, t1) -> actionPanelController.updateNumberOfRays(t1.intValue()));//(controller::updateNumberOfRays);
        toleranceSlider.valueProperty().addListener((observableValue, number, t1) -> actionPanelController.updateTolerance(t1.intValue()));
        castThroughCheckbox.selectedProperty().addListener((observableValue, number, t1) -> actionPanelController.updateNumberOfStrikes(t1.booleanValue()));
        flipCasterButton.setOnMouseClicked(mouseEvent -> actionPanelController.flipSelectedCaster(true));

        //TODO working on. need to set in a service
        colorPicker.valueProperty().addListener((observableValue, number, t1)->{
            canvasPanelController.setCasterColor(t1);
            //messy
            springContext.getBean(CanvasPanel.class).repaint();
        });

        showLoadedImageCheckbox.setOnMouseClicked(e->{
            actionPanelControllerToCanvasPanelViewLink.toggleShowBackground();
        });

        uploadOnlineButton.setOnMouseClicked(mouseEvent -> {

            var link = actionPanelControllerToCanvasPanelViewLink.saveCanvasToMongoRepository();

            var button = new Button(link);
            button.setOnMouseClicked(e -> {
                actionPanelController.viewImageOnWeb(link);
            });

            list.getItems().add(button);
        });

        openFileButton.setOnMouseClicked(e->{
            var file = fileChooser.showOpenDialog(stage);

            canvasPanelController.openFile(file);

            //shouldn't be done here like this
            springContext.getBean(CanvasPanel.class).repaint();

        });

        saveFileButton.setOnMouseClicked(e->{
            //Show save file dialog
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                actionPanelControllerToCanvasPanelViewLink.saveFile(file);
                //save file
                //saveTextToFile(sampleText, file);
            }
        });

        loginButton.setOnMouseClicked(e->{
            updateAccountSectionWithLogin(actionPanelController.login(usernameField.getText(),passwordField.getText()));
        });

        deleteSelectedCasterButton.setOnMouseClicked(e->{

        });

        createNewCasterButton.setOnMouseClicked(e -> {
            var button = new Button("New Caster " + createCasterCounter++);
            var actionPanelCO = actionPanelController.createCaster(e, button.getText());
            list.getItems().add(button);
            button.setOnMouseClicked(x -> {
                actionPanelController.selectCaster(button.getText());
                UpdateActionPanelToSelectedCaster();
            });
        });
    }

    void buildMenu(){

        var F_SIZE = "-fx-font-size: 20;";

        fileChooser.setTitle("Open File");
        colorPicker.setValue(new Color(0,0,0,1));
        canvasPanelController.setCasterColor(colorPicker.getValue());


        FileChooser.ExtensionFilter extFilter = new FileChooser
                .ExtensionFilter("Image Files", "*.png","*.jpg","*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);


        toleranceSlider.setMax(255);
        raysSlider.setMax(200);

        mainSplitPane.setOrientation(Orientation.VERTICAL);
        mainSplitPane.setDividerPosition(0, 0.7);

        //horizontalSplitPane.setDividerPosition(0, 0.3);

        borderPane.getStyleClass().add(JMetroStyleClass.BACKGROUND);

        openFileButton.setStyle(F_SIZE);
        saveFileButton.setStyle(F_SIZE);

        loginButton.setPrefWidth(B_WIDTH);
        openWebsiteButton.setPrefWidth(B_WIDTH);
        openFileButton.setPrefWidth(B_WIDTH);
        saveFileButton.setPrefWidth(B_WIDTH);
        uploadOnlineButton.setPrefWidth(B_WIDTH);
        casterToolButton.setPrefWidth(B_WIDTH);
        brushToolButton.setPrefWidth(B_WIDTH);

        casterToolOptionsBox.setSpacing(12);

        fileBox.getChildren().addAll(openFileButton, saveFileButton);
        profileBox.getChildren().addAll(usernameField, passwordField, loginButton);
        loginAcc.getPanes().add(loginPane);
        uploadBox.getChildren().addAll(loginAcc, openWebsiteButton, uploadOnlineButton);
        accountBox.getChildren().addAll(uploadAcc);

        showLoadedImageCheckbox.setSelected(true);

        canvasBox.getChildren().add(showLoadedImageCheckbox);
        accountBox.getChildren().add(canvasAcc);
        toolGroup.getToggles().addAll(casterToolButton, brushToolButton);
        toolSelectionBox.getChildren().addAll(casterToolButton, brushToolButton);
        casterToolOptionsBox.getChildren().addAll(
                //casterToolSeparatorTop,
                createNewCasterButton,
                deleteSelectedCasterButton,
                new Separator(),
                toolOptionsLabel,
                raysLabel, raysSlider,
                toleranceLabel, toleranceSlider,
                castThroughCheckbox,
                showCasterCheckbox,
                showStrokesCheckbox,
                flipCasterButton
                //casterToolSeparatorBottom
        );
        brushBox.getChildren().addAll(brushSizeLabel, brushSizeSlider,
                densityLabel, densitySlider, brushSeparator, colorPicker);
        menuPane.getChildren().addAll(
                fileAcc,
                accountBox,
                //toolSelectionAcc,
                toolOptionsAcc,
                brushOptionsPane,list);


        borderPane.setLeft(menuPane);
        root.getStyleClass().add(JMetroStyleClass.BACKGROUND);

        new JMetro(scene, STYLE);
    }

    private void setUpJPanelCanvasInJavaFX() {
        SwingUtilities.invokeLater(() ->
                FXCanvasNode.setContent(springContext.getBean(InkClass.class).canvasPanel));
        fxCanvasPane.getChildren().add(FXCanvasNode);
        borderPane.setCenter(fxCanvasPane);
    }

    private void UpdateActionPanelToSelectedCaster(){



    }



    private void updateAccountSectionWithLogin(ActionPanelAccountInfoCO infoCO) {
        profileBox.getChildren().add(0, new Label("Welcome " + infoCO.getName()+"!"));
    }

}
