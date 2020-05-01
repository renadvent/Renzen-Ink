package com.erick.model;

import com.erick.AbstractModel;
import com.erick.defcon;
import com.erick.view.canvas_panel;
import com.erick.view.selectable;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.nio.Buffer;
import java.util.LinkedList;
import java.awt.AlphaComposite;

import javax.imageio.ImageIO;



//import jaiimageio.PerspectiveTransform;


import static org.opencv.core.CvType.CV_32F;

public class texture extends AbstractModel {


    //https://stackoverflow.com/questions/14942881/image-3d-rotation-opencv
    //https://docs.opencv.org/2.4/modules/imgproc/doc/geometric_transformations.html
    //https://stackoverflow.com/questions/7019407/translating-and-rotating-an-image-in-3d-using-opencv
    //https://stackoverflow.com/questions/17087446/how-to-calculate-perspective-transform-for-opencv-from-rotation-angles
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}


    //CONVERTS BUFFERED IMAGE TO BYTE ARRAY
    //byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
    // Then you can simply put it to Mat if you set type to CV_8UC3
    //image_final.put(0, 0, pixels);

    //Ultraviolet
    //https://stackoverflow.com/a/46196408/12245915

    public static Mat BufferedImage2Mat() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage image = new BufferedImage(1000,1000, BufferedImage.TYPE_BYTE_GRAY); //ARGB doesn't work here
        System.out.println("SIZE: "+byteArrayOutputStream.size());

        if (!ImageIO.write(image, "bmp", byteArrayOutputStream)) {
            System.out.println("failed!!!!");
            //return null;
        }
        System.out.println("SIZE: "+byteArrayOutputStream.size());
        byteArrayOutputStream.flush();
        System.out.println("SIZE: "+byteArrayOutputStream.size());
        byte[] array = byteArrayOutputStream.toByteArray();
        MatOfByte mb =new MatOfByte(array);
        System.out.println("SIZE: "+array.length);
        //return Imgcodecs.imdecode(mb, Imgcodecs.IMREAD_UNCHANGED);
        return Imgcodecs.imdecode(mb, Imgcodecs.IMREAD_UNCHANGED);
        //return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    }

/*    public static Mat BufferedImage2Mat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        //byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }*/

    public static BufferedImage Mat2BufferedImage(Mat matrix)throws IOException {
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode("jpg", matrix, mob);
        return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
    }


    //BufferedImage rect_sect;

    //static LinkedList<BufferedImage> transformed;
    public static void get_rects() throws IOException {

        canvas_panel.draw_from_anywhere=new LinkedList<BufferedImage>();

        LinkedList<BufferedImage> transformed=new LinkedList<BufferedImage>();
        //Graphics2D g2d = stroke_rendered_buffer.createGraphics();

        for (selectable p: selectable.list){

            if(p.rect!=null && p.rect.width>100 && p.rect.height>100 ) {

                BufferedImage temp;

                temp = texture.test_buffer.getSubimage(100,100,100,100);
                //temp = stroke_rendered_buffer.getSubimage((int) p.rect.x, (int) p.rect.y, (int) p.rect.width, (int) p.rect.height);
                Mat m_temp = BufferedImage2Mat();


                Mat corners = Mat.zeros(4, 2, CV_32F);

                corners.put(0, 0, 0);
                corners.put(0, 1, 512);
                corners.put(1, 0, 0);
                corners.put(1, 1, 0);
                corners.put(2, 0, 512);
                corners.put(2, 1, 0);
                corners.put(3, 0, 512);
                corners.put(3, 1, 512);

                //----

                Mat target = Mat.zeros(4, 2, CV_32F);

                Mat M = Imgproc.getPerspectiveTransform(corners, target);
                //warped = Imgproc.warpPerspective(image, M, (maxWidth, maxHeight))


                Imgproc.warpPerspective(m_temp, M, corners, new Size(1280, 1024));

                temp = Mat2BufferedImage(M);


                canvas_panel.draw_from_anywhere.add(temp);
            }







        }

        //if (ink.se)
        //return temp;
    }

    //-------------------------------------------------------------------------------

    // PUBLIC
    public static LinkedList <texture> textures = new LinkedList<texture>();

    public static BufferedImage test_buffer = new BufferedImage(1000,1000, BufferedImage.TYPE_INT_ARGB);

    // constructor for a blank canvas
    public texture(int width, int height) {

        textures.add(this);

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

    // add a component to canvas
    public void add_component(Component comp) {

        Components.add(comp);

        //----
        firePropertyChange(defcon.NEW_COMPONENT,null, comp);
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
        firePropertyChange(defcon.NEW_VIEW,null,x);

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

        firePropertyChange(defcon.NEW_CASTER,null,temp_pov);


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
    };

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

    // PRIVATE

    private int orig_width;
    private int orig_height;

    private BufferedImage component_rendered_buffer; // viewers read from this buffer
    public BufferedImage stroke_rendered_buffer; // viewers write to this buffer
    private BufferedImage tool_rendered_buffer; // viewers' tools render to this buffer
    private BufferedImage shade_rendered_buffer; // viewers write shade to this buffer
    BufferedImage prop_rendered_buffer;

    public BufferedImage alpha_rendered_buffer;

    public BufferedImage overlay_buffer;

    public LinkedList<Component> getComponents(){
        return Components;
    }

    private LinkedList<Component> Components; // individual components to be rendered to component buffer
    private LinkedList<Caster> casters; // viewer's are placed on the drawing to render strokes

    public LinkedList<Caster> get_casters() {
        return casters;
    }

    private BufferedImage render_components_on_texture() {

        wipe_buffer(component_rendered_buffer.createGraphics());
        Graphics2D g2d = component_rendered_buffer.createGraphics();

        for (Component i : Components) {
            i.update_component(component_rendered_buffer);
            g2d.drawImage(i.get_buffer(),null,0,0);
        }

        return (component_rendered_buffer);
    };

    private BufferedImage render_strokes_on_texture() {

        wipe_buffer(stroke_rendered_buffer.createGraphics());
        Graphics2D g2d = stroke_rendered_buffer.createGraphics();

        for (Caster i : casters) {

            i.update_casts();          

            if (!i.highlighted){
                g2d.drawImage (i.stroke_buffer,null,0,0);
                g2d.drawImage (i.shade_buffer,null,0,0);
            }
            else if (i.highlighted){
                g2d.drawImage (i.highlighted_strokes_buffer,null,0,0);
                System.out.println("showing highlight");
            }

        }

        g2d.drawImage(alpha_rendered_buffer,null,0,0);//might have to edit later

        return (stroke_rendered_buffer);
    }

    private BufferedImage render_tools_on_texture() {

        wipe_buffer(tool_rendered_buffer.createGraphics());
        Graphics2D g2d = tool_rendered_buffer.createGraphics();

        for (Caster i : casters) {
            i.update_tools();
            if (!i.highlighted){
                g2d.drawImage (i.tool_buffer, null, 0,0);
            } else{
                g2d.drawImage(i.highlighted_tools_buffer,null,0,0);
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

        firePropertyChange(defcon.LOADED_COMPONENT,null, comp);
    }

    private void wipe_buffer(Graphics2D g2d) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2d.fillRect(0, 0, 1280, 1024); // HARD CODED
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
    }

}