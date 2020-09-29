package io.renzen.ink.Links;

import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderObjectService;
import io.renzen.ink.Views.CanvasPanel;
import org.springframework.stereotype.Controller;

@Controller
public class StrokePanelControllerToCanvasPanelViewLink {

    /**
     * Can be used to have canvas outline
     * what is currently being edited etc
     */

    final CanvasPanel canvasPanel;

    final RenderObjectService renderObjectService;
    final CasterService casterService;

    public StrokePanelControllerToCanvasPanelViewLink(CanvasPanel canvasPanel, RenderObjectService renderObjectService, CasterService casterService) {
        this.canvasPanel = canvasPanel;
        this.renderObjectService = renderObjectService;
        this.casterService = casterService;
    }
}
