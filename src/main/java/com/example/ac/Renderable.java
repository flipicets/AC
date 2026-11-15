package com.example.ac;

import java.net.URL;
import java.util.Objects;

import javafx.scene.image.Image;

public class Renderable {

    private final Image imagem;
    private final double largura;
    private final double altura;

    public Renderable(String caminho, double largura, double altura) {
        URL url = Objects.requireNonNull(
            getClass().getResource(caminho),
            "Sprite não encontrado: " + caminho
        );

        this.imagem = new Image(url.toExternalForm());
        this.largura = largura;
        this.altura = altura;
    }

    public Image getImagem() {
        return imagem;
    }

    public double getLargura() {
        return largura;
    }

    public double getAltura() {
        return altura;
    }
}