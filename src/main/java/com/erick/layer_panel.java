package com.erick;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class layer_panel extends JPanel {

    JTree tree;
    final DefaultMutableTreeNode tools;
    DefaultMutableTreeNode top;
    DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;

    layer_panel(JFrame frame, final Ink ink){


        rootNode = new DefaultMutableTreeNode("Ink");


        //rootNode = new DefaultMutableTreeNode("Root Node");
        treeModel = new DefaultTreeModel(rootNode);
        //treeModel.addTreeModelListener(new MyTreeModelListener());

        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        //////////

        //tree = new JTree(top);

        tools = new DefaultMutableTreeNode("Tools");
        rootNode.add(tools);



        this.add(tree);
    }


}
