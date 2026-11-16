package com.example.ac;

/*
 * exceção nossa, é lançada quando tentam comprar um upgrade que já tá no nível máximo
 * o caso principal é a regra de só poder ter 1 auto clicker de cada, tentar comprar o segundo fefo cai aqui
 *
 * quem lança é o Upgrade.comprar() e quem trata é o UpgradeCellController (try/catch)
 *
 * na tela o botão já fica desabilitado no máximo, mas a regra fica garantida na classe tb
 * caso alguém chame o comprar() por outro caminho
 */
public class NivelMaximoException extends Exception {

    // construtor, recebe o nome do upgrade e monta a mensagem (ex "Final Fefo já está no nível máximo")
    // super manda o texto pra classe mãe (Exception), é ele que volta no e.getMessage()
    public NivelMaximoException(String nomeDoUpgrade) {
        super(nomeDoUpgrade + " já está no nível máximo");
    }
}