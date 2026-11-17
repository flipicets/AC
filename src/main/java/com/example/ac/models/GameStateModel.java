package com.example.ac.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.example.ac.GameState;
import com.example.ac.Upgrade;

/*
 * Integração com o banco, mas tá pela metade   <- [FEITO] agora salva tudo
 *
 * TODO: - Atualizar a implementação para ela puxar todos os status de dentro do GameState, pq no momento só
 * 		 tá puxando a 'bufunfa' e a vida do Adrian, então
 *       [FEITO, mas de outro jeito, ver explicação abaixo]
 *
 * 		 - Criar controller de acesso para que não fique tudo jogado pela Model
 *       [FEITO] é a classe SaveController. Aqui ficou SÓ o SQL.
 *
 * ---------------------------------------------------------------------------
 * [RUBRICA EXTRA: Integração com banco de dados] SQLite via JDBC.
 *
 * O QUE VAI PRO BANCO (duas tabelas):
 *
 *   estado_do_jogo      (id, adrian_vida, bufunfa)   -> 1 linha só (id = 1)
 *   upgrades_comprados  (id, nivel)                  -> 1 linha por upgrade comprado
 *                        ex: ("fe", 1), ("fe_dano", 4), ("teste", 12)
 *
 * POR QUE NÃO SALVAMOS dano do clique, renda passiva, dano do Fefo etc.:
 * todos esses números são CONSEQUÊNCIA dos upgrades comprados. Na hora de
 * carregar, a gente zera o jogo e reaplica cada upgrade "nivel" vezes
 * (Upgrade.restaurar), e tudo volta a ser exatamente o que era. Assim, se a
 * gente criar um upgrade ou atributo novo, não precisa mexer na tabela.
 * Só vida e bufunfa precisam ser salvas direto porque dependem do que o
 * jogador fez, não dá pra recalcular.
 *
 * A tabela estado_do_jogo é a mesma de antes, então save antigo ainda abre.
 *
 * [OBS] Os "?" nos SQL são do PreparedStatement: a gente preenche com
 * setLong/setString em vez de concatenar texto (mais seguro e mais limpo).
 * O try (...) com parênteses fecha o statement sozinho no final.
 * ---------------------------------------------------------------------------
 */

public class GameStateModel {

	public GameStateModel() {
	}


	public void migrar(Connection conn) throws SQLException {

	    try (Statement statement = conn.createStatement()) {

	        statement.executeUpdate(
	            "CREATE TABLE IF NOT EXISTS estado_do_jogo ("
	            + "id INTEGER PRIMARY KEY, "
	            + "adrian_vida INTEGER, "
	            + "bufunfa REAL"
	            + ")"
	        );

	        // [NOVO]
	        statement.executeUpdate(
	            "CREATE TABLE IF NOT EXISTS upgrades_comprados ("
	            + "id TEXT PRIMARY KEY, "
	            + "nivel INTEGER"
	            + ")"
	        );

	    } catch (SQLException e) {
	        System.out.println("faiô o banco dos dado affe");
	        e.printStackTrace();
	        throw e;
	    }
	}

	/*
	 * [ATUALIZADO] antes o catch daqui só dava printStackTrace e a tela dizia
	 * que salvou mesmo quando não salvou. Agora o erro sobe (throws) pra
	 * HelloController avisar o jogador.
	 */
	public void salvarEstado(Connection conn) throws SQLException {

	    String sql = "INSERT INTO estado_do_jogo "
	        + "(id, adrian_vida, bufunfa) VALUES (?, ?, ?) "
	        + "ON CONFLICT(id) DO UPDATE SET "
	        + "adrian_vida = excluded.adrian_vida, "
	        + "bufunfa = excluded.bufunfa";

	    try (PreparedStatement statement = conn.prepareStatement(sql)) {

	        statement.setInt(1, 1);
	        statement.setLong(2, GameState.getInstance().getAdrianVida().longValue());
	        statement.setDouble(3, GameState.getInstance().getBufunfa().doubleValue());

	        statement.executeUpdate();
	    }
	}

	/*
	 * [NOVO] Salva o nível de cada upgrade comprado.
	 * Apaga tudo e insere de novo: é o jeito mais simples de garantir que o
	 * banco fica igual ao jogo (inclusive depois de um "Novo jogo").
	 */
	public void salvarUpgrades(Connection conn, Map<String, Upgrade> upgrades) throws SQLException {

	    try (Statement statement = conn.createStatement()) {
	        statement.executeUpdate("DELETE FROM upgrades_comprados");
	    }

	    String sql = "INSERT INTO upgrades_comprados (id, nivel) VALUES (?, ?)";

	    try (PreparedStatement statement = conn.prepareStatement(sql)) {

	        // Percorre o mapa: getKey() é o id ("fe"), getValue() é o Upgrade
	        for (Map.Entry<String, Upgrade> entrada : upgrades.entrySet()) {
	            Upgrade upgrade = entrada.getValue();

	            if (upgrade.getNivel() > 0) {
	                statement.setString(1, entrada.getKey());
	                statement.setInt(2, upgrade.getNivel());
	                statement.executeUpdate();
	            }
	        }
	    }
	}

	// [NOVO] Existe alguma coisa salva? (pra não zerar o jogo à toa no Carregar)
	public boolean existeSave(Connection conn) throws SQLException {
		String sql = "select count(*) from estado_do_jogo where id = 1";

		try (Statement statement = conn.createStatement();
		     ResultSet results = statement.executeQuery(sql)) {

			return results.next() && results.getInt(1) > 0;
		}
	}


	public void carregarEstado(Connection conn) throws SQLException {
		String sql = "select * from estado_do_jogo where id = 1";

		// [ATUALIZADO] agora dentro do try(...) pra fechar o statement e o ResultSet
		try (Statement statement = conn.createStatement();
		     ResultSet results = statement.executeQuery(sql)) {

			while(results.next()) {
				GameState.getInstance().setAdrianVida(results.getLong(2));
				GameState.getInstance().setBufunfa(results.getDouble(3));
			}
		}
	}

	// [NOVO] Devolve id -> nível de tudo que estava comprado no save
	public HashMap<String, Integer> carregarNiveis(Connection conn) throws SQLException {
		HashMap<String, Integer> niveis = new HashMap<>();
		String sql = "select id, nivel from upgrades_comprados";

		try (Statement statement = conn.createStatement();
		     ResultSet results = statement.executeQuery(sql)) {

			while (results.next()) {
				niveis.put(results.getString(1), results.getInt(2));
			}
		}

		return niveis;
	}

}
