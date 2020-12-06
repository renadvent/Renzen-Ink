package io.renzen.ink.Links;

import io.renzen.ink.CommandObjectsPanel.CanvasPanelCO;
import io.renzen.ink.Controllers.CanvasPanelController;
import io.renzen.ink.DomainObjects.Caster;
import io.renzen.ink.DomainObjects.RenderShape;
import io.renzen.ink.Services.BrushService;
import io.renzen.ink.Services.CasterService;
import io.renzen.ink.Services.RenderObjectService;
import io.renzen.ink.Services.RenzenService;
import io.renzen.ink.Views.CanvasPanel;
import io.renzen.ink.Views.JavaFXPanel;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    final RenderObjectService renderObjectService;
    final CasterService casterService;
    final RenzenService renzenService;
    final BrushService brushService;
    public JavaFXPanel javaFXPanel;

    final CanvasPanelController canvasPanelController;

    //CanvasPanelCO canvasPanelCO;


    /**
     * Mongo save to profile
     */

    //messy injection

    //ActionPanel actionPanel;
    public ActionPanelControllerToCanvasPanelViewLink(CanvasPanel canvasPanel, RenderObjectService renderObjectService,
                                                      CasterService casterService, RenzenService renzenService, BrushService brushService
    //){
            , CanvasPanelController canvasPanelController) {
        this.canvasPanel = canvasPanel;
        this.renderObjectService = renderObjectService;
        this.casterService = casterService;
        this.renzenService = renzenService;

        this.brushService = brushService;
        this.canvasPanelController = canvasPanelController;


        //this.canvasPanelController.getCanvasPanelCOtoRepaint().getBaseBuffer();


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


    public void paintOnCanvas() {

        var brush = brushService.getSelectedBrush();
        //var color = brushService.getSelectedColor();

        var adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                renderObjectService.addRenderShape(
                        new RenderShape("first click from brush",
                                new Ellipse2D.Double(e.getX() - brush.getSize() / 2, e.getY() - brush.getSize() / 2,
                                        brush.getSize(), brush.getSize()), brush.getColor()));


                repaintCanvas();
            }

            RenderShape last;
            double lastX;
            double lastY;

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                var temp = renderObjectService.addRenderShape(
                        new RenderShape("while dragging",
                                new Ellipse2D.Double(e.getX() - brush.getSize() / 2, e.getY() - brush.getSize() / 2,
                                        brush.getSize(), brush.getSize()), brush.getColor()));


                //TODO working on rendering lines

//                var temp = renderObjectService.addRenderShape(
//                        new RenderShape("firstClick", new Ellipse2D.Double(e.getX() - 50, e.getY() - 50, 100, 100)));
//

                if (last != null) {
                    renderObjectService.addRenderShape(
                            new RenderShape("line between",
                                    new Line2D.Double(lastX, lastY, e.getX(), e.getY()), brush.getColor()));
                }

                last = temp;
                lastX = e.getX();
                lastY = e.getY();

                repaintCanvas();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);


                removeCanvasListeners();
                repaintCanvas();
            }

        };

        canvasPanel.addMouseListener(adapter);
        canvasPanel.addMouseMotionListener(adapter);
    }


    public void toggleShowBackground() {
        canvasPanel.setShowBackground(!canvasPanel.isShowBackground());
        repaintCanvas();
    }

    public void saveFile(File file) {

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


    //TODO remove
    public String saveCanvasToArticle(String articleId) {
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

//        Map<String, Object> multiValueMap = new HashMap<>();
//        multiValueMap.put("title", "an image");
//        multiValueMap.put("file", fileContent);
//        multiValueMap.put("userId", renzenService.getLoggedInUser().get_id());


        //@PostMapping(path="/addScreenshotToArticle/{id}")
        //create request
        var request = webClient
                .post()
                .uri(URI.create(renzenService.getRoot() + "/addScreenshotToArticle/" + articleId))
                .header("Authorization", renzenService.getAuthToken())
                //.uri(URI.create("http://localhost:8080/addScreenshotToArticle/"+articleId))
//                .uri(URI.create("http://renzen.io/addScreenshotToArticle/"+articleId))
//                .uri(URI.create("http://localhost:8080/addImage"))
                .bodyValue(fileContent);

        //send request and get response
        var jacksonResponse = Objects.requireNonNull(request.exchange().block())
                .bodyToMono(String.class).block();

        //print response
        System.out.println(jacksonResponse);

        return jacksonResponse;
    }


    public String SAVECANVASANDCREATENEWARTICLEONRENZEN() {

        //get canvas and save it to a temporary file as a png
        var base = this.canvasPanelController.getCanvasPanelCO().getBaseBuffer();
        var canvasPanelCO = this.canvasPanelController.getCanvasPanelCO();

        //TODO
        //for cases for right now, base buffer will be largest buffer
        //for future, will need to set max canvas size in CanvasPanelCO

        BufferedImage bi = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //var bi = base.getSubimage(0,0,base.getWidth(),base.getHeight());
        //BufferedImage bi = new BufferedImage(canvasPanelCO.getBaseBuffer().getWidth(), canvasPanelCO.getBaseBuffer().getHeight(), BufferedImage.TYPE_INT_ARGB);


        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        //this.canvasPanelController.getInit().getBaseBuffer().

        //g2d.drawImage(base);
        //canvasPanel.printAll(g2d);

        //if (showBackground) {
            g2d.drawImage(canvasPanelCO.getBaseBuffer(), 0, 0, null);
        //}

        for (var caster : canvasPanelController.getCanvasPanelCOtoRepaint().getCasterCOList()) {
            g2d.drawImage(caster.getStrokeBuffer(), 0, 0, null);
        }

        //draws RenderShapes on screen
        for (RenderShape renderShape : renderObjectService.getRenderShapeArrayList()) {
            g2d.setColor(renderShape.getColor());
            g2d.draw(renderShape.getShape());
        }


        g2d.dispose();

        File file = null;

        try {
            file = File.createTempFile("image", ".png");
//            ImageIO.write(bi, "png", file);
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
        //server needs to get userid from auth, not here

        //create request
        var request = webClient
                .post()

                .uri(URI.create(renzenService.getRoot() + "/CREATE_ARTICLE_DRAFT_FROM_APP"))
//                .uri(URI.create(renzenService.getRoot() + "/addImage"))
                .header("Authorization", renzenService.getAuthToken())
//                .uri(URI.create("http://localhost:8080/addImage"))
                .bodyValue(multiValueMap);

        //send request and get response
//        var jacksonResponse = Objects.requireNonNull(request.exchange().block())
//                .bodyToMono(String.class).block();

        var jacksonResponse = Objects.requireNonNull(request.exchange().block())
                .bodyToMono(HashMap.class).block();

        //print response
        System.out.println(jacksonResponse);


        System.out.println("Trying to open");

        //TODO switch from uploading just an image, to uploading an image that creates a draft

        try {


            var URL = new java.net.URL(renzenService.getRoot()



                    + "/OPEN_ARTICLE_DRAFT_FROM_APP?articleID="
                    //+ "/newCreateArticle?image="
                    + URLEncoder.encode((String)jacksonResponse.get("articleID"), StandardCharsets.UTF_8)
                    + "&token="
                    + URLEncoder.encode(renzenService.getAuthToken(), StandardCharsets.UTF_8));


//                    + "&link="
//                    + URLEncoder.encode((String)jacksonResponse.get("absoluteURL"), StandardCharsets.UTF_8))
                    ;


//            var URL = new java.net.URL(renzenService.getRoot()
//                    + "/newCreateArticle?image="
//                    + URLEncoder.encode((String)jacksonResponse.get("SASUrl"), StandardCharsets.UTF_8)
//                    + "&token="
//                    + URLEncoder.encode(renzenService.getAuthToken(), StandardCharsets.UTF_8)
//                    + "&link="
//                    + URLEncoder.encode((String)jacksonResponse.get("absoluteURL"), StandardCharsets.UTF_8))
//                    ;

            //opens browser window, logs in, and goes to page to create a post
            java.awt.Desktop.getDesktop().browse(URL.toURI());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("could not open");
        }

        return (String)jacksonResponse.get("SASUrl");

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

                //?????
//                renderObjectService.addRenderShape(
//                        new RenderShape("firstClick", new Ellipse2D.Double(e.getX() - 50, e.getY() - 50, 100, 100)));

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
