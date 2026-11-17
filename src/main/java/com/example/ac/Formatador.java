package com.example.ac;

/*
 * classe utilitária só pra escrever número grande de um jeito que dá pra ler
 * pq com 10 trilhões de vida não dá pra mostrar "10000000000000.00" na tela
 * ex 950 fica "950", 1500 fica "1,50 K", 2300000 fica "2,30 M"
 *
 * é usada em quase tudo que mostra número (bufunfa, preço da loja, vida, dano subindo, status dos personagens)
 *
 * os métodos são static pq ela não guarda nada, é só uma calculadora que nem o Math
 * então chama direto Formatador.numero(x) sem dar new
 */
public final class Formatador {

    // vetor (array) com os sufixos, K = mil, M = milhão, B = bilhão, T = trilhão, Qa = quatrilhão
    // a posição 0 é vazia pq número menor que mil não tem sufixo
    private static final String[] SUFIXOS = { "", " K", " M", " B", " T", " Qa" };

    // construtor private e vazio, é só pra ninguém conseguir dar new Formatador()
    private Formatador() {
    }

    // transforma um número em texto curto com sufixo
    // quem usa é HelloController (bufunfa e dano do clique), UpgradeCellController (preço),
    // AutoClicker (número de dano que sobe na tela) e os getTextoStatus dos personagens
    public static String numero(double valor) {
        // se for negativo resolve como positivo e põe o "-" na frente
        // o método chama ele mesmo aqui, isso é recursão
        if (valor < 0) {
            return "-" + numero(-valor);
        }

        // vai dividindo por 1000 e andando uma casa no vetor de sufixos
        // ex 2300000 -> 2300 (K) -> 2.3 (M), para quando fica menor que 1000
        // o indice < SUFIXOS.length - 1 é pra não passar do último sufixo
        int indice = 0;
        while (valor >= 1000 && indice < SUFIXOS.length - 1) {
            valor = valor / 1000;
            indice++;
        }

        // indice 0 = número pequeno, não dividiu nenhuma vez
        if (indice == 0) {
            // se for praticamente inteiro mostra sem casa decimal
            // o "praticamente" é pq conta com double às vezes dá 440.00000000000006 em vez de 440
            // Math.rint = arredonda pro inteiro mais perto, Math.abs = tira o sinal (distância)
            // então a conta é "o número tá a menos de 0.05 de um inteiro?"
            if (Math.abs(valor - Math.rint(valor)) < 0.05) {
                // String.format monta texto, %.0f = número sem casa decimal
                return String.format("%.0f", valor);
            }
            // %.1f = uma casa decimal
            return String.format("%.1f", valor);
        }
        // número grande, %.2f = duas casas + o sufixo da posição que parou
        return String.format("%.2f", valor) + SUFIXOS[indice];
    }

    // número inteiro completo com separador de milhar, tipo 10.000.000.000.000
    // %,d = inteiro com separador, a HelloController usa no texto da vida do adrian
    public static String inteiroCompleto(long valor) {
        return String.format("%,d", valor);
    }

    // tempo em segundos com 1 casa, tipo "2,5s"
    // os personagens usam no getTextoStatus ("a cada 3,0s")
    public static String segundos(double valor) {
        return String.format("%.1fs", valor);
    }
}