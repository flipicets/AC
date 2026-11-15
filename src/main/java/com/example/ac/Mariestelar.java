package com.example.ac;
import java.util.Random;

public class Mariestelar extends AutoClicker{
    private int cooldown;
    private int nivelChuvaE;



    Random random = new Random();

    public Mariestelar(int poderClique, String nome, Adrian adrian) {
        super(poderClique, nome, adrian);
        this.cooldown= 8000; //milisegundos
        this.nivelChuvaE = 1;

    }

    @Override
    protected int ataque() {
        return random.nextInt(20,100)*nivelChuvaE;
    }

    @Override
    public void aumentarDano(int multiplicador) {
        this.poderClique *= multiplicador;
    }

    @Override
    public void diminuirCooldawn(int multiplicador) {
        this.cooldown -= cooldown * multiplicador;
    }

    public void uparChuvaE(){
        this.nivelChuvaE+=1;
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
