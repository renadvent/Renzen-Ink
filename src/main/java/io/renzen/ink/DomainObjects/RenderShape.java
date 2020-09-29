package io.renzen.ink.DomainObjects;

import lombok.Getter;

import java.awt.*;

@Getter
public class RenderShape {

    String id;
    Shape shape;

    public RenderShape(String id, Shape shape) {
        this.id = id;
        this.shape = shape;
    }
//
//    @Embedded
//    Shape shape;
}
