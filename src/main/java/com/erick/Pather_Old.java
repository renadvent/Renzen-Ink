package com.erick;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.awt.*;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Ellipse2D;
import java.awt.geom.*;

//import org.apache.commons.math3.analysis.interpolation.*;

// to make more stroke styles
public class Pather_Old {

    // stroke info
    Brusher brush;

    Color stroke_color; // color of stroke
    int line_opacity; // alpha of line (for layering)

    int layer_each_stroke_num; // number of lines rendered for each stroke (for layering)
    int[] connect_every_n_points;

    int cvs_on_main_stroke;
    int cvs_on_pre_stroke;
    int cvs_on_post_stroke;

    boolean render_pre_stroke;
    boolean render_main_stroke;
    boolean render_post_stroke;

    // need one for each cv on stroke
    LinkedList<control_vertex_gen_INFO> cvis_main;
    LinkedList<control_vertex_gen_INFO> cvis_pre;
    LinkedList<control_vertex_gen_INFO> cvis_post;

    LinkedList<Path2D.Double> last_rendered_curves;

    // LinkedList<envelope> envelopes = new LinkedList<envelope>();

    // example of inputs
    // should only need two ray collision points
    public Path2D.Double generate_curve(double sx, double sy, double ex, double ey) {

        return null;
    }

    // gonna take some work
    public LinkedList<Shape> generate_range_preview(double le, double y, int times) {

        // double y = 100;// y offset

        Path2D.Double temp = new Path2D.Double();
        temp.moveTo(0, 0 + y);

        // add list of cv's to curve
        // bezier curves
        // limit for now: 2 CV's
        // for (control_vertex_gen_INFO i : cvis_main){

        control_vertex_gen_INFO i = cvis_main.get(0);
        control_vertex_gen_INFO j = cvis_main.get(1);

        // not accurate
        LinkedList<Shape> ll = new LinkedList<Shape>();

        for (int p = 0; p < times; p++) {

            temp = new Path2D.Double();
            temp.moveTo(0, 0 + y);

            temp.curveTo((le * i.prop_along) + r(i.main_rand) + i.main_const, y + r(i.main_rand) + i.main_const, // ctrl
                                                                                                                   // pt
                                                                                                                   // 1
                    (le * j.prop_along) + r(i.main_rand) + i.main_const,
                    -j.main_const + y + r(i.main_rand) + i.main_const, // ctrl
                    // pt 2
                    // //
                    // used
                    // -
                    le, 0 + y); // end

            //System.out.println("i:" + i.prop_along_x + "j: " + j.prop_along_x);

            // not accruate

            CubicCurve2D.Double cc = new CubicCurve2D.Double(0, y, (le * i.prop_along), i.main_const + y, // ctrl pt 1
                    (le * j.prop_along), -j.main_const + y, // ctrl pt 2 // used -
                    le, 0 + y);

            // ll.add(cc);
            // ellipses not accurate
            ll.add(temp);
            ll.add(new Ellipse2D.Double(0, y, 10, 10));
            ll.add(new Ellipse2D.Double(le, y, 10, 10));
            ll.add(new Ellipse2D.Double((le * i.prop_along), i.main_const + y, 10, 10));
            ll.add(new Ellipse2D.Double((le * j.prop_along), y - j.main_const, 10, 10));

        }

        return ll;
    }

    public LinkedList<Path2D.Double> generate_layered_preview_ALONG_component() {
        // generates a preview with
        // layers_per_stroke_num
        // connect_every_n_point

        return null;
    }

    public void generate_layered_preview_THROUGH_component() {
        // generates a preview with
        // layers_per_stroke_num
        // connect_every_n_point
    }

