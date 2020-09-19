package com.renzen.Models;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter@Setter
public class Brusher { // used bystroke

    public int opacity = 100;
    public Color color = new Color(0, 0, 0, opacity);
    public int thickness = 1;

}