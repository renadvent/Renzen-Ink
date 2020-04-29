package com.erick.view;

//import com.erick.Ink_Controller;
import com.erick.abstractviewpanel;
import com.erick.model.Caster;
import com.erick.Ink;
import com.erick.model.texture;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.*;

public class action_panel extends JPanel implements abstractviewpanel {

    Rectangle2D.Double r;
    Caster lastSelected;

    static final int min_rays = 10;
    static final int max_rays = 150;
    static final int init_rays = 125;

    public JButton selected_button = null;

    JFrame frame;
    Ink ink;

    // keeps changelisteners from causing rerenders
    // when updating sliders etc due to selection change
    // specifically, stops flag_selected_for_rerender from completing
    boolean set_by_code = false;

    @Override
    public void paintComponent(Graphics g) {

        Caster v = ink.selected_caster();

        set_by_code = true;

        if ((v != null) && (v != lastSelected)) {
            // num_of_rays.setValue(v.get_ray_count());
            num_of_strikes.setValue(v.max_penetrations);
            tolerance.setValue(v.tolerance);
            num_of_rays_s.setValue(v.get_ray_count());
            draw_from_caster.setSelected(v.cast_from_source);
            cast_shade.setSelected(v.cast_through);
            cast_strokes.setSelected(v.cast_along);
            lastSelected = v;

        } else if ((v == null) && v != lastSelected) {

            lastSelected = null;

        } else if ((v != null) && (v == lastSelected)) {

            num_of_strikes.setValue(v.max_penetrations);
            tolerance.setValue(v.tolerance);
            num_of_rays_s.setValue(v.get_ray_count());
            draw_from_caster.setSelected(v.cast_from_source);
            cast_shade.setSelected(v.cast_through);
            cast_strokes.setSelected(v.cast_along);
            lastSelected = v;

        }

        set_by_code = false;

    }

    public void flag_rerender_selected_caster() {
        if ((ink.selected_caster() != null) && (!set_by_code)) {
            ink.selected_caster().casts_rendered = false;
        }
    }

    public void flag_cvs_edited() {
        if ((ink.selected_caster() != null) && (!set_by_code)) {
            ink.selected_caster().cvs_edited = false;
        }
    }

    JSlider num_of_rays;
    JSlider num_of_strikes;
    JSlider tolerance;
    JSlider num_of_rays_s;
    JCheckBox draw_from_caster;
    JCheckBox cast_shade;
    JCheckBox cast_strokes;

    public JCheckBox check_base = null;
    public JCheckBox check_strokes = null;
    public JCheckBox check_tools = null;

    public final JCheckBox alpha_draw = new JCheckBox();

