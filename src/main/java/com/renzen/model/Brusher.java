package com.renzen.model;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.LinkedList;

public class Brusher{ // used bystroke

    public int opacity=100;
    public Color color=new Color (0,0,0,opacity);

    public int thickness=1;

    public void draw_curves(Graphics2D g2d, LinkedList<Path2D.Double> lc){

        color=new Color (color.getRed(),color.getGreen(),color.getBlue(),opacity);

        g2d.setStroke(new BasicStroke(thickness));
        g2d.setColor(color);

        double [] coordinates1 = new double[6];
        double [] coordinates2 = new double[6];
        for (Path2D.Double p : lc){

            //skips short lines within a tolerance
            PathIterator pi = p.getPathIterator(null);

            pi.next();
            pi.currentSegment(coordinates1);
            pi.next();
            pi.currentSegment(coordinates2);

            //double distance = Math.hypot(coordinates1[0]-coordinates2[0], coordinates1[1]-coordinates2[1]);

            double distance = Math.hypot(coordinates1[0]-coordinates1[2], coordinates1[1]-coordinates2[3]);

            int distTol=50;
            if (distance<=distTol){
                continue;
            }

            g2d.draw(p);
        }

    }


}