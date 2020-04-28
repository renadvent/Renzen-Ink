package com.erick;

import com.erick.model.Caster;
import com.erick.model.Component;
import com.erick.model.frame;
import com.erick.model.texture;
import com.erick.view.action_panel;
import com.erick.view.canvas_panel;
import com.erick.view.prop_panel;

import javax.swing.*;

public class Ink_Controller {

    Ink ink;
    JFrame frame;

    private Caster selected_caster;
    public texture selected_texture;
    public frame selected_frame;
    public Component selected_component;

    // GETTERS
    public JFrame frame() {
        return frame;
    }
    public Caster selected_caster() {
        return selected_caster;
    }
    public texture selected_texture() {
        return selected_texture;
    }
    public com.erick.model.frame selected_frame(){return selected_frame; }
    public Component selected_component(){return selected_component;}
    public canvas_panel can_pan() {
        return ink.can_pan;
    }
    public action_panel act_pan() {
        return ink.act_pan;
    }
    public prop_panel prop_pan() {
        return ink.prop_pan;
    }

    Ink_Controller(Ink ink){

        this.ink=ink;
        this.frame=ink.frame;

    }

    // SETTERS
    public void change_selected_caster(Caster v) {

        if (selected_caster() != null) {
            selected_caster().highlighted = false;
        }

        selected_caster = v;

        if (selected_caster() != null) {
            selected_caster().highlighted = true;
        }

    }

    public void change_selected_texture(texture c) {
        selected_texture = c;
    }

    static texture new_texture(){
        // all can be accessed at texture.textures
        //lay_pan.textures.insertN

        //DefaultMutableTreeNode node=new DefaultMutableTreeNode("texture #"+texture.textures.size());
        //lay_pan.model.insertNodeInto(node, lay_pan.tools, lay_pan.tools.getChildCount());

        return new texture(1280,1024);
    }
}
