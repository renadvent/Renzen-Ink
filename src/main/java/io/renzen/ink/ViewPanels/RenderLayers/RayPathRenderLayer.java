package io.renzen.ink.ViewPanels.RenderLayers;

import io.renzen.ink.Services.CanvasService;
import io.renzen.ink.ViewPanels.CanvasPanel;

import java.awt.*;
import java.awt.geom.Line2D;

public class RayPathRenderLayer extends AbstractCustomRenderLayer {

    public RayPathRenderLayer(CanvasService canvasService) {
        super(canvasService);
    }

    @Override
    public void render(Graphics g) {

        var g2d = CanvasPanel.resetHints(g);

        for (var caster : canvasService.getCanvasPanelCOtoRepaint().getCasterCOList()) {
//            g2d.drawImage(caster.getStrokeBuffer(), 0, 0, null);

            //draws ray bath on canvas
            if (canvasPanel.isShowRayPath()) {
                for (var x : caster.getRay_path()) {
                    g2d.draw(new Line2D.Double(x.getOrigin_ray().get_x(), x.getOrigin_ray().get_y(),
                            x.get_x(), x.get_y()));
                }
            }
        }

    }
}
