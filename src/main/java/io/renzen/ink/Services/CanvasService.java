package io.renzen.ink.Services;

import io.renzen.ink.ArtObjects.Caster;
import io.renzen.ink.ArtObjects.RenderShape;
import io.renzen.ink.Converters.CasterAndBaseToCasterCOConverter;
import io.renzen.ink.MouseInputAdaptors.CasterAdaptor;
import io.renzen.ink.MouseInputAdaptors.PaintAdaptor;
import io.renzen.ink.ViewObjects.CanvasPanelCO;
import io.renzen.ink.ViewPanels.CanvasPanel;
import io.renzen.ink.ViewPanels.JavaFXPanel;
import io.renzen.ink.ViewPanels.RenderLayers.BackgroundRenderLayer;
import io.renzen.ink.ViewPanels.RenderLayers.CasterRenderLayer;
import io.renzen.ink.ViewPanels.RenderLayers.RayPathRenderLayer;
import io.renzen.ink.ViewPanels.RenderLayers.ShapeRenderLayer;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;

/**
 * Gives and set information on the Canvas
 * Used by ActionPanelController
 * and CanvasPanelController to interact
 */

@Service
@Data
public class CanvasService {

    public final CanvasPanel canvasPanel;
    public final RenderShapeService renderShapeService;
    public final CasterService casterService;
    public final RenzenService renzenService;
    public final BrushService brushService;
    final CasterAndBaseToCasterCOConverter casterAndBaseToCasterCOConverter;
    //public final CanvasPanelController canvasPanelController;
    public JavaFXPanel javaFXPanel;
    public CanvasPanelCO canvasPanelCO;
    public BufferedImage tempBackground;


    public CanvasService(CanvasPanel canvasPanel, RenderShapeService renderShapeService,
                         CasterService casterService, RenzenService renzenService, BrushService brushService,
                         CasterAndBaseToCasterCOConverter casterAndBaseToCasterCOConverter) {

        this.canvasPanel = canvasPanel;
        getInit();


        this.renderShapeService = renderShapeService;
        this.casterService = casterService;
        this.renzenService = renzenService;

        this.brushService = brushService;
        //this.canvasPanelController = canvasPanelController;
        this.casterAndBaseToCasterCOConverter = casterAndBaseToCasterCOConverter;

        //add render layers on top of background
        canvasPanel.addRenderLayer(new BackgroundRenderLayer(this));
        canvasPanel.addRenderLayer(new CasterRenderLayer(this));
        canvasPanel.addRenderLayer(new ShapeRenderLayer(this));
        canvasPanel.addRenderLayer(new RayPathRenderLayer(this));


    }

    public CanvasPanelCO getInit() {

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

        return getCanvasPanelCO();

    }

    public void paintOnCanvas() {
        new PaintAdaptor(this);
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

    public void saveCanvasAsFile(File file) {

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

    public String SAVE_CANVAS_AND_CREATE_NEW_ARTICLE_ON_RENZEN() {

        //get canvas and save it to a temporary file as a png
        var base = getCanvasPanelCO().getBaseBuffer();
        var canvasPanelCO = getCanvasPanelCO();

        //TODO
        //for cases for right now, base buffer will be largest buffer
        //for future, will need to set max canvas size in CanvasPanelCO

        BufferedImage bi = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //var bi = base.getSubimage(0,0,base.getWidth(),base.getHeight());
        //BufferedImage bi = new BufferedImage(canvasPanelCO.getBaseBuffer().getWidth(), canvasPanelCO.getBaseBuffer().getHeight(), BufferedImage.TYPE_INT_ARGB);


        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        //this.canvasPanelController.getInit().getBaseBuffer().

        //g2d.drawImage(base);
        //canvasPanel.printAll(g2d);

        //if (showBackground) {
        g2d.drawImage(canvasPanelCO.getBaseBuffer(), 0, 0, null);
        //}

        for (var caster : getCanvasPanelCOtoRepaint().getCasterCOList()) {
            g2d.drawImage(caster.getStrokeBuffer(), 0, 0, null);
        }

        //draws RenderShapes on screen
        for (RenderShape renderShape : renderShapeService.getRenderShapeArrayList()) {
            g2d.setColor(renderShape.getColor());
            g2d.draw(renderShape.getShape());
        }


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

        var jacksonResponse = renzenService.UploadArticle(fileContent);


        System.out.println("Trying to open");

        //TODO switch from uploading just an image, to uploading an image that creates a draft

        try {
            OpenArticleInBrowser(jacksonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("could not open");
        }

        return (String) jacksonResponse.get("SASUrl");

    }

    public CanvasPanelCO getCanvasPanelCOtoRepaint() {

        var canvasPanelCO = new CanvasPanelCO();

        for (var caster : casterService.getAll()) {

            casterService.findInCache(caster.getName())
                    .ifPresentOrElse(casterCO -> {

                                //if in cache
                                if (casterService.getSelectedCaster() != null) {
                                    if (casterService.getSelectedCaster().getName().equals(caster.getName())) {
                                        if (((Caster) casterCO).equals(casterService.getSelectedCaster())) {
                                            canvasPanelCO.getCasterCOList().add(casterCO);
                                        } else {
                                            canvasPanelCO.getCasterCOList()
                                                    .add(casterAndBaseToCasterCOConverter.toCasterCO(caster, tempBackground));
                                        }

                                    } else {
                                        canvasPanelCO.getCasterCOList().add(casterCO);
                                    }

                                } else {
                                    canvasPanelCO.getCasterCOList().add(casterCO);
                                }
                            },


                            //if not in cache
                            () -> canvasPanelCO.getCasterCOList()
                                    .add(casterAndBaseToCasterCOConverter.toCasterCO(caster, tempBackground)));

        }

        casterService.setCasterRenderCache(canvasPanelCO.getCasterCOList());
        return canvasPanelCO;
    }

    private void OpenArticleInBrowser(HashMap<?, ?> jacksonResponse) throws IOException, URISyntaxException {
        var URL = new java.net.URL(renzenService.getRoot()


                + "/OPEN_ARTICLE_DRAFT_FROM_APP?articleID="
                //+ "/newCreateArticle?image="
                + URLEncoder.encode((String) jacksonResponse.get("articleID"), StandardCharsets.UTF_8)
                + "&token="
                + URLEncoder.encode(renzenService.getAuthToken(), StandardCharsets.UTF_8));

        //opens browser window, logs in, and goes to page to create a post
        Desktop.getDesktop().browse(URL.toURI());
    }

//    CanvasPanelCO canvasPanelCO;

    /**
     * this function will create a click listener
     * that will listen for the given number of clicks on the canvas
     * and then
     */
    public void getClicksFromCanvasPanelAndCreateCaster(String casterName) {
        new CasterAdaptor(this, casterName);
    }


}
