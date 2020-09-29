package io.renzen.ink.Controllers;

import io.renzen.ink.CommandObjectsPanel.CanvasPanelCO;
import io.renzen.ink.Converters.CasterAndBaseToCasterCOConverter;
import io.renzen.ink.DomainObjects.Caster;
import io.renzen.ink.Services.CasterService;
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


    public CanvasPanelController(CasterService casterService, CasterAndBaseToCasterCOConverter casterAndBaseToCasterCOConverter) {
        this.casterService = casterService;
        this.casterAndBaseToCasterCOConverter = casterAndBaseToCasterCOConverter;
    }

    public CanvasPanelCO getInit() {

        CanvasPanelCO canvasPanelCO = new CanvasPanelCO();

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

                                        var test1 = (Caster) casterCO;
                                        var test2 = casterService.getSelectedCaster();
                                        var test3 = (Caster) casterService.getSelectedCaster();
                                        var test4 = ((Caster) casterCO).equals(casterService.getSelectedCaster());
                                        var test5 = casterService.getSelectedCaster().equals(casterCO);


                                        if (((Caster) casterCO).equals(casterService.getSelectedCaster())) {
                                            System.out.println("CASTERS EQUAL!" + caster.getName());
                                            canvasPanelCO.getCasterCOList().add(casterCO);
                                        } else {
                                            System.out.println("CASTERS NOT EQUAL! CHANGED " + caster.getName());
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
