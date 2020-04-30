package com.erick.view;

import com.erick.Ink;
import com.erick.model.Part;
import com.erick.model.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.event.MouseInputAdapter;

import static java.lang.Math.abs;
import static java.lang.Math.min;

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

    JCheckBox select_parts = new JCheckBox("select parts");

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

        this.add(select_parts);
        select_parts.addActionListener(new ActionListener() {


            rect_from_mouse.select_multiple sm = null; //new rect_from_mouse.select_multiple()

            {


            }

            @Override
            public void actionPerformed(ActionEvent e) {

                if (select_parts.isSelected()){
                    sm = new rect_from_mouse.select_multiple(ink.selected_frame.items);
                    ink.can_pan().addMouseListener(sm);
                    ink.can_pan().addMouseMotionListener(sm);
                }

            }
        });

        this.add(movePart);
        movePart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                rect_from_mouse.move_with_mouse mml = new rect_from_mouse.move_with_mouse(ink){
                    @Override
                    void set_variables() {
                        super.set_variables();
                        // need to fix here

                        ink.selected_part.rect.x=end_x; //maybe use Integer types so it can reset by itself
                        ink.selected_part.rect.y=end_y;
                    }

                    @Override
                    public void on_completion() {
                        super.on_completion();

                        boolean contains = ink.selected_frame.rect.contains(ink.selected_part.rect);

                        if (!contains){
                            ink.selected_part.rect.x=start_x;
                            ink.selected_part.rect.y=start_y;
                            reset();
                            ink.can_pan().repaint();
                        }
                    }
                };

            }


        });


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

                    //prop_renderables.add(r_temp);
                    rect_from_mouse mml = new rect_from_mouse(ink);

                    temp.rect = mml.r_temp;

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

        addPart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (ink.selected_frame() != null) {

                    final Part temp = ink.create_part();
                    ink.selected_frame().items.add(temp);
                    ink.selected_part = temp;

                    final JButton new_part = new JButton("part: " + frame_counter);
                    part_counter++;

                    //prop_renderables.add(r_temp);
                    //mouseinputadaptor
                    rect_from_mouse mml = new rect_from_mouse(ink) {
                        @Override
                        public void on_completion() {
                            super.on_completion();

                            // constrains part into parent frame
                            boolean contains = ink.selected_frame().rect.contains(r_temp);

                            if (!contains) {

                                reset();

                            }
                        }
                    };

                    temp.rect = mml.r_temp;

                    ink.can_pan().addMouseListener(mml);
                    ink.can_pan().addMouseMotionListener(mml);

                    panel.add(new_part);
                    panel.revalidate();

                    new_part.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ink.selected_part = temp;
                        }
                    });


                }

            }


        });

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

    private static class rect_from_mouse extends MouseInputAdapter {

        final Rectangle2D.Double r_temp;
        private final Ink ink;
        //boolean completed=false;


        int start_x;
        int start_y;

        int end_x;
        int end_y;

        public rect_from_mouse(Ink ink) {
            this.ink = ink;
            r_temp = new Rectangle2D.Double();
            ink.can_pan().prop_renderables.add(r_temp);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mouseClicked(e);

            System.out.println("pressed");

            start_x = e.getX();
            start_y = e.getY();

            r_temp.x = start_x;
            r_temp.y = start_y;

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

            r_temp.width = abs(start_x - end_x);
            r_temp.height = abs(start_y - end_y);

            ink.can_pan().repaint();

            ink.can_pan().removeMouseListener(this); //?
            ink.can_pan().removeMouseMotionListener(this); //?
            on_completion();
            //completed=true;
        }

        public void reset() {
            ink.can_pan().addMouseListener(this); //?
            ink.can_pan().addMouseMotionListener(this); //?
            r_temp.setRect(0, 0, 0, 0);
        }

        public void on_completion() {
        }


        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);

            System.out.println("dragged");

            end_x = e.getX();
            end_y = e.getY();

            r_temp.x = min(end_x, start_x);
            r_temp.y = min(end_y, start_y);

            r_temp.width = abs(start_x - end_x);
            r_temp.height = abs(start_y - end_y);

            ink.can_pan().repaint();


        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }


        //=================================================================

        public static class move_with_mouse extends MouseInputAdapter {

            //final Rectangle2D.Double r_temp;
            private final Ink ink;
            //boolean completed=false;

            int start_x;
            int start_y;

            int end_x;
            int end_y;

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                start_x=(int) ink.selected_part.rect.x;
                start_y=(int) ink.selected_part.rect.y;
            }

            void reset(){
                ink.can_pan().addMouseMotionListener(this);
                ink.can_pan().addMouseListener(this);
            }

            public move_with_mouse(Ink ink) {
                this.ink = ink;
                ink.can_pan().addMouseMotionListener(this);
                ink.can_pan().addMouseListener(this);
                ink.can_pan().repaint();
                //r_temp = new Rectangle2D.Double();
                //ink.can_pan().prop_renderables.add(r_temp);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                ink.can_pan().removeMouseMotionListener(this);
                ink.can_pan().removeMouseListener(this);
                on_completion();
            }

            public void on_completion() {
            }


            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                System.out.println("dragged");

                end_x = e.getX();
                end_y = e.getY();

                //change variables

                set_variables();

                ink.can_pan().repaint();


            }

            void set_variables() {
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }


        }

        public static class select_multiple extends MouseInputAdapter{

            LinkedList<selectable> boxes=null;

            select_multiple(LinkedList<selectable> sl){

                boxes=sl;

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                for (selectable sb : boxes){

                    if (sb.rect!=null) {
                        if (sb.rect.contains(e.getX(), e.getY())) {
                            sb.selected = !sb.selected;
                        }
                    }

                }

                //ink.can_pan.repaint();
            }
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
}