    public action_panel(final JFrame frame, final Ink ink) {

        this.frame = frame;
        this.ink = ink;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        //create_overall_detail_slider();
        create_strikes_slider();
        create_tolerance_slider();
        create_detail_slider();

        this.add(new JLabel("VIEW TOGGLES", JLabel.CENTER));
        create_show_base_checkbox();
        create_show_strokes_checkbox();
        create_show_tools_checkbox();

        this.add(new JLabel("CREATE/DELETE CASTERS", JLabel.CENTER));
        create_create_tool_button();
        create_delete_caster_button();

        this.add(new JLabel("CASTER TOGGLES", JLabel.CENTER));
        create_draw_from_base_checkbox();
        create_cast_shade_checkbox();
        create_cast_strokes_checkbox();

        this.add(new JLabel("CASTER BUTTONS", JLabel.CENTER));
        create_flip_button();
        create_move_caster_button();

        this.add(new JLabel("erase", JLabel.CENTER));
        //alpha_draw = ;
        this.add(alpha_draw);
        alpha_draw.addActionListener(new ActionListener() {

            boolean draw = false;

            @Override
            public void actionPerformed(ActionEvent e) {

                draw = !draw;
                lx = null;
                ly = null;

            }

            MouseMotionListener listener = null;
            boolean start = false;

            Integer lx;
            Integer ly;

            {
                listener = new MouseMotionListener() {

                    @Override
                    public void mouseDragged(MouseEvent e) {


                        if (draw) {

                            if ((lx == null) && (ly == null)) {
                                lx = e.getX();
                                ly = e.getY();
                                return;
                            }

                            Graphics2D g2d = ink.selected_texture().alpha_rendered_buffer.createGraphics();
                            Color color = new Color(0, 0, 100, 255);
                            int thickness = 30;
                            g2d.setStroke(new BasicStroke(thickness));
                            g2d.drawLine(lx, ly, e.getX(), e.getY());
                            lx = null;
                            ly = null;
                            //g2d.fillOval(e.getX(), e.getY(), thickness, thickness);
                            g2d.dispose();
                            ink.can_pan().repaint();

                        } else {
                            lx = null;
                            ly = null;
                        }

                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {

                    }
                };

                ink.can_pan().addMouseMotionListener(listener);
            }

        });

        this.add(new JLabel("CASTER LIST", JLabel.CENTER));

    }

    private void create_move_caster_button() {

        JButton move_caster = new JButton("move");
        move_caster.addActionListener(new ActionListener() {

            Rectangle2D.Double r;
            boolean active;
            double width;
            double height;

            {
                active = false;

                MouseMotionAdapter cl = new MouseMotionAdapter() {

                    {
                        Rectangle2D.Double r = new Rectangle2D.Double();
                        ink.can_pan().move_tool = r;
                        r.width = 20;
                        r.height = 20;
                    }

                    public void mouseMoved(MouseEvent e) {
                        if (active) {

                            r.x = e.getX();
                            r.y = e.getY();

                            Caster v = ink.selected_caster();

                            v.set_from(e.getX() - width, e.getY() - height);
                            v.set_to(e.getX() + width, e.getY() + height);

                            flag_rerender_selected_caster();
                            frame.repaint();

                        }
                    }

                };

                ink.can_pan().addMouseMotionListener(cl);
            }

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!active) {
                    if (ink.selected_caster() != null) {
                        active = true;
                        Caster temp = ink.selected_caster();
                        r = new Rectangle2D.Double(temp.from_x, temp.from_y, 50, 50);
                        ink.can_pan().move_tool = r;
                        Caster v = ink.selected_caster();
                        width = (v.from_x - v.to_x) / 2;
                        height = (v.from_y - v.to_y) / 2;
                    }

                } else if (active) {
                    r = null;
                    ink.can_pan().move_tool = null;
                    active = false;
                }

                frame.repaint();
            }
        });

