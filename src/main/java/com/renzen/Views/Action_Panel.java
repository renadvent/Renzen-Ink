package com.renzen.Views;

import com.renzen.Ink;
import com.renzen.Models.Caster;
import com.renzen.Models.Texture;
import com.renzen.abstractviewpanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;

public class Action_Panel extends JPanel implements abstractviewpanel {

    static final int min_rays = 10;
    static final int max_rays = 150;
    static final int init_rays = 125;
    public final JCheckBox alpha_draw = new JCheckBox();
    public JButton selected_button = null;
    public JCheckBox check_base = null;
    public JCheckBox check_strokes = null;
    public JCheckBox check_tools = null;
    Rectangle2D.Double r;
    Caster lastSelected;
    boolean set_by_code = false;
    JFrame frame;
    Ink ink;
    JSlider num_of_rays;
    JSlider num_of_strikes;
    JSlider tolerance;
    JSlider num_of_rays_s;
    JCheckBox draw_from_caster;
    JCheckBox cast_shade;
    JCheckBox cast_strokes;

    public Action_Panel(final JFrame frame, final Ink ink) {

        this.frame = frame;
        this.ink = ink;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

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

            final boolean start = false;
            boolean draw = false;
            MouseMotionListener listener = null;
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
                                ink.can_pan().repaint();
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

            @Override
            public void actionPerformed(ActionEvent e) {

                draw = !draw;
                lx = null;
                ly = null;

            }

        });

        this.add(new JLabel("CASTER LIST", JLabel.CENTER));

    }

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

    private void create_move_caster_button() {

        final JCheckBox move_caster = new JCheckBox("transform");//new JButton("move");
        move_caster.addActionListener(new ActionListener() {

            Rectangle2D.Double r;
            Rectangle2D.Double t;
            Rectangle2D.Double f;
            boolean active;
            double width;
            double height;

            boolean in_move_square = false;
            boolean in_top_square = false;
            boolean in_bottom_square = false;

            boolean released = false;


            {
                active = false;

                MouseInputAdapter ia = new MouseInputAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        released = false;
                        in_move_square = (r.contains(e.getX(), e.getY()));
                        in_top_square = (t.contains(e.getX(), e.getY()));
                        in_bottom_square = (f.contains(e.getX(), e.getY()));
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        released = true;
                    }
                };

                //ink.can_pan().addMouseMotionListener(ia);

                MouseMotionAdapter cl = new MouseMotionAdapter() {

                    {
                        r = new Rectangle2D.Double();
                        ink.can_pan().move_tool = r;
                        r.width = 50;
                        r.height = 50;

                        t = new Rectangle2D.Double();
                        ink.can_pan().resize_tool_1 = t;
                        t.width = 50;
                        t.height = 50;

                        f = new Rectangle2D.Double();
                        ink.can_pan().resize_tool_2 = f;
                        f.height = 50;
                        f.width = 50;

                    }

                    public void mouseDragged(MouseEvent e) {
                        //public void mouseMoved(MouseEvent e) {

                        // detect where it was clicked

                        //if  (!released){

                        //  released=false;
                        //}

/*if                        if (released) {
                            //from last time, test again
                            in_move_square = (r.contains(e.getX(), e.getY()));
                            in_top_square = (t.contains(e.getX(), e.getY()));
                            in_bottom_square = (f.contains(e.getX(), e.getY()));
                            released=false;
                        }*/


                        //e.
                        if (!released) {
                            if (active && in_move_square) {

                                r.x = e.getX() - r.width / 2;
                                r.y = e.getY() - r.height / 2;

                                Caster v = ink.selected_caster();

                                v.set_from(e.getX() - width, e.getY() - height);
                                v.set_to(e.getX() + width, e.getY() + height);

                                flag_rerender_selected_caster();
                                frame.repaint();

                            } else if (active && in_top_square) {

                                t.x = e.getX() - t.width / 2;
                                t.y = e.getY() - t.height / 2;

                                Caster v = ink.selected_caster();

                                v.to_x = e.getX();
                                v.to_y = e.getY();

                                flag_rerender_selected_caster();
                                frame.repaint();

                                //v.set

                            } else if (active && in_bottom_square) {

                                f.x = e.getX() - f.width / 2;
                                f.y = e.getY() - f.height / 2;

                                Caster v = ink.selected_caster();

                                v.from_x = e.getX();
                                v.from_y = e.getY();

                                flag_rerender_selected_caster();
                                frame.repaint();

                            }


                            Caster v = ink.selected_caster();

                            if (v != null) {
                                width = (v.from_x - v.to_x) / 2;
                                height = (v.from_y - v.to_y) / 2;

                                r.x = v.from_x - width;
                                r.y = v.from_y - height;

                                f.x = v.from_x;
                                f.y = v.from_y;

                                t.x = v.to_x;
                                t.y = v.to_y;
                            }

                        }
                    }

                    //public void mouse

                };

                ink.can_pan().addMouseListener(ia);
                ink.can_pan().addMouseMotionListener(cl);
            }

            // initial placement of tool
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!active) {
                    if (ink.selected_caster() != null) {
                        active = true;
                        Caster temp = ink.selected_caster();

                        //r = new Rectangle2D.Double(temp.from_x, temp.from_y, 50, 50);
                        //ink.can_pan().move_tool = r;
                        Caster v = ink.selected_caster();
                        width = (v.from_x - v.to_x) / 2;
                        height = (v.from_y - v.to_y) / 2;

                        r.x = temp.from_x - width;
                        r.y = temp.from_y - height;

                        f.x = temp.from_x;
                        f.y = temp.from_y;

                        t.x = temp.to_x;
                        t.y = temp.to_y;
                    }

                } else if (active) {
                    //r = null;
                    //ink.can_pan().move_tool = null;
                    active = false;
                    //released=false;
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
            final ClickListener cl;

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
                int ray_count_s = source.getValue();

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
                int vary = source.getValue();

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
                int strike_count = source.getValue();

                if (ink.selected_caster() != null) {
                    ink.selected_caster().cast_through = strike_count > 0;
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
                int ray_count = source.getValue();

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

    class ClickListener extends MouseAdapter {

        public boolean finish = false;
        public int first_x = 0;
        public int first_y = 0;
        public boolean active = false;
        Texture c;
        Ink ink;

        int tool_counter = 1;

        public ClickListener(Texture c, Ink ink) {
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

                    new_caster_button.addActionListener(new ActionListener() {

                        final Caster v = new_caster;

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
}