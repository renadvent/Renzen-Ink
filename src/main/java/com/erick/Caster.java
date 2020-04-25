package com.erick;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.util.Iterator;
import java.util.LinkedList;

// this class creates rays which intersect with the canvas component buffer
// these intersection points are then used to create guide points for drawing strokes

public class Caster {

    // TODO
    // add re-randomize draw function

    // defaults

    // temp
    boolean cast_through = false;
    boolean cast_along = true;
    boolean cast_press = false;
    boolean cast_light = false;
    boolean cast_splatter = false;

    boolean casts_rendered = false;
    boolean casts_updated = false; // redraw without re-rendering

    boolean cast_from_source = false;

    int min_penetrations = 0;
    int max_penetrations = 0;
    double max_ray_length = 1200;
    int tolerance = 75;// 100;
    int rays; // number of rays coming from POV line

    // these to be replaced
    public BufferedImage parent_component_rendered_buffer; // POV's read from this buffer
    // public BufferedImage parent_stroke_rendered_buffer; // POV's write to this
    // buffer
    // public BufferedImage parent_tool_rendered_buffer; // POV tools render to this
    // buffer

    // develop these
    public BufferedImage shade_buffer = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);
    public BufferedImage stroke_buffer = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);
    public BufferedImage tool_buffer = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);
    public BufferedImage highlighted_tools_buffer = create_highlight_buffer();
    public BufferedImage highlighted_strokes_buffer = create_highlight_buffer();// = new BufferedImage(1280, 1024,
                                                                        // BufferedImage.TYPE_INT_ARGB);

    public BufferedImage press_buffer;
    public BufferedImage light_buffer;
    public BufferedImage splatter_buffer;

    // these to be replaced
    double from_x, from_y;// position of POV line in convas
    double to_x, to_y;

    // TEMP
    boolean highlighted = false;
    // Color highlight_color = new Color(255, 0, 0);

    // develop this
    LinkedList<point> caster_path;

    int flip_status = 1; // used to flip POV if necessary (if rays are facing the wrong direction)

    LinkedList<point> ray_path = new LinkedList<point>(); // used to render ray path for tools

    LinkedList<point> intersection_points = new LinkedList<point>(); // list of ray/component intersections
    LinkedList<Path2D.Double> stroke_curves = new LinkedList<Path2D.Double>(); // list of curves rendered
    LinkedList<point> draw_from_caster = new LinkedList<point>();

    // used to determine stroke drawing algorithm
    Pather_Old stroke_style = new Pather_Old();
    Shade_Style shade_style = new Shade_Style();

    // PUBLIC

    public int get_ray_count() {
        return rays;
    }

    public void rotate_caster(double theta) {

        theta = Math.toRadians(theta);
        AffineTransform at = AffineTransform.getRotateInstance(theta, from_x + ((from_x - to_x) / 2),
                from_y + ((from_y - to_y) / 2));

        double[] src = { from_x, from_y, to_x, to_y };
        double[] dest = { 0, 0, 0, 0 };

        at.transform(src, 0, dest, 0, 2);

        from_x = dest[0];
        from_y = dest[1];
        to_x = dest[2];
        to_y = dest[3];

    }

    public void flip() {
        flip_status = flip_status * (-1);
    }

    // the viewer is a line, and this is it's start point
    public void set_from(int x, int y) {
        this.from_x = x;
        this.from_y = y;
    };

    // the end point of the viewer
    public void set_to(int to_x, int to_y) {
        this.to_x = to_x;
        this.to_y = to_y;
    }

    public void set_to(double f, double g) {
        this.to_x = (int) f;
        this.to_y = (int) g;
    }

    public void set_from(double d, double e) {
        this.from_x = (int) d;
        this.from_y = (int) e;
    }

    // how many rays the viewer shoots along its length
    public void set_ray_count(int rays) {
        this.rays = rays;
    };

    // renders the tools (the ray path, and the viewer line) to the tool buffer.
    public void update_tools() {

        if (!casts_rendered) {

            Graphics2D g2d = tool_buffer.createGraphics();
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints(rh);
            g2d.setColor(Color.BLUE);

            g2d.draw(new Line2D.Double(from_x, from_y, to_x, to_y));
            g2d.setColor(Color.LIGHT_GRAY);

            Iterator<point> iter = ray_path.iterator();

            while (iter.hasNext()) {
                point start = iter.next();
                g2d.draw(new Line2D.Double(start.get_ray_x(), start.get_ray_y(), start.get_x(), start.get_y()));
            }

            g2d.dispose();

            //render_list_to_buffer(shade_style.last_rendered_shade, );
            copy_to_highlight_tool_buffer(tool_buffer);
        }

    }

    void render_list_to_buffer(LinkedList<Path2D.Double> ll, BufferedImage b){

        Graphics2D g2d = b.createGraphics();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        g2d.setStroke(new BasicStroke(bb.thickness));
        //g2d.setColor(new Color(255,0,0,bb.opacity));
        g2d.setColor(new Color(bb.color.getRed(),bb.color.getGreen(),bb.color.getBlue(),bb.opacity));
        for (Path2D.Double p : ll){
            g2d.draw(p);
        }
    }

    Pather p1=new Pather();
    //Pather p2=new Pather();
    //Pather p3=new Pather();

    Tracer t=new Tracer(this);
    Brusher bb = new Brusher();

    // renders the strokes to the canvas buffer
    public void update_casts() {

        if (casts_updated && casts_rendered) {
            casts_updated = false;
        }else if (casts_updated && !casts_rendered){
            Graphics2D g2d = null;
            //draw saved last curves with new color/opacity

        }

        if (!casts_rendered) {

            reset_caster();
            //Tracer t = new Tracer(this);
            t.get_intersection_points();
            update_tools();

            Graphics2D g2d = null;
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            p1.last_rendered_curves=new LinkedList<Path2D.Double>();

            if (cast_from_source) {

                g2d = stroke_buffer.createGraphics();
                g2d.setRenderingHints(rh);

                p1.create_curves_across_points(draw_from_caster,false);
                bb.draw_curves(g2d, p1.last_rendered_curves);
                
                //render_list_to_buffer(p1.last_rendered_curves, highlighted_strokes_buffer );
                g2d.dispose();
                
            }

            if (cast_along) {

                g2d = stroke_buffer.createGraphics();
                g2d.setRenderingHints(rh);          

                p1.create_curves_across_points(intersection_points,false);
                bb.draw_curves(g2d, p1.last_rendered_curves);
                //render_list_to_buffer(p1.last_rendered_curves, highlighted_strokes_buffer );

                g2d.dispose();

            }

            if (cast_through && (max_penetrations > 0)) {
                
                g2d = (Graphics2D) shade_buffer.getGraphics();
                g2d.setRenderingHints(rh);
                
                //Pather p3 = new Pather();
                //-----------------------------------------------------
                //int[] a = { 1, 2, 3, 4, 5, 6, 7 };
                //p1.connect_at = a;
                
                p1.create_curves_across_points(intersection_points,false);
                bb.draw_curves(g2d, p1.last_rendered_curves);
                //render_list_to_buffer(p1.last_rendered_curves, highlighted_strokes_buffer );

                g2d.dispose();

            }

            render_list_to_buffer(p1.last_rendered_curves, highlighted_strokes_buffer );


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

    // PRIVATE

    

    private void reset_caster() {
        intersection_points = new LinkedList<point>();
        ray_path = new LinkedList<point>();
        stroke_curves = new LinkedList<Path2D.Double>();
        draw_from_caster = new LinkedList<point>();
        //last_rendered_curves=new LinkedList<Path2D.Double>();

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

}