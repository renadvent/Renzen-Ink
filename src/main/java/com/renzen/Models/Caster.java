package com.renzen.Models;

import com.renzen.Controllers.Brusher_Controller;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;

// this class creates rays which intersect with the canvas component buffer
// these intersection points are then used to create guide points for drawing strokes

@Getter
@Setter
public class Caster {

    // temp
    public boolean cast_through = false;
    public boolean cast_along = true;

    public boolean casts_rendered = false;
    public boolean casts_updated = false; // redraw without re-rendering
    public boolean cvs_edited = false; //

    public boolean cast_from_source = false;

    public int min_penetrations = 0;
    public int max_penetrations = 0;
    public double max_ray_length = 1200;
    public int tolerance = 75;// 100;
    public int rays; // number of rays coming from POV line

    // these to be replaced
    public BufferedImage parent_component_rendered_buffer; // POV's read from this buffer

    // develop these
    public BufferedImage shade_buffer = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);
    public BufferedImage stroke_buffer = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);
    public BufferedImage tool_buffer = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);
    public BufferedImage highlighted_tools_buffer = create_highlight_buffer();
    public BufferedImage highlighted_strokes_buffer = create_highlight_buffer();

    // these to be replaced
    public double from_x;
    public double from_y;// position of POV line in convas
    public double to_x;
    public double to_y;

    // TEMP
    public boolean highlighted = false;
    // used to determine stroke drawing algorithm
    public Pather p1 = new Pather();
    public Tracer t = new Tracer(this);
    public Brusher bb = new Brusher();
    // develop this
    LinkedList<Point> caster_path;
    int flip_status = 1; // used to flip POV if necessary (if rays are facing the wrong direction)
    LinkedList<Point> ray_path = new LinkedList<Point>(); // used to render ray path for tools

    // PUBLIC
    LinkedList<Point> intersection_points = new LinkedList<Point>(); // list of ray/component intersections
    LinkedList<Path2D.Double> stroke_curves = new LinkedList<Path2D.Double>(); // list of curves rendered
    LinkedList<Point> draw_from_caster = new LinkedList<Point>();

    public int get_ray_count() {
        return rays;
    }

    // how many rays the viewer shoots along its length
    public void set_ray_count(int rays) {
        this.rays = rays;
    }

    public void set_to(double f, double g) {
        this.to_x = (int) f;
        this.to_y = (int) g;
    }

    public void set_from(double d, double e) {
        this.from_x = (int) d;
        this.from_y = (int) e;
    }

    // renders the tools (the ray path, and the viewer line) to the tool buffer.
    public void update_tools() {

        if (!casts_rendered) {

            Graphics2D g2d = tool_buffer.createGraphics();
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints(rh);
            g2d.setColor(Color.BLUE);

            g2d.draw(new Line2D.Double(from_x, from_y, to_x, to_y));
            g2d.setColor(Color.LIGHT_GRAY);

            Iterator<Point> iter = ray_path.iterator();

            while (iter.hasNext()) {
                Point start = iter.next();
                g2d.draw(new Line2D.Double(start.get_ray_x(), start.get_ray_y(), start.get_x(), start.get_y()));
            }

            g2d.dispose();

            copy_to_highlight_tool_buffer(tool_buffer);
        }

    }

    void render_list_to_buffer(LinkedList<Path2D.Double> ll, BufferedImage b) {

        Graphics2D g2d = b.createGraphics();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        g2d.setStroke(new BasicStroke(bb.thickness));
        g2d.setColor(new Color(bb.color.getRed(), bb.color.getGreen(), bb.color.getBlue(), bb.opacity));
        for (Path2D.Double p : ll) {
            g2d.draw(p);
        }
    }

    // renders the strokes to the canvas buffer
    public void update_casts() {

        Graphics2D g2d = null;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        if (casts_updated) {

            wipe_buffer(stroke_buffer);
            wipe_buffer(shade_buffer);
            wipe_buffer(highlighted_strokes_buffer);
            wipe_buffer(tool_buffer);
            wipe_buffer(highlighted_tools_buffer);

            if (p1.last_rendered_curves != null) {
                g2d = stroke_buffer.createGraphics(); // might change
                g2d.setRenderingHints(rh);
                Brusher_Controller.draw_curves(bb, g2d, p1.last_rendered_curves);
                render_list_to_buffer(p1.last_rendered_curves, highlighted_strokes_buffer);
                casts_updated = false;
                g2d.dispose();
                return;
            }

            return;
            //draw saved last curves with new color/opacity

        }

        if (!casts_rendered) {

            reset_caster();
            t.get_intersection_points();
            update_tools();


            p1.last_rendered_curves = new LinkedList<Path2D.Double>();

            if (cast_from_source) {

                g2d = stroke_buffer.createGraphics();
                g2d.setRenderingHints(rh);

                p1.create_curves_across_points(draw_from_caster, false);
                Brusher_Controller.draw_curves(bb, g2d, p1.last_rendered_curves);

                g2d.dispose();

            }

            if (cast_along) {

                g2d = stroke_buffer.createGraphics();
                g2d.setRenderingHints(rh);

                p1.create_curves_across_points(intersection_points, false);
                Brusher_Controller.draw_curves(bb, g2d, p1.last_rendered_curves);

                g2d.dispose();

            }

            if (cast_through && (max_penetrations > 0)) {

                g2d = (Graphics2D) shade_buffer.getGraphics();
                g2d.setRenderingHints(rh);

                p1.create_curves_across_points(intersection_points, false);
                Brusher_Controller.draw_curves(bb, g2d, p1.last_rendered_curves);

                g2d.dispose();

            }

            render_list_to_buffer(p1.last_rendered_curves, highlighted_strokes_buffer);


        }

        casts_rendered = true;

    }

    public BufferedImage create_highlight_buffer() {

        return new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);
    }

    private void copy_to_highlight_strokes_buffer(BufferedImage b) {
        Graphics2D g2d;
        g2d = highlighted_strokes_buffer.createGraphics();
        g2d.drawImage(b, null, 0, 0);
        g2d.dispose();
    }

    private void copy_to_highlight_tool_buffer(BufferedImage b) {
        Graphics2D g2d;
        g2d = highlighted_tools_buffer.createGraphics();
        g2d.drawImage(b, null, 0, 0);
        g2d.dispose();
    }

    private void reset_caster() {
        intersection_points = new LinkedList<Point>();
        ray_path = new LinkedList<Point>();
        stroke_curves = new LinkedList<Path2D.Double>();
        draw_from_caster = new LinkedList<Point>();

        // reset image buffers
        wipe_buffer(stroke_buffer);
        wipe_buffer(shade_buffer);
        wipe_buffer(highlighted_strokes_buffer);
        wipe_buffer(tool_buffer);
        wipe_buffer(highlighted_tools_buffer);

    }

    private void wipe_buffer(BufferedImage b) {
        Graphics2D g2d = b.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2d.fillRect(0, 0, b.getWidth(), b.getHeight());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
    }

    //TODO implement
    public void flip() {
        flip_status = flip_status * -1;
    }
}