package io.renzen.ink.Converters;

import io.renzen.ink.CommandObjectsDomain.CasterCO;
import io.renzen.ink.DomainObjects.Caster;
import io.renzen.ink.DomainObjects.Point;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

//TODO needs to be rewritten

/**
 * Takes base and Caster, performs intersection tests, and draws strokes
 */

@Component
@Data
public class CasterAndBaseToCasterCOConverter {


    /**
     * Curve-Generation Functions
     */

    Curve stroke = new Curve();

    BufferedImage stroke_buffer = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);
    BufferedImage tool_buffer = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);

    LinkedList<Path2D.Double> last_rendered_curves = new LinkedList<Path2D.Double>();
    java.util.List<Point> ray_path = new ArrayList<>(); // used to render ray path for tools
    LinkedList<Point> intersection_points = new LinkedList<>(); // list of ray/component intersections
    java.util.List<Path2D.Double> stroke_curves = new ArrayList<>(); // list of curves rendered
    List<Point> draw_from_caster = new ArrayList<>();

    /**
     * Render-to-Buffer Functions
     */

    public static void draw_curves(Graphics2D g2d, LinkedList<Path2D.Double> lc) {

        //brusher.color = new Color(brusher.color.getRed(), brusher.color.getGreen(), brusher.color.getBlue(), brusher.opacity);

        //g2d.setStroke(new BasicStroke(2));
        //g2d.setColor(color);

        double[] coordinates1 = new double[6];
        double[] coordinates2 = new double[6];
        for (Path2D.Double p : lc) {

            //skips short lines within a tolerance
            PathIterator pi = p.getPathIterator(null);

            pi.next();
            pi.currentSegment(coordinates1);
            pi.next();
            pi.currentSegment(coordinates2);

            double distance = Math.hypot(coordinates1[0] - coordinates1[2], coordinates1[1] - coordinates2[3]);

            int distTol = 50;
            if (distance <= distTol) {
                continue;
            }

            g2d.draw(p);
        }

    }

    private void wipe_buffer(Graphics2D g2d) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2d.fillRect(0, 0, 1280, 1024); // HARD CODED
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
    }

    /**
     * Conversion function to be called
     */
    public CasterCO toCasterCO(Caster caster, BufferedImage image) {

        //TODO currently only testing rendering along outline

        stroke = new Curve();

        stroke_buffer = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);
        tool_buffer = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);

        last_rendered_curves = new LinkedList<Path2D.Double>();
        ray_path = new ArrayList<>(); // used to render ray path for tools
        intersection_points = new LinkedList<>(); // list of ray/component intersections
        stroke_curves = new ArrayList<>(); // list of curves rendered
        draw_from_caster = new ArrayList<>();

        //------------------------

        get_intersection_points(caster, image);
        create_curves_across_points(getIntersection_points(), false, caster);

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        var g2d = getStroke_buffer().createGraphics();

        wipe_buffer(g2d);
        //g2d.clearRect(0,0,caster.getStroke_buffer().getWidth(),caster.getStroke_buffer().getHeight());

        g2d.setRenderingHints(rh);

        //drawing curves here
        g2d.setColor(caster.color);
        draw_curves(g2d, last_rendered_curves);
        //render_list_to_buffer(last_rendered_curves, caster.getStroke_buffer());

        var casterCO = new CasterCO();

        casterCO.setStrokeBuffer(getStroke_buffer());

        casterCO.setOpacity(caster.getOpacity());
        casterCO.setColor(caster.getColor());
        casterCO.setThickness(caster.getThickness());
        casterCO.setLayers(caster.getLayers());
