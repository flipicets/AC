package com.example.ac;

// java.sql = as classes de banco de dados do java (JDBC)
// Connection = a conexão aberta com o banco, DriverManager = quem abre ela, SQLException = erro de banco
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
// Map = "dicionário" chave -> valor (HashMap e LinkedHashMap são tipos de Map)
import java.util.HashMap;
import java.util.Map;

// a nossa classe que tem os SQL
import com.example.ac.models.GameStateModel;

/*
 * é o controller de acesso aos dados (era um TODO antigo nosso), faz o salvar, carregar e resetar
 *
 * quem faz o quê
 *   HelloController -> botões da tela e mensagens pro jogador
 *   SaveController  -> abre/fecha a conexão e decide A ORDEM das coisas
 *   GameStateModel  -> só o SQL
 *
 * nenhum método daqui trata SQLException, eles só repassam (throws) e quem trata é a
 * HelloController pq é ela que consegue mostrar o erro na tela
 */
public class SaveController {

    // endereço do banco, "jdbc:sqlite:" diz que é SQLite e "sample.db" é o arquivo
    // ele é criado na pasta de onde o jogo roda e tá no .gitignore, então cada um tem o seu save
    private static final String URL_DO_BANCO = "jdbc:sqlite:sample.db";

    // quem tem os SQL
    private final GameStateModel model = new GameStateModel();

    // salva o jogo, quem chama é o botão Salvar (HelloController.salvar())
    // recebe o mapa com todos os upgrades do jogo
    public void salvar(Map<String, Upgrade> upgrades) throws SQLException {
        // try com parênteses (try-with-resources) = abre a conexão e FECHA sozinho no final,
        // dando certo ou dando erro
        try (Connection conn = DriverManager.getConnection(URL_DO_BANCO)) {
            model.migrar(conn);                    // cria as tabelas se ainda não existirem
            model.salvarEstado(conn);              // grava vida do adrian e bufunfa
            model.salvarUpgrades(conn, upgrades);  // grava o nível de cada upgrade comprado
        }
    }

    // carrega o save pra dentro do jogo, quem chama é o botão Carregar (HelloController.carregar())
    // devolve false se não existir save (e aí NAO mexe em nada do jogo atual)
    // a ordem importa
    //   1) lê os níveis do banco
    //   2) zera o jogo inteiro
    //   3) reaplica cada upgrade o número de vezes salvo (sem cobrar)
    //   4) por último põe a vida e a bufunfa salvas por cima
    public boolean carregar(Map<String, Upgrade> upgrades) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL_DO_BANCO)) {
            model.migrar(conn);

            // não tem nada salvo, sai sem mexer no jogo
            if (!model.existeSave(conn)) {
                return false;
            }

            // mapa id -> nível que tava salvo, ex "fe" -> 1, "fe_dano" -> 4
            HashMap<String, Integer> niveis = model.carregarNiveis(conn);

            resetarJogo(upgrades);

            // passa por cada par (id, upgrade) do mapa, getKey() é o id e getValue() é o Upgrade
            for (Map.Entry<String, Upgrade> entrada : upgrades.entrySet()) {
                // getOrDefault = pega o nível salvo desse id, se o id não tiver no save considera 0
                int nivelSalvo = niveis.getOrDefault(entrada.getKey(), 0);
                // restaurar é do Upgrade, roda o executar() esse número de vezes sem cobrar
                entrada.getValue().restaurar(nivelSalvo);
            }

            // por último a vida e a bufunfa (tem que ser depois pq o reset e o restaurar mexem nelas)
            model.carregarEstado(conn);
            return true;
        }
    }

    // zera o jogo, usado aqui no carregar() e tb pelo botão Novo jogo da HelloController
    public void resetarJogo(Map<String, Upgrade> upgrades) {
        // zera os números globais e enche a vida do adrian
        GameState.getInstance().resetar();

        // polimorfismo, a chamada é a mesma mas cada classe reseta os atributos dela (fefo, gale...)
        for (Upgrade upgrade : upgrades.values()) {
            upgrade.resetar();
        }
    }
}