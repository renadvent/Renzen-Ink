package io.renzen.ink.DomainObjects;

import lombok.Getter;

import java.awt.*;

@Getter
public class RenderShape {

    String id;
    Shape shape;
    Color color;

    public RenderShape(String id, Shape shape) {
        this.id = id;
        this.shape = shape;
        color = new Color(0, 0, 0, 1);
    }

    public RenderShape(String id, Shape shape, Color color) {
        this.id = id;
        this.shape = shape;
        this.color = color;
    }
//
//    @Embedded
//    Shape shape;
}
