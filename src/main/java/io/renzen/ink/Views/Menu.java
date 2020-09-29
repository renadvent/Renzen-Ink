package io.renzen.ink.Views;

import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class Menu extends JMenuBar {
    JMenu fileMenu;
    JMenu editMenu;

    public Menu() {
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");

        this.add(fileMenu);
        this.add(editMenu);

        //setPreferredSize();

    }
}
