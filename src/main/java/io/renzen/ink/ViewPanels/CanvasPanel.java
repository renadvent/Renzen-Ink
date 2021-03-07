package io.renzen.ink.ViewPanels;

import io.renzen.ink.ViewObjects.CanvasPanelCO;
import io.renzen.ink.ViewPanels.RenderLayers.AbstractCustomRenderLayer;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Component
@Data
public class CanvasPanel extends JPanel {

    public CanvasPanelCO canvasPanelCO;
    boolean showBackground = true;
    boolean showRayPath = true;
    ArrayList<AbstractCustomRenderLayer> renderLayerArrayList = new ArrayList<>();

    public CanvasPanel() {
        getInit();
    }

    public void getInit() {

        setCanvasPanelCO(new CanvasPanelCO());


        BufferedImage bufferedImage = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        BufferedImage loadedImage = null;

        try {
            File f = new File("src/main/java/io/renzen/ink/body.jpg");
            loadedImage = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        g2d.drawImage(loadedImage, 0, 0, null);

        getCanvasPanelCO().setBaseBuffer(bufferedImage);
//
//        tempBackground = bufferedImage;
//        setTempBackground(tempBackground);

    }

    public static Graphics2D resetHints(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        return g2d;
    }

    public void addRenderLayer(AbstractCustomRenderLayer rl) {
        renderLayerArrayList.add(rl);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderLayers(g);
    }

    public void renderLayers(Graphics g) {

        if (canvasPanelCO != null) {
            renderLayerArrayList.forEach(rl -> {
                rl.render(g);
            });
        }


    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }

}
