package com.erick.model;

class Constraint {

    Part base_part; // box
    Part constrained_part; // box


    double size_ratio; // supposed to be in terms of frame
                        // not each other

    double dist_ratio; //relation to base_part
    double angle_2D; //

    double size_dev;
    double dist_dev;
    double angle_dev;

    get_functions get;
    set_functions set;

    class set_functions {
        void size_ratio(double s) {
            size_ratio = s;
        }

        void set_base(Part p) {
            base_part = p;
        }

        ;
    }

    class get_functions {
        double size_ratio() {
            return size_ratio;
        }
    }


}
