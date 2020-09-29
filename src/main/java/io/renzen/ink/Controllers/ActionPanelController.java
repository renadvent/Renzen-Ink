package io.renzen.ink.Controllers;

import io.renzen.ink.CommandObjectsDomain.ActionPanelAccountInfoCO;
import io.renzen.ink.CommandObjectsPanel.ActionPanelCO;
import io.renzen.ink.Converters.ModelToActionPanelCOConverter;
import io.renzen.ink.DomainObjects.Caster;
import io.renzen.ink.Links.ActionPanelControllerToCanvasPanelViewLink;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenzenService;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Action Panel makes request to this controller
 */

@Controller
public class ActionPanelController {

    final ModelToActionPanelCOConverter modelToActionPanelCOConverter;
    final CasterService casterService;

    final RenzenService renzenService;


    //    public void login(ActionEvent e){
//
//        renzenService.getLoginInfo();
//        //System.out.println(renzenService.getHomeStreamsPlainJSON());
//        //return "";
//    }
    final ActionPanelControllerToCanvasPanelViewLink actionPanelControllerToCanvasPanelViewLink;

    public ActionPanelController(ModelToActionPanelCOConverter modelToActionPanelCOConverter, CasterService casterService, RenzenService renzenService, ActionPanelControllerToCanvasPanelViewLink actionPanelControllerToCanvasPanelViewLink) {
        this.modelToActionPanelCOConverter = modelToActionPanelCOConverter;
        this.casterService = casterService;
        this.renzenService = renzenService;
        this.actionPanelControllerToCanvasPanelViewLink = actionPanelControllerToCanvasPanelViewLink;
    }

//    public void openRenzen(String username, String password){
//        renzenService.openRenzen();
//    }

    public ActionPanelAccountInfoCO login(String username, char[] password) {


        return renzenService.getLoginInfo(username, new String(password));

        //return "";
        //System.out.println(renzenService.getHomeStreamsPlainJSON());
        //return "";
    }

    public ActionPanelAccountInfoCO login(String username, String password) {


        return renzenService.getLoginInfo(username, password);

        //return "";
        //System.out.println(renzenService.getHomeStreamsPlainJSON());
        //return "";
    }

//    final ImageRepository imageRepository;

//    public void saveCanvasToMongoRepository(){
//        actionPanelControllerToCanvasPanelViewLink.saveCanvasToMongoRepository();
//    }

    public void openRenzen() {
        renzenService.openRenzen();
    }

    public void openRenzen(ActionEvent e) {
        renzenService.openRenzen();
    }

    public void viewImageOnWeb(String id) {
        renzenService.viewImageOnWeb(id);
    }

    public ActionPanelCO getInit() {
        return null;
    }

    public ActionPanelCO loadCasterToPanel(String name) {

        //casterService.

        return null;
    }

    public ActionPanelCO createCaster(ActionEvent e, String casterName) {
        //public ActionPanelCO createCaster(int x1,int y1, int x2, int y2){

        //TODO testing this call
        //appears there are no cyclic injections
        actionPanelControllerToCanvasPanelViewLink.getClicksFromCanvasPanelAndCreateCaster(casterName);

//        Caster caster = casterService.save(new Caster(0,0,0,0));
        //loadCasterToPanel(caster.getName());

        //TODO working
        var dummyCO = new ActionPanelCO();
        //dummyCO.se


        return dummyCO; //caster;
    }

    public void deleteSelectedCaster(ActionEvent e) {

    }

    public void flipSelectedCaster(ActionEvent e) {
        casterService.getSelectedCaster().setFlip_status(casterService.getSelectedCaster().getFlip_status() * -1);
        actionPanelControllerToCanvasPanelViewLink.repaintCanvas();
    }

