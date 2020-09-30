package io.renzen.ink.Views;

import io.renzen.ink.CommandObjectsPanel.CanvasPanelCO;
import io.renzen.ink.Controllers.CanvasPanelController;
import io.renzen.ink.DomainObjects.RenderShape;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderObjectService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component @Data
public class CanvasPanel extends JPanel {

    final CasterService casterService;
    final RenderObjectService renderObjectService;

    final CanvasPanelController canvasPanelController;
    CanvasPanelCO canvasPanelCO;

    boolean showBackground=true;

    @Autowired
    public CanvasPanel(CasterService casterService, RenderObjectService renderObjectService, CanvasPanelController canvasPanelController) {
        this.casterService = casterService;
        this.renderObjectService = renderObjectService;
        this.canvasPanelController = canvasPanelController;
        canvasPanelCO = canvasPanelController.getInit();
    }


    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        //clears buffer
        //g2d.clearRect(0, 0, , 1000);

        //draws background image

        if (showBackground) {
            g2d.drawImage(canvasPanelCO.getBaseBuffer(), 0, 0, null);
        }

        for (var caster : canvasPanelController.getCanvasPanelCOtoRepaint().getCasterCOList()) {
            g2d.drawImage(caster.getStrokeBuffer(), 0, 0, null);
        }

        //draws RenderShapes on screen
        for (RenderShape renderShape : renderObjectService.getRenderShapeArrayList()) {
            g2d.draw(renderShape.getShape());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }
}
