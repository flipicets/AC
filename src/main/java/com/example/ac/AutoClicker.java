package com.example.ac;

public abstract class AutoClicker extends Thread implements Atributo{

    protected int poderClique;
    protected String nome;
    protected Adrian adrian;

    public AutoClicker(int poderClique, String nome, Adrian adrian) {
        this.poderClique = poderClique;
        this.nome = nome;
        this.adrian= adrian;
    }

    protected abstract int ataque();
}

