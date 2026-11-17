package com.example.ac;

/*
 * Final Fefo (eeeeuuuu), o personagem de dano contínuo, vai queimando o hp do adrian a cada tick
 * e uma parte de cada queimadura fica acumulada pra soltar numa explosão de tempos em tempos
 *
 * antes ele agendava um dano fixo de 1000 por segundo sem cobrar nada, agora herda de AutoClicker
 * e o danoAcumulado que ficava solto no GameState veio pra cá
 */
public class FinalFefo extends AutoClicker{

    // números de balanceamento (valores iniciais), static final = constante
    private static final double PRECO = 300;
    private static final double DANO_POR_TICK = 8;
    private static final double SEGUNDOS_ENTRE_TICKS = 1;
    private static final double FRACAO_ACUMULADA = 0.5;      // 50% de cada tick vai pro acúmulo
    private static final int TICKS_PARA_EXPLODIR = 15;
    private static final int MINIMO_DE_TICKS = 4;            // Pavio Curto não desce abaixo disso
    private static final double MULTIPLICADOR_EXPLOSAO = 1.0;

    // esses mudam durante o jogo, começam valendo as constantes de cima
    private double danoAcumulado = 0;              // quanto já juntou pra próxima explosão
    private int ticksDesdeExplosao = 0;            // quantos ticks já deu desde a última
    private int ticksParaExplodir = TICKS_PARA_EXPLODIR;
    private double multiplicadorExplosao = MULTIPLICADOR_EXPLOSAO;

    // construtor, quem dá new é a HelloController no initialize()
    // super manda tudo pro AutoClicker (nome, descrição, ícone, preço, sprite, dano e intervalo)
    public FinalFefo(){
        super("Final Fefo", "Bom velhinho", "images/finalfefoIcone.png", PRECO,
                new Renderable("images/finalfefo.png", 140, 140),
                DANO_POR_TICK, SEGUNDOS_ENTRE_TICKS);

        // cria as melhorias dele e guarda na lista do AutoClicker, "this" = o próprio fefo (o dono)

        adicionarMelhoria(new MelhoriaDano(this, "Fire - BTS", "Fefo: +50% de dano por tick",     150,   1.5,   2.0,    SEM_LIMITE));
        adicionarMelhoria(new MelhoriaVelocidade(this, "Through the Fire and Flames - DragonForce", "Fefo: queima 20% mais rápido", 400, 1.2, 2.5,   8));
        adicionarMelhoria(new MelhoriaCooldown(this, "Light My Fire - The Doors", "Fefo: explode com menos ticks", 600,  1.25,  3.0,    5));
        adicionarMelhoria(new MelhoriaEspecial(this, "Firework - Katy Perry", "Fefo: explosão +0,5x",      1000,  0.5,   2.5,    SEM_LIMITE));
    }

    // o ataque dele (um tick), quem chama é o AutoClicker.atualizar() quando passa o intervalo
    @Override
    protected void atacar() {
        // 1) a queimadura normal, causarDano é do AutoClicker e manda pro GameState
        causarDano(getDano());

        // 2) guarda metade do dano pra explosão e conta mais um tick
        danoAcumulado += getDano() * FRACAO_ACUMULADA;
        ticksDesdeExplosao++;

        // 3) chegou a hora? solta tudo que acumulou x o multiplicador e zera pra começar de novo
        if (ticksDesdeExplosao >= ticksParaExplodir) {
            causarDano(danoAcumulado * multiplicadorExplosao, "EXPLOSÃO! ");
            danoAcumulado = 0;
            ticksDesdeExplosao = 0;
            // falta sprite, aqui é o lugar pra trocar por um de explosão (setSprite) ou tocar um som
        }
    }

    // no fefo cooldown = quantos ticks até a explosão
    // sobrescreve o padrão do AutoClicker, quem chama é a MelhoriaCooldown
    // Math.round arredonda (o (int) converte pra int), Math.max não deixa descer abaixo de 4
    @Override
    public void diminuirCooldawn(double multiplicador) {
        if (multiplicador > 0) {
            int novo = (int) Math.round(ticksParaExplodir / multiplicador);
            ticksParaExplodir = Math.max(MINIMO_DE_TICKS, novo);
        }
    }

    // especial dele, soma o valor (0.5) no multiplicador da explosão, quem chama é a MelhoriaEspecial
    @Override
    public void melhorarEspecial(double valor) {
        multiplicadorExplosao += valor;
    }

    // volta tudo pro começo (Novo jogo e Carregar)
    // super.resetar() faz a parte do AutoClicker e do Upgrade, depois volta o que é só do fefo
    @Override
    public void resetar() {
        super.resetar();
        danoAcumulado = 0;
        ticksDesdeExplosao = 0;
        ticksParaExplodir = TICKS_PARA_EXPLODIR;
        multiplicadorExplosao = MULTIPLICADOR_EXPLOSAO;
    }

    // textinho embaixo do sprite dele na arena, tipo "8 a cada 1.0s / explosão em 7 ticks"
    // o \n quebra a linha, quem usa é o Renderer
    @Override
    public String getTextoStatus() {
        return Formatador.numero(getDano()) + " a cada " + Formatador.segundos(getIntervalo())
                + "\nexplosão em " + (ticksParaExplodir - ticksDesdeExplosao) + " ticks";
    }
}