package io.renzen.ink.ViewPanels.RenderLayers;

import io.renzen.ink.Services.CanvasService;
import io.renzen.ink.ViewPanels.CanvasPanel;

import java.awt.*;

public class BackgroundRenderLayer extends AbstractCustomRenderLayer {

    public BackgroundRenderLayer(CanvasService canvasService) {
        super(canvasService);
    }

    @Override
    public void render(Graphics g) {

        var g2d = CanvasPanel.resetHints(g);
        if (canvasPanel.isShowBackground()) {
            g2d.drawImage(canvasPanelCO.getBaseBuffer(), 0, 0, null);
        }
    }
}
