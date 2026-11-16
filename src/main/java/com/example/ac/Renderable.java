package com.example.ac;

// URL = endereço de um arquivo, Objects = utilitário do java (aqui pra checar se algo é null)
import java.net.URL;
import java.util.Objects;

// Image do JavaFX = uma imagem carregada na memória
import javafx.scene.image.Image;

/*
 * é um sprite pronto pra desenhar, a imagem + o tamanho da "caixa" onde ela cabe
 * cada Upgrade que aparece na arena tem um Renderable e quem desenha ele é o Renderer
 *
 * sprite faltando não derruba o jogo, se o arquivo não existir carrega o placeholder
 * (o quadrado rosa e preto com "?") e avisa no console qual faltou
 * pra colocar sprite novo é só salvar o PNG na pasta images com o nome que o código pede
 */
public class Renderable {

    // caminho do placeholder, usado sempre que um sprite pedido ainda não existe
    public static final String PLACEHOLDER = "images/placeholder.png";

    // final pq depois de criado o sprite não muda, se quiser outro cria um Renderable novo
    private final Image imagem;
    private final double largura;
    private final double altura;

    // construtor, recebe o caminho da imagem e o tamanho da caixa
    // quem dá new são os upgrades no construtor deles (e o furúnculo quando troca de sprite)
    public Renderable(String caminho, double largura, double altura) {
        this.imagem = carregarImagem(caminho, largura, altura);
        this.largura = largura;
        this.altura = altura;
    }

    // carrega uma imagem da pasta resources cabendo em largura x altura
    // é static pq outras classes usam sem criar Renderable (Upgrade.getIcone pro ícone da loja
    // e a HelloController pra foto do adrian)
    public static Image carregarImagem(String caminho, double largura, double altura) {
        // getResource procura o arquivo na pasta resources, se não achar devolve null
        URL url = Renderable.class.getResource(caminho);

        // não achou, avisa no console e usa o placeholder no lugar
        if (url == null) {
            System.out.println("[SPRITE FALTANDO] " + caminho + " -> usando placeholder");
            // requireNonNull = se até o placeholder for null estoura erro com essa mensagem
            // aí é pra quebrar mesmo pq sem ele não tem o que mostrar
            url = Objects.requireNonNull(
                    Renderable.class.getResource(PLACEHOLDER),
                    "Nem o placeholder foi encontrado: " + PLACEHOLDER
            );
        }

        // já carrega no tamanho pedido pq nossos PNG são gigantes (tipo 2684x2515) e comiam memória à toa
        // o true = não estica nem achata o desenho, o false = não borra o pixel art
        return new Image(url.toExternalForm(), largura, altura, true, false);
    }

    // retorna a imagem, o Renderer usa pra desenhar no canvas
    public Image getImagem() {
        return imagem;
    }

    // largura da caixa, hoje ninguém usa pq o Renderer pega o tamanho direto da imagem
    public double getLargura() {
        return largura;
    }

    // altura da caixa, mesma coisa, ninguém usa hoje
    public double getAltura() {
        return altura;
    }
}