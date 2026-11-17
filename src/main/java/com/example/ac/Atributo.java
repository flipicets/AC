package com.example.ac;

/*
 * é uma interface, ou seja um contrato que nem a profe disse, quem assina (implements) é obrigado a ter
 * esses 3 métodos. aqui não tem código nenhum, só o nome deles.
 *
 * quem assina é o AutoClicker, então os 4 personagens (fefo, furúnculo, gale, mari) tem os 3.
 * quem chama são as melhorias da loja (MelhoriaDano, MelhoriaVelocidade, MelhoriaCooldown),
 * aparece no jogo quando compra algo na aba "Melhorias".
 *
 * antes era abstract class, virou interface pq os personagens já herdam de Upgrade
 * e em java só dá pra herdar de UMA classe, mas dá pra implementar interface junto:
 * AutoClicker extends Upgrade implements Atributo.
 *
 * o mesmo diminuirCooldawn() faz coisa diferente dependendo do personagem
 * (fefo explode mais cedo, gale crita garantido mais cedo, furúnculo/mari recarregam mais rápido).
 */
public interface Atributo {
     //Gente explicando aqui minha ídeia (maristela falando)
     //esses métodos abstratos subescrevam em cada uma das classes de personagem
     //a ideia é poder upar eles várias vezes, por isso recebe um multiplicador;
     //mas são só os passivos ner, os ativos vai de cada um lá ele
     //todos esses são um set nos atributos base do personagem tá bom

     // método de interface não tem corpo (sem { }) e já é public sozinho, nem precisa escrever.
     // o multiplicador é double pra aceitar quebrado, tipo 1.5 = +50%.

     // multiplica o dano do personagem. igual pros 4, o código fica no AutoClicker,
     // quem chama é a MelhoriaDano.
     void aumentarDano(double multiplicador);

     // faz atacar mais vezes (diminui o tempo entre ataques). tb fica no AutoClicker,
     // quem chama é a MelhoriaVelocidade.
     void aumentarVelAtaque(double multiplicador);

     // diminui o cooldown. o padrão fica no AutoClicker (furúnculo e mari usam esse),
     // fefo e gale sobrescrevem com o deles. quem chama é a MelhoriaCooldown.
     void diminuirCooldawn(double multiplicador);

     // "Cooldawn" tá escrito errado (é cooldown dawn é de amanhecer kkkkkkkk) mas DEIXEM ASSIM NAO MUDEM PQ VAI QUEBRAR TUDO NAO MUDA PELO AMOR DE DEUS

}