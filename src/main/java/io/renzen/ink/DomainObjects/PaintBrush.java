package io.renzen.ink.DomainObjects;

import lombok.Data;

import java.awt.*;
import java.awt.geom.Ellipse2D;

@Data
public class PaintBrush {

    double size = 5;
    Color color;

    RadialGradientPaint radialGradientPaint;

    Shape brushShape;

    public PaintBrush(Color color) {
        this.color = color;
        brushShape = new Ellipse2D.Double(0, 0, size, size);
    }
}
