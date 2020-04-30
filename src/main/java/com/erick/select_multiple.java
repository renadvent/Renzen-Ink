package com.erick;

import com.erick.view.selectable;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class select_multiple extends MouseInputAdapter {

    static LinkedList<selectable> boxes=null;
    Ink ink;

    public select_multiple(LinkedList<selectable> sl, Ink ink){


        boxes=selectable.list;
        //boxes=sl;
        this.ink=ink;

    }


    // mix up with list and items
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        for (selectable sb : boxes){

            if (sb.rect!=null) {
                System.out.println(("RECT EXISTS"));
                if (sb.rect.contains(e.getX(), e.getY())) {
                    System.out.println(("A: "+sb.selected));
                    sb.selected = (!sb.selected);
                    System.out.println(("B "+sb.selected));
                }
            }

        }

        System.out.println("REPAINT CLICK");
        ink.can_pan.repaint();
    }
}
