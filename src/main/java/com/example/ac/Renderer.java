package com.example.ac;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {

    private final Canvas canvas;
    private final GraphicsContext ctx;

    private final List<Upgrade> personagens = new ArrayList<>();

    private final AnimationTimer timer;

    public Renderer(Canvas canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();

        ctx.setImageSmoothing(false);

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };

        timer.start();
    }

    public void add(Upgrade upgrade) {
        if (upgrade.getSprite() != null && !personagens.contains(upgrade)) {
            personagens.add(upgrade);
        }
    }

    private void render() {
        ctx.clearRect(
            0, 0,
            canvas.getWidth(),
            canvas.getHeight()
        );

        int colunas = Math.max(1, (int) (canvas.getWidth() / 64));

        int i = 0;

        for (Upgrade upgrade : personagens) {
            Renderable personagem = upgrade.getSprite();

            double x = (i % colunas) * 64;
            double y = (i / colunas) * 64;

            ctx.drawImage(
                personagem.getImagem(),
                x,
                y,
                personagem.getLargura(),
                personagem.getAltura()
            );

            i++;
        }
    }

    public void parar() {
        timer.stop();
    }
}