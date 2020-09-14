package com.renzen;

import java.awt.*;

public class defcon extends AbstractController {

    public static final String ELEMENT_TEXT_PROPERTY = "Text";
    public static final String ELEMENT_FONT_PROPERTY = "Font";
    public static final String ELEMENT_X_PROPERTY = "X";
    public static final String ELEMENT_Y_PROPERTY = "Y";
    public static final String ELEMENT_OPACITY_PROPERTY = "Opacity";
    public static final String ELEMENT_ROTATION_PROPERTY = "Rotation";


    public static final String NEW_COMPONENT = "Component";
    public static final String NEW_VIEW = "View";
    public static final String NEW_CASTER = "Caster";
    public static final String LOADED_COMPONENT = "Load_Comp";

    //  Code omitted

    public void changeElementText(String newText) {
        setModelProperty(ELEMENT_TEXT_PROPERTY, newText);
    }

    public void changeElementFont(Font newFont) {
        setModelProperty(ELEMENT_FONT_PROPERTY, newFont);
    }

    public void changeElementXPosition(int newX) {
        setModelProperty(ELEMENT_X_PROPERTY, newX);
    }

    public void changeElementYPosition(int newY) {
        setModelProperty(ELEMENT_Y_PROPERTY, newY);
    }

    public void changeElementOpacity(int newOpacity) {
        setModelProperty(ELEMENT_OPACITY_PROPERTY, newOpacity);
    }

    public void changeElementRotation(int newRotation) {
        setModelProperty(ELEMENT_ROTATION_PROPERTY, newRotation);
    }

}