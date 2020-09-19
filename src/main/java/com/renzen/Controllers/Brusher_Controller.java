package com.renzen.Controllers;

import com.renzen.Models.Brusher;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.LinkedList;

public class Brusher_Controller {

    public static void draw_curves(Brusher brusher, Graphics2D g2d, LinkedList<Path2D.Double> lc) {

        brusher.color = new Color(brusher.color.getRed(), brusher.color.getGreen(), brusher.color.getBlue(), brusher.opacity);

        g2d.setStroke(new BasicStroke(brusher.thickness));
        g2d.setColor(brusher.color);

        double[] coordinates1 = new double[6];
        double[] coordinates2 = new double[6];
        for (Path2D.Double p : lc) {

            //skips short lines within a tolerance
            PathIterator pi = p.getPathIterator(null);

            pi.next();
            pi.currentSegment(coordinates1);
            pi.next();
            pi.currentSegment(coordinates2);

            double distance = Math.hypot(coordinates1[0] - coordinates1[2], coordinates1[1] - coordinates2[3]);

            int distTol = 50;
            if (distance <= distTol) {
                continue;
            }

            g2d.draw(p);
        }

    }
}
