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
        color = Color.BLACK;
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
