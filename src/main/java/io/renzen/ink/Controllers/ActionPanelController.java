package io.renzen.ink.Controllers;

import io.renzen.ink.CommandObjectsDomain.ActionPanelAccountInfoCO;
import io.renzen.ink.CommandObjectsPanel.ActionPanelCO;
import io.renzen.ink.Converters.ModelToActionPanelCOConverter;
import io.renzen.ink.DomainObjects.Caster;
import io.renzen.ink.Links.ActionPanelControllerToCanvasPanelViewLink;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenzenService;
import org.springframework.stereotype.Controller;

/**
 * Action Panel makes request to this controller
 */

@Controller
public class ActionPanelController {

    final ModelToActionPanelCOConverter modelToActionPanelCOConverter;
    final CasterService casterService;

    final RenzenService renzenService;

    final ActionPanelControllerToCanvasPanelViewLink actionPanelControllerToCanvasPanelViewLink;

    public ActionPanelController(ModelToActionPanelCOConverter modelToActionPanelCOConverter, CasterService casterService, RenzenService renzenService, ActionPanelControllerToCanvasPanelViewLink actionPanelControllerToCanvasPanelViewLink) {
        this.modelToActionPanelCOConverter = modelToActionPanelCOConverter;
        this.casterService = casterService;
        this.renzenService = renzenService;
        this.actionPanelControllerToCanvasPanelViewLink = actionPanelControllerToCanvasPanelViewLink;
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

    public ActionPanelCO createCaster(Object e, String casterName) {
        //public ActionPanelCO createCaster(int x1,int y1, int x2, int y2){

        //TODO testing this call
        //appears there are no cyclic injections
        actionPanelControllerToCanvasPanelViewLink.getClicksFromCanvasPanelAndCreateCaster(casterName);

        //TODO working
        var dummyCO = new ActionPanelCO();
        //dummyCO.se

        return dummyCO; //caster;
    }

    public void flipSelectedCaster(boolean e) {
        casterService.getSelectedCaster().setFlip_status(casterService.getSelectedCaster().getFlip_status() * -1);
        //casterService.getSelectedCaster().setFlip_status(casterService.getSelectedCaster().getFlip_status() * -1);
        actionPanelControllerToCanvasPanelViewLink.repaintCanvas();
    }

    public void updateNumberOfRays(int e) {
        casterService.getSelectedCaster().setRays(e);
        //casterService.getSelectedCaster().setRays((int)((Slider) e.getSource()).getValue());
        actionPanelControllerToCanvasPanelViewLink.repaintCanvas();
    }

    public void selectCaster(String id) {
        casterService.selectCaster(id);
    }

    public Caster getSelectedCaster() {
        return casterService.getSelectedCaster();
    }

    public void updateNumberOfStrikes(boolean e) {


        int x =  (e) ? 1 : 0;
        casterService.getSelectedCaster().setMax_penetrations(x);
        //casterService.getSelectedCaster().setMax_penetrations(((JSlider) changeEvent.getSource()).getValue());
        actionPanelControllerToCanvasPanelViewLink.repaintCanvas();
    }

    public void updateTolerance(int e) {
        casterService.getSelectedCaster().setTolerance(e);
        //casterService.getSelectedCaster().setTolerance(((JSlider) changeEvent.getSource()).getValue());
        actionPanelControllerToCanvasPanelViewLink.repaintCanvas();
    }

}
