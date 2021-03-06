package io.renzen.ink.MouseInputAdaptors;

import io.renzen.ink.Controllers.CanvasPanelController;
import io.renzen.ink.Services.*;
import io.renzen.ink.ViewPanels.CanvasPanel;

import java.awt.event.MouseAdapter;

/**
 * Extend this class to create MouseAdapters
 * that can draw to canvas, create shapes, etc
 */
public abstract class Abstract_CanvasInputAdaptor extends MouseAdapter {

    final CanvasPanel canvasPanel;
    final RenderShapeService renderShapeService;
    final CasterService casterService;
    final RenzenService renzenService;
    final BrushService brushService;
    final CanvasPanelController canvasPanelController;
    final CanvasService canvasService;

    public Abstract_CanvasInputAdaptor(CanvasService canvasService) {
        super();
        this.canvasPanel = canvasService.canvasPanel;
        this.renderShapeService = canvasService.renderShapeService;
        this.casterService = canvasService.casterService;
        this.renzenService = canvasService.renzenService;
        this.brushService = canvasService.brushService;
        this.canvasPanelController = canvasService.canvasPanelController;
//            this.brush=canvasService.brushService.getSelectedBrush();
        this.canvasService = canvasService;
    }

}
