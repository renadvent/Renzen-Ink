package com.erick;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;


// handles all aspects of proportion
// including rendering



public class frame { // collection of frames

    // a frame is a box that contains parts
    // that are related positionally by position

    // a frame is a collection of parts and other frames

    String name;

    LinkedList<frame> frames = new LinkedList<frame>(); // not using rn

    LinkedList<part> parts = new LinkedList<part>();
    LinkedList<constraint> constraints = new LinkedList<constraint>();
    LinkedList<Line2D.Float> last_gen_curves = new LinkedList<Line2D.Float>();

    int x;
    int y;

    int width;
    int height;

    double pitch;
    double yaw;
    double roll;

    // call after constraint validation
    void draw_parts(BufferedImage b){

        Graphics2D g2d = b.createGraphics();

        for (part p : parts){

            // draw rect
            g2d.drawRect(x+p.val_x,y+p.val_y,x+p.width,y+p.height);

        }

    }

    LinkedList<Line2D.Float> get_last_curves() {
        return last_gen_curves;
    }

    // fills values of parts
    void validate_constraints() {

        LinkedList<constraint> used_constraints = new LinkedList<>();

        for (part p : parts){

            LinkedList<constraint> temp = new LinkedList<constraint>();

            for (constraint i : constraints){
                if (i.base_part==p || i.constrained_part==p){

                    // make sure constraint not already calculated
                    if (!used_constraints.contains(i)) {
                        temp.add(i); // part uses this constraint
                        used_constraints.add(i);
                    }
                }
            }

            // calculate constraint impact on part here
            // in order added

            for (constraint j: temp){

                // make sure position satisfies all constraints
                // constraints are against the x,y (not counting width, height, for now)

                double check_w = j.base_part.width * j.size_ratio;
                double check_h = j.base_part.height * j.size_ratio;

                double check_x = j.base_part.x-j.constrained_part.x;
                double check_y = j.base_part.y-j.constrained_part.y;

                // how to check for incompatible constraints?
                // Simplex method?
                // https://people.richland.edu/james/ictcm/2006/simplex.html ?

                if (j.constrained_part.width<check_w){
                    j.constrained_part.val_w= (int) check_w;
                    j.constrained_part.altered=true;
                }

                if (j.constrained_part.height<check_h){
                    j.constrained_part.val_h= (int) check_h;
                    j.constrained_part.altered=true;
                }

            }



        }



    }

    public part create_part() {
        part temp = new part(0,0,0);
        parts.add(temp);
        return temp;
    }


    // must be customized by frame
    // just values
    static class part {

        // shape can be a box, circle, line, curve, triangle
        // for now, just rect

        boolean altered; // used during constraint process to revalidate within function

        part(double sds, double sdx, double sdy) {
            stand_dev_size = sds;
            stand_dev_x = sdx;
            stand_dev_y = sdy;
        }

        LinkedList<part> parts = new LinkedList<part>();


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

    class bBox {

    }

    class constraint {

        part base_part; // box
        part constrained_part; // box


        double size_ratio; // supposed to be in terms of frame
                            // not each other

        double dist_ratio; //relation to base_part
        double angle_2D; //

        double size_dev;
        double dist_dev;
        double angle_dev;

        get_functions get;
        set_functions set;

        class set_functions {
            void size_ratio(double s) {
                size_ratio = s;
            }

            void set_base(part p) {
                base_part = p;
            }

            ;
        }

        class get_functions {
            double size_ratio() {
                return size_ratio;
            }
        }


    }


}
