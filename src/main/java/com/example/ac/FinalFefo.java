package com.example.ac;

import javafx.scene.control.Label;

public class FinalFefo extends AutoClicker{

    private int qntdAtaques;

    public FinalFefo(int poderClique, String nome, Label vida){
        super(poderClique, nome, vida);
        this.qntdAtaques = 1;
    }

    public void setQntdAtaques(int multiplicador){
        this.qntdAtaques = this.qntdAtaques * multiplicador;
    }

}
