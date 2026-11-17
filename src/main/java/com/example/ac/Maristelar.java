package com.example.ac;

// Random é o "dado" do java, a gente usa pra sortear o dano de cada ataque
import java.util.Random;

/*
 * Mariestelar, dano imenso mas aleatório, ataca devagar (a cada 6s) e cada ataque sorteia
 * um dano entre o mínimo e o máximo, então pode vir um golpe fraquinho ou um absurdo
 * mínimo = dano base, máximo = dano base x amplitude
 *
 * antes era um dano fixo de 10 milhões por segundo custando 300, matava o jogo
 *
 * a classe chama "Maristelar" mas na tela e nos sprites é "Mariestelar"
 */
public class Maristelar extends AutoClicker{

    // números de balanceamento (valores iniciais), static final = constante
    private static final double PRECO = 25_000;
    private static final double DANO_MINIMO = 300;
    private static final double SEGUNDOS_ENTRE_ATAQUES = 6;
    private static final double AMPLITUDE = 20;      // máximo = mínimo x 20

    // muda durante o jogo com a melhoria especial, começa valendo a constante de cima
    private double amplitude = AMPLITUDE;

    // o dado, nextDouble() sorteia um número de 0.0 a 1.0
    private final Random random = new Random();

    // construtor, quem dá new é a HelloController no initialize()
    // super manda tudo pro AutoClicker (nome, descrição, ícone, preço, sprite, dano e intervalo)
    public Maristelar(){
        super("Mariestelar", "Slk", "images/mariestelarIcone.png", PRECO,
                new Renderable("images/mariestelar.png", 140, 140),
                DANO_MINIMO, SEGUNDOS_ENTRE_ATAQUES);

        // cria as melhorias dela e guarda na lista do AutoClicker, "this" = a própria mari (a dona)
        // a ordem dos números é preço, valor, quanto o preço multiplica por compra, nível máximo
        adicionarMelhoria(new MelhoriaDano(this, "Xuxuzinho", "Mari: +50% no dano mínimo e máximo",     12_000, 1.5,  2.0, SEM_LIMITE));
        adicionarMelhoria(new MelhoriaCooldown(this, "Planeta de lesbos", "Mari: ataca 15% mais rápido",     30_000, 1.15, 2.5, 10));
        adicionarMelhoria(new MelhoriaEspecial(this, "Trocar de curso para bsi", "Mari: dano máximo +50% (mais sorte)", 20_000, 1.5,  2.5, SEM_LIMITE));
    }

    // menor dano possível, é o próprio dano base do AutoClicker
    // usado no atacar() e no getTextoStatus() daqui
    public double getDanoMinimo() {
        return getDano();
    }

    // maior dano possível = dano base x amplitude
    // por isso a melhoria de dano sobe os dois juntos, e a especial sobe só o máximo
    public double getDanoMaximo() {
        return getDano() * amplitude;
    }

    // o ataque dela, quem chama é o AutoClicker.atualizar() quando passa o intervalo
    @Override
    protected void atacar() {
        // sorteia de 0.0 a 1.0, é "em que ponto do range caiu" (0 = mínimo, 1 = máximo, 0.5 = bem no meio)
        double sorte = random.nextDouble();
        // começa no mínimo e soma um pedaço da distância até o máximo
        double dano = getDanoMinimo() + sorte * (getDanoMaximo() - getDanoMinimo());
        // causarDano é do AutoClicker e manda pro GameState
        causarDano(dano);
    }

    // especial dela, "valor" multiplica a amplitude (1.5 = máximo 50% maior), quem chama é a MelhoriaEspecial
    // o if só aceita valor maior que 1 pq menor que isso ia diminuir o máximo
    @Override
    public void melhorarEspecial(double valor) {
        if (valor > 1) {
            amplitude = amplitude * valor;
        }
    }

    // volta tudo pro começo (Novo jogo e Carregar)
    // super.resetar() faz a parte do AutoClicker e do Upgrade, depois volta a amplitude
    @Override
    public void resetar() {
        super.resetar();
        amplitude = AMPLITUDE;
    }

    // textinho embaixo do sprite dela na arena, tipo "300 ~ 6,00 K / a cada 6,0s"
    // o \n quebra a linha, quem usa é o Renderer
    @Override
    public String getTextoStatus() {
        return Formatador.numero(getDanoMinimo()) + " ~ " + Formatador.numero(getDanoMaximo())
                + "\na cada " + Formatador.segundos(getIntervalo());
    }
}