    public Pather_Old() {

        // defaults
        line_opacity = 100;
        stroke_color = new Color(0, 0, 0, line_opacity);
        layer_each_stroke_num = 5;
        connect_every_n_points = new int[] { 3, 5 };

        cvs_on_main_stroke = 2;
        cvs_on_pre_stroke = 2;
        cvs_on_post_stroke = 2;

        render_pre_stroke = true;
        render_main_stroke = true;
        render_post_stroke = true;

        cvis_pre = new LinkedList<control_vertex_gen_INFO>();
        cvis_pre.add(new control_vertex_gen_INFO(1 / 4));
        cvis_pre.add(new control_vertex_gen_INFO(3 / 4));

        cvis_main = new LinkedList<control_vertex_gen_INFO>();
        cvis_main.add(new control_vertex_gen_INFO(1/4)); // issue when passing decimal ex .25 or .75
        cvis_main.add(new control_vertex_gen_INFO(3/4));

        cvis_post = new LinkedList<control_vertex_gen_INFO>();
        cvis_post.add(new control_vertex_gen_INFO(1 / 4));
        cvis_post.add(new control_vertex_gen_INFO(3 / 4));

    }

    public void set_render_pre_stroke(boolean a) {
        render_pre_stroke = a;
    };

    public void set_render_main_stroke(boolean a) {
        render_main_stroke = a;
    };

    public void set_render_post_stroke(boolean a) {
        render_post_stroke = a;
    };

    // public void render_stroke(){};
    public void set_connect_every_n_points(int[] a) {
        connect_every_n_points = a;
    }

    public void set_lines_per_stroke(int a) {
        layer_each_stroke_num = a;
    }

    // ---------------------------

    // LinkedList<cust_curve> cust_curves = new LinkedList<cust_curves>();

    public void generate_CV() {

    }

    public void render_stroke_across_points(Graphics2D g2d, LinkedList<point> intersection_points) {

        last_rendered_curves = new LinkedList<Path2D.Double>();

        point last_point = null;
        g2d.setColor(new Color(0, 0, 0, line_opacity));

        // GO THROUGH LIST, SKIPPING POINTS BASED ON SKIP_ARRAY
        for (int element : connect_every_n_points) {

            ListIterator<point> li = intersection_points.listIterator();

            if (!li.hasNext()) {
                continue;
            } // if nothting strikes

            point i = li.next();

            // EACH PASS THROUGH LIST
            while (true) {

                if (last_point != null) {
                    // CODE TO DRAW LINE STYLE

                    double x_width = i.get_x() - last_point.get_x();
                    double y_height = i.get_y() - last_point.get_y();

                    for (int j = 0; j < layer_each_stroke_num; j++) {

                        int mod = (int) r(5);
                        int len = 10;
                        int mod2 = 30;

                        double theta = Math.atan2(y_height, x_width); // angle between current and last stroke

                        Path2D.Double pre_stroke = pre_stroke(g2d, i, len, theta);
                        Path2D.Double main_stroke = main_stroke(last_point, i, x_width, y_height, theta, cvis_main);
                        Path2D.Double post_stroke = post_stroke(g2d, last_point, len, mod2, theta);

                        // g2d.setStroke(new BasicStroke(5));

                        if (render_pre_stroke) {
                            g2d.draw(pre_stroke);
                            last_rendered_curves.add(pre_stroke);
                        }

                        if (render_main_stroke) {
                            g2d.draw(main_stroke);
                            last_rendered_curves.add(main_stroke);
                        }

                        if (render_post_stroke) {
                            g2d.draw(post_stroke);
                            last_rendered_curves.add(post_stroke);
                        }

                        // add stroke to calling viewers curve list

                    }

                } else {
                    // render first line
                    Path2D.Double first_stroke = new Path2D.Double();
                    first_stroke.moveTo(i.get_x(), i.get_y());
                    first_stroke.curveTo(i.get_x() + (1 / 4) * 5, i.get_y() + (1 / 4) * 5, i.get_x() + (3 / 4) * 5,
                            i.get_y() + (1 / 4) * 5, i.get_x() + r(5), i.get_y() + r(5));
                    // g2d.setColor(new Color(0, 0, 0, line_thickness));
                    g2d.draw(first_stroke);
                }

                // code to check that skip_array doesn't go out of index when skipping through
                // intersection_points
                // and if it does, just grab the last element of the intersection_points
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

            }

        }
    }

