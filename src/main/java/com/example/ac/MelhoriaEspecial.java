package com.example.ac;

/*
 * melhoria da mecânica especial do personagem dono, aquela que só ele tem
 * (explosão do fefo, +1 furúnculo, crítico do gale, dano máximo da mari)
 */
public class MelhoriaEspecial extends MelhoriaAutoClicker {

    // construtor, só repassa tudo pra mãe (MelhoriaAutoClicker) com super
    // quem dá new são os personagens no construtor deles
    public MelhoriaEspecial(AutoClicker alvo, String nome, String descricao,
                            double preco, double valor, double crescimentoDoPreco, int nivelMaximo) {
        super(alvo, nome, descricao, preco, valor, crescimentoDoPreco, nivelMaximo);
    }

    // o efeito, quem chama é o executar() da MelhoriaAutoClicker
    // diferente das outras 3 essa NAO usa a interface Atributo, chama direto o melhorarEspecial()
    // que é método abstrato do AutoClicker, então cada personagem faz o dele (polimorfismo)
    @Override
    protected void aplicar() {
        getAlvo().melhorarEspecial(getValor());
    }

    // pedaço do id no banco, fica tipo "fe_esp", a HelloController usa ao registrar
    @Override
    public String getTipo() {
        return "esp";
    }
}