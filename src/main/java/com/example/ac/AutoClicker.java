package com.example.ac;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class AutoClicker implements Runnable{

    private int poderClique;
    private String nome;
    private Label vida;
    
    public AutoClicker(int poderClique, String nome, Label vida) {
        this.poderClique = poderClique;
        this.nome = nome;
        this.vida = vida;
    }

    public int getPoderClique(){
        return this.poderClique;
    }
    
    public void attack() {
    	Adrian adrian = Adrian.getInstance();
    	adrian.levarDano(this);
    }
    
    @Override
    public void run() {
    	System.out.println("ativei");
    	Adrian adrian = Adrian.getInstance();
    	this.attack();
    	Platform.runLater(() -> {
    		this.vida.setText(String.valueOf(adrian.getVidaAtual()));
    	});
    }


}