    private Path2D.Double post_stroke(Graphics2D g2d, point last_point, int len, int mod2, double theta) {
        // POST STROKE (last point to random point)
        Path2D.Double post_stroke = new Path2D.Double();
        int rand_3 = 10;
        post_stroke.moveTo(last_point.get_x() + r(mod2), last_point.get_y() + r(mod2));

        post_stroke.curveTo(last_point.get_x() - (Math.cos(theta) * (1 / 4) * len) + r(rand_3),
                last_point.get_y() - (Math.sin(theta) * (1 / 4) * len) + r(rand_3),
                last_point.get_x() - (Math.cos(theta) * (3 / 4) * len) + r(rand_3),
                last_point.get_y() - (Math.sin(theta) * (3 / 4) * len) + r(rand_3),
                last_point.get_x() - (Math.cos(theta) * len) + r(rand_3),
                last_point.get_y() - (Math.sin(theta) * len) + r(rand_3));

        // g2d.setColor( new Color (0,0,255, line_thickness));
        // g2d.draw(post_stroke);
        return post_stroke;
    }

    private Path2D.Double main_stroke(point last_point, point i, double x_width, double y_height, double theta, LinkedList<control_vertex_gen_INFO> cv) {
        // STROKE (from last point to this point)
        Path2D.Double stroke = new Path2D.Double();
        int rand_2 = 30;
        int curvy_2 = 25;
        stroke.moveTo(last_point.get_x(), last_point.get_y());

        control_vertex_gen_INFO a = cv.get(0);
        control_vertex_gen_INFO b = cv.get(1);

        stroke.curveTo(
                last_point.get_x() + Math.cos(theta) * a.prop_along * x_width + r(a.main_rand) + a.main_const,
                last_point.get_y() + Math.sin(theta) * a.prop_along * y_height + r(a.main_rand) + a.main_const,

                last_point.get_x() + Math.cos(theta) * ((b.prop_along) * x_width) + r(b.main_rand) + b.main_const,
                last_point.get_y() + Math.sin(theta) * ((b.prop_along) * y_height) + r(b.main_rand) - b.main_const,
                
                i.get_x(), i.get_y());

        return stroke;
    }

    private Path2D.Double pre_stroke(Graphics2D g2d, point i, int len, double theta) {
        // PRE-STROKE (from this point to random point)
        Path2D.Double pre_stroke = new Path2D.Double();
        int rand_1 = 30;
        pre_stroke.moveTo(i.get_x() + r(rand_1), i.get_y() + r(rand_1)); // set
                                                                         // initial
                                                                         // point
                                                                         // for
                                                                         // pre-stroke

        pre_stroke.curveTo(i.get_x() + (Math.cos(theta) * (1 / 4) * len) + r(rand_1),
                i.get_y() + (Math.sin(theta) * (1 / 4) * len) + r(rand_1),
                i.get_x() + (Math.cos(theta) * (3 / 4) * len) + r(rand_1),
                i.get_y() + (Math.sin(theta) * (3 / 4) * len) + r(rand_1),
                i.get_x() + (Math.cos(theta) * len) + r(rand_1), i.get_y() + (Math.sin(theta) * len) + r(rand_1));

        // g2d.setColor( new Color (255,0,0, line_thickness));
        // g2d.draw(pre_stroke);
        return pre_stroke;
    }

    // COMPARTMENTALIZING

    class point_weights {
        LinkedList<point> weights = new LinkedList<point>();
    }

    public point average_inner_points(LinkedList<point> ll) {
        // add weights to points
        return null;
    }

    // get a random number for stroke drawing
    public double r(double range) {

        Random r = new Random();
        double x = r.nextInt((int) range);

        Random f = new Random();

        if (f.nextBoolean() == true) {
            return (x * (-1));
        }

        return x;
    }

    public double get_rand(double range) {

        Random r = new Random();
        double x = r.nextInt((int) range);

        Random f = new Random();

        if (f.nextBoolean() == true) {
            return (x * (-1));
        }

        return x;
    }

}