    public void flipSelectedCaster(boolean e) {
        casterService.getSelectedCaster().setFlip_status(casterService.getSelectedCaster().getFlip_status() * -1);
        //casterService.getSelectedCaster().setFlip_status(casterService.getSelectedCaster().getFlip_status() * -1);
        actionPanelControllerToCanvasPanelViewLink.repaintCanvas();
    }

    public Point2D collectCoordinatesOfClickFromCanvas() {
        return null;
    }

    public List<Point2D> collectCoordinatesOfClickFromCanvas(int numberOfClicks) {
        return null;
    }

    //old
//    public void updateNumberOfRays(ChangeEvent e) {
//        casterService.getSelectedCaster().setRays(((JSlider) e.getSource()).getValue());
//        actionPanelControllerToCanvasPanelViewLink.repaintCanvas();
//    }

    public void updateNumberOfRays(int e) {
        casterService.getSelectedCaster().setRays(e);
        //casterService.getSelectedCaster().setRays((int)((Slider) e.getSource()).getValue());
        actionPanelControllerToCanvasPanelViewLink.repaintCanvas();
    }

    //--------------------------------------------------------

    int getSelectedCasterStrikes() {
        return 0;
    }

    int setSelectedCasterStrikes() {
        return 0;
    }

    int getSelectedCasterTolerance() {
        return 0;
    }

    int setSelectedCasterTolerance() {
        return 0;
    }

    int getSelectedCasterDetail() {
        return 0;
    }

    int setSelectedCasterDetail() {
        return 0;
    }

    void highlightSelectedCaster() {

    }

    //TODO figure out how selectCaster will work
    //will it be a variable in the controller? [likely]
    //will it be set in the model?
    //store in service

    //    Caster selectedCaster;
//
    public void selectCaster(String id) {
        casterService.selectCaster(id);
    }

    public Caster getSelectedCaster() {
        return casterService.getSelectedCaster();
    }

    public void updateNumberOfStrikes(ChangeEvent changeEvent) {
        casterService.getSelectedCaster().setMax_penetrations(((JSlider) changeEvent.getSource()).getValue());
        actionPanelControllerToCanvasPanelViewLink.repaintCanvas();
    }

    public void updateNumberOfStrikes(boolean e) {


        int x =  (e) ? 1 : 0;
        casterService.getSelectedCaster().setMax_penetrations(x);
        //casterService.getSelectedCaster().setMax_penetrations(((JSlider) changeEvent.getSource()).getValue());
        actionPanelControllerToCanvasPanelViewLink.repaintCanvas();
    }


    public void updateTolerance(ChangeEvent changeEvent) {
        casterService.getSelectedCaster().setTolerance(((JSlider) changeEvent.getSource()).getValue());
        actionPanelControllerToCanvasPanelViewLink.repaintCanvas();
    }

    public void updateTolerance(int e) {
        casterService.getSelectedCaster().setTolerance(e);
        //casterService.getSelectedCaster().setTolerance(((JSlider) changeEvent.getSource()).getValue());
        actionPanelControllerToCanvasPanelViewLink.repaintCanvas();
    }

    public void updateShowBackground(ActionEvent actionEvent) {
    }

    public void updateShowStrokes(ActionEvent actionEvent) {
    }

    public void updateShowTools(ActionEvent actionEvent) {
    }

    //--------------------------------------------------------------------------------------------------------

    public void updateDrawFromSelectedCaster(ItemEvent itemEvent) {
        casterService.getSelectedCaster().setCast_from_source(itemEvent.getStateChange() == ItemEvent.SELECTED);
    }

//    public void updateCastShadeFromSelectedCaster(ItemEvent itemEvent) {
//        casterService.getSelectedCaster().setCast_through(itemEvent.getStateChange() == ItemEvent.SELECTED);
//    }
//
//    public void updateCastStrokesFromSelectedCaster(ItemEvent itemEvent) {
//        casterService.getSelectedCaster().setCast_along(itemEvent.getStateChange() == ItemEvent.SELECTED);
//    }


}
