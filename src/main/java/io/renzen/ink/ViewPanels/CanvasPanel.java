package io.renzen.ink.ViewPanels;

import io.renzen.ink.ViewObjects.CanvasPanelCO;
import io.renzen.ink.ViewPanels.RenderLayers.AbstractCustomRenderLayer;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Component
@Data
public class CanvasPanel extends JPanel {

    public CanvasPanelCO canvasPanelCO;

    boolean showBackground = true;
    boolean showRayPath = true;

    ArrayList<AbstractCustomRenderLayer> renderLayerArrayList = new ArrayList<>();

    public static Graphics2D resetHints(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        return g2d;
    }

    public void addRenderLayer(AbstractCustomRenderLayer rl) {
        renderLayerArrayList.add(rl);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderLayers(g);
    }

    public void renderLayers(Graphics g) {
        renderLayerArrayList.forEach(rl -> rl.render(g));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }
}
