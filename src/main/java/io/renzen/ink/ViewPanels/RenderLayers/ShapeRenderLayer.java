package io.renzen.ink.ViewPanels.RenderLayers;

import io.renzen.ink.ArtObjects.RenderShape;
import io.renzen.ink.Services.CanvasService;
import io.renzen.ink.ViewPanels.CanvasPanel;

import java.awt.*;

public class ShapeRenderLayer extends AbstractCustomRenderLayer {

    public ShapeRenderLayer(CanvasService canvasService) {
        super(canvasService);
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
