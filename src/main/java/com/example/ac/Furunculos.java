package com.example.ac;

public class Furunculos extends AutoClicker{
    private int cooldown;
    private int nivelPancada;

    public Furunculos(int poderClique, String nome, Adrian adrian) {
        super(poderClique, nome, adrian);
        this.cooldown= 25000; //milisegundos
        this.nivelPancada = 1;
    }

    @Override
    protected int ataque() {
        return this.poderClique;
    }

    @Override
    //esse metodo funciona como comprar um furunculo
    public void aumentarDano(int multiplicador) {
        this.poderClique *= multiplicador;
    }

    @Override
    public void diminuirCooldawn(int multiplicador) {
        this.cooldown -= cooldown * multiplicador;
    }


    public void uparPorrada(){
        this.nivelPancada +=1;
    }

    public void run() {
        try {
            while (true){
                adrian.levarDano(ataque());
                Thread.sleep(cooldown);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
