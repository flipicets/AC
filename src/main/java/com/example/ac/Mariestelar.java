package com.example.ac;

public class Mariestelar extends AutoClicker{
    private double velAtaque;
    private double dano;
    private double cooldown;

    public Mariestelar(int poderClique, String nome) {
        super(poderClique, nome);
    }

    @Override
    protected void aumentarDano(double multiplicador) {
        this.dano *= multiplicador;
    }

    @Override
    protected void aumentarVelAtaque(double multiplicador) {
        this.velAtaque *= multiplicador;
    }

    @Override
    protected void diminuirCooldawn(double multiplicador) {
        this.cooldown *= multiplicador;
    }
}
