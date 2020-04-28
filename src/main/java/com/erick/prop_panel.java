package com.erick;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class prop_panel extends JPanel {

    Ink ink;
    JFrame frame;

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

    public prop_panel(JFrame frame, Ink ink) {
        this.ink = ink;
        this.frame = frame;


        this.add(addFrame);
        this.add(addPart);
        this.add(addConstraint);
        add(lockPart);
        add(editConstraint);

        addPart.addActionListener(new ActionListener() {

            {

            }

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });



    }
}
