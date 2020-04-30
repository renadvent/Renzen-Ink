package com.erick.model;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

// must be customized by frame
// just values
public class Part {

    // shape can be a box, circle, line, curve, triangle
    // for now, just rect

    public Rectangle2D.Double rect = null;

    boolean altered; // used during constraint process to revalidate within function

    Part(double sds, double sdx, double sdy) {
        stand_dev_size = sds;
        stand_dev_x = sdx;
        stand_dev_y = sdy;
    }

    LinkedList<Part> parts = new LinkedList<Part>();


    // in relation to frame
    double stand_dev_size;
    double stand_dev_y;
    double stand_dev_x;

    set_functions set;

    class set_functions {
        void stand_dev_size(double x) {
            stand_dev_size = x;
        }

        void stand_dev_y(double x) {
            stand_dev_y = x;
        }

        void stand_dev_x(double x) {
            stand_dev_x = x;
        }


    }

    get_functions get;

    class get_functions {

        public double stand_dev_size() {
            return stand_dev_size;
        }

        public double stand_dev_y() {
            return stand_dev_y;
        }

        public double stand_dev_x() {
            return stand_dev_x;
        }

    }

    //bbox

    int x;
    int y;
    int width;
    int height;

    Integer val_x; //validated by validate function
    Integer val_y;
    Integer val_w;
    Integer val_h;



}
