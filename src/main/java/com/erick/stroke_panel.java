package com.erick;
import com.erick.Pather.Curve.Section.CV;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


//import sun.tools.jconsole.ConnectDialog;

import java.awt.Graphics2D;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
//import java.awt.Rectangle2D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.*;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

import javax.swing.colorchooser.*;

public class stroke_panel extends JPanel {

    // color_gradient
    // thickness_gradient
    // connect_every_n_points []
    // on_every_m_points []

    // preview canvases
    BufferedImage single_stroke_buffer;
    BufferedImage outline_strokes_buffer;
    BufferedImage shade_strokes_buffer;

    int render_width = 500;
    int render_height = 500;

    JCheckBox render_pre;
    JCheckBox render_main;
    JCheckBox render_post;

    JSlider line_thickness;
    JSlider layer_each_stroke;

    // connect_every_n_rays

    // TODO
    // move to stroke style

    Ink ink;
    JFrame frame;

    boolean set_by_code = false;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // preview
        /*
         * Graphics2D g2d = (Graphics2D) g;
         * 
         * if (ink.selected_caster() != null) {
         * 
         * set_by_code = true; main_mod.setValue((int)
         * ink.selected_caster().stroke_style.cvis_main.getLast().main_const);
         * 
         * Graphics2D g2d2 = single_stroke_buffer.createGraphics(); g2d2.setColor(new
         * Color(255, 0, 0)); wipe_buffer(single_stroke_buffer);
         * 
         * // g2d.drawRect(0,0, 100, 150); LinkedList<Shape> ll =
         * ink.selected_caster().stroke_style.generate_range_preview(100, 100, 1); for
         * (Shape s : ll) { // need to check if these shapes are clicked on
         * g2d2.draw(s); }
         * 
         * ll = ink.selected_caster().stroke_style.generate_range_preview(100, 200, 10);
         * for (Shape s : ll) { g2d2.draw(s); }
         * 
         * g2d2.dispose();
         * 
         * }
         * 
         * g2d.drawImage(single_stroke_buffer, 0, 100, null); // see javadoc for more
         * info on the parameters
         */

