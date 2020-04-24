package com.erick;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.*;

public class Menu extends JMenuBar {

    JMenu fileMenu;
    JMenu editMenu;

    Menu() {
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");

        this.add(fileMenu);
        this.add(editMenu);

    }

}