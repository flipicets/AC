package com.example.ac;

public class FinalFefo extends AutoClicker{
    private int cooldown;
    private int nivelLaserSabedoria;
    private int qntdAtaques;

    public FinalFefo(int poderClique, String nome, Adrian adrian){
        super(poderClique, nome, adrian);
        this.qntdAtaques = 1;
        this.cooldown = 200; //milisegundos pra dar tempo de trocar de thread
        this.nivelLaserSabedoria=1;
    }

    public void setQntdAtaques(int multiplicador){
        this.qntdAtaques = this.qntdAtaques * multiplicador;
    }

    @Override
    public void aumentarDano(int multiplicador) {
        this.poderClique *= multiplicador;
    }

    @Override
    public void diminuirCooldawn(int multiplicador) {
    }

    public int ataque(){
        return poderClique *=nivelLaserSabedoria;
    }

    public void run() {
        try {
            while (true){
                ataque();
                Thread.sleep(cooldown);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
