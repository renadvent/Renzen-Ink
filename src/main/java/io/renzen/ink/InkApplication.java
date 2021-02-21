package io.renzen.ink;


import io.renzen.ink.ViewPanels.JavaFXPanel;
import javafx.application.Application;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.Style;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InkApplication extends Application {

    private static final Style STYLE = Style.DARK;

    public static Stage stage;
    public ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        var ctx = new SpringApplicationBuilder(InkApplication.class)
                .headless(false).run();
        springContext = ctx;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        var javaFXPanel = new JavaFXPanel(stage, springContext);
    }

    @Override
    public void stop() {
        springContext.stop();
        springContext.close();
    }

}
