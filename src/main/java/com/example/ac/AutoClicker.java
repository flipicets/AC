package com.example.ac;

// ArrayList = lista que cresce sozinha, a gente usa pra guardar as melhorias de cada personagem
import java.util.ArrayList;

/* Ó Ó EU VO EXPLICAR TUDO dessa buçanga aqui ela é a
 * classe mãe (feminismo) dos 4 personagens que atacam sozinhos (fefo, furúnculo, gale, mari).
 *
 * um AutoClicker É UM Upgrade (tem preço, ícone, aparece na loja) só que além disso ataca.
 * e cada personagem É UM AutoClicker com o jeito dele de bater.
 *
 * é abstract pq não existe "um auto clicker genérico" no jogo, só os 4. tudo que é igual
 * pros 4 já tá pronto aqui (contar tempo, dano, intervalo, lista de melhorias), o que é
 * diferente ficou abstract e cada um escreve o seu: atacar() e melhorarEspecial().
 *
 * implements Atributo = assina o contrato dos 3 métodos que as melhorias chamam.
 *
 * regra de só poder ter 1 de cada: o nível máximo que a gente passa pro Upgrade é 1,
 * tentou comprar o segundo o Upgrade.comprar() lança NivelMaximoException.
 *
 * como funciona o tempo: a HelloController tem um loop que roda 10x por segundo e chama
 * atualizar(0.1) em cada personagem. cada um vai somando esse tempo e quando passa do
 * intervalo dele, ataca. pra atacar mais rápido é só diminuir o intervalo.
 */
public abstract class AutoClicker extends Upgrade implements Atributo {

    // ninguém ataca mais rápido que 0.2s, se não vira bagunça. static final = constante.
    private static final double INTERVALO_MINIMO = 0.2;

    // valores de fábrica, guardados só pra conseguir voltar no resetar(). final = nunca mudam.
    private final double danoInicial;
    private final double intervaloInicial;

    private double dano;            // dano base de cada ataque
    private double intervalo;       // segundos entre um ataque e outro
    private double tempoAcumulado;  // quanto tempo já passou desde o último ataque

    // texto do último ataque ("-150"), fica aqui esperando o Renderer pegar pra mostrar na tela
    private String textoDeAtaque;

    // coleção + associação: cada personagem TEM várias melhorias (as dele) e cada melhoria
    // sabe quem é o dono. os personagens enchem essa lista no construtor deles.
    private final ArrayList<MelhoriaAutoClicker> melhorias = new ArrayList<>();

    // construtor, quem chama são os 4 personagens com super(...).
    // os 5 primeiros vão pro Upgrade, dano e intervalo ficam aqui.
    public AutoClicker(
            String nome,
            String descricao,
            String imagem,
            double preco,
            Renderable sprite,
            double dano,
            double intervalo
    ) {
        // super = chama o construtor da mãe (Upgrade). esse 1 no final é o nível máximo, ou seja só pode ter um
        super(nome, descricao, imagem, preco, sprite, 1);
        this.danoInicial = dano;
        this.intervaloInicial = intervalo;
        this.dano = dano;
        this.intervalo = intervalo;
        this.tempoAcumulado = 0;
    }

    // AQUI É O QUE OS PERSONAGEM ESCREVE

    // um ataque do personagem, cada um dos 4 escreve o seu. quem chama é o atualizar() aqui embaixo.
    // protected = só essa classe e as filhas enxergam.
    protected abstract void atacar();

    // efeito da melhoria especial de cada um (explosão do fefo, crítico do gale etc).
    // quem chama é a MelhoriaEspecial.
    public abstract void melhorarEspecial(double valor);

    //COMPRA

    // o que acontece quando compra o personagem: só zera o relógio dele.
    // quem chama é o Upgrade.comprar(). quem liga o ataque de vdd é o isComprado() no atualizar().
    @Override
    public void executar() {
        tempoAcumulado = 0;
    }

    //  AQUI O LOOP BOSTA

    // chamado pelo tick() da HelloController 10 veis por segundo.
    // dt = "delta time", quanto tempo passou desde a última chamada, em segundos (aqui sempre 0.1).
    public void atualizar(double dt) {
        // não comprou = não faz nada. o "!" inverte (não comprado)
        if (!isComprado()) {
            return;
        }

        // vai somando o tempo que passou
        tempoAcumulado += dt;

        // passou do intervalo? desconta e ataca. é while e não if pq se o intervalo
        // for bem pequeno pode caber mais de um ataque no mesmo passo.
        while (tempoAcumulado >= intervalo) {
            tempoAcumulado -= intervalo;
            atacar();   // GENTE AQUI É POLIMORFISMO roda o atacar() do personagem que for (fefo, gale...)
        }
    }

