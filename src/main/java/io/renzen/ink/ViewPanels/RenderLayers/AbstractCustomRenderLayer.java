package io.renzen.ink.ViewPanels.RenderLayers;

import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderShapeService;
import io.renzen.ink.ViewObjects.layerCO;
import io.renzen.ink.ViewPanels.CanvasPanel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class AbstractCustomRenderLayer {

    final CasterService casterService;
    final RenderShapeService renderShapeService;
    final CanvasPanel canvasPanel;
    layerCO layerCO;

    protected AbstractCustomRenderLayer(CasterService casterService, RenderShapeService renderShapeService, CanvasPanel canvasPanel) {
        this.casterService = casterService;
        this.renderShapeService = renderShapeService;
        this.canvasPanel = canvasPanel;
        this.layerCO = canvasPanel.getLayerCO();
    }


    public abstract void render(Graphics g);

}
