package com.example.ac;

/*
 * exceção nossa, é lançada quando tentam comprar algo sem ter bufunfa suficiente
 * no jogo é a mensagem amarela lá embaixo "Não há money: faltam X de bufunfa"
 *
 * é extends Exception (checada) de propósito, assim o java OBRIGA quem chama comprar()
 * a tratar ou repassar, não tem como nao
 */
public class BufunfaInsuficienteException extends Exception {

    // guarda quanto custava e quanto o jogador tinha na hora, final pq não muda depois de criada
    private final double preco;
    private final double saldo;

    // construtor, quem dá new nela é o GameState.gastarBufunfa() passando o preço e o saldo
    // super manda a mensagem pronta pra classe mãe (Exception), é esse texto que volta no e.getMessage()
    // Formatador.numero deixa o número bonito (1500 vira 1,50 K)
    public BufunfaInsuficienteException(double preco, double saldo) {
        super("Não há money: faltam " + Formatador.numero(preco - saldo) + " de bufunfa");
        this.preco = preco;
        this.saldo = saldo;
    }

    // os 3 de baixo ninguém usa hoje pq o catch só pega o getMessage()
    // ficam aqui caso a gente queira mostrar o erro de outro jeito na tela

    // retorna o preço do que tentaram comprar
    public double getPreco() {
        return preco;
    }

    // retorna quanto de bufunfa o jogador tinha
    public double getSaldo() {
        return saldo;
    }

    // retorna quanto faltou
    public double getFaltam() {
        return preco - saldo;
    }
}