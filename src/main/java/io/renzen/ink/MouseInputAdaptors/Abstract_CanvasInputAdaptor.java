package io.renzen.ink.MouseInputAdaptors;

import io.renzen.ink.Services.BrushService;
import io.renzen.ink.Services.CanvasService;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderShapeService;
import io.renzen.ink.ViewPanels.CanvasPanel;
import org.springframework.stereotype.Component;

import java.awt.event.MouseAdapter;

/**
 * Extend this class to create MouseAdapters
 * that can draw to canvas, create shapes, etc
 */
@Component
public abstract class Abstract_CanvasInputAdaptor extends MouseAdapter {

    final CanvasService canvasService;
    final BrushService brushService;
    final RenderShapeService renderShapeService;
    final CanvasPanel canvasPanel;
    final CasterService casterService;
    protected Abstract_CanvasInputAdaptor(CanvasService canvasService, BrushService brushService, RenderShapeService renderShapeService, CanvasPanel canvasPanel, CasterService casterService) {
        this.canvasService = canvasService;
        this.brushService = brushService;
        this.renderShapeService = renderShapeService;
        this.canvasPanel = canvasPanel;
        this.casterService = casterService;
    }

    public abstract void activate();

}
