package com.example.ac;

// Property do JavaFX: variável que avisa a tela quando muda (é o que faz o bind funcionar)
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

/*
 * cuida só da VIDA do Adrian (a barra vermelha lá em cima).
 * Todo dano do jogo termina aqui, passando antes pelo GameState.
 *
 * é Singleton ou seja só existe UM Adrian no programa, mais ninguem dá new,
 * todo mundo usa Adrian.getInstance().
 *
 * temos atributos private, e a vida nunca fica
 * negativa nem passa do máximo.
 *
 * "final" na classe = ninguém pode herdar de Adrian funciona tipo constante.
 */
public final class Adrian {

    // 10 trilhões de HP, é long porque int só vai até tipo 2 bilhões.
    // o "L" marca o número como long, os "_" são só pra facilitar a leitura (eu vi todo mundo fazendo e quis fazer igual).
    private static final long VIDA_INICIAL = 10_000_000_000_000L;

    // vida máxima não muda durante o jogo, então é long normal.
    private long vidaMax;

    // vida atual é Property pra tela acompanhar sozinha.
    private final LongProperty vidaAtual;

    // o único Adrian do programa (static = pertence à classe).
    private static Adrian INSTANCE = new Adrian();

    // construtor private impede um new Adrian() fora daqui e começa com a vida cheia.
    private Adrian(){
        this.vidaMax = VIDA_INICIAL;
        this.vidaAtual = new SimpleLongProperty(vidaMax);
    }

    // devolve o único Adrian é usado muito no nosso GameState e na HelloController.
    public static Adrian getInstance() {
        return INSTANCE;
    }

    // tira vida e devolve quanto de dano entrou de verdade.
    // chamado só pelo GameState.causarDano(), que transforma o retorno em $$$$$$$ moneeeeyyy yyy y bufunfa.
    public long receberDano(long dano) {
        // dano zero/negativo ou Adrian já morto não faz nada
        if (dano <= 0 || estaMorto()) {
            return 0;
        }
        // math.min = o menor dos dois, isso impede a vida de ficar negativa.
        long danoReal = Math.min(dano, vidaAtual.get());
        // .set() muda o valor e a tela atualiza sozinha por causa do bind
        vidaAtual.set(vidaAtual.get() - danoReal);
        return danoReal;
    }

    // diz se a vida zerou, a HelloController usa pra parar o jogo e mostrar quando ganha.
    public boolean estaMorto() {
        return vidaAtual.get() <= 0;
    }

    // enche a vida de novo é chamado pelo GameState.resetar() (Novo jogo e Carregar).
    public void resetar() {
        vidaAtual.set(vidaMax);
    }

    // retorna a vida máxima e a HelloController usa pra calcular a barra de vida.
    public long getVidaMax() {
        return vidaMax;
    }

    // troca a vida máxima (só aceita maior que zero) a gente nao ta usando pq o reset serve pra isso e não tem como o Adrian se curar.
    public void setVidaMax(long vidaMax) {
        if (vidaMax > 0) {
            this.vidaMax = vidaMax;
        }
    }

    // retorna a vida atual como número a HelloController usa no texto da vida.
    public long getVidaAtual() {
        return vidaAtual.get();
    }

    // define a vida num valor, travado entre 0 e o máximo (Math.min segura o teto,
    // Math.max segura o zero) é usado pelo GameState ao carregar o save.
    public void setVidaAtual(long vidaAtual) {
        long valor = Math.max(0, Math.min(vidaAtual, vidaMax));
        this.vidaAtual.set(valor);
    }

    // retorna a Property inteira (não só o número), pra quem quer "escutar" as mudanças.
    // A HelloController usa nos binds da barra e do label e o GameState usa pra salvar.
    public LongProperty vidaAtualProperty() {
        return vidaAtual;
    }

}
