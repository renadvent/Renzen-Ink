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

public class Ink {

    private static final long serialVersionUID = 1L;

    JFrame frame = new JFrame("Ink");

    boolean start = init_default_model(); // important

    private final Ink_Controller ink_con=new Ink_Controller(this);

    final canvas_panel can_pan = new canvas_panel(frame, this);
    final action_panel act_pan = new action_panel(frame, this);
    final stroke_panel str_pan = new stroke_panel(frame, this);
    private final JTabbedPane jtpr = new JTabbedPane();
    private final JTabbedPane jtpl=new JTabbedPane();
    final prop_panel prop_pan = new prop_panel(frame, this);;
    public final layer_panel lay_pan=new layer_panel(frame, this);
    private final animation_panel anim_panel=new animation_panel();
    private final library_panel lib_pan=new library_panel();
    private final Paint_panel paint_pan=new Paint_panel();
    private final JTabbedPane jtps = new JTabbedPane();
    private final Structure_panel struct_pan = new Structure_panel();
    private final Menu menu = new Menu();



    boolean init_default_model(){

        texture c = ink_con.new_texture();
        ink_con.change_selected_texture(c);
        c.load_component("C:\\Users\\erick\\OneDrive\\Documents\\Ink\\ink-app\\src\\main\\java\\com\\erick\\body.jpg");

        return true;
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

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // top
        frame.getContentPane().add(menu,BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(can_pan), BorderLayout.CENTER);

        // left
        frame.getContentPane().add(jtpl, BorderLayout.WEST);
        jtpl.addTab("Actions",new JScrollPane(act_pan));
        jtpl.addTab("Layers",new JScrollPane(lay_pan));

        // right
        frame.getContentPane().add(jtpr, BorderLayout.EAST);
        jtpr.addTab("Stroke",new JScrollPane(str_pan));
        jtpr.addTab("Proportions", new JScrollPane(prop_pan));
        jtpr.addTab("Library",new JScrollPane(lib_pan));
        jtpr.addTab("Paint",new JScrollPane(paint_pan));

        // bottom
        frame.getContentPane().add(jtps,BorderLayout.SOUTH);
        jtps.addTab("timelapse + randomization",new JScrollPane(anim_panel));
        jtps.addTab("timelapse + randomization",new JScrollPane(struct_pan));

        //temp
        DefaultMutableTreeNode node=new DefaultMutableTreeNode("texture #1");
        DefaultTreeModel model = (DefaultTreeModel) lay_pan.tree.getModel();
        model.insertNodeInto(node, lay_pan.textures, lay_pan.textures.getChildCount());

        frame.pack();
        frame.setVisible(true);

    }

}