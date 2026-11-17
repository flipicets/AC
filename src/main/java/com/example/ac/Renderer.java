package com.example.ac;

// java normal, ArrayList = lista, LinkedHashMap = "dicionário" id -> objeto que lembra a ordem, Random = o dado
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

// tudo do JavaFX
// AnimationTimer = chama um método a cada frame da tela, Canvas = a "folha" onde desenha
// GraphicsContext = o "pincel" do canvas, o resto é imagem, cor, fonte e alinhamento de texto
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/*
 * desenha a arena no canvas, os personagens comprados em volta do adrian (3 de cada lado),
 * a barrinha de carga de cada um e os números de dano subindo
 * ele só DESENHA, não tem regra de jogo nenhuma aqui
 *
 * o AnimationTimer chama o handle() a cada frame da tela (uns 60x por segundo) e redesenha tudo
 * o canvas fica POR CIMA da foto do adrian mas é transparente e não pega clique (isso tá no fxml)
 *
 * aqui NAO tem instanceof, o Renderer pergunta getProgresso/getTextoStatus/pegarTextoDeAtaque
 * pra qualquer Upgrade e cada classe responde do seu jeito (polimorfismo)
 *
 * falta o fundo da arena, hoje é só uma cor no fxml, pra pôr imagem é desenhar ela no começo do render()
 */
public class Renderer {

    // medidas de cada "vaga" de personagem em pixels, static final = constante
    private static final double LARGURA_VAGA = 170;
    private static final double ALTURA_SPRITE = 100;
    private static final int VAGAS_POR_COLUNA = 3;

    private final Canvas canvas;          // a folha
    private final GraphicsContext ctx;    // o pincel, todo desenho é ctx.algumaCoisa

    // coleção de objetos, id -> upgrade que tem sprite na arena
    // é Linked pra manter a ordem de compra, senão os personagens podiam trocar de lugar na tela
    private final LinkedHashMap<String, Upgrade> personagens = new LinkedHashMap<>();

    // coleção de objetos, os números de dano que tão na tela agora
    private final ArrayList<TextoFlutuante> textos = new ArrayList<>();

    private final Random random = new Random();

    private final AnimationTimer timer;
    private long ultimoFrame = 0;   // horário do frame anterior, pra calcular o dt