    // os personagens usam isso dentro do atacar() pra bater no adrian.
    // só chama o de baixo sem texto na frente (dois métodos com mesmo nome e parâmetros diferentes = sobrecarga).
    protected void causarDano(double valor) {
        causarDano(valor, "");
    }

    // mesma coisa mas com um texto na frente do número ("CRÍTICO! ", "EXPLOSÃO! ").
    // manda o dano pro GameState.causarDano() (que tira a vida do adrian e dá a bufunfa)
    // e guarda o texto pro Renderer mostrar subindo na tela.
    protected void causarDano(double valor, String prefixo) {
        GameState.getInstance().causarDano(valor);
        textoDeAtaque = prefixo + "-" + Formatador.numero(valor);
    }

    //COISAS DE ATRIBUTO (a interface da maristela(eu te amo maristela asssinado fellipe))

    // igual pros 4: multiplica o dano base. quem chama é a MelhoriaDano.
    // o if é só pra ignorar multiplicador zerado ou negativo.
    @Override
    public void aumentarDano(double multiplicador) {
        if (multiplicador > 0) {
            dano = dano * multiplicador;
        }
    }

    // igual pros 4: atacar mais vezes = intervalo menor, por isso DIVIDE.
    // quem chama é a MelhoriaVelocidade.
    @Override
    public void aumentarVelAtaque(double multiplicador) {
        if (multiplicador > 0) {
            setIntervalo(intervalo / multiplicador);
        }
    }

    // cooldown padrão: no furúnculo e na mari o cooldown é o próprio tempo de recarga do golpe,
    // então diminui o intervalo. fefo e gale SOBRESCREVEM esse pq neles o cooldown é outra coisa
    // (explosão e crítico garantido). quem chama é a MelhoriaCooldown.
    @Override
    public void diminuirCooldawn(double multiplicador) {
        if (multiplicador > 0) {
            setIntervalo(intervalo / multiplicador);
        }
    }

    // NEGOCIO DE RESET

    // volta o personagem pro zero (Novo jogo e Carregar), quem chama é o SaveController.resetarJogo().
    // super.resetar() faz a parte do Upgrade (nível, preço, sprite) e depois a gente volta o que é daqui.
    @Override
    public void resetar() {
        super.resetar();
        dano = danoInicial;
        intervalo = intervaloInicial;
        tempoAcumulado = 0;
        textoDeAtaque = null;
    }

    //O QUE O RENDERER DESENHA ISSO E IMPORTANTE se bem que a prof nem vai perguntar de javafx aff

    // barrinha amarela de carga: 0 = acabou de atacar, 1 = vai atacar agora.
    // Math.min segura em 1.0 pra barra nunca passar do tamanho. quem usa é o Renderer.
    @Override
    public double getProgresso() {
        return Math.min(1.0, tempoAcumulado / intervalo);
    }

    // entrega o texto do ataque UMA vez e limpa (null), senão o Renderer ia repetir o número todo frame.
    @Override
    public String pegarTextoDeAtaque() {
        String texto = textoDeAtaque;
        textoDeAtaque = null;
        return texto;
    }

    //AS MELHORIAS QUE TEM

    // coloca uma melhoria na lista. os personagens chamam no construtor deles.
    protected void adicionarMelhoria(MelhoriaAutoClicker melhoria) {
        melhorias.add(melhoria);
    }

    // devolve a lista de melhorias. a HelloController usa pra colocar na aba "Melhorias" e no save.
    public ArrayList<MelhoriaAutoClicker> getMelhorias() {
        return melhorias;
    }

    //GETTERS / SETTERS E OS GROOGLES!

    // retorna o dano base, os personagens usam no atacar() e no texto de status.
    public double getDano() {
        return dano;
    }

    // retorna o tempo entre ataques, os personagens usam no texto de status ("a cada 3.0s").
    public double getIntervalo() {
        return intervalo;
    }

    // é private de propósito: de fora só dá pra mexer no intervalo pelos métodos do Atributo.
    // Math.max = o maior dos dois, não deixa o intervalo ficar abaixo do mínimo.
    private void setIntervalo(double novoIntervalo) {
        intervalo = Math.max(INTERVALO_MINIMO, novoIntervalo);
    }
}