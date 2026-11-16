package com.example.ac;

/*
 * melhoria que aumenta o dano do personagem dono
 */
public class MelhoriaDano extends MelhoriaAutoClicker {

    // construtor, só repassa tudo pra mãe (MelhoriaAutoClicker) com super
    // quem dá new são os personagens no construtor deles
    public MelhoriaDano(AutoClicker alvo, String nome, String descricao,
                        double preco, double valor, double crescimentoDoPreco, int nivelMaximo) {
        super(alvo, nome, descricao, preco, valor, crescimentoDoPreco, nivelMaximo);
    }

    // o efeito, quem chama é o executar() da MelhoriaAutoClicker
    // guarda o alvo numa variável do tipo da INTERFACE (Atributo) pq aqui só precisa do contrato,
    // e chama o aumentarDano dele passando o valor, que é o multiplicador (1.5 = +50% de dano)
    @Override
    protected void aplicar() {
        Atributo atributos = getAlvo();
        atributos.aumentarDano(getValor());
    }

    // pedaço do id no banco, fica tipo "fe_dano", a HelloController usa ao registrar
    @Override
    public String getTipo() {
        return "dano";
    }
}