    // construtor, quem dá new é a HelloController no initialize() passando o canvas da tela
    public Renderer(Canvas canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();

        // false = não borra o pixel art quando aumenta/diminui a imagem
        ctx.setImageSmoothing(false);

        // classe anônima, a gente cria um AnimationTimer e já escreve o handle() dele aqui mesmo
        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // "now" vem em nanossegundos, dividindo por 1 bilhão vira segundos
                // dt = quanto tempo passou desde o frame anterior (no primeiro frame fica 0)
                double dt = 0;
                if (ultimoFrame != 0) {
                    dt = (now - ultimoFrame) / 1_000_000_000.0;
                }
                ultimoFrame = now;

                render(dt);
            }
        };

        timer.start();
    }

    // coloca um upgrade na arena, quem chama é a HelloController quando ele é comprado
    // só entra se tiver sprite (os de clique e as melhorias são null) e se ainda não tiver lá
    public void add(String id, Upgrade upgrade) {
        if (upgrade.getSprite() != null && !personagens.containsKey(id)) {
            personagens.put(id, upgrade);
        }
    }

    // tira da arena, quem chama é a HelloController quando o jogo é resetado
    public void remover(String id) {
        personagens.remove(id);
    }

    // troca o sprite de alguém que já tá na arena
    // hoje ninguém usa, o furúnculo troca o próprio sprite direto com setSprite
    public void updateRenderable(String id, Renderable renderable) {
        if (personagens.containsKey(id)) {
            this.personagens.get(id).setSprite(renderable);
        }
    }

    // cria o número de dano do clique, quem chama é a HelloController.farmarAura()
    // nasce perto do meio do canvas e um pouco espalhado
    // (random.nextDouble() - 0.5) dá um número de -0.5 a 0.5, vezes 160 espalha até 80px pra cada lado
    public void adicionarTextoDeClique(String texto) {
        double x = canvas.getWidth() / 2 + (random.nextDouble() - 0.5) * 160;
        double y = canvas.getHeight() / 2 - 60 + (random.nextDouble() - 0.5) * 120;
        textos.add(new TextoFlutuante(texto, x, y));
    }

    // desenha um frame inteiro, quem chama é o handle() do timer lá em cima
    private void render(double dt) {
        // apaga tudo que tinha no canvas pra desenhar de novo
        ctx.clearRect(
                0, 0,
                canvas.getWidth(),
                canvas.getHeight()
        );

        double alturaVaga = canvas.getHeight() / VAGAS_POR_COLUNA;

        int i = 0;

        // passa por cada upgrade que tá na arena
        for (Upgrade upgrade : personagens.values()) {
            // "%" é o resto da divisão, i par -> coluna da esquerda, i ímpar -> coluna da direita
            boolean esquerda = (i % 2 == 0);
            // i / 2 com inteiros joga fora o resto, então 0 e 1 = linha 0, 2 e 3 = linha 1, 4 e 5 = linha 2
            int linha = (i / 2) % VAGAS_POR_COLUNA;

            // "condição ? a : b" é if curtinho (ternário), escolhe o x da esquerda ou da direita
            double xCentro = esquerda
                    ? LARGURA_VAGA / 2
                    : canvas.getWidth() - LARGURA_VAGA / 2;
            double yTopo = linha * alturaVaga + 6;

            desenharPersonagem(upgrade, xCentro, yTopo);

            // se ele atacou desde o último frame cria o número subindo do lado do adrian
            // só AutoClicker devolve texto aqui, os outros devolvem null
            String ataque = upgrade.pegarTextoDeAtaque();
            if (ataque != null) {
                double xTexto = esquerda ? LARGURA_VAGA + 60 : canvas.getWidth() - LARGURA_VAGA - 60;
                textos.add(new TextoFlutuante(ataque, xTexto, yTopo + ALTURA_SPRITE / 2));
            }

            i++;
        }

        desenharTextos(dt);
    }

    // desenha um personagem na vaga dele, sprite + nome + barrinha + status
    private void desenharPersonagem(Upgrade upgrade, double xCentro, double yTopo) {
        // sprite centralizado na vaga e sem esticar
        // escala = quanto precisa encolher pra caber, Math.min pega a menor pra caber na altura E na largura
        Image imagem = upgrade.getSprite().getImagem();
        double escala = Math.min(ALTURA_SPRITE / imagem.getHeight(), (LARGURA_VAGA - 20) / imagem.getWidth());
        double largura = imagem.getWidth() * escala;
        double altura = imagem.getHeight() * escala;

        // drawImage(imagem, x, y, largura, altura), o x e y são o canto de cima da esquerda
        ctx.drawImage(
                imagem,
                xCentro - largura / 2,
                yTopo + (ALTURA_SPRITE - altura) / 2,
                largura,
                altura
        );

        double y = yTopo + ALTURA_SPRITE + 14;

        // nome, setFill = cor, setFont = fonte, fillText = escreve
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setFill(Color.WHITE);
        ctx.setFont(Font.font("System", FontWeight.BOLD, 13));
        ctx.fillText(upgrade.getNome(), xCentro, y);

        // barrinha de carga, só pra quem tem (o Upgrade normal devolve -1, o AutoClicker devolve de 0 a 1)
        double progresso = upgrade.getProgresso();
        if (progresso >= 0) {
            double larguraBarra = 120;
            double xBarra = xCentro - larguraBarra / 2;
            y += 6;

            // fillRect = retângulo pintado, primeiro o fundo cinza inteiro
            ctx.setFill(Color.web("#3a3a55"));
            ctx.fillRect(xBarra, y, larguraBarra, 7);
            // depois o amarelo por cima só até onde o progresso chegou
            ctx.setFill(Color.web("#f1c40f"));
            ctx.fillRect(xBarra, y, larguraBarra * progresso, 7);

            y += 7;
        }

        // status (pode ter mais de uma linha, separadas por \n)
        ctx.setFill(Color.web("#c8c8e0"));
        ctx.setFont(Font.font("System", 11));
        ctx.fillText(upgrade.getTextoStatus(), xCentro, y + 13);
    }

    // atualiza e desenha os números de dano, e apaga os que já sumiram
    private void desenharTextos(double dt) {
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setFont(Font.font("System", FontWeight.BOLD, 18));

        // percorre de trás pra frente pq a gente REMOVE itens durante o for
        // indo do fim pro começo remover um não bagunça o índice dos que ainda faltam olhar
        for (int i = textos.size() - 1; i >= 0; i--) {
            TextoFlutuante texto = textos.get(i);
            texto.atualizar(dt);   // sobe um pouco e gasta tempo de vida

            // acabou o tempo, tira da lista e pula pro próximo (continue)
            if (texto.acabou()) {
                textos.remove(i);
                continue;
            }

            // setGlobalAlpha = transparência, vai ficando mais apagado conforme some
            ctx.setGlobalAlpha(texto.getOpacidade());
            ctx.setFill(Color.BLACK);
            ctx.fillText(texto.getTexto(), texto.getX() + 1, texto.getY() + 1);   // sombra
            ctx.setFill(Color.web("#ff5e57"));
            ctx.fillText(texto.getTexto(), texto.getX(), texto.getY());
        }

        // volta a transparência pro normal senão o próximo frame saía todo apagado
        ctx.setGlobalAlpha(1.0);
    }

    // para o timer, quem chama é a HelloController.encerrar() quando fecha o jogo
    public void parar() {
        timer.stop();
    }
}