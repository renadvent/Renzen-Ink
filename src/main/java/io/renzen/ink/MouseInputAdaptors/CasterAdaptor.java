package io.renzen.ink.MouseInputAdaptors;

import io.renzen.ink.ArtObjects.Caster;
import io.renzen.ink.ArtObjects.RenderShape;
import io.renzen.ink.Services.CanvasService;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class CasterAdaptor extends Abstract_CanvasInputAdaptor {

    String casterName;

    public CasterAdaptor(CanvasService canvasService, String casterName) {
        super(canvasService);
        canvasPanel.addMouseListener(this);
        canvasPanel.addMouseMotionListener(this);
        this.casterName = casterName;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        renderShapeService.deleteByName("beforeClick");


         //start tracking mouse and drawing preview
         //to current location

        renderShapeService.addRenderShape(
                new RenderShape("firstClick", new Ellipse2D.Double(e.getX() - 50, e.getY() - 50, 100, 100)));


        canvasPanel.validate();
        canvasPanel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        var firstClick = (Ellipse2D.Double) renderShapeService.findByName("firstClick").getShape();

        //creates caster extending from first click to where mouse was released
        var temp = new Caster(casterName, firstClick.getX(), firstClick.getY(), e.getX(), e.getY());

        //TODO working on here
        temp.setColor(casterService.getCasterColor());
        Caster caster = casterService.save(temp);

        casterService.setSelectedCaster(temp);

        canvasService.javaFXPanel.UpdateActionPanelToSelectedCaster();

        renderShapeService.deleteByName("drag");
        renderShapeService.deleteByName("firstClick");

        canvasPanel.validate();
        canvasPanel.repaint();


        canvasService.removeCanvasListeners();

        //end tracking and delete listener and create new Caster


    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        renderShapeService.deleteByName("drag");

        RenderShape renderShape = renderShapeService.findByName("firstClick");

        Shape shape = renderShape.getShape();
        Ellipse2D.Double circle = (Ellipse2D.Double) shape;

        renderShapeService.addRenderShape(new RenderShape("drag",
                new Ellipse2D.Double(e.getX() - 25, e.getY() - 25, 50, 50)));

        renderShapeService.addRenderShape(new RenderShape("drag",
                new Line2D.Double(circle.getCenterX(), circle.getCenterY(), e.getX(), e.getY())));

        //?????
//                renderObjectService.addRenderShape(
//                        new RenderShape("firstClick", new Ellipse2D.Double(e.getX() - 50, e.getY() - 50, 100, 100)));

        canvasPanel.validate();
        canvasPanel.repaint();
    }

    /**
     * shows user it is active
     *
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);

        if (renderShapeService.findByName("firstClick") == null) {
            renderShapeService.deleteByName("beforeClick");

            renderShapeService.addRenderShape(new RenderShape("beforeClick",
                    new Ellipse2D.Double(e.getX() - 25, e.getY() - 25, 50, 50)));

            canvasPanel.validate();
            canvasPanel.repaint();
        }
    }
}
