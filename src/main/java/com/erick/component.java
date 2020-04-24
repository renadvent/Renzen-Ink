package com.erick;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// buffers that are rendered to the component buffer
class component {

    private BufferedImage component_buffer;
    private int x, y;
    
	public component(texture canvas, int width, int height) {
		component_buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void set_location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update_component(BufferedImage canvas_buffer) {

        Graphics2D g2d = canvas_buffer.createGraphics();
        g2d.drawImage(component_buffer, x, y, null);
        g2d.dispose();
    }

    public BufferedImage get_buffer() {
        return component_buffer;
    }

    public Graphics2D get_graphics(){
        return component_buffer.createGraphics();
    }


}