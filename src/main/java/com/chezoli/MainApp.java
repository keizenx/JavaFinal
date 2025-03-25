package com.chezoli;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;
    private ContactApp contactApp;
    private AboutApp aboutApp;
    private MenuApp menuApp;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.contactApp = new ContactApp(this);
        this.aboutApp = new AboutApp(this);
        this.menuApp = new MenuApp(this);
        
        primaryStage.setTitle("CHEZ OLI");
        showMenu(); // Start with the menu page
        primaryStage.show();
    }

    public void showContact() {
        Scene contactScene = contactApp.createScene();
        primaryStage.setScene(contactScene);
    }

    public void showAbout() {
        Scene aboutScene = aboutApp.createScene();
        primaryStage.setScene(aboutScene);
    }

    public void showMenu() {
        Scene menuScene = menuApp.createScene();
        primaryStage.setScene(menuScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
} 