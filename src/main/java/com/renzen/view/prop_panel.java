package com.renzen.view;

import com.renzen.Ink;

import javax.swing.*;

public class prop_panel extends JPanel {

    final Ink ink;
    final JFrame frame;

    public prop_panel(final JFrame frame, final Ink ink) {
        this.ink = ink;
        this.frame = frame;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        ink.selected_component = ink.selected_texture().getComponents().getFirst();

    }
}
