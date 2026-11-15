package com.example.ac;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.ModuleLayer.Controller;

/*
 * Implementação padrão do arquétipo do openjfx com a adição do método stop
 * ele chama o encerrar da controller para matar o agendador e não ficar spawnando
 * thread a toa
 * */

public class HelloApplication extends Application {
	
	private HelloController controller;
	
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        this.controller = fxmlLoader.getController();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    
    //Bem fofinho o método stop
    @Override
    public void stop() {
    	controller.encerrar();
    }
}
