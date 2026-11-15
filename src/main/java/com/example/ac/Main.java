package com.example.ac;
import java.util.Scanner;

public class Main {

    public int bufunfa;

    Ajuda ajuda = new Ajuda();
    static Adrian adrian= new Adrian();
    static Mariestelar mariestelar = new Mariestelar(10,"Mariestelar", adrian);

    static Scanner scanner = new Scanner(System.in);

    static void main(String[] args) {

        mariestelar.start();

        while (adrian.taVivo()){

            System.out.println(adrian.getVidaAtual());

           // System.exit(0);
        }
    }
}
