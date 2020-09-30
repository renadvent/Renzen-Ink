package io.renzen.ink.Controllers;

import io.renzen.ink.CommandObjectsPanel.CanvasPanelCO;
import io.renzen.ink.Converters.CasterAndBaseToCasterCOConverter;
import io.renzen.ink.DomainObjects.Caster;
import io.renzen.ink.Services.CasterService;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Controller;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Canvas view makes requests to this controller
 */

@Controller
public class CanvasPanelController {

    final CasterService casterService;


    final CasterAndBaseToCasterCOConverter casterAndBaseToCasterCOConverter;
    BufferedImage tempBackground;//for collision testing

    //CanvasPanel canvasPanel;
//    @PostConstruct
//    void postConstructInit(CanvasPanel canvasPanel){
//        this.canvasPanel = canvasPanel;
//    }
    CanvasPanelCO canvasPanelCO;

    public CanvasPanelController(CasterService casterService, CasterAndBaseToCasterCOConverter casterAndBaseToCasterCOConverter) {
        this.casterService = casterService;
        this.casterAndBaseToCasterCOConverter = casterAndBaseToCasterCOConverter;
    }

    public void setCasterColor(Color color) {
        casterService.setCasterColor(color);


//        var caster = casterService.getSelectedCaster();
//
//        var r = (int) color.getRed();
//        var g = (int)color.getGreen();
//        var b = (int)color.getBlue();
//        var a =(int) color.getOpacity()*255;
//        float opacity = (float) (a / 255.0) ;
//        java.awt.Color conColor = new java.awt.Color(r, g, b, opacity);
//
//        caster.color = conColor;

    }

    public void openFile(File file) {

        BufferedImage loadedImage = null;

        try {
            //File f = new File("src/main/java/io/renzen/ink/body.jpg");
            loadedImage = ImageIO.read(file);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            System.exit(0);
        }

        canvasPanelCO.setBaseBuffer(loadedImage);

        tempBackground = loadedImage;
    }

    public CanvasPanelCO getInit() {

        canvasPanelCO = new CanvasPanelCO();

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

        canvasPanelCO.setBaseBuffer(bufferedImage);

        tempBackground = bufferedImage;

        return canvasPanelCO;

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


    CanvasPanelCO updateActionPanelCOtoRepaint() {

        //only rerender selected caster


        return null;
    }

}
