package io.renzen.ink.ViewPanels;

import io.renzen.ink.ArtObjects.RenderShape;
import io.renzen.ink.Controllers.CanvasPanelController;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderShapeService;
import io.renzen.ink.ViewObjects.CanvasPanelCO;
import io.renzen.ink.ViewPanels.RenderLayers.BackgroundRenderLayer;
import io.renzen.ink.ViewPanels.RenderLayers.CasterRenderLayer;
import io.renzen.ink.ViewPanels.RenderLayers.CustomRenderLayer;
import io.renzen.ink.ViewPanels.RenderLayers.ShapeRenderLayer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

@Component
@Data
public class CanvasPanel extends JPanel {

    public final CasterService casterService;
    public final RenderShapeService renderShapeService;

    public final CanvasPanelController canvasPanelController;
    public CanvasPanelCO canvasPanelCO;

    boolean showBackground = true;
    boolean showRayPath = true;

    @Autowired
    public CanvasPanel(CasterService casterService, RenderShapeService renderShapeService, CanvasPanelController canvasPanelController) {
        super();
        this.casterService = casterService;
        this.renderShapeService = renderShapeService;
        this.canvasPanelController = canvasPanelController;
        canvasPanelCO = canvasPanelController.getInit();

        addRenderLayer(new BackgroundRenderLayer(this));
        addRenderLayer(new CasterRenderLayer(this));
        addRenderLayer(new ShapeRenderLayer(this));

    }

    public static Graphics2D resetHints(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        return g2d;
    }

    ArrayList<CustomRenderLayer> renderLayerArrayList = new ArrayList<>();

    public void addRenderLayer(CustomRenderLayer rl){
        renderLayerArrayList.add(rl);
    }

    public void renderLayers(Graphics g){
        for (var rl : renderLayerArrayList){
            rl.render(g);
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderLayers(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }
}
