package com.example.ac;

/*
 * é um numerozinho de dano que aparece na arena, sobe e some ("-1", "CRÍTICO! -300")
 * só guarda onde tá, o que tá escrito e quanto tempo ainda vive
 * quem cria, atualiza e apaga é o Renderer, que guarda todos num ArrayList
 */
public class TextoFlutuante {

    // static final = constante
    private static final double DURACAO = 1.0;            // segundos na tela
    private static final double VELOCIDADE_SUBIDA = 45;   // pixels por segundo

    // texto e x são final pq não mudam (ele só sobe, não anda pro lado)
    private final String texto;
    private final double x;
    private double y;               // muda, vai diminuindo pra subir
    private double tempoRestante;   // muda, vai gastando até zerar

    // construtor, recebe o texto e a posição onde nasce, quem dá new é o Renderer
    // (no adicionarTextoDeClique pro clique e no render pros ataques dos personagens)
    public TextoFlutuante(String texto, double x, double y) {
        this.texto = texto;
        this.x = x;
        this.y = y;
        this.tempoRestante = DURACAO;
    }

    // anda um passo, quem chama é o Renderer.desenharTextos() a cada frame
    // dt = segundos desde o último frame
    // o y DIMINUI pra subir pq na tela o y=0 é lá em cima
    public void atualizar(double dt) {
        y -= VELOCIDADE_SUBIDA * dt;
        tempoRestante -= dt;
    }

    // diz se o tempo acabou, o Renderer usa pra tirar ele da lista
    public boolean acabou() {
        return tempoRestante <= 0;
    }

    // de 1 (acabou de nascer) até 0 (sumindo), o Renderer usa pra ir deixando transparente
    // Math.max segura em 0 pra não dar opacidade negativa
    public double getOpacidade() {
        return Math.max(0, tempoRestante / DURACAO);
    }

    // os 3 de baixo o Renderer usa pra saber o que escrever e onde

    public String getTexto() {
        return texto;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}