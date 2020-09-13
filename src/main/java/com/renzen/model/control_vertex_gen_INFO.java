package com.renzen.model;

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

        init_prop_along=px;
        init_abs_along_x=r(abs_along_x);
        init_abs_along_y=r(abs_along_y);
        init_pre_rand=r(pre_rand);
        init_main_start_rand=r(main_start_rand);
        init_main_rand=r(main_rand);
        init_post_rand=r(post_rand);
    }

    // for calculations to changes after
    double init_prop_along;/// =1/4;

    double init_abs_along_x;
    double init_abs_along_y;

    double init_pre_rand = 30;

    double init_main_start_rand = 0;
    double init_main_rand = 30;
    double init_main_end_rand = 5;

    double init_post_rand = 10;


    public double r(double range) {

        if (range==0){
            return 0;
        }

        Random r = new Random();
        double x = r.nextInt((int) range);

        Random f = new Random();

        if (f.nextBoolean() == true) {
            return (x * (-1));
        }

        return x;
    }

}