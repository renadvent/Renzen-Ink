package io.renzen.ink.Services;

import io.renzen.ink.DomainObjects.PaintBrush;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;

@Service
public class BrushService {

    final ArrayList<PaintBrush> brushArrayList = new ArrayList<>();
    PaintBrush selectedBrush;
    Color awtColor;
    //set up init default
    BrushService() {
        awtColor = new Color(0, 0, 0, 1);
        selectedBrush = new PaintBrush(awtColor);
    }

    public PaintBrush getSelectedBrush() {
        return selectedBrush;
    }

    public void setSelectedBrush(PaintBrush paintBrush) {
        selectedBrush = paintBrush;
    }

    public double getSelectedBrushSize() {
        return selectedBrush.getSize();
    }

    public Color getSelectedColor() {
        return awtColor;
    }

    public void setSelectedColor(javafx.scene.paint.Color color) {
        this.awtColor = convertFXtoAWTColor(color);
        selectedBrush.setColor(awtColor);
    }

    public void setSelectedColor(Color color) {
        this.awtColor = color;
    }

    public Color convertFXtoAWTColor(javafx.scene.paint.Color color) {
        var r = (float) color.getRed();
        var g = (float) color.getGreen();
        var b = (float) color.getBlue();
        var a = (float) color.getOpacity();
        float opacity = a;// (a * 255.0) ;
        //opacity = (opacity>255) ? 255 : opacity;
        java.awt.Color conColor = new java.awt.Color(r, g, b, opacity);
        return conColor;
    }


}
