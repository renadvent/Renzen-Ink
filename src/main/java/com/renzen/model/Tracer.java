package com.renzen.model;

import java.awt.*;
import java.util.LinkedList;

public class Tracer {

    Caster c;

    Tracer(Caster c) {

        this.c = c;
    }

    private void add_intersection_point(Point a) {
        c.intersection_points.add(a);
    }

    public void get_intersection_points() {

        double viewer_width = c.from_x - c.to_x;
        double viewer_height = c.from_y - c.to_y;
        double width_between_rays = viewer_width / (c.rays - 1);
        double height_between_rays = viewer_height / (c.rays - 1);
        double angle_perp_to_veiwer = Math.atan2(viewer_height, viewer_width) + Math.toRadians(90);

        Color canvas_color = new Color(255, 255, 255);// new Color(0, 0, 0, 0);

        int canvas_width = c.parent_component_rendered_buffer.getWidth();
        int canvas_height = c.parent_component_rendered_buffer.getHeight();

        // move along viewer
        for (int i = 0; i < c.rays; i++) {

            // starting position for ray
            double current_viewer_x = c.from_x - ((i * width_between_rays));
            double current_viewer_y = c.from_y - ((i * height_between_rays));

            // reset for next ray
            int penetration_count = 0;
            Color this_pixel = null;
            Color last_pixel = null;
            Point last_pen = null;

            // move along ray
            for (int j = 0; j < c.max_ray_length; j++) {

                // continue along ray path

                //added Math.abs to make constant direction when moving caster //removed
                double current_ray_x = (j * (Math.cos(angle_perp_to_veiwer)) * c.flip_status) + current_viewer_x;
                double current_ray_y = (j * (Math.sin(angle_perp_to_veiwer)) * c.flip_status) + current_viewer_y;

                if (on_canvas(canvas_width, canvas_height, current_ray_x, current_ray_y)) {

                    this_pixel = new Color(
                            c.parent_component_rendered_buffer.getRGB((int) current_ray_x, (int) current_ray_y), true);

                    // test if continuance of last penetration // might have to move down

                    if (last_pixel == null) {// first pixel //should only be entered once
                        last_pixel = this_pixel;
                        canvas_color = this_pixel;
                        continue;
                    }

                    if ((last_pixel != null) && (last_pen != null)) {
                        if ((color_similiar(this_pixel, last_pixel)
                                && (color_similiar(this_pixel, last_pen.get_color())))) {
                            continue;
                        }
                    }

                    last_pixel = this_pixel;

                    // test if color is similiar to canvaas
                    if (!color_similiar(this_pixel, canvas_color)) {

                        if (c.max_penetrations == penetration_count) {

                            add_collision(current_ray_x, current_ray_y, current_viewer_x, current_viewer_y, last_pen,
                                    this_pixel);
                            store_to_ray_path(current_ray_x, current_ray_y, current_viewer_x, current_viewer_y);
                            break;

                        } else {
                            penetration_count += 1;
                            last_pen = add_penetration(current_ray_x, current_ray_y, current_viewer_x, current_viewer_y,
                                    last_pen, this_pixel);
                        }

                    }
                }
            }

        }

    }

    private boolean color_similiar(Color color_this_loop, Color color_last_loop) {

        int r = color_this_loop.getRed() - color_last_loop.getRed();
        int g = color_this_loop.getGreen() - color_last_loop.getGreen();
        int b = color_this_loop.getBlue() - color_last_loop.getBlue();

        r = Math.abs(r);
        g = Math.abs(g);
        b = Math.abs(b);

        return ((r < c.tolerance) && (g < c.tolerance) && (b < c.tolerance));
    }

    private Point add_penetration(double ray_path_x, double ray_path_y, double rx, double ry, Point last_pen,
                                  Color color) {

        Point new_pen;

        if (last_pen == null) {

            new_pen = new Point((int) ray_path_x, (int) ray_path_y);
            new_pen.set_origin_ray(new Point((int) rx, (int) ry));
            new_pen.intersection_color = color;

        } else {

            new_pen = new Point((int) ray_path_x, (int) ray_path_y);
            new_pen.set_origin_ray(new Point((int) rx, (int) ry));
            new_pen.penetration_points = (LinkedList<Point>) last_pen.penetration_points.clone();
            new_pen.penetration_points.add(last_pen);
            new_pen.intersection_color = color;

            last_pen.next = new_pen;

        }

        return new_pen;
    }

    private void add_collision(double ray_path_x, double ray_path_y, double rx, double ry, Point last_pen,
                               Color found_color) {

        Point new_intersection_point;
        new_intersection_point = new Point((int) ray_path_x, (int) ray_path_y);
        new_intersection_point.set_origin_ray(new Point((int) rx, (int) ry));
        new_intersection_point.set_color(found_color);

        if (last_pen != null) {
            new_intersection_point.penetration_points = (LinkedList<Point>) last_pen.penetration_points.clone();

            new_intersection_point.penetration_points.add(last_pen);
            last_pen = new_intersection_point;
        }

        add_intersection_point(new_intersection_point); // these are rendered
        last_pen = null;
    }

    private boolean on_canvas(int canvas_width, int canvas_height, double ray_path_x, double ray_path_y) {
        return ((int) ray_path_x < canvas_width) && ((int) ray_path_y < canvas_height) && ((int) ray_path_x > 0)
                && ((int) ray_path_y > 0);
    }

    private void store_to_ray_path(double current_ray_x, double current_ray_y, double current_viewer_x,
                                   double current_viewer_y) {
        Point p = new Point((int) current_ray_x, (int) current_ray_y);
        Point q = new Point((int) current_viewer_x, (int) current_viewer_y);
        p.set_origin_ray(q);
        c.ray_path.add(p);

        // temp
        c.draw_from_caster.add(q);
        c.draw_from_caster.add(p);
    }


}