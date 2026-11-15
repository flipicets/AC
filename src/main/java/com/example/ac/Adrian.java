package com.example.ac;

public class Adrian {
    private String nome;
    private long vidaMax;
    private long vidaAtual;

    public Adrian(){
        this.nome = "Adrian";
        this.vidaMax = 10_000_000_000_000L;
        this.vidaAtual = vidaMax;
    }

    public void levarDano(AutoClicker clico){

        this.vidaAtual -= clico.getPoderClique();

        if (this.vidaAtual < 0) {
            vidaAtual = 0;
        }}
    public boolean estaMorto() {
        return vidaAtual <= 0;
    }
    public String getNome() {
        return nome;
    }

    public long getVidaAtual() {
        return vidaAtual;
    }

    public long getVidaMax() {
        return vidaMax;
    }
}


