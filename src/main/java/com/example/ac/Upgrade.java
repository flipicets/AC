package com.example.ac;

import javafx.beans.property.*;

public abstract class Upgrade {

    protected String nome;
    protected String descricao;
    protected String imagem;
    protected Renderable sprite;

    protected DoubleProperty preco;

    private final BooleanProperty comprado =
        new SimpleBooleanProperty(false);

    public Upgrade(
        String nome,
        String descricao,
        String imagem,
        double preco,
        Renderable sprite
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
        this.preco = new SimpleDoubleProperty(preco);
        this.sprite = sprite;
    }

    public abstract void executar();

    protected void registrarCompra() {
        comprado.set(true);
    }

    public ReadOnlyBooleanProperty compradoProperty() {
        return comprado;
    }

    public boolean isComprado() {
        return comprado.get();
    }

    public double getPreco() {
        return preco.get();
    }

    public void setPreco(double valor) {
        preco.set(valor);
    }

    public DoubleProperty precoProperty() {
        return preco;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public Renderable getSprite() {
        return sprite;
    }
}