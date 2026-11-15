package com.example.ac;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.ac.models.GameStateModel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class HelloController {

	@FXML Button invocarThread;
	@FXML Label vida;
	
	@FXML
	private ListView<Upgrade> listaUpgrades;
    
	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	@FXML
	Label bufunfa;
	
	@FXML
	Button addBufunfa_btn;
	
	
	@FXML
	/*
	 * Na initialize estou fazendo os binds das alterações de variáveis já atribuídas
	 * à UI, basicamente estou definindo um observer que, a cada atualização na varíavel
	 * bufunfa e adrianVida, eu já atualizo automaticamente o texto
	 * 
	 * Criando uma lista com os upgrades disponíveis
	 * 
	 * Agendando as tarefas de dano automático e ganho de dinheiro automático a cada segundo
	 * */
    public void initialize(){
		bufunfa.textProperty().bind(GameState.getInstance().bufunfa.asString("%.2f"));
		vida.textProperty().bind(GameState.getInstance().adrianVida.asString());
		
		ArrayList<String> array = new ArrayList<>(); 
		
		 listaUpgrades.setCellFactory(
			        list -> new UpgradeCell()
			    );
		 			
		Upgrade teste = new TesteUpgrade();
		Upgrade ad = new AutoDamager();
		 
	    listaUpgrades.getItems().addAll(
	    		teste, ad
	    		);
	    scheduler.scheduleAtFixedRate(() -> {
	        Platform.runLater(() -> {
	            GameState gs = GameState.getInstance();
	            gs.setBufunfa(
	                gs.getBufunfa().get() + gs.getBufunfaMultiplier()
	            );
	            gs.setAdrianVida(
	            	    (long) (gs.getAdrianVida().doubleValue() - gs.passiveDamage)
	            	);
	        });
	    }, 0, 1, TimeUnit.SECONDS);
	}
	
	/*
	 * Faz o encerramento do serviço de agendamento de Threads, sem isto
	 * o programa continua invocando threads mesmo fechado.
	 * */
	@FXML
	public void encerrar() {
		scheduler.shutdownNow();
	}
	
	/*
	 * TODO: implementar controller de acesso aos dados
	 * 
	 * esta função está fazendo muitas coisas, está funcionando, porém
	 * por padrões de desenvolvimento é uma função que o nome não é descritivo
	 * salvar() cria o banco de dados, instancia a model (que medo)
	 * inicia a conexão com o banco de dados
	 * e salva os dados no banco
	 * */
	@FXML
	public void salvar() throws SQLException {
		GameStateModel model = new GameStateModel();
		Connection conn = DriverManager.getConnection("jdbc:sqlite:sample.db");
		model.migrar(conn);
		model.salvarEstado(conn);
	}
	
	
	/*
	 * Essa função aqui também
	 * */
	@FXML
	public void carregar() throws SQLException {
		GameStateModel model = new GameStateModel();
		Connection conn = DriverManager.getConnection("jdbc:sqlite:sample.db");
		model.migrar(conn);
		model.carregarEstado(conn);
		Adrian.getInstance().setVidaAtual(GameState.getInstance().getAdrianVida().longValue());
	}
	
	/*
	 * Função de teste que adiciona dinheiro
	 * */
	@FXML
	public void addBufunfa() {
		GameState.getInstance().setBufunfa(GameState.getInstance().getBufunfa().doubleValue() + 5000);
	}
	
	/*
	 * Função com nome temporário que cria uma bufunfa só uma.
	 * */
	@FXML
	public void farmarAura() {
		GameState.getInstance().setBufunfa(GameState.getInstance().getBufunfa().doubleValue() + 1.0);
	}
	
	



}
