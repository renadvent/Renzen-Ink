package com.renzen.model;

import com.renzen.AbstractModel;
import com.renzen.defcon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/*<<<<<<< HEAD
<<<<<<< HEAD
import com.erick.enums;
=======
>>>>>>> parent of 51432fc... long way to go
import com.erick.view.canvas_panel;
import com.erick.view.selectable;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
=======
>>>>>>> parent of 7790d25... beginning rewrite*/

public class Texture extends AbstractModel {

    // PUBLIC
    public static LinkedList<Texture> Textures = new LinkedList<Texture>();
    private final int orig_width;
    private final int orig_height;
    private final BufferedImage component_rendered_buffer; // viewers read from this buffer
    private final BufferedImage stroke_rendered_buffer; // viewers write to this buffer
    private final BufferedImage tool_rendered_buffer; // viewers' tools render to this buffer
    private final BufferedImage shade_rendered_buffer; // viewers write shade to this buffer
    private final LinkedList<Component> Components; // individual components to be rendered to component buffer
    private final LinkedList<Caster> casters; // viewer's are placed on the drawing to render strokes
    public BufferedImage alpha_rendered_buffer;
    BufferedImage prop_rendered_buffer;
    BufferedImage overlay_buffer;

    // constructor for a blank canvas
    public Texture(int width, int height) {

        Textures.add(this);

        // initialize image buffers
        component_rendered_buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        stroke_rendered_buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        tool_rendered_buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        shade_rendered_buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        alpha_rendered_buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        prop_rendered_buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        overlay_buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        orig_height = height;
        orig_width = width;

        // initialize linkedLists
        Components = new LinkedList<Component>();
        casters = new LinkedList<Caster>();
    }

    // PRIVATE

    // add a component to canvas
    public void add_component(Component comp) {

        Components.add(comp);

        //----
        firePropertyChange(defcon.NEW_COMPONENT, null, comp);
    }

    public Graphics2D create_blank_component_graphics(int w, int h) {
        Component temp = new Component(this, w, h);
        Graphics2D g2d = temp.get_graphics();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        add_component(temp);
        return g2d;
    }

    // add a viewer to canvas
    public Caster create_viewer(int from_x, int from_y, int to_x, int to_y, int rays, boolean to_flip,
                                boolean to_shade) {
        Caster x = create_caster(from_x, from_y, to_x, to_y, rays, to_flip);
        x.cast_through = to_shade;


        //---
        firePropertyChange(defcon.NEW_VIEW, null, x);

        return x;
    }

    public Caster create_caster(double d, double e, double f, double g, int rays, boolean to_flip) {
        Caster temp_pov = new Caster();

        temp_pov.set_from(d, e);
        temp_pov.set_to(f, g);
        temp_pov.set_ray_count(rays);

        temp_pov.parent_component_rendered_buffer = this.component_rendered_buffer;
        //temp_pov.parent_stroke_rendered_buffer = this.stroke_rendered_buffer;
        //temp_pov.parent_tool_rendered_buffer = this.tool_rendered_buffer;

        if (to_flip == true) {
            temp_pov.flip();
        }

        casters.add(temp_pov);

        firePropertyChange(defcon.NEW_CASTER, null, temp_pov);


        return temp_pov;
    }

    public void update_all_buffers() {
        //overlay_buffer = new BufferedImage(orig_width, orig_height, BufferedImage.TYPE_INT_ARGB);
        render_components_on_texture();
        render_strokes_on_texture();
        render_tools_on_texture();

    }

    public BufferedImage get_stroke_buffer() {
        return stroke_rendered_buffer;
    }

    public BufferedImage get_component_buffer() {
        return component_rendered_buffer;
    }

    public BufferedImage get_tool_buffer() {
        return tool_rendered_buffer;
    }

    public BufferedImage get_shade_buffer() {
        return shade_rendered_buffer;
    }

    public int get_canvas_width() {
        return (orig_width);
    }

    public int get_canvas_height() {
        return (orig_height);
    }

    public LinkedList<Component> getComponents() {
        return Components;
    }

    public LinkedList<Caster> get_casters() {
        return casters;
    }

    private BufferedImage render_components_on_texture() {

        wipe_buffer(component_rendered_buffer.createGraphics());
        Graphics2D g2d = component_rendered_buffer.createGraphics();

        for (Component i : Components) {
            i.update_component(component_rendered_buffer);
            g2d.drawImage(i.get_buffer(), null, 0, 0);
        }

        return (component_rendered_buffer);
    }

    private BufferedImage render_strokes_on_texture() {

        wipe_buffer(stroke_rendered_buffer.createGraphics());
        Graphics2D g2d = stroke_rendered_buffer.createGraphics();

        for (Caster i : casters) {

            i.update_casts();

            if (!i.highlighted) {
                g2d.drawImage(i.stroke_buffer, null, 0, 0);
                g2d.drawImage(i.shade_buffer, null, 0, 0);
            } else if (i.highlighted) {
                g2d.drawImage(i.highlighted_strokes_buffer, null, 0, 0);
                System.out.println("showing highlight");
            }

        }

        g2d.drawImage(alpha_rendered_buffer, null, 0, 0);//might have to edit later

        return (stroke_rendered_buffer);
    }

    private BufferedImage render_tools_on_texture() {

        wipe_buffer(tool_rendered_buffer.createGraphics());
        Graphics2D g2d = tool_rendered_buffer.createGraphics();

        for (Caster i : casters) {
            i.update_tools();
            if (!i.highlighted) {
                g2d.drawImage(i.tool_buffer, null, 0, 0);
            } else {
                g2d.drawImage(i.highlighted_tools_buffer, null, 0, 0);
            }
        }

        return (tool_rendered_buffer);
    }

    public void load_component(String file) {

        Graphics2D comp = this.create_blank_component_graphics(1280, 1024);
        BufferedImage img = null;

        try {
            File f = new File(file);
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        comp.drawImage(img, 0, 0, null);

        firePropertyChange(defcon.LOADED_COMPONENT, null, comp);
    }

    private void wipe_buffer(Graphics2D g2d) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2d.fillRect(0, 0, 1280, 1024); // HARD CODED
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
    }

}