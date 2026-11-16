package com.example.ac;

// java normal
// SQLException = erro de banco, ArrayList = lista, LinkedHashMap = "dicionário" id -> objeto que lembra a ordem
// Optional = caixinha que pode ter ou não um valor dentro (usado na resposta do alerta)
// os 3 do concurrent são o agendador que roda o loop do jogo numa thread separada
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// tudo do JavaFX
// ScaleTransition = animação de aumentar/diminuir, Platform = pra falar com a thread da tela
// Bindings = monta os binds, @FXML = liga a variável/método com o arquivo fxml
// o resto são os componentes da tela (Label, Button, ListView, ProgressBar, Canvas, ImageView, Alert)
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/*
 * controller da tela principal (hello-view.fxml), é a "cola" entre a tela e o resto do jogo
 * ela não tem regra de jogo, só liga botão em método, cria os upgrades e roda o loop
 *
 * mapa pra se achar
 *   initialize()           -> binds da tela, cria os upgrades, liga o loop
 *   registrarUpgrade()     -> põe um upgrade na loja
 *   registrarAutoClicker() -> igual, mais as melhorias do personagem
 *   tick()                 -> um passo do loop do jogo (10x por segundo)
 *   farmarAura()           -> clique no adrian
 *   salvar() carregar() novoJogo() -> botões de baixo
 *   encerrar()             -> mata o scheduler ao fechar
 *
 * pra adicionar upgrade novo no jogo, cria a classe (extends Upgrade ou AutoClicker), dá new
 * e registra aqui no initialize() com um id que NUNCA muda pq é ele que vai pro banco
 */
public class HelloController {

    // de quanto em quanto tempo o loop roda, 100 ms = 10 passos por segundo
    // DT é a mesma coisa em segundos (0.1), o 1000.0 com ponto é pra divisão dar número quebrado
    private static final long PASSO_EM_MS = 100;
    private static final double DT = PASSO_EM_MS / 1000.0;

    private static final String FALA_PADRAO = "PARE DE ABAIXAR MINHAS CALCINHAS";

    // tudo com @FXML é um componente da tela, o JavaFX preenche sozinho procurando o fx:id com o mesmo nome no fxml
    @FXML Label vida;                 // texto da vida embaixo da barra
    @FXML ProgressBar barraVida;      // a barra vermelha

    // aba "Loja"
    @FXML
    private ListView<Upgrade> listaUpgrades;

    // aba "Melhorias", só mostra as melhorias dos personagens já comprados
    @FXML
    private ListView<Upgrade> listaMelhorias;

    // a arena, onde o Renderer desenha os personagens
    @FXML
    private Canvas canvas;

    // a foto do adrian, que é onde o jogador clica
    @FXML
    private ImageView adrianImg;

    @FXML Label fala;         // a frase embaixo do adrian
    @FXML Label danoClique;   // "Dano por clique: X"
    @FXML Label mensagem;     // aviso amarelo lá embaixo

    // quem desenha a arena
    static private Renderer renderer;

    // o agendador, roda tarefas de tempos em tempos numa thread separada (o 1 é quantas threads)
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // o número amarelo do dinheiro
    @FXML
    Label bufunfa;

    @FXML
    Button addBufunfa_btn;

    // coleções de objetos
    // upgrades = TODOS os upgrades do jogo por id ("fe", "gale_dano"...), é daqui que o save lê e o reset passa
    // é guardado como Upgrade (tipo da mãe) mesmo tendo 12 classes diferentes dentro, isso é polimorfismo
    // é Linked pq a ordem importa no carregar (o personagem tem que voltar antes das melhorias dele)
    // autoClickers = só os 4 personagens, pro loop chamar atualizar()
    private final LinkedHashMap<String, Upgrade> upgrades = new LinkedHashMap<>();
    private final ArrayList<AutoClicker> autoClickers = new ArrayList<>();

    // quem fala com o banco de dados
    private final SaveController saveController = new SaveController();

    // pra não abrir o aviso de vitória mais de uma vez
    private boolean avisouVitoria = false;

