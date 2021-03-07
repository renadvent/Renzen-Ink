package io.renzen.ink.ViewPanels.RenderLayers;

import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderShapeService;
import io.renzen.ink.ViewObjects.CanvasPanelCO;
import io.renzen.ink.ViewPanels.CanvasPanel;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public abstract class AbstractCustomRenderLayer {

    final CasterService casterService;
    final RenderShapeService renderShapeService;
    final CanvasPanel canvasPanel;
    CanvasPanelCO canvasPanelCO;

    protected AbstractCustomRenderLayer(CasterService casterService, RenderShapeService renderShapeService, CanvasPanel canvasPanel) {
        this.casterService = casterService;
        this.renderShapeService = renderShapeService;
        this.canvasPanel = canvasPanel;
        this.canvasPanelCO = canvasPanel.getCanvasPanelCO();
    }


    public abstract void render(Graphics g);

}
