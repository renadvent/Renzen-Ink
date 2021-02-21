package io.renzen.ink.ArtObjects;

import lombok.Data;

import java.awt.*;
import java.util.LinkedList;

@Data
public class CasterPoint {
    public int iteration;
    int penetration_count = 0;
    LinkedList<Point> penetration_points = new LinkedList<Point>();
    Point next;
    Color intersection_color;
    Point origin_ray;
    private double x; // location of intersection on canvas
    private double y;
}
