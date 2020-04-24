package com.erick;

import java.util.Random;

public class control_vertex_gen_INFO {

    // choose between prop/absolute positioning of CV's

    double prop_along;/// =1/4;

    double abs_along_x;
    double abs_along_y;

    double pre_rand = 30;

    double main_start_rand = 0;
    double main_rand = 30;
    double main_end_rand = 5;

    double post_rand = 10;

    // specified modifiers
    double pre_const = 0;
    double main_const = 25;// 25;
    double post_const = 0;

    control_vertex_gen_INFO(double px) {
        prop_along = px;
        // System.out.println(px + " " + py);
    }

}