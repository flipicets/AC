package com.example.ac;

/*
 * é um Upgrade (herança, tem preço, nível e é comprada pelo mesmo comprar()) e guarda quem é
 * o personagem dono no "alvo" (associação)
 *
 * é abstract pq o que é igual pras 4 melhorias já tá aqui (guardar alvo e valor, subir o preço)
 * e a única coisa que muda é QUAL atributo do alvo é mexido, isso cada filha escreve no aplicar()
 * as filhas são MelhoriaDano, MelhoriaVelocidade, MelhoriaCooldown e MelhoriaEspecial
 *
 * são 4 filhas pequenas em vez de um if/switch gigante aqui pq cada uma tem 1 linha de efeito
 * e se inventar um tipo novo de melhoria é só criar mais uma filha sem mexer nas outras
 */
public abstract class MelhoriaAutoClicker extends Upgrade {

    // os 3 são final pq são definidos no construtor e nunca mais mudam
    private final AutoClicker alvo;           // de quem é a melhoria
    private final double valor;               // o número passado pro alvo (multiplicador, bônus...)
    private final double crescimentoDoPreco;  // por quanto o preço multiplica a cada compra

    // construtor, quem chama são as 4 filhas com super(...)
    // e quem dá new nas filhas são os personagens no construtor deles (adicionarMelhoria)
    public MelhoriaAutoClicker(
            AutoClicker alvo,
            String nome,
            String descricao,
            double preco,
            double valor,
            double crescimentoDoPreco,
            int nivelMaximo
    ) {
        // super manda pro Upgrade, o ícone é o mesmo do personagem dono (alvo.getImagem())
        // e o null é o sprite, melhoria não tem desenho na arena
        // se quiser ícone próprio pra cada melhoria é só trocar o alvo.getImagem() por um caminho novo
        super(nome, descricao, alvo.getImagem(), preco, null, nivelMaximo);
        this.alvo = alvo;
        this.valor = valor;
        this.crescimentoDoPreco = crescimentoDoPreco;
    }

    // qual atributo do alvo essa melhoria mexe, cada filha escreve o seu
    // quem chama é o executar() aqui embaixo, protected = só essa classe e as filhas enxergam
    protected abstract void aplicar();

    // pedaço do id usado no banco ("dano", "vel", "cd", "esp"), cada filha devolve o seu
    // a HelloController junta com o id do dono e fica tipo "gale_dano"
    // NAO MUDAR depois que já existir save, senão o save antigo não acha mais a melhoria
    public abstract String getTipo();

    // o efeito da compra, igual pras 4, quem chama é o Upgrade.comprar() depois de cobrar
    // aplica no alvo (polimorfismo, roda o aplicar() da filha que for) e encarece a próxima compra
    @Override
    public void executar() {
        aplicar();
        setPreco(getPreco() * crescimentoDoPreco);
    }

    // devolve o personagem dono, as filhas usam no aplicar() pra chamar o método dele
    protected AutoClicker getAlvo() {
        return alvo;
    }

    // devolve o número da melhoria (ex 1.5), as filhas passam ele pro alvo no aplicar()
    protected double getValor() {
        return valor;
    }
}