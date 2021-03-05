package io.renzen.ink.ArtObjects;

import lombok.Data;

import java.awt.*;
import java.util.LinkedList;

@Data
public class Point {
    public int iteration;
    int penetration_count = 0;
    LinkedList<Point> penetration_points = new LinkedList<Point>();
    Point next;
    Color intersection_color;
    Point origin_ray;
    private double x; // location of intersection on canvas
    private double y;

    public Point(int x, int y) {
        set_location(x, y);
    }

    public void set_location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Color get_color() {
        return intersection_color;
    }

    public void set_color(Color rgb) {
        intersection_color = rgb;
    }

    public double get_ray_x() {
        return origin_ray.get_x();
    }

    public double get_x() {
        return x;
    }

    public double get_ray_y() {
        return origin_ray.get_y();
    }

    public double get_y() {
        return y;
    }

    public void set_origin_ray(Point x) {
        origin_ray = x;
    }
}
