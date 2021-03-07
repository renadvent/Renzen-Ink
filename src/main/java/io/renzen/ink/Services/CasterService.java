package io.renzen.ink.Services;

import io.renzen.ink.ArtObjects.Caster;
import io.renzen.ink.Converters.CasterAndBaseToCasterCOConverter;
import io.renzen.ink.ViewObjects.CanvasPanelCO;
import io.renzen.ink.ViewObjects.CasterCO;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CasterService {

    final ArrayList<Caster> casterArrayList = new ArrayList<>();
    final CasterAndBaseToCasterCOConverter casterAndBaseToCasterCOConverter;


    Caster selectedCaster;
    Color awtColor;
    //TODO use this to store rendered Casters that haven't changed
    List<CasterCO> casterRenderCache = new ArrayList<>();

    public CasterService(CasterAndBaseToCasterCOConverter casterAndBaseToCasterCOConverter) {
        this.casterAndBaseToCasterCOConverter = casterAndBaseToCasterCOConverter;
    }

    public Color getCasterColor() {
        return awtColor;
    }

    /**
     * takes in a fx color, and converts it to awt for caster
     *
     * @param color
     */
    public void setCasterColor(javafx.scene.paint.Color color) {


        var r = (float) color.getRed();
        var g = (float) color.getGreen();
        var b = (float) color.getBlue();
        var a = (float) color.getOpacity();
        float opacity = a;// (a * 255.0) ;
        //opacity = (opacity>255) ? 255 : opacity;
        java.awt.Color conColor = new java.awt.Color(r, g, b, opacity);

        if (selectedCaster != null) {
            selectedCaster.color = conColor;
        }

        awtColor = conColor;

    }

    public void setCasterRenderCache(List cache) {
        casterRenderCache = cache;
    }

    public Optional<CasterCO> findInCache(String id) {
        for (var casterCO : casterRenderCache) {
            if (casterCO.getName().equals(id)) {
                return Optional.of(casterCO);
            }
        }
        return Optional.empty();
    }

    public void selectCaster(String id) {
        selectedCaster = findByName(id);
    }

    public Caster findByName(String name) {
        for (Caster caster : casterArrayList) {
            if (caster.getName().equals(name)) {
                return caster;
            }
        }

        return null;
    }

    public Caster getSelectedCaster() {
        return selectedCaster;
    }

    public void setSelectedCaster(Caster caster) {
        selectedCaster = caster;
    }

    public Caster save(Caster caster) {

        casterArrayList.add(caster);

        return caster;

    }

    public List<Caster> getAll() {
        return casterArrayList;
    }



    public CanvasPanelCO getCanvasPanelCOtoRepaint(BufferedImage tempBackground) {

        var canvasPanelCO = new CanvasPanelCO();

        for (var caster : getAll()) {

            findInCache(caster.getName())
                    .ifPresentOrElse(casterCO -> {

                                //if in cache
                                if (getSelectedCaster() != null) {
                                    if (getSelectedCaster().getName().equals(caster.getName())) {
                                        if (((Caster) casterCO).equals(getSelectedCaster())) {
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

        setCasterRenderCache(canvasPanelCO.getCasterCOList());
        return canvasPanelCO;
    }


}
