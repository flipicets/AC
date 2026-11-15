package com.example.ac;

public class CondeGale extends AutoClicker{
    private int cooldown;
    private int nivelEspinhoSangrento;

    public CondeGale(int poderClique, String nome, Adrian adrian) {
        super(poderClique, nome, adrian);
        this.cooldown= 3000; //milisegundos
        this.nivelEspinhoSangrento=1;
    }

    @Override
    protected int ataque() {
        return poderClique;
    }

    @Override
    public void aumentarDano(int multiplicador) {
        this.poderClique *= multiplicador;
    }

    @Override
    public void diminuirCooldawn(int multiplicador) {
        this.cooldown -= cooldown * multiplicador;
    }

    public void uparEspinhoSangrento(){
        this.nivelEspinhoSangrento +=1;
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
