package io.renzen.ink.Links;

import io.renzen.ink.DomainObjects.Caster;
import io.renzen.ink.DomainObjects.RenderShape;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderObjectService;
import io.renzen.ink.Services.RenzenService;
import io.renzen.ink.Views.CanvasPanel;
import io.renzen.ink.Views.JavaFXPanel;
import lombok.Data;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Allows action panel to collect info from canvas panel
 * to then send to ActionPanelController
 */

@Controller
public class ActionPanelControllerToCanvasPanelViewLink {

    final CanvasPanel canvasPanel;
    public JavaFXPanel javaFXPanel;

    final RenderObjectService renderObjectService;
    final CasterService casterService;
    final RenzenService renzenService;


    /**
     * Mongo save to profile
     */

    //messy injection

    //ActionPanel actionPanel;


    public ActionPanelControllerToCanvasPanelViewLink(CanvasPanel canvasPanel, RenderObjectService renderObjectService,
                                                      CasterService casterService, RenzenService renzenService) {
        this.canvasPanel = canvasPanel;
        this.renderObjectService = renderObjectService;
        this.casterService = casterService;
        this.renzenService = renzenService;

    }

    public void repaintCanvas() {
        canvasPanel.validate();
        canvasPanel.repaint();
    }

    //returns link

//    public void openFile(File file){
//
//        BufferedImage loadedImage = null;
//
//        try {
//            //File f = new File("src/main/java/io/renzen/ink/body.jpg");
//            loadedImage = ImageIO.read(file);
//        } catch (IOException exception) {
//            System.out.println(exception.getMessage());
//            System.exit(0);
//        }
//
//        canvasPanelC
//
//
//
//    }

    public void toggleShowBackground(){
        canvasPanel.setShowBackground(!canvasPanel.isShowBackground());
        repaintCanvas();
    }

    public void saveFile(File file){

        BufferedImage bi = new BufferedImage(canvasPanel.getWidth(), canvasPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        canvasPanel.printAll(g2d);
        g2d.dispose();

        try {
            //file = File.createTempFile("image", ".png");
            ImageIO.write(bi, "png", file);
        } catch (Exception exception) {
            exception.printStackTrace();
            //return "failed";
        }
    }

    public String saveCanvasToMongoRepository() {

        //get canvas and save it to a temporary file as a png
        BufferedImage bi = new BufferedImage(canvasPanel.getWidth(), canvasPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        canvasPanel.printAll(g2d);
        g2d.dispose();

        File file = null;

        try {
            file = File.createTempFile("image", ".png");
            ImageIO.write(bi, "png", file);
        } catch (Exception exception) {
            exception.printStackTrace();
            return "failed";
        }

        String fileContent = "";

        try {
            fileContent = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
        } catch (Exception exception) {
            System.out.println("count get contents");
            exception.printStackTrace();
            return "failed";
        }

        //create webclient
        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Map<String, Object> multiValueMap = new HashMap<>();
        multiValueMap.put("title", "an image");
        multiValueMap.put("file", fileContent);
        multiValueMap.put("userId", renzenService.getLoggedInUser().get_id());

        //create request
        var request = webClient
                .post()
                .uri(URI.create("http://renzen.io/addImage"))
//                .uri(URI.create("http://localhost:8080/addImage"))
                .bodyValue(multiValueMap);

        //send request and get response
        var jacksonResponse = Objects.requireNonNull(request.exchange().block())
                .bodyToMono(String.class).block();

        //print response
        System.out.println(jacksonResponse);

        return jacksonResponse;

    }

//    Color color = null;
//    public void setCasterColor(Color color){
//        this.color = color;
//    }


    /**
     * this function will create a click listener
     * that will listen for the given number of clicks on the canvas
     * and then
     */
    public void getClicksFromCanvasPanelAndCreateCaster(String casterName) {

        /**
         * begins events to create a caster
         * "click and drag"
         */

        var adapter = new MouseAdapter() {

            /**
             * shows user it is active
             * @param e
             */
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                if (renderObjectService.findByName("firstClick") == null) {
                    renderObjectService.deleteByName("beforeClick");

                    renderObjectService.addRenderShape(new RenderShape("beforeClick",
                            new Ellipse2D.Double(e.getX() - 25, e.getY() - 25, 50, 50)));

                    canvasPanel.validate();
                    canvasPanel.repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                renderObjectService.deleteByName("drag");

                RenderShape renderShape = renderObjectService.findByName("firstClick");

                Shape shape = renderShape.getShape();
                Ellipse2D.Double circle = (Ellipse2D.Double) shape;

                renderObjectService.addRenderShape(new RenderShape("drag",
                        new Ellipse2D.Double(e.getX() - 25, e.getY() - 25, 50, 50)));

                renderObjectService.addRenderShape(new RenderShape("drag",
                        new Line2D.Double(circle.getCenterX(), circle.getCenterY(), e.getX(), e.getY())));

                canvasPanel.validate();
                canvasPanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                renderObjectService.deleteByName("beforeClick");

                /**
                 * start tracking mouse and drawing preview
                 * to current location
                 */

                //System.out.println("PRESSED");

                renderObjectService.addRenderShape(
                        new RenderShape("firstClick", new Ellipse2D.Double(e.getX() - 50, e.getY() - 50, 100, 100)));

                canvasPanel.validate();
                canvasPanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                var firstClick = (Ellipse2D.Double) renderObjectService.findByName("firstClick").getShape();

                //creates caster extending from first click to where mouse was released

                var temp = new Caster(casterName, firstClick.getX(), firstClick.getY(), e.getX(), e.getY());

                //TODO working on here
                temp.setColor(casterService.getCasterColor());
                Caster caster = casterService.save(temp);

                casterService.setSelectedCaster(temp);

                //canvasPanelControllerToActionPanelViewLink.updateActionPanelWithSelectedCaster();


                javaFXPanel.UpdateActionPanelToSelectedCaster();

                renderObjectService.deleteByName("drag");
                renderObjectService.deleteByName("firstClick");

                canvasPanel.validate();
                canvasPanel.repaint();


                removeCanvasListeners();

                /**
                 * end tracking and delete listener
                 * and create new Caster
                 */
            }
        };

        canvasPanel.addMouseListener(adapter);
        canvasPanel.addMouseMotionListener(adapter);
    }

    public void removeCanvasListeners() {

        for (var listener : canvasPanel.getMouseListeners()) {
            canvasPanel.removeMouseListener(listener);
        }

        for (var listener : canvasPanel.getMouseMotionListeners()) {
            canvasPanel.removeMouseMotionListener(listener);
        }
    }


}
