package io.renzen.ink.Views;

import io.renzen.ink.Controllers.StrokePanelController;
import io.renzen.ink.Links.StrokePanelControllerToCanvasPanelViewLink;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class StrokePanel extends JPanel {

    final StrokePanelController strokePanelController;
    final StrokePanelControllerToCanvasPanelViewLink strokePanelControllerToCanvasPanelViewLink;

    JColorChooser tcc;
    JScrollPane scroll = new JScrollPane();
    JSlider main_mod;
    JSlider prop_along = new JSlider(JSlider.HORIZONTAL, 0, 100, 0); //-100
    JSlider rx = new JSlider(JSlider.HORIZONTAL, 0, 300, 0);
    JSlider ry = new JSlider(JSlider.HORIZONTAL, 0, 300, 0);
    JSlider ox = new JSlider(JSlider.HORIZONTAL, -300, 300, 0);
    JSlider oy = new JSlider(JSlider.HORIZONTAL, -300, 300, 0);
    JSlider opacity = new JSlider(JSlider.HORIZONTAL, 0, 255, 1);
    JSlider thickness = new JSlider(JSlider.HORIZONTAL, 0, 50, 1);

    public StrokePanel(StrokePanelController strokePanelController, StrokePanelControllerToCanvasPanelViewLink strokePanelControllerToCanvasPanelViewLink) {
        this.strokePanelController = strokePanelController;
        this.strokePanelControllerToCanvasPanelViewLink = strokePanelControllerToCanvasPanelViewLink;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        add(new JLabel("Control Vertex", JLabel.CENTER));
        add(new JLabel("Random X", JLabel.CENTER));
        add(rx);
        add(new JLabel("Random Y", JLabel.CENTER));
        add(ry);
        add(new JLabel("Offset X", JLabel.CENTER));
        add(ox);
        add(new JLabel("Offset Y", JLabel.CENTER));
        add(oy);
        add(new JLabel("BRUSH", JLabel.CENTER));
        add(new JLabel("Opacity", JLabel.CENTER));
        add(opacity);
        add(new JLabel("Thickness", JLabel.CENTER));
        add(thickness);
        add(new JLabel("Color", JLabel.CENTER));
        tcc = new JColorChooser();
        add(tcc);
    }
}
