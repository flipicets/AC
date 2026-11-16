package com.example.ac;

/*
 * melhoria que faz o personagem dono atacar mais vezes
 */
public class MelhoriaVelocidade extends MelhoriaAutoClicker {

    // construtor, só repassa tudo pra mãe (MelhoriaAutoClicker) com super
    // quem dá new são os personagens no construtor deles (só o fefo e o gale tem essa)
    public MelhoriaVelocidade(AutoClicker alvo, String nome, String descricao,
                              double preco, double valor, double crescimentoDoPreco, int nivelMaximo) {
        super(alvo, nome, descricao, preco, valor, crescimentoDoPreco, nivelMaximo);
    }

    // o efeito, quem chama é o executar() da MelhoriaAutoClicker
    // guarda o alvo numa variável do tipo da INTERFACE (Atributo) pq aqui só precisa do contrato,
    // e chama o aumentarVelAtaque dele passando o valor, que é o multiplicador (1.2 = 20% mais rápido)
    @Override
    protected void aplicar() {
        Atributo atributos = getAlvo();
        atributos.aumentarVelAtaque(getValor());
    }

    // pedaço do id no banco, fica tipo "fe_vel", a HelloController usa ao registrar
    @Override
    public String getTipo() {
        return "vel";
    }
}