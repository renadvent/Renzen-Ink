package com.erick.view;

import com.erick.Ink;
import com.erick.model.texture;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
//import java.awt.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.JFrame;

public class canvas_panel extends JPanel {

    public Rectangle2D.Double resize_tool_1;
    public Rectangle2D.Double resize_tool_2;
    public LinkedList<Shape> prop_renderables=new LinkedList<Shape>();
    private Ink ink;
    private JFrame frame;

    public canvas_panel(JFrame frame, Ink ink) {
        this.ink = ink;
        this.frame=frame;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }

    Rectangle2D move_tool;

    @Override
    public void paintComponent(Graphics g) {


        long st=java.lang.System.currentTimeMillis();
        //super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        update_all_textures();
        show_all_textures(this.ink, g2d);
        
         if (move_tool != null) {
            g2d.fill(move_tool);
        } 
         
         if (resize_tool_1 != null){g2d.fill(resize_tool_1);}
        if (resize_tool_2 != null) {
            g2d.fill(resize_tool_2);
        }

        for (Shape s : prop_renderables){
            System.out.println("drawing renderables");

            Rectangle2D.Double te = (Rectangle2D.Double) s;

            //if (s.selected)
            g2d.draw(s);
        }

        // draw highlighted selectables
        for (selectable p : selectable.list){

            if (p.selected){
                System.out.println("highlighted drawn");
                g2d.setColor(new Color (0,255,0));
                g2d.draw(p.rect);
            }
        }
         
        st=java.lang.System.currentTimeMillis()-st;
        System.out.println("render millisecs: "+st);

    }

	public void show_all_textures(Ink ink, Graphics g2d) {
	
	    for (texture i : texture.textures) {
	
	        if (ink.act_pan().check_base.isSelected()) {
	            g2d.drawImage(i.get_component_buffer(), 0, 0, null);
	        }
	
	        if (ink.act_pan().check_tools.isSelected()) {
	            g2d.drawImage(i.get_tool_buffer(), 0, 0, null);
	        }
	
	        if (ink.act_pan().check_strokes.isSelected()) {
	            g2d.drawImage(i.get_stroke_buffer(), 0, 0, null);
	        }
	
	    }
	
	}

	public void update_all_textures() {
	    for (texture i : texture.textures) {
	        i.update_all_buffers();
	    }
	}

}