        this.add(move_caster);
    }

    private void create_delete_caster_button() {
        JButton delete_viewer = new JButton("delete tool");
        delete_viewer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // viewer v = c.getViewers().getLast();
                if (ink.selected_caster() != null) {
                    ink.selected_texture().get_casters().remove(ink.selected_caster());
                    ink.act_pan().remove(ink.act_pan().selected_button);
                }
                frame.revalidate();
                flag_rerender_selected_caster();
                frame.repaint();
            }
        });

        this.add(delete_viewer);
    }

    public void create_create_tool_button() {
        JButton make_viewer = new JButton("create tool");
        this.add(make_viewer);

        // anonymous class checks for button press to toggle tool
        make_viewer.addActionListener(new ActionListener() {

            // ClckListener class checks for clicks on canvas to get points
            ClickListener cl;

            {
                cl = new ClickListener(ink.selected_texture(), ink);
                ink.can_pan().addMouseListener(cl);
            }

            public void actionPerformed(ActionEvent e) {

                System.out.println("clicked");

                if (cl.active()) {
                    cl.deactivate_clickListener();

                } else if (!cl.active()) {
                    cl.activate_clickListener();

                }

            }
        });
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {

    }

    class ClickListener extends MouseAdapter {

        public boolean finish = false;
        public int first_x = 0;
        public int first_y = 0;
        public boolean active = false;
        texture c;
        Ink ink;

        int tool_counter = 1;

        public ClickListener(texture c, Ink ink) {
            this.c = c;
            this.ink = ink;
        }

        public boolean active() {
            return active;
        }

        public void activate_clickListener() {
            active = true;
        }

        public void deactivate_clickListener() {
            finish = false;
            first_x = 0;
            first_y = 0;
            active = false;
        }

        @Override
        public void mousePressed(MouseEvent e) {

            System.out.println("event " + active);

            if (active) {

                if (finish) {

                    final Caster new_caster = ink.selected_texture().create_caster(first_x, first_y, e.getX(), e.getY(), 25,
                            false);

                    // SELECT BUTTON
                    final JButton new_caster_button = new JButton("Caster #" + tool_counter);
                    ink.act_pan().add(new_caster_button);
                    ink.change_selected_caster(new_caster);
                    ink.act_pan().selected_button = new_caster_button;

                    //temp
                    DefaultMutableTreeNode node=new DefaultMutableTreeNode("tool #"+tool_counter);
                    ink.lay_pan.model.insertNodeInto(node, ink.lay_pan.tools, ink.lay_pan.tools.getChildCount());


                    ink.lay_pan.tree.scrollPathToVisible(new TreePath(node.getPath()));

                    new_caster_button.addActionListener(new ActionListener() {

                        Caster v = new_caster;

                        @Override
                        public void actionPerformed(ActionEvent e) {

                            // HIGHLIGHT toggle
                            if (ink.selected_caster() == v) {
                                ink.change_selected_caster(null);
                                ink.act_pan().selected_button = null;
                            } else {
                                ink.change_selected_caster(v);
                                ink.act_pan().selected_button = new_caster_button;
                            }

                            ink.frame().repaint();
                        }
                    });

                    tool_counter++;
                    ink.frame().revalidate();
                    ink.frame().repaint();
                    deactivate_clickListener();

                } else if (!finish) {

                    System.out.println("start: " + e.getX() + ", " + e.getY());

                    first_x = e.getX();
                    first_y = e.getY();

                    finish = true;
                }

            }

        }

    }

    private void create_cast_strokes_checkbox() {
        cast_strokes = new JCheckBox("cast strokes");
        cast_strokes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ink.selected_caster() != null) {
                    ink.selected_caster().cast_along = (!ink.selected_caster().cast_along);
                    System.out.println("now: " + ink.selected_caster().cast_along);
                }
                flag_rerender_selected_caster();
                frame.repaint();
            }
        });

        this.add(cast_strokes);
    }

    private void create_cast_shade_checkbox() {
        cast_shade = new JCheckBox("cast shade");
        cast_shade.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ink.selected_caster() != null) {

                    ink.selected_caster().cast_through = (!ink.selected_caster().cast_through);
                    ink.selected_caster().max_penetrations = 1;

                    if (!ink.selected_caster().cast_through) {
                        ink.selected_caster().max_penetrations = 0;
                    }
                }

                flag_rerender_selected_caster();
                frame.repaint();
            }
        });

        this.add(cast_shade);
    }

    private void create_draw_from_base_checkbox() {
        draw_from_caster = new JCheckBox("draw from caster", null, false);

        draw_from_caster.addActionListener(new ActionListener() {

            // public void
            public void actionPerformed(ActionEvent e) {

                if (ink.selected_caster() != null) {
                    ink.selected_caster().cast_from_source = !ink.selected_caster().cast_from_source;
                }
                flag_rerender_selected_caster();
                frame.repaint();
            }
        });

        this.add(draw_from_caster);
    }

    private void create_flip_button() {
        JButton flip_last = new JButton("flip selected tool");
        flip_last.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ink.selected_caster() != null) {
                    ink.selected_caster().flip();
                }
                flag_rerender_selected_caster();
                frame.repaint();
            }
        });

        this.add(flip_last);
    }

    private void create_show_tools_checkbox() {
        check_tools = new JCheckBox("show tools", null, false);
        check_tools.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                frame.repaint();
            }
        });
        this.add(check_tools);
    }

    private void create_show_strokes_checkbox() {
        check_strokes = new JCheckBox("show strokes", null, true);
        check_strokes.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                frame.repaint();
            }
        });
        this.add(check_strokes);
    }

    private void create_show_base_checkbox() {
        check_base = new JCheckBox("show base", null, true);

        check_base.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                frame.repaint();
            }
        });

        this.add(check_base);
    }

    private void create_detail_slider() {
        JLabel sliderLabel;
        sliderLabel = new JLabel("detail (selected)", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        num_of_rays_s = new JSlider(JSlider.HORIZONTAL, min_rays, max_rays, init_rays);
        num_of_rays_s.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int ray_count_s = (int) source.getValue();

                if (ink.selected_caster() != null) {
                    ink.selected_caster().set_ray_count(ray_count_s);
                }
                flag_rerender_selected_caster();
                frame.repaint();

            }

        });

        int major_spacing = 20;
        int minor_spacing = 5;
        set_slider_properties(num_of_rays_s, major_spacing, minor_spacing);

        this.add(sliderLabel);
        this.add(num_of_rays_s);
    }

    private void create_tolerance_slider() {
        JLabel sliderLabel;
        sliderLabel = new JLabel("tolerance (selected)", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tolerance = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
        tolerance.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int vary = (int) source.getValue();

                if (ink.selected_caster() != null) {

                    ink.selected_caster().tolerance = vary;
                }
                flag_rerender_selected_caster();
                frame.repaint();

            }
        });

        set_slider_properties(tolerance, 50, 10);

        this.add(sliderLabel);
        this.add(tolerance);
    }

    private void create_strikes_slider() {
        JLabel sliderLabel;
        // strikes slider
        sliderLabel = new JLabel("strikes (selected)", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        num_of_strikes = new JSlider(JSlider.HORIZONTAL, 0, 3, 0);
        num_of_strikes.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int strike_count = (int) source.getValue();

                if (ink.selected_caster() != null) {
                    if (strike_count > 0) {
                        ink.selected_caster().cast_through = true;
                    } else {
                        ink.selected_caster().cast_through = false;
                    }
                    ink.selected_caster().max_penetrations = strike_count;
                }

                flag_rerender_selected_caster();
                frame.repaint();

            }
        });

        set_slider_properties(num_of_strikes, 1, 1);

        this.add(sliderLabel);
        this.add(num_of_strikes);
    }

    private void create_overall_detail_slider() {
        JLabel sliderLabel = new JLabel("detail (all)", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        num_of_rays = new JSlider(JSlider.HORIZONTAL, min_rays, max_rays, init_rays);
        num_of_rays.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int ray_count = (int) source.getValue();

                for (Caster x : ink.selected_texture().get_casters()) {
                    x.set_ray_count(ray_count);
                    //x.casts_rendered = false;
                }

                frame.repaint();

            }
        });

        set_slider_properties(num_of_rays, 20, 5);

        this.add(sliderLabel);
        this.add(num_of_rays);
    }

    private void set_slider_properties(JSlider num_of_rays_s, int major_spacing, int minor_spacing) {
        Font font;
        num_of_rays_s.setSnapToTicks(true);
        num_of_rays_s.setSnapToTicks(true);
        num_of_rays_s.setMajorTickSpacing(major_spacing);
        num_of_rays_s.setMinorTickSpacing(minor_spacing);
        num_of_rays_s.setPaintTicks(true);
        num_of_rays_s.setPaintLabels(true);
        num_of_rays_s.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        font = new Font("Serif", Font.ITALIC, 15);
        num_of_rays_s.setFont(font);
    }
}