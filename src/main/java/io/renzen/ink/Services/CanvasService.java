package io.renzen.ink.Services;

import io.renzen.ink.ArtObjects.Caster;
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
import java.io.IOException;
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

    public final CasterService casterService;
    public final RenderShapeService renderShapeService;

    final CasterAndBaseToCasterCOConverter casterAndBaseToCasterCOConverter;
    //public final CanvasPanelController canvasPanelController;
    public JavaFXPanel javaFXPanel;
    public CanvasPanelCO canvasPanelCO;
    public BufferedImage tempBackground;


    public CanvasService(CanvasPanel canvasPanel,
                         CasterService casterService,
                         RenderShapeService renderShapeService, CasterAndBaseToCasterCOConverter casterAndBaseToCasterCOConverter) {

        this.canvasPanel = canvasPanel;
        this.renderShapeService = renderShapeService;
        getInit();


        this.casterService = casterService;

        this.casterAndBaseToCasterCOConverter = casterAndBaseToCasterCOConverter;

        //add render layers on top of background
        canvasPanel.addRenderLayer(new BackgroundRenderLayer(this));
        canvasPanel.addRenderLayer(new CasterRenderLayer(this));
        canvasPanel.addRenderLayer(new ShapeRenderLayer(this));
        canvasPanel.addRenderLayer(new RayPathRenderLayer(this));


    }

    public void getInit() {

        setCanvasPanelCO(new CanvasPanelCO());

        BufferedImage bufferedImage = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        BufferedImage loadedImage = null;

        try {
            File f = new File("src/main/java/io/renzen/ink/body.jpg");
            loadedImage = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        g2d.drawImage(loadedImage, 0, 0, null);

        getCanvasPanelCO().setBaseBuffer(bufferedImage);

        tempBackground = bufferedImage;
        setTempBackground(tempBackground);

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

        BufferedImage bi = new BufferedImage(canvasPanel.getWidth(), canvasPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        canvasPanel.printAll(g2d);
        g2d.dispose();

        try {
            ImageIO.write(bi, "png", file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public String getCanvasContents() {

        //get canvas and save it to a temporary file as a png
        var base = getCanvasPanelCO().getBaseBuffer();

        //TODO
        //for cases for right now, base buffer will be largest buffer
        //for future, will need to set max canvas size in CanvasPanelCO

        BufferedImage bi = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = (Graphics2D) bi.getGraphics();

        canvasPanel.printAll(g2d);
        g2d.dispose();

        File file = null;

        try {
            file = File.createTempFile("image", ".png");
//            ImageIO.write(bi, "png", file);
            ImageIO.write(bi, "png", file);
        } catch (Exception exception) {
            exception.printStackTrace();
            return "failed";
        }

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