//        casterCO.setCast_through(caster.isCast_through());
//        casterCO.setCast_along(caster.isCast_along());
        casterCO.setCast_from_source(caster.isCast_from_source());
        casterCO.setMin_penetrations(caster.getMin_penetrations());
        casterCO.setMax_penetrations(caster.getMax_penetrations());
        casterCO.setMax_ray_length(caster.getMax_ray_length());
        casterCO.setTolerance(caster.getTolerance());
        casterCO.setRays(caster.getRays());
        casterCO.setFrom_x(caster.getFrom_x());
        casterCO.setFrom_y(caster.getFrom_y());
        casterCO.setTo_x(caster.getTo_x());
        casterCO.setTo_y(caster.getTo_y());

        casterCO.setName(caster.getName());

        casterCO.setHighlighted(caster.isHighlighted());
        casterCO.setConnect_at(caster.getConnect_at());
        casterCO.setFlip_status(caster.getFlip_status());
        casterCO.setMax_dist_between_points(caster.getMax_dist_between_points());
        casterCO.setRex(caster.getRex());
        casterCO.setRey(caster.getRey());

        return casterCO;

    }

    void render_list_to_buffer(LinkedList<Path2D.Double> ll, BufferedImage b) {

        Graphics2D g2d = b.createGraphics();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        for (Path2D.Double p : ll) {
            g2d.draw(p);
        }
    }

    public void create_curves_across_points(LinkedList<Point> intersection_points, boolean reset, Caster c) {

        if (reset) {
            last_rendered_curves = new LinkedList<Path2D.Double>();
        }

        Point last_point = null;

        if (c.getConnect_at().length == 0) {
            System.out.println("0 size");
            return;
        }

        System.out.println("pather: " + c.getConnect_at().length);

        for (int element : c.getConnect_at()) {

            ListIterator<Point> li = intersection_points.listIterator();

            if (!li.hasNext()) {
                continue;
            } // if nothting strikes

            Point i = li.next();

            // EACH PASS THROUGH LIST
            while (true) {

                if (last_point != null) {

                    double x_width = i.get_x() - last_point.get_x();
                    double y_height = i.get_y() - last_point.get_y();

                    for (int j = 0; j < c.getLayers(); j++) { // number of times to repeat stroke

                        double theta = Math.atan2(y_height, x_width); // angle between current and last stroke

                        Path2D.Double path = new Path2D.Double();
                        path.moveTo(last_point.get_x() + r(c.getRex()), last_point.get_y() + r(c.getRey()));

                        //stroke.sections) {
                        for (Curve.Section sect : stroke.sections) { // get path for each section

                            Curve.Section.CV a = sect.cvs.get(0);
                            Curve.Section.CV b = sect.cvs.get(1);

                            path.curveTo(
                                    last_point.get_x() + Math.cos(theta) * (a.prop * x_width) + r(a.rx) + a.ox,
                                    last_point.get_y() + Math.sin(theta) * (a.prop * y_height) + r(a.ry) + a.oy,

                                    last_point.get_x() + Math.cos(theta) * ((b.prop) * x_width) + r(b.rx) + b.ox,
                                    last_point.get_y() + Math.sin(theta) * ((b.prop) * y_height) + r(b.ry) + b.oy,

                                    i.get_x() + r(c.getRex()), i.get_y() + r(c.getRey()));

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
                // code to check that skip_array doesn't go out of index when skipping through
                // intersection_points
                // and if it does, just grab the last element of the intersection_points
            }
        }
    }

    public double r(double range) {

        if (range == 0) {
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

    /**
     * Path-Tracing Functions to get collisions etc
     */

    private void add_intersection_point(Point a, Caster c) {
        getIntersection_points().add(a);
    }

    public void get_intersection_points(Caster c, BufferedImage image) {

        double viewer_width = c.from_x - c.to_x;
        double viewer_height = c.from_y - c.to_y;
        double width_between_rays = viewer_width / (c.rays - 1);
        double height_between_rays = viewer_height / (c.rays - 1);
        double angle_perp_to_veiwer = Math.atan2(viewer_height, viewer_width) + Math.toRadians(90);

        Color canvas_color = new Color(255, 255, 255);// new Color(0, 0, 0, 0);

        int canvas_width = image.getWidth();
        int canvas_height = image.getHeight();

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
                            image.getRGB((int) current_ray_x, (int) current_ray_y), true);

                    // test if continuance of last penetration // might have to move down

                    if (last_pixel == null) {// first pixel //should only be entered once
                        last_pixel = this_pixel;
                        canvas_color = this_pixel;
                        continue;
                    }

                    if ((last_pixel != null) && (last_pen != null)) {
                        if ((color_similiar(this_pixel, last_pixel, c)
                                && (color_similiar(this_pixel, last_pen.get_color(), c)))) {
                            continue;
                        }
                    }

                    last_pixel = this_pixel;

                    // test if color is similiar to canvaas
                    if (!color_similiar(this_pixel, canvas_color, c)) {

                        if (c.max_penetrations == penetration_count) {

                            add_collision(current_ray_x, current_ray_y, current_viewer_x, current_viewer_y, last_pen,
                                    this_pixel, c);
                            store_to_ray_path(current_ray_x, current_ray_y, current_viewer_x, current_viewer_y, c);
                            break;

                        } else {
                            penetration_count += 1;
                            last_pen = add_penetration(current_ray_x, current_ray_y, current_viewer_x, current_viewer_y,
                                    last_pen, this_pixel, c);
                        }

                    }
                }
            }

        }

    }

    private boolean color_similiar(Color color_this_loop, Color color_last_loop, Caster c) {

        int r = color_this_loop.getRed() - color_last_loop.getRed();
        int g = color_this_loop.getGreen() - color_last_loop.getGreen();
        int b = color_this_loop.getBlue() - color_last_loop.getBlue();

        r = Math.abs(r);
        g = Math.abs(g);
        b = Math.abs(b);

        return ((r < c.tolerance) && (g < c.tolerance) && (b < c.tolerance));
    }

    private Point add_penetration(double ray_path_x, double ray_path_y, double rx, double ry, Point last_pen,
                                  Color color, Caster c) {

        Point new_pen;

        if (last_pen == null) {

            new_pen = new Point((int) ray_path_x, (int) ray_path_y);
            new_pen.set_origin_ray(new Point((int) rx, (int) ry));
            new_pen.setIntersection_color(color);

        } else {

            new_pen = new Point((int) ray_path_x, (int) ray_path_y);
            new_pen.set_origin_ray(new Point((int) rx, (int) ry));
            new_pen.setPenetration_points((LinkedList<Point>) last_pen.getPenetration_points().clone());
            new_pen.getPenetration_points().add(last_pen);
            new_pen.setIntersection_color(color);

            last_pen.setNext(new_pen);

        }

        return new_pen;
    }

    private void add_collision(double ray_path_x, double ray_path_y, double rx, double ry, Point last_pen,
                               Color found_color, Caster c) {

        Point new_intersection_point;
        new_intersection_point = new Point((int) ray_path_x, (int) ray_path_y);
        new_intersection_point.set_origin_ray(new Point((int) rx, (int) ry));
        new_intersection_point.set_color(found_color);

        if (last_pen != null) {
            new_intersection_point.setPenetration_points((LinkedList<Point>) last_pen.getPenetration_points().clone());

            new_intersection_point.getPenetration_points().add(last_pen);
            last_pen = new_intersection_point;
        }

        add_intersection_point(new_intersection_point, c); // these are rendered
        last_pen = null;
    }

    private boolean on_canvas(int canvas_width, int canvas_height, double ray_path_x, double ray_path_y) {
        return ((int) ray_path_x < canvas_width) && ((int) ray_path_y < canvas_height) && ((int) ray_path_x > 0)
                && ((int) ray_path_y > 0);
    }

    private void store_to_ray_path(double current_ray_x, double current_ray_y, double current_viewer_x,
                                   double current_viewer_y, Caster c) {
        Point p = new Point((int) current_ray_x, (int) current_ray_y);
        Point q = new Point((int) current_viewer_x, (int) current_viewer_y);
        p.set_origin_ray(q);
        getRay_path().add(p);

        // temp
        getDraw_from_caster().add(q);
        getDraw_from_caster().add(p);
    }

    public class Curve {

        public LinkedList<Section> sections = new LinkedList<Section>();


        Curve() {
            sections.add(new Section());
            //sections.add(new Section());
            //sections.getLast().cvs.get(0).prop=(5/4);
            //sections.getLast().cvs.get(0).prop=(7/4);
        }

        public class Section {
            public boolean render = true;

            // need to order LinkedList
            public LinkedList<CV> cvs = new LinkedList<CV>();

            Section() {
                // default
                cvs.add(new CV(1 / 4, 30, 30, 0, 30));
                cvs.add(new CV(3 / 4, 30, 30, 0, -30));
            }

            public class CV {

                public double prop;

                public double rx;
                public double ry;

                public double ox;
                public double oy;

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


}
