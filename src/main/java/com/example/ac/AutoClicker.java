package com.example.ac;

public abstract class AutoClicker{

    private int poderClique;
    private String nome;

    public AutoClicker(int poderClique, String nome) {
        this.poderClique = poderClique;
        this.nome = nome;
    }

    public int getPoderClique(){
        return this.poderClique;
    }


}

