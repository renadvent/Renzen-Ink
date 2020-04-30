package com.erick.view;

import com.erick.Ink;
import com.erick.model.frame;
import com.erick.model.texture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.event.MouseInputAdapter;

import static java.lang.Math.abs;

public class prop_panel extends JPanel {

    final Ink ink;
    final JFrame frame;


    JButton addFrame = new JButton("add frame");
    JButton addPart = new JButton("add part");
    JButton movePart = new JButton("move part");
    JButton deletePart = new JButton("delete part");
    JButton resize_part = new JButton("resize part");
    JButton addConstraint = new JButton("add constraint");
    JButton testConstraints = new JButton("test constraints");

    JButton addTip = new JButton("add tip");
    JButton addTag = new JButton("add tag");

    JCheckBox lockPart = new JCheckBox("lock part");
    JCheckBox editConstraint = new JCheckBox("constraint locked");

    int frame_counter = 1;
    int part_counter = 1;

    final LinkedList<Shape> prop_renderables = new LinkedList<Shape>();

    public prop_panel(final JFrame frame, final Ink ink) {
        this.ink = ink;
        this.frame = frame;

        //ink.can_pan().prop_renderables = prop_renderables;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        final prop_panel panel = this;

        // just for test
        ink.selected_component = ink.selected_texture().getComponents().getFirst();



        this.add(addFrame);
        addFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (ink.selected_component() != null) {

                    final com.erick.model.frame temp = new frame();
                    ink.selected_component().frames.add(temp);
                    ink.selected_frame = temp;


                    final JButton new_frame = new JButton("frame: " + frame_counter);
                    frame_counter++;


                    final Rectangle2D.Double r_temp = new Rectangle2D.Double();

                    //prop_renderables.add(r_temp);

                    ink.can_pan().prop_renderables.add(r_temp);

                    MouseInputAdapter mml = new MouseInputAdapter() {

                        int start_x;
                        int start_y;

                        int end_x;
                        int end_y;

                        @Override
                        public void mousePressed(MouseEvent e) {
                            super.mouseClicked(e);

                            System.out.println("pressed");

                            start_x = e.getX();
                            start_y = e.getY();

                            r_temp.x=start_x;
                            r_temp.y=start_y;

                            //end_x = 100;//e.getX();
                            //end_y = 100;//e.getY();

                            ink.can_pan().repaint();
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            super.mouseReleased(e);


                            System.out.println("released");
                            end_x = e.getX();
                            end_y = e.getY();

                            r_temp.width = start_x - end_x;
                            r_temp.height = start_y - end_y;

                            ink.can_pan().repaint();

                            ink.can_pan().removeMouseListener(this); //?
                            ink.can_pan().removeMouseMotionListener(this); //?
                        }

                        @Override
                        public void mouseDragged(MouseEvent e) {
                            super.mouseDragged(e);

                            System.out.println("dragged");

                            end_x = e.getX();
                            end_y = e.getY();

                            r_temp.width = abs(start_x - end_x);
                            r_temp.height = abs(start_y - end_y);

                            ink.can_pan().repaint();


                        }

                        @Override
                        public void mouseMoved(MouseEvent e) {

                        }
                    };

                    ink.can_pan().addMouseListener(mml);
                    ink.can_pan().addMouseMotionListener(mml);

                    panel.add(new_frame);
                    panel.revalidate();

                    new_frame.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ink.selected_frame = temp;
                        }
                    });

                }

            }
        });


        this.add(addPart);
        this.add(addConstraint);
        add(lockPart);
        add(editConstraint);

        addPart.addActionListener(new ActionListener() {

            {

            }

            @Override
            public void actionPerformed(ActionEvent e) {

                if (ink.selected_frame() != null) {
                    //com.erick.model.frame.part temp = new frame.part();
                    ink.selected_frame().create_part();


                }

            }
        });


    }

/*    class get_one_click extends MouseAdapter{

        texture c;
        Ink ink;

        get_one_click(texture c, Ink ink){
            this.c=c;
            this.ink=ink;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

        }
    }*/


}
