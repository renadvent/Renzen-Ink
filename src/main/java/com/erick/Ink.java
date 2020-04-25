package com.erick;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class Ink {

    private static final long serialVersionUID = 1L;

    private JFrame frame;
    private Caster selected_caster;
    private texture selected_texture;
    
    canvas_panel can_pan;
    private action_panel act_pan;
    private stroke_panel str_pan;
    private Menu menu;

    // GETTERS
    public JFrame frame() {
        return frame;
    }

    public Caster selected_caster() {
        return selected_caster;
    }

    public texture selected_texture() {
        return selected_texture;
    }

    public canvas_panel can_pan() {
        return can_pan;
    }

    public action_panel act_pan() {
        return act_pan;
    }

    // SETTERS
    public void change_selected_caster(Caster v) {

        if (selected_caster() != null) {
            selected_caster().highlighted = false;
        }

        selected_caster = v;

        if (selected_caster() != null) {
            selected_caster().highlighted = true;
        }

    }

    public void change_selected_texture(texture c) {
        selected_texture = c;
    }

    // MAIN
    public static void main(String[] args) {
        new Ink();
    }

    public Ink() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                create_and_show_default_GUI();
            }
        });
    }

    // GUI
    public void create_and_show_default_GUI() {

        texture c = new texture(1280, 1024);
        change_selected_texture(c);

        c.load_component("C:\\Users\\erick\\OneDrive\\Documents\\Ink\\ink-app\\src\\main\\java\\com\\erick\\guidance\\body.jpg");



        frame = new JFrame("Ink");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menu = new Menu(); // doesn't show
        frame.getContentPane().add(menu,BorderLayout.CENTER);

        can_pan = new canvas_panel(frame, this);
        frame.getContentPane().add(new JScrollPane(can_pan), BorderLayout.CENTER);

        act_pan = new action_panel(frame, this);
        frame.getContentPane().add(new JScrollPane(act_pan), BorderLayout.WEST);

        str_pan = new stroke_panel(frame, this);
        frame.getContentPane().add(new JScrollPane(str_pan), BorderLayout.EAST);



        frame.pack();
        frame.setVisible(true);

    }

}