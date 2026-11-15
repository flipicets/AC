package com.example.ac;

import java.util.Scanner;
public class Ajuda {

    Scanner scanner= new Scanner(System.in);

    public int pegarEscolha (int menor, int maior){
        while (true ){
            int escolha = scanner. nextInt();
            if (escolha >= menor && escolha <=maior){
                return escolha;
            }else{
                System.out.println("Valores inválidos");
            }
        }
    }
}