        set_by_code = false;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 1000);
    }

    public BufferedImage create_initial_stroke_buffer() {

        BufferedImage temp = new BufferedImage(render_width, render_height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = temp.createGraphics();
        g2d.setColor(new Color(255, 0, 0));
        // g2d.drawRect(0, 0, 100, 500);

        return temp;
    }

    JScrollPane scroll = new JScrollPane();

    JSlider main_mod;

    JSlider prop_along = new JSlider(JSlider.HORIZONTAL, 0, 100, 0); //-100
    JSlider rx = new JSlider(JSlider.HORIZONTAL, 0, 300, 0);
    JSlider ry = new JSlider(JSlider.HORIZONTAL, 0, 300, 0);
    JSlider ox = new JSlider(JSlider.HORIZONTAL, -300, 300, 0);
    JSlider oy = new JSlider(JSlider.HORIZONTAL, -300, 300, 0);
    JSlider opacity = new JSlider(JSlider.HORIZONTAL, 0, 255, 1);

    JSlider thickness = new JSlider(JSlider.HORIZONTAL, 0, 50, 1);

    CV selected_cv;

    CV selected_cv() {
        return selected_cv;
    }

    String[] temp_list = { "CV #1", "CV #2" };
    JList<String> cv_list = new JList<String>(temp_list);

    int max_list_skip = 7;
    String[] temp_list2 = { "1", "2", "3", "4", "5", "6", "7" };
    JList<String> connect_list = new JList<String>(temp_list2);

    // Pather.Curve.Section.CV

    JPanel single_preview = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);

            CV cv = selected_cv;

            if (cv != null) {

                System.out.println("changing values");
                System.out.println(((cv.prop)));
                prop_along.setValue((int) (cv.prop * 100));
                rx.setValue((int) cv.rx);
                ry.setValue((int) cv.ry);
                ox.setValue((int) cv.ox);
                oy.setValue((int) cv.oy);

            }

            // single_preview.setBorder(BorderFactory.createLineBorder(Color.black));

            if (ink.selected_caster() != null) {

                set_by_code = true;

                opacity.setValue(ink.selected_caster().bb.opacity); // ?
                thickness.setValue(ink.selected_caster().bb.thickness);

                /*
                 * for (int u : ink.selected_caster().p1.connect_at) {
                 * connect_list.addSelectionInterval(u, u); }
                 */

                set_by_code = false;

                Graphics2D g2d2 = (Graphics2D) g;// single_stroke_buffer.createGraphics();
                g2d2.setColor(new Color(255, 0, 0));
                wipe_buffer(single_stroke_buffer);

                // ink.selected_caster().p1.create_curves_across_points(intersection_points);

                // wrong source
                LinkedList<Shape> ll = ink.selected_caster().stroke_style.generate_range_preview(0, 100, 1);
                for (Shape s : ll) {
                    g2d2.draw(s);
                }

                g2d2.dispose();

            }

        }

    };
    JPanel along_preview = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);

            if (ink.selected_caster() != null) {

                Graphics2D g2d2 = (Graphics2D) g;// single_stroke_buffer.createGraphics();
                g2d2.setColor(new Color(255, 0, 0));
                wipe_buffer(single_stroke_buffer);

                if (selected_cv != null && ink.selected_caster()!=null) {

                    //TODO fix previews

                    LinkedList<point> lp = new LinkedList<>();
                    lp.add(new point(0, 100));
                    lp.add(new point(100, 100));

                    //ink.selected_caster().t.
                    

                }

                /*
                 * LinkedList<Shape> ll =
                 * ink.selected_caster().stroke_style.generate_range_preview(0, 100, 1);
                 * 
                 * ll = ink.selected_caster().stroke_style.generate_range_preview(0, 100, 10);
                 * for (Shape s : ll) { g2d2.draw(s); }
                 */

                g2d2.dispose();

            }

        }

    };
    JPanel through_preview = new JPanel();

    public stroke_panel(final JFrame frame, final Ink ink) {

        this.ink = ink;
        this.frame = frame;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        single_stroke_buffer = create_initial_stroke_buffer();

        add(new JLabel("STROKE PREVIEWS", JLabel.CENTER));

        add(new JLabel("Single Stroke (Preview)", JLabel.CENTER));
        single_preview.setPreferredSize(new Dimension(100, 100));
        single_preview.setBorder(BorderFactory.createLineBorder(Color.black));
        add(single_preview);

        add(new JLabel("Layered Along (Preview)", JLabel.CENTER));
        along_preview.setPreferredSize(new Dimension(100, 100));
        add(along_preview);

        add(new JLabel("Layered Through (Preview)", JLabel.CENTER));
        through_preview.setPreferredSize(new Dimension(100, 100));
        add(through_preview);

        add(new JLabel("CV", JLabel.CENTER));

        add(new JLabel("connect skip", JLabel.CENTER));
        connect_list.setVisibleRowCount(max_list_skip);
        connect_list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        connect_list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (ink.selected_caster() != null) {
                    ink.selected_caster().p1.connect_at = null;
                    ink.selected_caster().p1.connect_at = connect_list.getSelectedIndices();
                    // System.out.println(connect_list.getSelectedIndices().toString());

                    int[] temp = connect_list.getSelectedIndices();
                    for (int w = 0; w < temp.length; w++) {
                        temp[w] = temp[w] + 1;
                        System.out.println(temp[w]);
                    }

                    ink.selected_caster().p1.connect_at = temp;
                    // don't select 1

                    flag_rerender_selected_caster();
                    frame.repaint();
                }

            }
        });

        add(connect_list);

        cv_list.setVisibleRowCount(2);

        final JPanel this_one = this;
        add(cv_list);
        cv_list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.out.println("list: " + cv_list.getSelectedIndex());
                selected_cv = ink.selected_caster().p1.stroke.sections.getFirst().cvs.get(cv_list.getSelectedIndex());
                this_one.repaint();
            }
        });

        add(new JLabel("proportion along", JLabel.CENTER));
        add(prop_along);
        prop_along.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                double v = (double) source.getValue();

                if (selected_cv != null) {
                    selected_cv.prop = (v / 100.0);
                    flag_rerender_selected_caster();
                    frame.repaint();
                }
            }

        });

        add(new JLabel("Random X", JLabel.CENTER));
        add(rx);
        rx.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int v = (int) source.getValue();

                if (selected_cv() != null) {
                    selected_cv.rx = v;
                    flag_rerender_selected_caster();
                    frame.repaint();
                }

            }
        });

        add(new JLabel("Random Y", JLabel.CENTER));
        add(ry);
        ry.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int v = (int) source.getValue();

                if (selected_cv() != null) {
                    selected_cv.ry = v;
                    flag_rerender_selected_caster();
                    frame.repaint();
                }

            }
        });

        add(new JLabel("Offset X", JLabel.CENTER));
        add(ox);
        ox.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int v = (int) source.getValue();

                if (selected_cv() != null) {
                    selected_cv.ox = v;
                    flag_rerender_selected_caster();
                    frame.repaint();
                }

            }
        });

        add(new JLabel("Offset Y", JLabel.CENTER));
        add(oy);
        oy.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int v = (int) source.getValue();

                if (selected_cv() != null) {
                    selected_cv.oy = v;
                    flag_rerender_selected_caster();
                    frame.repaint();
                }

            }
        });

        add(new JLabel("BRUSH", JLabel.CENTER));
        add(new JLabel("Opacity", JLabel.CENTER));
        add(opacity);
        opacity.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int v = (int) source.getValue();

                if (ink.selected_caster() != null) {
                    ink.selected_caster().bb.opacity = v;
                    flag_rerender_selected_caster();
                    frame.repaint();
                }

            }
        });

        add(new JLabel("Thickness", JLabel.CENTER));
        add(thickness);
        thickness.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int v = (int) source.getValue();

                if (ink.selected_caster() != null) {
                    ink.selected_caster().bb.thickness = v;
                    flag_rerender_selected_caster();
                    frame.repaint();
                }

            }
        });

        add(new JLabel("Color", JLabel.CENTER));
        //Set up color chooser for setting text color
        tcc = new JColorChooser();//(banner.getForeground());
        tcc.getSelectionModel().addChangeListener(new ChangeListener(){

        public void stateChanged(ChangeEvent e) {
            Color newColor = tcc.getColor();
            if (ink.selected_caster() != null) {
                ink.selected_caster().bb.color = newColor;
                flag_rerender_selected_caster();
                frame.repaint();
            }
            //banner.setForeground(newColor);

        }
        });
        tcc.setBorder(BorderFactory.createTitledBorder(
                "Choose Text Color"));

        //add(bannerPanel, BorderLayout.CENTER);
        add(tcc, BorderLayout.PAGE_END);

        /*
         * JLabel sliderLabel; sliderLabel = new JLabel("main mod", JLabel.CENTER);
         * sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT); main_mod = new
         * JSlider(JSlider.HORIZONTAL, -300, 300, 0);
         * 
         * 
         * 
         * set_slider_properties(main_mod, 100, 50); this.add(sliderLabel);
         * this.add(main_mod);
         */

    }

    protected JColorChooser tcc;

    public void flag_rerender_selected_caster() {
        if ((ink.selected_caster() != null) && (!set_by_code)) {
            ink.selected_caster().casts_rendered = false;
        }
    }

    public class gradient {

        // they are connected in order
        LinkedList<control_point> points_along;

        class control_point {
            Color color;
            double strength;
            double position;
        }
    }

    private void wipe_buffer(BufferedImage b) {
        Graphics2D g2d = b.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2d.fillRect(0, 0, b.getWidth(), b.getHeight());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
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