package com.erick;

import com.erick.model.Caster;
import com.erick.model.Component;
import com.erick.model.frame;
import com.erick.model.texture;
import com.erick.view.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.erick.model.Caster;
import com.erick.model.Component;
import com.erick.model.frame;
import com.erick.model.texture;
import com.erick.view.action_panel;
import com.erick.view.canvas_panel;
import com.erick.view.prop_panel;

import javax.swing.*;

public class Ink extends AbstractController {

    private static final long serialVersionUID = 1L;

    JFrame frame = new JFrame("Ink");

    texture start = init_default_model();




    private final Ink_Controller ink_con = new Ink_Controller(this);

    final canvas_panel can_pan = new canvas_panel(frame, this);
    final action_panel act_pan = new action_panel(frame, this);
    final stroke_panel str_pan = new stroke_panel(frame, this);
    final JTabbedPane jtpr = new JTabbedPane();
    final JTabbedPane jtpl = new JTabbedPane();
    final prop_panel prop_pan = new prop_panel(frame, this);
    ;
    public final layer_panel lay_pan = new layer_panel(frame, this);
    final animation_panel anim_panel = new animation_panel();
    final library_panel lib_pan = new library_panel();
    final Paint_panel paint_pan = new Paint_panel();
    final JTabbedPane jtps = new JTabbedPane();
    final Structure_panel struct_pan = new Structure_panel();
    final Menu menu = new Menu();

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

        // temp

        addView((act_pan));
        addModel(start);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // top
        frame.getContentPane().add(menu, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(can_pan), BorderLayout.CENTER);

        // left
        frame.getContentPane().add(jtpl, BorderLayout.WEST);
        jtpl.addTab("Actions", new JScrollPane(act_pan));
        jtpl.addTab("Layers", new JScrollPane(lay_pan));

        // right
        frame.getContentPane().add(jtpr, BorderLayout.EAST);
        jtpr.addTab("Stroke", new JScrollPane(str_pan));
        jtpr.addTab("Proportions", new JScrollPane(prop_pan));
        jtpr.addTab("Library", new JScrollPane(lib_pan));
        jtpr.addTab("Paint", new JScrollPane(paint_pan));

        // bottom
        frame.getContentPane().add(jtps, BorderLayout.SOUTH);
        jtps.addTab("timelapse + randomization", new JScrollPane(anim_panel));
        jtps.addTab("structures", new JScrollPane(struct_pan));

        //temp
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("texture #1");
        DefaultTreeModel model = (DefaultTreeModel) lay_pan.tree.getModel();
        model.insertNodeInto(node, lay_pan.textures, lay_pan.textures.getChildCount());

        frame.pack();
        frame.setVisible(true);

    }

    // GETTERS
    public JFrame frame() {
        return frame;
    }

    Ink ink = this;

    public Caster selected_caster() {
        return selected_caster;
    }

    public texture selected_texture() {
        return selected_texture;
    }

    public com.erick.model.frame selected_frame() {
        return selected_frame;
    }

    public Component selected_component() {
        return selected_component;
    }

    public canvas_panel can_pan() {
        return can_pan; //ink.can_pan;
    }

    public action_panel act_pan() {
        return ink.act_pan;
    }

    public prop_panel prop_pan() {
        return ink.prop_pan;
    }

    private Caster selected_caster;
    public texture selected_texture;
    public com.erick.model.frame selected_frame;
    public Component selected_component;

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

    texture init_default_model() {

        texture c = new_texture();
        change_selected_texture(c);
        c.load_component("C:\\Users\\erick\\OneDrive\\Documents\\Ink\\ink-app\\src\\main\\java\\com\\erick\\body.jpg");



        return c;
    }

    public void change_selected_texture(texture c) {
        selected_texture = c;
    }

    texture new_texture() {
        // all can be accessed at texture.textures
        //lay_pan.textures.insertN

        //DefaultMutableTreeNode node=new DefaultMutableTreeNode("texture #"+texture.textures.size());
        //lay_pan.model.insertNodeInto(node, lay_pan.tools, lay_pan.tools.getChildCount());

        return new texture(1280, 1024);
    }

    //-----------------------------------------------------------------

    public class Ink_Controller {

        Ink ink;
        JFrame frame;


        Ink_Controller(Ink ink) {

            this.ink = ink;
            this.frame = ink.frame;

            //init_default_model();

        }






    }
}