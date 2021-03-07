package io.renzen.ink.ViewPanels.RenderLayers;

import io.renzen.ink.Services.CanvasService;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderShapeService;
import io.renzen.ink.ViewObjects.CanvasPanelCO;
import io.renzen.ink.ViewPanels.CanvasPanel;
import org.springframework.stereotype.Component;

import java.awt.*;

public abstract class AbstractCustomRenderLayer {

    final CasterService casterService;
    final RenderShapeService renderShapeService;
    final CanvasPanelCO canvasPanelCO;
    final CanvasService canvasService;
    final CanvasPanel canvasPanel;

    AbstractCustomRenderLayer(CanvasService canvasService) {

        this.canvasPanel = canvasService.canvasPanel;
        this.canvasPanelCO = canvasService.getCanvasPanelCO();

        this.canvasService = canvasService;

        this.casterService = canvasService.casterService;
        this.renderShapeService = canvasService.renderShapeService;
    }

    public abstract void render(Graphics g);

}
