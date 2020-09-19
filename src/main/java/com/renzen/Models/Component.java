package com.renzen.Models;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

// buffers that are rendered to the component buffer

// components can either be a loaded image
// a drawn image
// or a frame or collection of frames

@Getter@Setter
public class Component {

    //public LinkedList<frame> frames = new LinkedList<>();

    private final BufferedImage component_buffer;
    private int x, y;

    public Component(Texture canvas, int width, int height) {
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

    public Graphics2D get_graphics() {
        return component_buffer.createGraphics();
    }


}