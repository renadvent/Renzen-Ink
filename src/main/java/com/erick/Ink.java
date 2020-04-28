package com.erick;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.*;

public class Ink {

    private static final long serialVersionUID = 1L;

    private JFrame frame;
    private Caster selected_caster;
    private texture selected_texture;
    frame selected_frame;
    component selected_component;
    
    canvas_panel can_pan;
    private action_panel act_pan;
    private stroke_panel str_pan;


    private JTabbedPane jtpr;
    private JTabbedPane jtpl;
    private prop_panel prop_pan;
    private layer_panel lay_pan;
    private animation_panel anim_panel;

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

    public frame selected_frame(){return selected_frame; }

    public component selected_component(){return selected_component;}

    public canvas_panel can_pan() {
        return can_pan;
    }

    public action_panel act_pan() {
        return act_pan;
    }
    public prop_panel prop_pan() {
        return prop_pan;
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


        jtpl=new JTabbedPane();
        frame.getContentPane().add(jtpl, BorderLayout.WEST);
        act_pan = new action_panel(frame, this);
        jtpl.addTab("actions",new JScrollPane(act_pan));

        lay_pan=new layer_panel();
        jtpl.addTab("layers",new JScrollPane(lay_pan));

        //frame.getContentPane().add(new JScrollPane(act_pan), BorderLayout.WEST);

        jtpr = new JTabbedPane();
        frame.getContentPane().add(jtpr, BorderLayout.EAST);
        str_pan = new stroke_panel(frame, this);
        jtpr.addTab("stroke",new JScrollPane(str_pan));

        prop_pan = new prop_panel(frame, this);
        jtpr.addTab("proportions", new JScrollPane(prop_pan));


        //frame.getContentPane().add(new JScrollPane(str_pan), BorderLayout.EAST);

        anim_panel=new animation_panel();
        JTabbedPane jtps = new JTabbedPane();
        jtps.addTab("timelapse",new JScrollPane(anim_panel));

        frame.getContentPane().add(jtps,BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

    }

}