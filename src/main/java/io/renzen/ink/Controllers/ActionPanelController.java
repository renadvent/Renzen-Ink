package io.renzen.ink.Controllers;

import io.renzen.ink.ArtObjects.Caster;
import io.renzen.ink.Converters.ModelToActionPanelCOConverter;
import io.renzen.ink.Services.BrushService;
import io.renzen.ink.Services.CanvasService;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenzenService;
import io.renzen.ink.ViewObjects.ActionPanelAccountInfoCO;
import io.renzen.ink.ViewObjects.ActionPanelCO;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Action Panel makes direct requests to this controller
 * Action Panel Function Definitions
 */

@Controller
public class ActionPanelController {

    final ModelToActionPanelCOConverter modelToActionPanelCOConverter;
    final CasterService casterService;
    final RenzenService renzenService;
    final BrushService brushService;
    final CanvasService canvasService;

    public ActionPanelController(ModelToActionPanelCOConverter modelToActionPanelCOConverter, CasterService casterService, RenzenService renzenService, BrushService brushService, CanvasService canvasService) {
        this.modelToActionPanelCOConverter = modelToActionPanelCOConverter;
        this.casterService = casterService;
        this.renzenService = renzenService;
        this.brushService = brushService;
        this.canvasService = canvasService;
    }

    public ActionPanelAccountInfoCO login(String username, String password) {
        return renzenService.getLoginInfo(username, password);
    }

    public void openRenzen() {
        renzenService.openRenzen();
    }

    public void viewImageOnWeb(String id) {
        renzenService.viewImageOnWeb(id);
    }

    public void deleteSelectedCaster() {
        var x = casterService.getAll().remove(casterService.getSelectedCaster());
        canvasService.repaintCanvas();
    }

    public ActionPanelCO createCaster(Object e, String casterName) {
        canvasService.getClicksFromCanvasPanelAndCreateCaster(casterName);
        return new ActionPanelCO();
    }

    public void flipSelectedCaster(boolean e) {
        casterService.getSelectedCaster().setFlip_status(casterService.getSelectedCaster().getFlip_status() * -1);
        canvasService.repaintCanvas();
    }

    public void updateNumberOfRays(int e) {
        casterService.getSelectedCaster().setRays(e);
        canvasService.repaintCanvas();
    }

    public void setBrushColor(Color color) {
        brushService.setSelectedColor(color);
    }

    public java.awt.Color getBrushColor(Color color) {
        return brushService.getSelectedColor();
    }

    public void selectCaster(String id) {
        casterService.selectCaster(id);
    }

    public Caster getSelectedCaster() {
        return casterService.getSelectedCaster();
    }

    public void updateNumberOfStrikes(boolean e) {
        int x = (e) ? 1 : 0;
        casterService.getSelectedCaster().setMax_penetrations(x);
        canvasService.repaintCanvas();
    }

    public void updateTolerance(int e) {
        casterService.getSelectedCaster().setTolerance(e);
        canvasService.repaintCanvas();
    }

    public void openFile(File file) {

        BufferedImage loadedImage = null;

        try {
            loadedImage = ImageIO.read(file);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            System.exit(0);
        }

        canvasService.getCanvasPanelCO().setBaseBuffer(loadedImage);

        canvasService.setTempBackground(loadedImage);

        //tempBackground = loadedImage;
    }

    public void setCasterColor(Color color) {
        casterService.setCasterColor(color);
    }

}
