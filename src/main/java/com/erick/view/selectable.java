package com.erick.view;


import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class selectable {

    public static LinkedList<selectable> list = new LinkedList<selectable>();

    //BufferedImage hold;

    public static int count_selected(){
        int temp=0;
        for (selectable s : list){
            if (s.selected){
                temp++;
            }
        }
        return temp;

    }

    public selectable(){
        System.out.println("selectable added");
        list.add(this);
    }

    LinkedList<Shape> items = new LinkedList<Shape>();
    public Rectangle2D.Double rect = null;
    public boolean selected=false;

}
