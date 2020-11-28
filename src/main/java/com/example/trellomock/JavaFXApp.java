package com.example.trellomock;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JavaFXApp extends Application {
    private ConfigurableApplicationContext applicationContext;
    private Parent root;

    @Override
    public void init() throws Exception {
        this.applicationContext = new SpringApplicationBuilder()
                .sources(TrelloMockApplication.class)
                .run(getParameters().getRaw().toArray(new String[0]));
        applicationContext
                .getAutowireCapableBeanFactory()
                .autowireBeanProperties(
                        this,
                        AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,
                        true
                );
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ScreensConfiguration screens = applicationContext.getBean(ScreensConfiguration.class);
        screens.setPrimaryStage(primaryStage);
        screens.loginDialog().show();
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }
}
