package com.example.ac;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class HelloController {

	@FXML Button invocarThread;
	@FXML Label vida;
	
	@FXML
	private ListView<Upgrade> listaUpgrades;
    
	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	@FXML
    public void initialize(){
		Adrian adrian = Adrian.getInstance();
		vida.setText(String.valueOf(adrian.getVidaAtual()));
		
		ArrayList<String> array = new ArrayList<>(); 
		
		 listaUpgrades.setCellFactory(
			        list -> new UpgradeCell()
			    );

			    listaUpgrades.getItems().addAll(
			        new Upgrade(
			            "Espada",
			            "+5 de dano",
			            "D:\\Downloads\\Homem Contemplando o Céu Estrelado.png"
			        ),
			        new Upgrade(
			            "Fogo",
			            "+10 de dano",
			            "D:\\Downloads\\WhatsApp Image 2026-11-13 at 19.58.21.jpeg"
			        ),
			        new Upgrade(
			            "Escudo",
			            "+20 de defesa",
			            "D:\\Downloads\\Selfie do fisiculturista no quarto.png"
			        )
			    );
    }
	
	
	@FXML
	public void invocarThread() {
		Runnable a = new AutoClicker(300, "enterogermina", vida);
		scheduler.scheduleAtFixedRate(a, 0,3, TimeUnit.SECONDS);
		
	}
	
	@FXML
	public void encerrar() {
		scheduler.shutdownNow();
	}
	
	
	



}
