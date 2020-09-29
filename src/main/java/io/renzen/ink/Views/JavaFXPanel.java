package io.renzen.ink.Views;

import io.renzen.ink.CommandObjectsDomain.ActionPanelAccountInfoCO;
import io.renzen.ink.Controllers.ActionPanelController;
import io.renzen.ink.InkClass;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

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
    final TitledPane toolOptionsPane = new TitledPane("Tool Options", casterToolOptionsBox);
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
    final CheckBox toggleButton = new CheckBox("Cast Through");
    final CheckBox showCasterCheckbox = new CheckBox("Show Caster");
    final CheckBox showStrokesCheckbox = new CheckBox("Show Strokes");
    final CheckBox showLoadedImageCheckbox = new CheckBox("Show Loaded Image");
    final Separator casterToolSeparatorTop = new Separator();
    final Separator brushSeparator = new Separator();
    final Separator casterToolSeparatorBottom = new Separator();
    final ColorPicker colorPicker = new ColorPicker();
    final int B_WIDTH = 300;
    ConfigurableApplicationContext springContext = null;
    ActionPanelController controller = null;
    Stage stage = null;
    int createCasterCounter =0;


    public JavaFXPanel(Stage primaryStage, ConfigurableApplicationContext springContext) {
        System.setProperty("prism.lcdtext", "false");

        stage = primaryStage;
        this.springContext = springContext;
        this.controller = springContext.getBean(ActionPanelController.class);
        var F_SIZE = "-fx-font-size: 20;";

        setUpJPanelCanvasInJavaFX();
        setUpOldToolPanelInJavaFX();

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
        canvasBox.getChildren().add(showLoadedImageCheckbox);
        accountBox.getChildren().add(canvasAcc);
        toolGroup.getToggles().addAll(casterToolButton, brushToolButton);
        toolSelectionBox.getChildren().addAll(casterToolButton, brushToolButton);
        casterToolOptionsBox.getChildren().addAll(
                casterToolSeparatorTop,
                toolOptionsLabel,
                raysLabel, raysSlider,
                toleranceLabel, toleranceSlider,
                toggleButton,
                showCasterCheckbox,
                showStrokesCheckbox,
                casterToolSeparatorBottom);
        brushBox.getChildren().addAll(brushSizeLabel, brushSizeSlider,
                densityLabel, densitySlider, brushSeparator, colorPicker);
        menuPane.getChildren().addAll(
                fileAcc,
                accountBox,
                toolSelectionAcc,
                toolOptionsAcc,
                brushOptionsPane);

        openWebsiteButton.setOnMouseClicked(mouseEvent -> controller.openRenzen());

        borderPane.setLeft(menuPane);
        root.getStyleClass().add(JMetroStyleClass.BACKGROUND);

        new JMetro(scene, STYLE);

        stage.setTitle("Renzen Ink");
        stage.setScene(scene);
        stage.show();
    }

    private void setUpJPanelCanvasInJavaFX() {
        SwingUtilities.invokeLater(() ->
                FXCanvasNode.setContent(springContext.getBean(InkClass.class).canvasPanel));
        fxCanvasPane.getChildren().add(FXCanvasNode);
        borderPane.setCenter(fxCanvasPane);
    }

    private void setUpOldToolPanelInJavaFX() {
        SwingUtilities.invokeLater(() -> OldToolNode
                .setContent(springContext.getBean(ActionPanel.class)));
        oldActionPane.getChildren().add(OldToolNode);
        borderPane.setRight(oldActionPane);
    }

    private void UpdateActionPanelToSelectedCaster(){

    }

    private void linkButtonsToActionController() {

    }

    private void updateAccountSectionWithLogin(ActionPanelAccountInfoCO infoCO){}


}
