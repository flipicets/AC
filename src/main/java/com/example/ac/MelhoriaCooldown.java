package com.example.ac;

/*
 * melhoria que diminui o cooldown do personagem dono
 */
public class MelhoriaCooldown extends MelhoriaAutoClicker {

    // construtor, só repassa tudo pra mãe (MelhoriaAutoClicker) com super
    // quem dá new são os personagens no construtor deles
    public MelhoriaCooldown(AutoClicker alvo, String nome, String descricao,
                            double preco, double valor, double crescimentoDoPreco, int nivelMaximo) {
        super(alvo, nome, descricao, preco, valor, crescimentoDoPreco, nivelMaximo);
    }

    // o efeito, quem chama é o executar() da MelhoriaAutoClicker
    // guarda o alvo numa variável do tipo da INTERFACE (Atributo) pq aqui só precisa do contrato,
    // não importa qual personagem é, e chama o diminuirCooldawn dele passando o valor (ex 1.25)
    @Override
    protected void aplicar() {
        Atributo atributos = getAlvo();
        atributos.diminuirCooldawn(getValor());
    }

    // pedaço do id no banco, fica tipo "fe_cd", a HelloController usa ao registrar
    @Override
    public String getTipo() {
        return "cd";
    }
}