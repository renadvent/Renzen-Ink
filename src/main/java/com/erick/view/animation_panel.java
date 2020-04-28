package com.erick.view;

import javax.swing.*;
import java.awt.*;

public class animation_panel extends JPanel {

    // allows "keyframe" animation of creation
    // user can change elements while it is "drawing"
    // for example, color, alpha, style, path style, brush style etc!!

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 150);
    }

}
