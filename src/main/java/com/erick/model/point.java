package com.erick.model;
import java.util.LinkedList;


import java.awt.Color;

// used to track intersenction points between rays and canvas components
public class point {

    int penetration_count=0;
    LinkedList<point> penetration_points= new LinkedList<point>();
    point next;

    public int iteration;

    Color intersection_color;

    public void set_color(Color rgb){
        intersection_color=rgb;
    }

    public Color get_color(){
        return intersection_color;
    }

    public point(int x, int y) {
        set_location(x, y);
    }

    public void set_location(int x, int y) {
        this.x = x;
        this.y = y;
    };

    public double get_x() {
        return x;
    };

    public double get_y() {
        return y;
    };



    public double get_ray_x() {
        return origin_ray.get_x();
    };

    public double get_ray_y() {
        return origin_ray.get_y();
    };

    public void set_origin_ray(point x){
        origin_ray = x;
    }


    private double x; // location of intersection on canvas
    private double y;
    
    point origin_ray;
    //private double ray_origin_x;
    //private double ray_origin_y;


    // TODO 
    // add pentration color? so it can test if the next pixel is part of it
    // maybe implement this on viewer get_intersections
}