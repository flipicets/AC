package com.example.ac;

public class Adrian {

    private long vidaMax;
    private long vidaAtual;

    public Adrian(){
        this.vidaMax = 10_000_000_000_000L;
        this.vidaAtual = vidaMax;
    }

    public void levarDano(AutoClicker clico){
        this.vidaAtual -= clico.getPoderClique();
    }

}
