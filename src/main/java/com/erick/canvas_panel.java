package com.erick;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
//import java.awt.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.JFrame;

public class canvas_panel extends JPanel {

    private Ink ink;
    private JFrame frame;

    canvas_panel(JFrame frame, Ink ink) {
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