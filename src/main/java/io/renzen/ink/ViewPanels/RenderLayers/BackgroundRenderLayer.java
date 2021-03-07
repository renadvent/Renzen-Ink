package io.renzen.ink.ViewPanels.RenderLayers;

import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderShapeService;
import io.renzen.ink.ViewPanels.CanvasPanel;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class BackgroundRenderLayer extends AbstractCustomRenderLayer {


    protected BackgroundRenderLayer(CasterService casterService, RenderShapeService renderShapeService, CanvasPanel canvasPanel) {
        super(casterService, renderShapeService, canvasPanel);
    }

    @Override
    public void render(Graphics g) {

        var g2d = CanvasPanel.resetHints(g);
        if (canvasPanel.isShowBackground()) {
            g2d.drawImage(canvasPanelCO.getBaseBuffer(), 0, 0, null);
        }
    }
}
