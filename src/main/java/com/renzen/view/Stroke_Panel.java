package com.renzen.view;

import com.renzen.Ink;
import com.renzen.model.Pather.Curve.Section.CV;
import com.renzen.model.Point;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Stroke_Panel extends JPanel {

    protected JColorChooser tcc;
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

    // connect_every_n_rays

    // TODO
    // move to stroke style
    JSlider layer_each_stroke;
    Ink ink;
    JFrame frame;
    boolean set_by_code = false;
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
    String[] temp_list = {"CV #1", "CV #2"};
    JList<String> cv_list = new JList<String>(temp_list);
    int max_list_skip = 7;
    String[] temp_list2 = {"1", "2", "3", "4", "5", "6", "7"};
    JList<String> connect_list = new JList<String>(temp_list2);
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

            if (ink.selected_caster() != null) {

                set_by_code = true;

                opacity.setValue(ink.selected_caster().bb.opacity); // ?
                thickness.setValue(ink.selected_caster().bb.thickness);

                set_by_code = false;

                Graphics2D g2d2 = (Graphics2D) g;// single_stroke_buffer.createGraphics();
                g2d2.setColor(new Color(255, 0, 0));
                wipe_buffer(single_stroke_buffer);

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

                if (selected_cv != null && ink.selected_caster() != null) {

                    //TODO fix previews

                    LinkedList<Point> lp = new LinkedList<>();
                    lp.add(new Point(0, 100));
                    lp.add(new Point(100, 100));
                }

                g2d2.dispose();

            }

        }

    };
    JPanel through_preview = new JPanel();

    public Stroke_Panel(final JFrame frame, final Ink ink) {

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

                    int[] temp = connect_list.getSelectedIndices();
                    for (int w = 0; w < temp.length; w++) {
                        temp[w] = temp[w] + 1;
                        System.out.println(temp[w]);
                    }

                    ink.selected_caster().p1.connect_at = temp;
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
                double v = source.getValue();

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
                int v = source.getValue();

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
                int v = source.getValue();

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
                int v = source.getValue();

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
                int v = source.getValue();

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
                int v = source.getValue();

                if (ink.selected_caster() != null) {
                    ink.selected_caster().bb.opacity = v;

                    flag_update_selected_caster();
                    //flag_rerender_selected_caster();
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
                int v = source.getValue();

                if (ink.selected_caster() != null) {
                    ink.selected_caster().bb.thickness = v;

                    flag_update_selected_caster();
                    //flag_rerender_selected_caster();
                    frame.repaint();
                }

            }
        });

        add(new JLabel("Color", JLabel.CENTER));
        //Set up color chooser for setting text color
        tcc = new JColorChooser();//(banner.getForeground());
        tcc.getSelectionModel().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                Color newColor = tcc.getColor();
                if (ink.selected_caster() != null) {
                    ink.selected_caster().bb.color = newColor;

                    flag_update_selected_caster();
                    //flag_rerender_selected_caster();
                    frame.repaint();
                }
            }
        });
        tcc.setBorder(BorderFactory.createTitledBorder(
                "Choose Text Color"));

        add(tcc, BorderLayout.PAGE_END);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
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

        return temp;
    }

    CV selected_cv() {
        return selected_cv;
    }

    public void flag_update_selected_caster() {
        if (ink.selected_caster() != null) {
            ink.selected_caster().casts_updated = true;
        }
    }

    public void flag_rerender_selected_caster() {
        if ((ink.selected_caster() != null) && (!set_by_code)) {
            ink.selected_caster().casts_rendered = false;
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