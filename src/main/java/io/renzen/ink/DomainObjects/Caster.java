package io.renzen.ink.DomainObjects;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.Arrays;

/**
 * this class creates rays which intersect with the canvas component buffer
 * these intersection points are then used to create guide points for drawing strokes
 **/

/**
 * Combine Pather,Tracer,Caster?
 */

@Data
@NoArgsConstructor
public class Caster {

    public int opacity;
    public Color color;
    public int thickness;
    public int layers;

    public boolean cast_from_source;
    public int min_penetrations;
    public int max_penetrations;
    public double max_ray_length;
    public int tolerance;
    public int rays;

    public double from_x;
    public double from_y;
    public double to_x;

    public double to_y;

    public boolean highlighted;
    public int[] connect_at;
    public int flip_status;

    public double max_dist_between_points;
    public double rex;
    public double rey;

    public String name;

    public Caster(String casterName, double x1, double y1, double x2, double y2) {

        setFrom_x(x1);
        setFrom_y(y1);
        setTo_x(x2);
        setTo_y(y2);

        setOpacity(100);
        setColor(new Color(0, 0, 0, getOpacity()));
        setThickness(1);
        setLayers(5);
        setCast_from_source(false);
        setMin_penetrations(0);
        setMax_penetrations(0);
        setMax_ray_length(1200);
        setTolerance(75);
        setRays(100);
        setHighlighted(false);
        setConnect_at(new int[]{1});
        setFlip_status(1);
        //setm
        setRex(5);
        setRey(5);
        name = casterName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Caster)) return false;

        Caster caster = (Caster) o;

        if (opacity != caster.opacity) return false;
        if (thickness != caster.thickness) return false;
        if (layers != caster.layers) return false;
        if (cast_from_source != caster.cast_from_source) return false;
        if (min_penetrations != caster.min_penetrations) return false;
        if (max_penetrations != caster.max_penetrations) return false;
        if (Double.compare(caster.max_ray_length, max_ray_length) != 0) return false;
        if (tolerance != caster.tolerance) return false;
        if (rays != caster.rays) return false;
        if (Double.compare(caster.from_x, from_x) != 0) return false;
        if (Double.compare(caster.from_y, from_y) != 0) return false;
        if (Double.compare(caster.to_x, to_x) != 0) return false;
        if (Double.compare(caster.to_y, to_y) != 0) return false;
        if (highlighted != caster.highlighted) return false;
        if (flip_status != caster.flip_status) return false;
        if (Double.compare(caster.max_dist_between_points, max_dist_between_points) != 0) return false;
        if (Double.compare(caster.rex, rex) != 0) return false;
        if (Double.compare(caster.rey, rey) != 0) return false;
        if (color != null ? !color.equals(caster.color) : caster.color != null) return false;
        if (!Arrays.equals(connect_at, caster.connect_at)) return false;
        return name != null ? name.equals(caster.name) : caster.name == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = opacity;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + thickness;
        result = 31 * result + layers;
        result = 31 * result + (cast_from_source ? 1 : 0);
        result = 31 * result + min_penetrations;
        result = 31 * result + max_penetrations;
        temp = Double.doubleToLongBits(max_ray_length);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + tolerance;
        result = 31 * result + rays;
        temp = Double.doubleToLongBits(from_x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(from_y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(to_x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(to_y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (highlighted ? 1 : 0);
        result = 31 * result + Arrays.hashCode(connect_at);
        result = 31 * result + flip_status;
        temp = Double.doubleToLongBits(max_dist_between_points);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rex);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rey);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
