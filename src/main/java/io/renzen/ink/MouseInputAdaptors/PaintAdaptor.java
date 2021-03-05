package io.renzen.ink.MouseInputAdaptors;

import io.renzen.ink.ArtObjects.PaintBrush;
import io.renzen.ink.ArtObjects.RenderShape;
import io.renzen.ink.Services.CanvasService;

import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class PaintAdaptor extends CanvasInputAdaptor {

    RenderShape last;
    double lastX;
    double lastY;

    PaintBrush brush; //= brushService.getSelectedBrush();

    public PaintAdaptor(CanvasService canvasService) {
        super(canvasService);
        canvasPanel.addMouseListener(this);
        canvasPanel.addMouseMotionListener(this);
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
