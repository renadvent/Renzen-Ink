package io.renzen.ink.ViewPanels.RenderLayers;

import io.renzen.ink.ArtObjects.RenderShape;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderShapeService;
import io.renzen.ink.ViewPanels.CanvasPanel;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class ShapeRenderLayer extends AbstractCustomRenderLayer {


    protected ShapeRenderLayer(CasterService casterService, RenderShapeService renderShapeService, CanvasPanel canvasPanel) {
        super(casterService, renderShapeService, canvasPanel);
    }

    @Override
    public void render(Graphics g) {

        var g2d = CanvasPanel.resetHints(g);

        for (RenderShape renderShape : renderShapeService.getRenderShapeArrayList()) {
            g2d.setColor(renderShape.getColor());
            g2d.draw(renderShape.getShape());
        }
    }
}
