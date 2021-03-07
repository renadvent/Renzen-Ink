package io.renzen.ink.MouseInputAdaptors;

import io.renzen.ink.ArtObjects.PaintBrush;
import io.renzen.ink.ArtObjects.RenderShape;
import io.renzen.ink.Services.BrushService;
import io.renzen.ink.Services.CanvasService;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderShapeService;
import io.renzen.ink.ViewPanels.CanvasPanel;
import org.springframework.stereotype.Component;

import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;


//TODO make a component. inject services into adapter?
//TODO inject this into canvasService? or actionPanelController?
//TODO paintAdapter.activate

@Component
public class PaintAdaptor extends Abstract_CanvasInputAdaptor {

    RenderShape last;
    double lastX;
    double lastY;
    PaintBrush brush; //= brushService.getSelectedBrush();

//    final CanvasService canvasService;
//    final BrushService brushService;
//    final RenderShapeService renderShapeService;
//    final CanvasPanel canvasPanel;

    protected PaintAdaptor(CanvasService canvasService, BrushService brushService, RenderShapeService renderShapeService, CanvasPanel canvasPanel, CasterService casterService) {
        super(canvasService, brushService, renderShapeService, canvasPanel, casterService);
    }

    @Override
    public void activate() {
        this.canvasPanel.addMouseListener(this);
        this.canvasPanel.addMouseMotionListener(this);
        brush = this.brushService.getSelectedBrush();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        renderShapeService.addRenderShape(
                new RenderShape("first click from brush",
                        new Ellipse2D.Double(e.getX() - brush.getSize() / 2, e.getY() - brush.getSize() / 2,
                                brush.getSize(), brush.getSize()), brush.getColor()));


        canvasService.repaintCanvas();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);


        canvasService.removeCanvasListeners();
        canvasService.repaintCanvas();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        var temp = renderShapeService.addRenderShape(
                new RenderShape("while dragging",
                        new Ellipse2D.Double(e.getX() - brush.getSize() / 2, e.getY() - brush.getSize() / 2,
                                brush.getSize(), brush.getSize()), brush.getColor()));


        //TODO working on rendering lines

//                var temp = renderObjectService.addRenderShape(
//                        new RenderShape("firstClick", new Ellipse2D.Double(e.getX() - 50, e.getY() - 50, 100, 100)));
//

        if (last != null) {
            renderShapeService.addRenderShape(
                    new RenderShape("line between",
                            new Line2D.Double(lastX, lastY, e.getX(), e.getY()), brush.getColor()));
        }

        last = temp;
        lastX = e.getX();
        lastY = e.getY();

        canvasService.repaintCanvas();
    }


}
