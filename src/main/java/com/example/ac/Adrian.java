package com.example.ac;

public class Adrian {

    private long vidaMax;
    private long vidaAtual;
    private boolean vivo;

    public Adrian(){
        this.vidaMax = 10_000_000_000_000L;
        this.vidaAtual = vidaMax;
        this.vivo= true;
    }

    public boolean taVivo(){
        return vivo;
    }
    public void levarDano(int dano){
        this.vidaAtual -= dano;
    }

    public long getVidaAtual(){
        return this.vidaAtual;
    }
}
