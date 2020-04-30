package com.erick.model;

import com.erick.view.drawable;
import com.erick.view.selectable;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


import java.util.LinkedList;

// must be customized by frame
// just values
public class Part extends selectable implements drawable {


    //https://stackoverflow.com/questions/14942881/image-3d-rotation-opencv
    //https://docs.opencv.org/2.4/modules/imgproc/doc/geometric_transformations.html
    //https://stackoverflow.com/questions/7019407/translating-and-rotating-an-image-in-3d-using-opencv
    //https://stackoverflow.com/questions/17087446/how-to-calculate-perspective-transform-for-opencv-from-rotation-angles
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    // shape can be a box, circle, line, curve, triangle
    // for now, just rect

    //public Rectangle2D.Double rect = null;

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
