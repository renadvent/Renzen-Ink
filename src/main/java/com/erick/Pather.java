package com.erick;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.awt.geom.Path2D;
import java.awt.Graphics2D;

public class Pather {

    // new variables
    double max_dist_between_points;

    // Brusher brush=new Brusher();
    int layers = 5;
    int[] connect_at = new int[] {1};//{ 3, 5 };
    Curve stroke = new Curve();
    LinkedList<Path2D.Double> last_rendered_curves=new LinkedList<Path2D.Double>();
    double rex=5;//rand ends
    double rey=5;

    class Curve {

        LinkedList<Section> sections = new LinkedList<Section>();



        Curve(){
            sections.add(new Section());
            //sections.add(new Section());
            //sections.getLast().cvs.get(0).prop=(5/4);
            //sections.getLast().cvs.get(0).prop=(7/4);
        }

        class Section {
            boolean render=true;

            // need to order LinkedList
            LinkedList<CV> cvs = new LinkedList<CV>();

            Section() {
                // default
                cvs.add(new CV(1 / 4, 30, 30, 0, 30));
                cvs.add(new CV(3 / 4, 30, 30, 0, -30));
            }

            public class CV {

                double prop;

                double rx;
                double ry;

                double ox;
                double oy;

                CV(double px, double rx, double ry, double ox, double oy) {
                    prop = px;
                    this.rx = rx;
                    this.ry = ry;
                    this.ox = ox;
                    this.oy = oy;
                }

            }
        }
    }

    public void create_curves_across_points(LinkedList<point> intersection_points, boolean reset) {

        if (reset){
            last_rendered_curves = new LinkedList<Path2D.Double>();
        }

        point last_point = null;

        if (connect_at.length==0){
            System.out.println("0 size");
            return;
        }

        System.out.println("pather: "+connect_at.length);

        for (int element : connect_at) {

            ListIterator<point> li = intersection_points.listIterator();

            if (!li.hasNext()) {
                continue;
            } // if nothting strikes

            point i = li.next();

            // EACH PASS THROUGH LIST
            while (true) {

                if (last_point != null) {

                    double x_width = i.get_x() - last_point.get_x();
                    double y_height = i.get_y() - last_point.get_y();

                    for (int j = 0; j < layers; j++) { // number of times to repeat stroke

                        double theta = Math.atan2(y_height, x_width); // angle between current and last stroke

                        Path2D.Double path = new Path2D.Double();
                        path.moveTo(last_point.get_x()+r(rex), last_point.get_y()+r(rey));

                        for (Curve.Section sect : stroke.sections) { // get path for each section

                            Curve.Section.CV a = sect.cvs.get(0);
                            Curve.Section.CV b = sect.cvs.get(1);

                            path.curveTo(
                                    last_point.get_x() + Math.cos(theta) * (a.prop * x_width) + r(a.rx) + a.ox,
                                    last_point.get_y() + Math.sin(theta) * (a.prop * y_height) + r(a.ry) + a.oy,

                                    last_point.get_x() + Math.cos(theta) * ((b.prop) * x_width) + r(b.rx) + b.ox,
                                    last_point.get_y() + Math.sin(theta) * ((b.prop) * y_height) + r(b.ry) + b.oy,

                                    i.get_x()+r(rex), i.get_y()+r(rey));

                            last_rendered_curves.add(path);

                        }

                    }
                }
                int in = intersection_points.indexOf(i);

                if (i == intersection_points.getLast()) {
                    i = null;
                    last_point = null;
                    break;
                } else if ((in + element >= intersection_points.size()) && (i == intersection_points.getLast())
                        && (in != -1)) {
                    i = null;
                    last_point = null;
                    break;
                } else if ((in + element >= intersection_points.size()) && (i != intersection_points.getLast())
                        && (in != -1)) {
                    last_point = i;
                    i = intersection_points.getLast();
                } else {
                    last_point = i;
                    for (int q = 0; q < element; q++) {
                        i = li.next();
                    }
                }

                /*
                 * } else { // render first line Path2D.Double first_stroke = new
                 * Path2D.Double(); first_stroke.moveTo(i.get_x(), i.get_y());
                 * first_stroke.curveTo(i.get_x() + (1 / 4) * 5, i.get_y() + (1 / 4) * 5,
                 * i.get_x() + (3 / 4) * 5, i.get_y() + (1 / 4) * 5, i.get_x() + r(5), i.get_y()
                 * + r(5));
                 */

                // code to check that skip_array doesn't go out of index when skipping through
                // intersection_points
                // and if it does, just grab the last element of the intersection_points

            }

            //System.out.println(last_rendered_curves.size());
        }

    }

    public double r(double range) {

        if (range==0){
            return 0;
        }

        Random r = new Random();
        double x = r.nextInt((int) range);

        Random f = new Random();

        if (f.nextBoolean() == true) {
            return (x * (-1));
        }

        return x;
    }

}