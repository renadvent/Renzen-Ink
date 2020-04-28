package com.erick.model;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.LinkedList;

// renders from first penetration point to last collision
public class Shade_Style {

    Pather_Old stroke_style;
    LinkedList<Path2D.Double> last_rendered_curves;

    public Shade_Style() {

        stroke_style = new Pather_Old();
        stroke_style.set_lines_per_stroke(10);
        int[] a = { 1, 2, 3, 4, 5, 6, 7 };
        stroke_style.set_connect_every_n_points(a);
        stroke_style.set_render_post_stroke(false);
        stroke_style.set_render_pre_stroke(false);

    }

    public void render_shade(Graphics2D g2d, LinkedList<point> intersection_points) {

        LinkedList<point> stroke_shade = new LinkedList<point>();

        if (intersection_points.isEmpty()) {
            last_rendered_curves=new LinkedList<Path2D.Double>();
            return;
        }

        for (point i : intersection_points) {
            stroke_shade.add(i);
            if ((i.origin_ray != null) && (i.next == null) && (i != intersection_points.getFirst())) {

                if (i.penetration_points.getFirst() != null) {
                    stroke_shade.add(i.penetration_points.getFirst());
                } else {
                    // stroke_shade.add(i.origin_ray);
                }
            }
        }

        stroke_style.render_stroke_across_points(g2d, intersection_points);
        last_rendered_curves=stroke_style.last_rendered_curves;

    }

    private Path2D.Double flat_shade(LinkedList<point> intersection_points) {

        Path2D.Double path = new Path2D.Double();

        for (point i : intersection_points) {

            if (i == intersection_points.getFirst()) {
                path.moveTo(i.get_ray_x(), i.get_ray_y());
                path.lineTo(i.get_x(), i.get_y());

            } else if (i == intersection_points.getLast()) {
                path.lineTo(i.get_x(), i.get_y());
                path.lineTo(i.get_ray_x(), i.get_ray_y());
            } else {
                path.lineTo(i.get_x(), i.get_y());
            }

        }

        path.closePath();
        return path;
    };

}