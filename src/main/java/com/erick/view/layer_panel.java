package com.erick.view;

import com.erick.Ink;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

public class layer_panel extends JPanel {

    public JTree tree;
    public final DefaultMutableTreeNode tools;
    public final DefaultMutableTreeNode textures;

    DefaultMutableTreeNode top;
    DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    public DefaultTreeModel model;

    public layer_panel(JFrame frame, final Ink ink){


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

        tools = new DefaultMutableTreeNode("Casters");
        textures = new DefaultMutableTreeNode("Textures");

        rootNode.add(textures);
        rootNode.add(tools);

        model = (DefaultTreeModel) tree.getModel();


        this.add(tree);
    }


}
