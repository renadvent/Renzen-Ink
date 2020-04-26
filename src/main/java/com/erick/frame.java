package com.erick;

import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;


// handles all aspects of proportion
// including rendering


/*

example flow:

frame=container.create_frame()

part1=frame.create_part("eye")
part1.set.position(new relative_pos(.5,.7))
part1.set.size(new absolute_size(50,50))

part1.set.shape(new Shape(....))

part2=frame.create_part()

con=frame.create_constraint(part1,part2)
con.set.base(part1)
con.set.size_ratio(1.5)
con.set.dist_ratio(3)
con.set.angle(90)

frame.validate_constraints // updates values based on constraints if possible

frame.draw_bBoxes(b)
frame.draw_parts(b)

//-------------

texture.update_components
texture.drawComponents
    Component.frame.draw_parts
caster.update_cast
caster.draw_casts


 */

public class frame { // collection of frames

    // a frame is a box that contains parts
    // that are related positionally by position

    // a frame is a collection of parts and other frames

    String name;
    LinkedList<Line2D.Float> last_gen_curves = new LinkedList<Line2D.Float>();
    LinkedList<frame> frames = new LinkedList<frame>();
    LinkedList<part> parts = new LinkedList<part>();
    LinkedList<constraint> constraints = new LinkedList<constraint>();

    double x;
    double y;

    double width;
    double height;

    double pitch;
    double yaw;
    double roll;



    void create_curves(part p) {
    }

    void update_curve_CVs() {
    }

    void filter_curve() {
    }

    LinkedList<Line2D.Float> get_last_curves() {
        return last_gen_curves;
    }



    void validate_constraints() {
    }

    //calc position of bb of part
    double part_bbx1(part p) {
        return 0;
    }

    double part_bby1(part p) {
        return 0;
    }

    double part_bbx2(part p) {
        return 0;
    }

    double part_bby2(part p) {
        return 0;
    }


    void create_part() {

    }

    void rotate(double p, double y, double r) {

    }

    void place(double x, double y) {

    }

    void translate(double tx, double ty) {

    }

    // must be customized by frame
    // just values
    class part {

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

    }

    class bBox {
        double x;
        double y;
        double width;
        double height;
    }

    class constraint {

        part base_part; // box
        part constrained_part; // box


        double size_ratio;
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
