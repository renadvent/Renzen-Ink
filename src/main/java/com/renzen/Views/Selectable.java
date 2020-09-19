package com.renzen.Views;


import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class Selectable {

    public static LinkedList<Selectable> list = new LinkedList<Selectable>();
    public Rectangle2D.Double rect = null;
    public boolean selected = false;
    LinkedList<Shape> items = new LinkedList<Shape>();

    public Selectable() {
        System.out.println("selectable added");
        list.add(this);
    }

    public static int count_selected() {
        int temp = 0;
        for (Selectable s : list) {
            if (s.selected) {
                temp++;
            }
        }
        return temp;

    }

}
