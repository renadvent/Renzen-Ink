package io.renzen.ink.Services;

import io.renzen.ink.Converters.CasterAndBaseToCasterCOConverter;
import io.renzen.ink.ViewObjects.CanvasPanelCO;
import io.renzen.ink.ViewPanels.CanvasPanel;
import io.renzen.ink.ViewPanels.JavaFXPanel;
import io.renzen.ink.ViewPanels.RenderLayers.BackgroundRenderLayer;
import io.renzen.ink.ViewPanels.RenderLayers.CasterRenderLayer;
import io.renzen.ink.ViewPanels.RenderLayers.RayPathRenderLayer;
import io.renzen.ink.ViewPanels.RenderLayers.ShapeRenderLayer;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Gives and set information on the Canvas
 * Used by ActionPanelController
 * and CanvasPanelController to interact
 */

@Service
@Data
public class CanvasService {

    public final CanvasPanel canvasPanel;

    final CasterAndBaseToCasterCOConverter casterAndBaseToCasterCOConverter;
    final BackgroundRenderLayer backgroundRenderLayer;
    final CasterRenderLayer casterRenderLayer;
    final ShapeRenderLayer shapeRenderLayer;
    final RayPathRenderLayer rayPathRenderLayer;
    public JavaFXPanel javaFXPanel;
    public CanvasPanelCO canvasPanelCO;
    public BufferedImage tempBackground;


    public CanvasService(CanvasPanel canvasPanel,

                         CasterAndBaseToCasterCOConverter casterAndBaseToCasterCOConverter,
                         BackgroundRenderLayer backgroundRenderLayer,
                         CasterRenderLayer casterRenderLayer,
                         ShapeRenderLayer shapeRenderLayer,
                         RayPathRenderLayer rayPathRenderLayer) {

        this.canvasPanel = canvasPanel;
        this.backgroundRenderLayer = backgroundRenderLayer;
        this.casterRenderLayer = casterRenderLayer;
        this.shapeRenderLayer = shapeRenderLayer;
        this.rayPathRenderLayer = rayPathRenderLayer;

        this.casterAndBaseToCasterCOConverter = casterAndBaseToCasterCOConverter;

        canvasPanel.addRenderLayer(this.backgroundRenderLayer);
        canvasPanel.addRenderLayer(this.casterRenderLayer);
        canvasPanel.addRenderLayer(this.shapeRenderLayer);
        canvasPanel.addRenderLayer(this.rayPathRenderLayer);

    }

    public void removeCanvasListeners() {

        for (var listener : canvasPanel.getMouseListeners()) {
            canvasPanel.removeMouseListener(listener);
        }

        for (var listener : canvasPanel.getMouseMotionListeners()) {
            canvasPanel.removeMouseMotionListener(listener);
        }
    }

    public void toggleShowBackground() {
        canvasPanel.setShowBackground(!canvasPanel.isShowBackground());
        repaintCanvas();
    }

    public void repaintCanvas() {
        canvasPanel.validate();
        canvasPanel.repaint();
    }

    public void toggleShowRayPath() {
        canvasPanel.setShowRayPath(!canvasPanel.isShowRayPath());
        repaintCanvas();
    }

    public void saveCanvasToFile(File file) {

        var bi = getCanvasBufferedImage();

        try {
            ImageIO.write(bi, "png", file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public BufferedImage getCanvasBufferedImage(){
        var base = getCanvasPanelCO().getBaseBuffer();

        //TODO
        //for cases for right now, base buffer will be largest buffer
        //for future, will need to set max canvas size in CanvasPanelCO

        BufferedImage bi = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = (Graphics2D) bi.getGraphics();

        canvasPanel.printAll(g2d);
        g2d.dispose();

        return bi;
    }

    public File getTempCanvasFile(){

        //get canvas and save it to a temporary file as a png

        var bi = getCanvasBufferedImage();

        File file = null;

        try {
            file = File.createTempFile("image", ".png");
//            ImageIO.write(bi, "png", file);
            ImageIO.write(bi, "png", file);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
            //return "failed";
        }

        return file;

    }

    public String getCanvasBase64String() {


        var file = getTempCanvasFile();

        String fileContent = "";

        try {
            fileContent = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
        } catch (Exception exception) {
            System.out.println("count get contents");
            exception.printStackTrace();
            return "failed";
        }

        return fileContent;

    }

}
