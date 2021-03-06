package io.renzen.ink.ViewPanels.RenderLayers;

import io.renzen.ink.Controllers.CanvasPanelController;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderShapeService;
import io.renzen.ink.ViewObjects.CanvasPanelCO;
import io.renzen.ink.ViewPanels.CanvasPanel;

import java.awt.*;

public abstract class CustomRenderLayer {

    final CasterService casterService;
    final RenderShapeService renderShapeService;
    final CanvasPanelController canvasPanelController;
    final CanvasPanelCO canvasPanelCO;

    final CanvasPanel canvasPanel;

    CustomRenderLayer(CanvasPanel canvasPanel){
        this.canvasPanel=canvasPanel;
        this.casterService = canvasPanel.casterService;
        this.renderShapeService = canvasPanel.renderShapeService;
        this.canvasPanelController = canvasPanel.canvasPanelController;
        this.canvasPanelCO = canvasPanel.getCanvasPanelCO();
    }

    public abstract void render(Graphics g);

}
