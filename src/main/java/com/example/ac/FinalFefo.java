package com.example.ac;

public class FinalFefo extends AutoClicker{

    private int qntdAtaques;

    public FinalFefo(int poderClique, String nome){
        super(poderClique, nome);
        this.qntdAtaques = 1;
    }

    public void setQntdAtaques(int multiplicador){
        this.qntdAtaques = this.qntdAtaques * multiplicador;
    }

}
