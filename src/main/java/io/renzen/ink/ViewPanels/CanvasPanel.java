package io.renzen.ink.ViewPanels;

import io.renzen.ink.Controllers.CanvasPanelController;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderShapeService;
import io.renzen.ink.ViewObjects.CanvasPanelCO;
import io.renzen.ink.ViewPanels.RenderLayers.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
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
        addRenderLayer(new RayPathRenderLayer(this));

    }

    public static Graphics2D resetHints(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        return g2d;
    }

    ArrayList<AbstractCustomRenderLayer> renderLayerArrayList = new ArrayList<>();

    public void addRenderLayer(AbstractCustomRenderLayer rl){
        renderLayerArrayList.add(rl);
    }

    public void renderLayers(Graphics g){
        renderLayerArrayList.forEach(rl->rl.render(g));
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
