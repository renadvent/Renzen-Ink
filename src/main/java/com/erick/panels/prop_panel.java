package com.erick.panels;

import com.erick.Ink;
import com.erick.frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;

public class prop_panel extends JPanel {

    final Ink ink;
    final JFrame frame;




    JButton addFrame=new JButton("add frame");
    JButton addPart=new JButton("add part");
    JButton movePart=new JButton("move part");
    JButton deletePart=new JButton("delete part");
    JButton resize_part=new JButton("resize part");
    JButton addConstraint=new JButton("add constraint");
    JButton testConstraints=new JButton("test constraints");

    JButton addTip=new JButton("add tip");
    JButton addTag=new JButton("add tag");

    JCheckBox lockPart=new JCheckBox("lock part");
    JCheckBox editConstraint= new JCheckBox("constraint locked");

    int frame_counter=1;

    public prop_panel(final JFrame frame, final Ink ink) {
        this.ink = ink;
        this.frame = frame;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        final prop_panel panel=this;

        // just for test
        ink.selected_component=ink.selected_texture().getComponents().getFirst();


        this.add(addFrame);
        addFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (ink.selected_component()!=null){

                    final com.erick.frame temp = new frame();
                    ink.selected_component().frames.add(temp);
                    ink.selected_frame=temp;


                    final JButton new_frame = new JButton("frame: "+frame_counter);
                    frame_counter++;
                    panel.add(new_frame);
                    panel.revalidate();

                    new_frame.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ink.selected_frame=temp;
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

                if (ink.selected_frame()!=null){
                    //com.erick.frame.part temp = new frame.part();
                    ink.selected_frame().create_part();
                }

            }
        });



    }
}
