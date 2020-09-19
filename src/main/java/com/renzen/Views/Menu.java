package com.renzen.Views;

import javax.swing.*;

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