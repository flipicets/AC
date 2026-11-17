package com.example.ac;

// tudo do JavaFX
// Application = classe mãe de todo app JavaFX, FXMLLoader = lê o arquivo .fxml e monta a tela
// Scene = o conteúdo da janela, Stage = a janela em si
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// erro de arquivo, pode acontecer se o fxml não for encontrado
import java.io.IOException;

/*
 * é a classe que abre a janela do jogo, o JavaFX chama o start() quando o programa liga
 * e o stop() quando fecha, quem dispara tudo é o Launcher (o main)
 *
 * é o padrão do arquétipo do openjfx com a adição do stop, que chama o encerrar da controller
 * pra matar o agendador e não ficar spawnando thread a toa
 */

public class HelloApplication extends Application {

    // guarda a controller da tela principal pra conseguir chamar o encerrar() lá no stop()
    private HelloController controller;

    // roda quando o jogo abre, o JavaFX que chama e já entrega a janela (stage) pronta
    // throws IOException pq o load() do fxml pode falhar, aí o erro sobe e o jogo nem abre
    @Override
    public void start(Stage stage) throws IOException {
        // aponta pro arquivo da tela, getResource procura na pasta resources
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        // load() monta a tela e já cria a HelloController e chama o initialize() dela
        // 1150 x 720 é o tamanho da janela
        Scene scene = new Scene(fxmlLoader.load(), 1150, 720);
        // pega a controller que o load criou
        this.controller = fxmlLoader.getController();
        stage.setTitle("Mate Adrian");
        stage.setScene(scene);
        stage.setResizable(false);   // a arena tem tamanho fixo, então travamos a janela
        stage.show();                // mostra a janela
    }

    //Bem fofinho o método stop
    // roda quando fecha a janela, manda a HelloController parar o scheduler e o Renderer
    // sem isso o programa continua rodando em segundo plano mesmo fechado
    @Override
    public void stop() {
        controller.encerrar();
    }
}