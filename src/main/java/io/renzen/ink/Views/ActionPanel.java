package io.renzen.ink.Views;

import io.renzen.ink.CommandObjectsDomain.ActionPanelAccountInfoCO;
import io.renzen.ink.CommandObjectsPanel.ActionPanelCO;
import io.renzen.ink.Controllers.ActionPanelController;
import io.renzen.ink.Links.ActionPanelControllerToCanvasPanelViewLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class ActionPanel extends JLayeredPane {
//public class ActionPanel extends JPanel {

    final ActionPanelController actionPanelController;
    final ActionPanelControllerToCanvasPanelViewLink actionPanelControllerToCanvasPanelViewLink;

    final JLayeredPane accountPane = new JLayeredPane();
    final JLayeredPane casterPropertyPane = new JLayeredPane();
    final JLayeredPane brushPane = new JLayeredPane();
    final JLayeredPane casterListPane = new JLayeredPane();

    final JButton open_renzen = new JButton("Open Renzen");
    final JButton login_button = new JButton("Login to Renzen.io");
    final JTextField username = new JTextField("");
    final JPasswordField password = new JPasswordField("");

    final JButton upload_button = new JButton("Upload to Renzen.io");

    /**
     * Current Article
     * Start New WIP Article
     * Upload new WIP image     *
     */

    final JButton create_button = new JButton("Create New Caster");
    final JButton delete_button = new JButton("Delete Selected Caster");
    final JButton flip = new JButton("Flip selected Caster");

    final JSlider num_of_rays = new JSlider();
    final JSlider num_of_strikes = new JSlider();
    final JSlider tolerance = new JSlider();

    final JCheckBox check_base = new JCheckBox("Show Background");
    final JCheckBox check_strokes = new JCheckBox("Show Strokes");
    final JCheckBox check_tools = new JCheckBox("Show Casters");

    final JCheckBox draw_from_caster = new JCheckBox("Draw Strokes from Caster");
    final JCheckBox cast_shade = new JCheckBox("Cast Shading");
    final JCheckBox cast_strokes = new JCheckBox("Cast Strokes");
    final JPanel toolbarPane = new JPanel();
    final JToolBar buttonJList = new JToolBar(SwingConstants.VERTICAL);
    int counter = 0;

    @Autowired
    public ActionPanel(ActionPanelController actionPanelController, ActionPanelControllerToCanvasPanelViewLink actionPanelControllerToCanvasPanelViewLink) {
        this.actionPanelController = actionPanelController;
        this.actionPanelControllerToCanvasPanelViewLink = actionPanelControllerToCanvasPanelViewLink;

        //messy injection
        actionPanelControllerToCanvasPanelViewLink.setActionPanel(this);

        ActionPanelCO actionPanelCO = actionPanelController.getInit();

        createActionPanelGUI();
        linkButtonsToActionController();
    }

    public void UpdateActionPanelToSelectedCaster() {

        var caster = actionPanelController.getSelectedCaster();
        num_of_rays.setValue(caster.rays);
        num_of_strikes.setValue(caster.max_penetrations);
        tolerance.setValue(caster.tolerance);

        validate();
        repaint();

    }

    void linkButtonsToActionController() {

        create_button.addActionListener(e -> {
            var button = new JButton("New Caster " + counter++);
            var actionPanelCO = actionPanelController.createCaster(e, button.getText());
            buttonJList.add(button);
            button.addActionListener(actionEvent -> {
                actionPanelController.selectCaster(button.getText());
                UpdateActionPanelToSelectedCaster();
            });
            validate();
            repaint();
        });


        delete_button.addActionListener(actionPanelController::deleteSelectedCaster);
        flip.addActionListener(actionPanelController::flipSelectedCaster);

        //num_of_rays.addChangeListener(actionPanelController::updateNumberOfRays);
        num_of_strikes.addChangeListener(actionPanelController::updateNumberOfStrikes);
        tolerance.addChangeListener(actionPanelController::updateTolerance);

        check_base.addActionListener(actionPanelController::updateShowBackground);
        check_strokes.addActionListener(actionPanelController::updateShowStrokes);
        check_tools.addActionListener(actionPanelController::updateShowTools);

        draw_from_caster.addItemListener(actionPanelController::updateDrawFromSelectedCaster);

//        upload_button.addActionListener(event -> {
//            var link = actionPanelControllerToCanvasPanelViewLink.saveCanvasToMongoRepository(event);
//
//            var button = new JButton(link);
//            button.addActionListener(e -> {
//                actionPanelController.viewImageOnWeb(link);
//            });
//
//            buttonJList.add(button);
//        });

        login_button.addActionListener(e -> {
            updateAccountSectionWithLogin(actionPanelController.login(username.getText(), password.getPassword()));
        });
        open_renzen.addActionListener(actionPanelController::openRenzen);

    }

    public void updateAccountSectionWithLogin(ActionPanelAccountInfoCO infoCO) {

        accountPane.remove(username);
        accountPane.remove(password);
        accountPane.remove(login_button);

        accountPane.add(new JLabel("welcome " + infoCO.getName()), 1);

        accountPane.validate();
        accountPane.repaint();
    }

    public void createAccountPane() {
        accountPane.setLayout(new BoxLayout(accountPane, BoxLayout.PAGE_AXIS));
        accountPane.add(new JLabel("Account"), 0);
        //accountPane.add(open_renzen);
        username.setMaximumSize(new Dimension(300, 50));
        accountPane.add(username);
        password.setMaximumSize(new Dimension(300, 50));
        accountPane.add(password);
        accountPane.add(login_button);
        //accountPane.add(upload_button);

        accountPane.validate();
        accountPane.repaint();

        add(accountPane, 0);
    }

    void createActionPanelGUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        createAccountPane();

        //add(new JLabel("Rays"));
        num_of_rays.setMaximum(120);
        num_of_rays.setMinimum(10);
        num_of_rays.setValue(10);
        //this.add(num_of_rays);

        //add(new JLabel("Tolerance"));
        tolerance.setMinimum(0);
        tolerance.setMaximum(255);
        tolerance.setValue(0);
        //this.add(tolerance);

        //add(new JLabel("Strikes"));
        num_of_strikes.setMinimum(0);
        num_of_strikes.setMaximum(1);//can cause stackoverflow for some reason
        num_of_strikes.setValue(0);
        //this.add(num_of_strikes);


//        this.add(new JLabel("VIEW TOGGLES", JLabel.CENTER));
//        this.add(check_base);
//        this.add(check_strokes);
//        this.add(check_tools);

        this.add(new JLabel("CREATE/DELETE CASTERS", JLabel.CENTER));
        this.add(create_button);
        this.add(delete_button);

//        this.add(new JLabel("CASTER TOGGLES", JLabel.CENTER));
//        this.add(draw_from_caster);
//        this.add(cast_shade);
//        this.add(cast_strokes);

        //this.add(new JLabel("CASTER BUTTONS", JLabel.CENTER));
        //this.add(flip);

        add(new JLabel("CASTERS", JLabel.CENTER));

        //buttonJList.setVisibleRowCount(5);
        //buttonJList.setLayout(new BoxLayout(buttonJList,BoxLayout.PAGE_AXIS));
        //buttonJList.setOrientation(JList.VERTICAL_WRAP);
        //buttonJList.set
        //add(toolbarPane);
        //toolbarPane.add(buttonJList,BorderLayout.PAGE_END);
        add(buttonJList, BorderLayout.PAGE_END);
    }
}
