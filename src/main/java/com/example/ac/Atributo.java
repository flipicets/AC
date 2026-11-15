package com.example.ac;

public interface Atributo {
    //Gente explicando aqui minha ídeia
    //esses métodos abstratos subescrevam em cada uma das classes de personagem
    //a ideia é poder upar eles várias vezes, por isso recebe um multiplicador;
    //mas são só os passivos ner, os ativos vai de cada um lá ele
    //todos esses são um set nos atributos base do personagem tá bom

      abstract void aumentarDano(int multiplicador);
      abstract void diminuirCooldawn(int multiplicador);


}
