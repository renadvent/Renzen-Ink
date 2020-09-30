package io.renzen.ink.Services;

import io.renzen.ink.CommandObjectsDomain.CasterCO;
import io.renzen.ink.DomainObjects.Caster;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CasterService {

    //int counter=0;
    final ArrayList<Caster> casterArrayList = new ArrayList<>();

    Caster selectedCaster;

    public void setSelectedCaster(Caster caster){
        selectedCaster=caster;
    }

    Color awtColor;



    /**takes in a fx color, and converts it to awt for caster
     *
     * @param color
     */
    public void setCasterColor(javafx.scene.paint.Color color){


        var r = (float) color.getRed();
        var g = (float)color.getGreen();
        var b = (float)color.getBlue();
        var a =(float) color.getOpacity();
        float opacity = (float)a;// (a * 255.0) ;
        //opacity = (opacity>255) ? 255 : opacity;
        java.awt.Color conColor = new java.awt.Color(r, g, b, opacity);

        if (selectedCaster!=null){
            selectedCaster.color=conColor;
        }

        awtColor=conColor;

    }

    public Color getCasterColor(){
        return awtColor;
    }

    //TODO use this to store rendered Casters that haven't changed
    List<CasterCO> casterRenderCache = new ArrayList<>();

    public void setCasterRenderCache(List cache){
        casterRenderCache=cache;
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

    public Caster getSelectedCaster() {
        return selectedCaster;
    }

    public Caster save(Caster caster) {

        casterArrayList.add(caster);

//        counter++;
//        caster.setName("caster " + counter);

        return caster;

    }

    public Caster findByName(String name) {
        for (Caster caster : casterArrayList) {
            if (caster.getName().equals(name)) {
                return caster;
            }
        }

        return null;
    }


    public List<Caster> getAll() {
        return casterArrayList;
    }
}
