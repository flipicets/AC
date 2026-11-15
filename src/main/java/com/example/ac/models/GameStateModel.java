package com.example.ac.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.ac.GameState;

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

	    } catch (SQLException e) {
	        System.out.println("faiô o banco dos dado affe");
	        e.printStackTrace();
	        throw e;
	    }
	}
	
	public void salvarEstado(Connection conn) {

	    String sql = "INSERT INTO estado_do_jogo "
	        + "(id, adrian_vida, bufunfa) VALUES (?, ?, ?) "
	        + "ON CONFLICT(id) DO UPDATE SET "
	        + "adrian_vida = excluded.adrian_vida, "
	        + "bufunfa = excluded.bufunfa";

	    try (PreparedStatement statement = conn.prepareStatement(sql)) {

	        statement.setInt(1, 1);
	        statement.setLong(2, GameState.getInstance().getAdrianVida());
	        statement.setDouble(3, GameState.getInstance().getBufunfa());

	        statement.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	public void carregarEstado(Connection conn) throws SQLException {
		String sql = "select * from estado_do_jogo where id = 1";
		Statement statement = conn.createStatement();
		ResultSet results = statement.executeQuery(sql);
		
		while(results.next()) {
			GameState.getInstance().setAdrianVida(results.getLong(2));
			GameState.getInstance().setBufunfa(results.getDouble(3));
		}
		
	}
		
}
