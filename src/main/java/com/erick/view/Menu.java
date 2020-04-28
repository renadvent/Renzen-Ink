package com.erick.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class Menu extends JMenuBar {

    JMenu fileMenu;
    JMenu editMenu;

    public Menu() {
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");

        this.add(fileMenu);
        this.add(editMenu);

    }

}