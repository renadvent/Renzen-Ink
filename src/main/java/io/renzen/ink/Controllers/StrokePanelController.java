package io.renzen.ink.Controllers;

import io.renzen.ink.CommandObjectsPanel.StrokePanelCO;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderObjectService;
import org.springframework.stereotype.Controller;

@Controller
public class StrokePanelController {

    final CasterService casterService;
    final RenderObjectService renderObjectService;

    public StrokePanelController(CasterService casterService, RenderObjectService renderObjectService) {
        this.casterService = casterService;
        this.renderObjectService = renderObjectService;
    }

    StrokePanelCO getInit() {
        return null;
    }

    //    StrokeCurve getSelectedCV();
//    void setSelectedCV(String id);
//
//    Double getSelectedCVRandomX();
//    Double getSelectedCVRandomY();
//
//    Double getSelectedCVOffsetX();
//    Double getSelectedCVOffsetY();
//
//    Double getSelectedOpacity();
//    Double getSelectedThickness();
//
//    Double getSelectedColor();
//
//    Double setSelectedCVRandomX();
//    Double setSelectedCVRandomY();
//
//    Double setSelectedCVOffsetX();
//    Double setSelectedCVOffsetY();
//
//    Double setSelectedOpacity();
//    Double setSelectedThickness();
//
//    Double setSelectedColor();


}
