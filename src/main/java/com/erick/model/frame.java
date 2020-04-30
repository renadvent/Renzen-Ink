package com.erick.model;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;


// handles all aspects of proportion
// including rendering



public class frame { // collection of frames

    // a frame is a box that contains parts
    // that are related positionally by position

    // a frame is a collection of parts and other frames

    String name;

    public Rectangle2D.Double rect = null;

    LinkedList<frame> frames = new LinkedList<frame>(); // not using rn

    public LinkedList<Part> parts = new LinkedList<Part>();
    LinkedList<Constraint> constraints = new LinkedList<Constraint>();
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

        for (Part p : parts){

            // draw rect
            g2d.drawRect(x+p.val_x,y+p.val_y,x+p.width,y+p.height);

        }

    }

    LinkedList<Line2D.Float> get_last_curves() {
        return last_gen_curves;
    }

    // fills values of parts
    void validate_constraints() {

        LinkedList<Constraint> used_constraints = new LinkedList<>();

        for (Part p : parts){

            LinkedList<Constraint> temp = new LinkedList<Constraint>();

            for (Constraint i : constraints){
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

            for (Constraint j: temp){

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

    public Part create_part() {
        Part temp = new Part(0,0,0);
        parts.add(temp);
        return temp;
    }


    class bBox {

    }


}