    // o JavaFX chama sozinho quando a tela termina de carregar (lá no fxmlLoader.load() da HelloApplication)
    // faz os binds da tela, cria os upgrades e liga o loop do jogo
    @FXML
    public void initialize() {
        GameState gs = GameState.getInstance();
        Adrian adrian = Adrian.getInstance();

        // BINDS, o texto da tela segue a variável sozinho
        // createStringBinding lê assim "o texto é o resultado dessa função, refaz sempre que a property do final mudar"
        // o "() ->" é lambda, uma função sem nome escrita ali mesmo

        // número da bufunfa, refaz quando a bufunfa do GameState muda
        bufunfa.textProperty().bind(Bindings.createStringBinding(
                () -> Formatador.numero(gs.getBufunfa().get()),
                gs.getBufunfa()
        ));

        // texto da vida "9.999.999 / 10,00 T", refaz quando a vida do Adrian muda
        vida.textProperty().bind(Bindings.createStringBinding(
                () -> Formatador.inteiroCompleto(adrian.getVidaAtual())
                        + "  /  " + Formatador.numero(adrian.getVidaMax()),
                adrian.vidaAtualProperty()
        ));

        // ProgressBar vai de 0.0 a 1.0, então é vidaAtual / vidaMax
        // o (double) é cast, sem ele a divisão seria de inteiros e daria sempre 0
        barraVida.progressProperty().bind(
                adrian.vidaAtualProperty().divide((double) adrian.getVidaMax())
        );

        // o aviso de baixo segue a mensagem do GameState
        mensagem.textProperty().bind(gs.mensagemProperty());

        // listener = "me avisa quando mudar", toda vez que a vida muda confere se ele morreu
        adrian.vidaAtualProperty().addListener(
                (observable, antigo, novo) -> conferirVitoria()
        );

        // ADRIAN
        // falta sprite, a foto tá com fundo (Adrian.jpg), se alguém recortar salva em PNG e troca o nome aqui
        // tb dá pra ter um sprite de "tomando dano" e um de "morto", trocando com setImage no farmarAura() e no conferirVitoria()
        adrianImg.setImage(Renderable.carregarImagem("images/Adrian.jpg", 400, 400));
        fala.setText(FALA_PADRAO);

        // cria o Renderer passando o canvas onde ele vai desenhar
        renderer = new Renderer(canvas);

        // diz pras duas listas usarem a nossa célula customizada (UpgradeCell) em cada linha
        listaUpgrades.setCellFactory(
                list -> new UpgradeCell()
        );
        listaMelhorias.setCellFactory(
                list -> new UpgradeCell()
        );

        // UPGRADES, cria um objeto de cada
        // repara que a variável é do tipo da mãe (Upgrade / AutoClicker) e o objeto é da filha
        Upgrade cliqueDano = new UpgradeDanoClique();
        Upgrade cliqueMult = new UpgradeMultiplicadorClique();
        Upgrade cliqueZaralha = new UpgradeZaralhaCliques();
        Upgrade dobrarMulti = new UpgradeMultiplicadorQntdClique();
        Upgrade teste = new TesteUpgrade();
        Upgrade ad = new AutoDamager();
        AutoClicker f = new Furunculo();
        AutoClicker fe = new FinalFefo();
        AutoClicker gale = new CondeGale();
        AutoClicker mari = new Maristelar();

        // a ordem aqui é a ordem que aparece na loja, e o texto é o id que vai pro banco
        registrarUpgrade("clique_dano", cliqueDano);
        registrarUpgrade("clique_mult", cliqueMult);
        registrarUpgrade("clique_zaralha",cliqueZaralha );
        registrarUpgrade("dobra_mult", dobrarMulti );
        registrarUpgrade("ad", ad);
        registrarUpgrade("teste", teste);
        registrarAutoClicker("f", f);
        registrarAutoClicker("fe", fe);
        registrarAutoClicker("gale", gale);
        registrarAutoClicker("mari", mari);

        // LOOP DO JOGO
        // o scheduler roda numa thread separada e o JavaFX só deixa mexer na tela pela thread dele
        // o Platform.runLater é o "recado", pede pra thread do JavaFX rodar o tick() assim que puder
        // os números são: começa em 0 de atraso, repete a cada PASSO_EM_MS (100), em milissegundos
        scheduler.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                tick();
            });
        }, 0, PASSO_EM_MS, TimeUnit.MILLISECONDS);
    }

    // põe um upgrade no jogo, guarda no mapa (pro save) e coloca na aba Loja
    // e fica escutando o "comprado" dele pra avisar o Renderer
    private void registrarUpgrade(String id, Upgrade upgrade) {
        // quando comprado vira true aparece na arena, quando vira false (reset) sai
        upgrade.compradoProperty().addListener(
                (observable, antigo, novo) -> {
                    if (novo) {
                        renderer.add(id, upgrade);
                    } else {
                        renderer.remover(id);
                    }
                }
        );

        if (upgrade.isComprado()) {
            renderer.add(id, upgrade);
        }

        upgrades.put(id, upgrade);               // put = guarda no mapa com esse id
        listaUpgrades.getItems().add(upgrade);   // aparece na loja
    }

    // registra um personagem e as melhorias dele
    // as melhorias entram no mapa geral (pra salvar/resetar) mas só aparecem na aba Melhorias depois que compra o personagem
    private void registrarAutoClicker(String id, AutoClicker autoClicker) {
        registrarUpgrade(id, autoClicker);   // a parte normal de upgrade
        autoClickers.add(autoClicker);       // entra na lista do loop

        // for-each, passa por cada melhoria do personagem
        for (MelhoriaAutoClicker melhoria : autoClicker.getMelhorias()) {
            // id fica tipo "fe_dano", "gale_esp"
            upgrades.put(id + "_" + melhoria.getTipo(), melhoria);
        }

        // comprou o personagem = as melhorias dele aparecem na aba, resetou = somem
        autoClicker.compradoProperty().addListener(
                (observable, antigo, novo) -> {
                    if (novo) {
                        listaMelhorias.getItems().addAll(autoClicker.getMelhorias());
                    } else {
                        listaMelhorias.getItems().removeAll(autoClicker.getMelhorias());
                    }
                }
        );
    }

    // um passo do jogo, roda 10x por segundo (quem chama é o scheduler lá do initialize)
    private void tick() {
        GameState gs = GameState.getInstance();

        // se o adrian morreu para tudo
        if (!Adrian.getInstance().estaMorto()) {
            // renda do Link furry e dano do Cesar Pateta
            gs.atualizarPassivos(DT);

            // polimorfismo, a lista é de AutoClicker mas cada objeto roda o atacar() da própria classe quando chega a hora
            for (AutoClicker autoClicker : autoClickers) {
                autoClicker.atualizar(DT);
            }
        }

        // atualiza o texto do dano por clique
        danoClique.setText("Dano por clique: " + Formatador.numero(gs.getDanoPorClique()));
    }

    // chamado toda vez que a vida do adrian muda (pelo listener do initialize)
    // se morreu e ainda não avisou, mostra a janelinha de vitória
    private void conferirVitoria() {
        // "&&" é E, precisa das duas coisas serem verdade
        if (Adrian.getInstance().estaMorto() && !avisouVitoria) {
            avisouVitoria = true;
            fala.setText("...");
            GameState.getInstance().setMensagem("O Adrian foi derrotado!");

            // Alert = janelinha pronta do JavaFX
            // é show() e não showAndWait() pq isso roda dentro do loop e não pode travar ele
            // falta fazer uma tela de vitória de vdd (créditos, tempo de jogo...)
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Vitória");
            alerta.setHeaderText("Você matou o Adrian!");
            alerta.setContentText("10 trilhões de HP depois... Clique em \"Novo jogo\" pra recomeçar.");
            alerta.show();
        }
    }

    // para o scheduler e o Renderer, quem chama é o stop() da HelloApplication quando fecha a janela
    // sem isso o programa continua rodando thread mesmo fechado
    // se quiser salvar sozinho ao fechar é só chamar salvar() aqui
    @FXML
    public void encerrar() {
        scheduler.shutdownNow();
        renderer.parar();
    }

    // botão Salvar, pede pro SaveController gravar tudo no banco e mostra a mensagem
    // tratamento de exceção, se o banco falhar a SQLException cai no catch e o jogador vê o erro na tela
    @FXML
    public void salvar() {
        try {
            saveController.salvar(upgrades);
            GameState.getInstance().setMensagem("Jogo salvo");
        } catch (SQLException e) {
            mostrarErro("Não deu pra salvar o jogo", e);
        }
    }

    // botão Carregar, pede pro SaveController trazer o save do banco
    // ele devolve true se achou save e false se não tinha nada salvo
    @FXML
    public void carregar() {
        try {
            boolean achou = saveController.carregar(upgrades);

            if (achou) {
                // se o save já era de um adrian morto não reabre o aviso de vitória
                avisouVitoria = Adrian.getInstance().estaMorto();
                // "condição ? a : b" é um if curtinho (ternário), se avisouVitoria usa "..." senão a fala padrão
                fala.setText(avisouVitoria ? "..." : FALA_PADRAO);
                GameState.getInstance().setMensagem("Jogo carregado");
            } else {
                GameState.getInstance().setMensagem("Não existe jogo salvo ainda");
            }
        } catch (SQLException e) {
            mostrarErro("Não deu pra carregar o jogo", e);
        }
    }

    // botão Novo jogo, pergunta se tem certeza e zera tudo
    @FXML
    public void novoJogo() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Novo jogo");
        confirmacao.setHeaderText("Começar do zero?");
        confirmacao.setContentText("Todo o progresso que não foi salvo será perdido.");

        // showAndWait espera o jogador responder e devolve qual botão ele apertou
        // é Optional pq pode vir vazio se ele fechar no X
        Optional<ButtonType> resposta = confirmacao.showAndWait();

        // só reseta se respondeu E a resposta foi OK
        if (resposta.isPresent() && resposta.get() == ButtonType.OK) {
            saveController.resetarJogo(upgrades);
            avisouVitoria = false;
            fala.setText(FALA_PADRAO);
            GameState.getInstance().setMensagem("Novo jogo começado");
        }
    }

    // erro de banco, mostra a mensagem embaixo + uma janelinha de erro com o detalhe
    // quem chama é o catch do salvar() e do carregar()
    private void mostrarErro(String titulo, Exception e) {
        e.printStackTrace();   // joga o erro completo no console pra gente debugar
        GameState.getInstance().setMensagem(titulo);

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(titulo);
        alerta.setContentText(e.getMessage());
        alerta.show();
    }

    // botão "+5000 (teste)", dá bufunfa de graça
    // ajuda MUITO na apresentação pra mostrar os upgrades caros sem ficar 20 min clicando
    // a gente tem que decidir se fica na versão final, pra tirar é só apagar o botão no hello-view.fxml
    @FXML
    public void addBufunfa() {
        GameState.getInstance().adicionarBufunfa(5000);
    }

    // o CLIQUE NO ADRIAN (o nome antigo ficou), quem chama é o onMouseClicked da foto lá no fxml
    // dá o dano do clique e a bufunfa vem junto pq todo dano vira bufunfa lá no GameState.causarDano
    @FXML
    public void farmarAura() {
        // morto não leva clique
        if (Adrian.getInstance().estaMorto()) {
            return;
        }

        // GameState faz o dano e devolve quanto foi, o Renderer mostra o número subindo
        //bloco de código antes de ter o upgrade de zaralhar cliques

        //double dano = GameState.getInstance().clicar();
        //renderer.adicionarTextoDeClique("-" + Formatador.numero(dano));

        GameState gs = GameState.getInstance();
       // double danoTotal = 0;

        for (int i = 0; i < gs.getCliquesPorClique(); i++) {
            double dano = gs.clicar();
            renderer.adicionarTextoDeClique("-" + Formatador.numero(dano));
        }
        //renderer.adicionarTextoDeClique("-" + Formatador.numero(danoTotal));

        // "soquinho", a foto encolhe pra 94% e volta pro tamanho normal em 80 ms
        ScaleTransition soco = new ScaleTransition(Duration.millis(80), adrianImg);
        soco.setFromX(0.94);
        soco.setFromY(0.94);
        soco.setToX(1.0);
        soco.setToY(1.0);
        soco.play();
    }
}