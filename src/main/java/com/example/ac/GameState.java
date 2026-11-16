package com.example.ac;

// tudo do JavaFX, Property = variável que avisa a tela quando muda (é o que faz o bind funcionar)
// Double pra número quebrado, Long pra inteiro grande, String pra texto, os "Simple" são os que a gente dá new
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * o "cofre" do jogo, guarda todos os números globais da partida (bufunfa, dano do clique,
 * renda passiva, dano passivo) e é por aqui que passa TODO dano e TODO gasto de bufunfa
 *
 * é Singleton que nem o Adrian, só existe um e todo mundo usa GameState.getInstance()
 *
 * antes os atributos eram public e qualquer classe mexia direto (gs.passiveDamage += 1)
 * agora é tudo private com get/set e as regras ficam nos métodos daqui (encapsulamento)
 *
 * regra da economia, todo dano causado no adrian vira bufunfa, 1 de dano = 1 de bufunfa
 */
public class GameState {

	// quanto de bufunfa cada 1 de dano rende, static final = constante
	public static final double BUFUNFA_POR_DANO = 1.0;

	// o dinheiro, é Property pra tela (o número amarelo) acompanhar sozinha
	private final DoubleProperty bufunfa;

	private double bufunfaMultiplier = 0.0;   // bufunfa ganha por segundo (Link furry)
	private double passiveDamage = 0.0;       // dano por segundo (Cesar Pateta)

	private double danoClique = 1.0;          // dano base de cada clique
	private double multiplicadorClique = 1.0; // multiplica o dano base

	// a vida do adrian é inteira (long) mas o dano pode vir quebrado, tipo 0,1 por passo do loop
	// se arredondasse cada pedacinho o 0,1 virava 0 e o dano sumia
	// então o que sobra depois da vírgula fica guardado aqui e entra junto no próximo dano
	private double restoDano = 0.0;

	// texto de aviso lá embaixo na tela ("Não há money", "Jogo salvo"...), Property pra tela acompanhar
	private final StringProperty mensagem = new SimpleStringProperty("");

	// o único GameState do programa (static = pertence à classe), começa com 0 de bufunfa
	private static GameState gs = new GameState(0);

	// construtor private, impede new GameState() fora daqui
	private GameState(double bufunfa) {
		this.bufunfa = new SimpleDoubleProperty(bufunfa);
	}

	private int cliquesPorClique= 1;


	public void setCliquesPorClique(int valor){
		this.cliquesPorClique += 1;
	}

	public int getCliquesPorClique(){
		return this.cliquesPorClique;
	}
	// devolve o único GameState, quase toda classe do projeto usa
	public static GameState getInstance() {
		return gs;
	}

	// BUFUNFA

	// retorna a Property da bufunfa (a caixinha inteira, não só o número)
	// a HelloController usa no bind do label e o GameStateModel usa pra salvar
	public DoubleProperty getBufunfa() {
		return bufunfa;
	}

	// define a bufunfa num valor, Math.max = o maior dos dois, não deixa ficar negativa
	// quem usa é o GameStateModel.carregarEstado() ao carregar o save
	public void setBufunfa(Double bufunfa) {
		this.bufunfa.set(Math.max(0, bufunfa));
	}

	// soma bufunfa (ignora valor zero ou negativo)
	// quem usa é o causarDano e o atualizarPassivos daqui e o botão +5000 da HelloController
	public void adicionarBufunfa(double valor) {
		if (valor > 0) {
			bufunfa.set(bufunfa.get() + valor);
		}
	}

	// único jeito de gastar bufunfa no jogo, quem chama é o Upgrade.comprar()
	// se não tiver saldo lança a nossa exceção e NÃO desconta nada
	// "throws" na assinatura = avisa que esse método pode lançar, "throw" = lança de fato
	public void gastarBufunfa(double preco) throws BufunfaInsuficienteException {
		if (bufunfa.get() < preco) {
			throw new BufunfaInsuficienteException(preco, bufunfa.get());
		}
		bufunfa.set(bufunfa.get() - preco);
	}

	// DANO

	// TODO dano do jogo passa por aqui, tira a vida do adrian e paga a bufunfa
	// quem chama é o clicar() e o atualizarPassivos() daqui e o AutoClicker.causarDano() (os 4 personagens)
	public void causarDano(double dano) {
		if (dano <= 0) {
			return;
		}

		// junta com o que tinha sobrado da última vez
		restoDano += dano;
		// (long) na frente = cast, corta tudo depois da vírgula (2.7 vira 2)
		long danoInteiro = (long) restoDano;
		// guarda o que sobrou (o 0.7) pra próxima
		restoDano -= danoInteiro;

		// manda pro Adrian e ele devolve quanto entrou de verdade
		long danoReal = Adrian.getInstance().receberDano(danoInteiro);
		// o dano que entrou vira bufunfa
		adicionarBufunfa(danoReal * BUFUNFA_POR_DANO);
	}

	// clique do jogador no adrian, quem chama é a HelloController.farmarAura()
	// devolve o dano pra tela poder mostrar o número subindo
	public double clicar() {
		double dano = getDanoPorClique();
		causarDano(dano);
		return dano;
	}

	public void zaralharCliques(int vezes){
		for(int i = 0; i < vezes; i++){
			clicar();
		}
	}

	// dano final do clique = base x multiplicador
	// usado no clicar() e no label "Dano por clique" da HelloController
	public double getDanoPorClique() {
		return danoClique * multiplicadorClique;
	}

	// renda do Link furry e dano do Cesar Pateta, quem chama é o tick() da HelloController 10x por segundo
	// dt = quanto tempo passou desde a última chamada em segundos (0.1)
	// multiplica por dt pq os valores são POR SEGUNDO, então em cada passo entra só 1/10 deles
	public void atualizarPassivos(double dt) {
		adicionarBufunfa(bufunfaMultiplier * dt);
		causarDano(passiveDamage * dt);
	}

	// volta tudo pro começo e manda o Adrian encher a vida tb
	// quem chama é o SaveController.resetarJogo() (Novo jogo e Carregar)
	public void resetar() {
		bufunfa.set(0);
		bufunfaMultiplier = 0.0;
		passiveDamage = 0.0;
		danoClique = 1.0;
		multiplicadorClique = 1.0;
		cliquesPorClique = 1;
		restoDano = 0.0;
		Adrian.getInstance().resetar();
	}

	// VIDA DO ADRIAN (ela mora no Adrian, esses dois só repassam)

	// retorna a Property da vida lá do Adrian, o GameStateModel usa pra salvar
	public LongProperty getAdrianVida() {
		return Adrian.getInstance().vidaAtualProperty();
	}

	// define a vida do adrian, o GameStateModel usa ao carregar o save
	public void setAdrianVida(long adrianDanado) {
		Adrian.getInstance().setVidaAtual(adrianDanado);
	}

	// GETTERS / SETTERS
	// em todos os set o Math.max é a trava, não deixa o valor ficar abaixo do mínimo (0 ou 1)

	// bufunfa por segundo, quem lê e muda é o TesteUpgrade (Link furry)
	public double getBufunfaMultiplier() {
		return bufunfaMultiplier;
	}

	public void setBufunfaMultiplier(double bufunfaMultiplier) {
		this.bufunfaMultiplier = Math.max(0, bufunfaMultiplier);
	}

	// dano por segundo, quem lê e muda é o AutoDamager (Cesar Pateta)
	public double getPassiveDamage() {
		return passiveDamage;
	}

	public void setPassiveDamage(double passiveDamage) {
		this.passiveDamage = Math.max(0, passiveDamage);
	}

	// dano base do clique, quem lê e muda é o UpgradeDanoClique
	public double getDanoClique() {
		return danoClique;
	}

	public void setDanoClique(double danoClique) {
		this.danoClique = Math.max(1, danoClique);
	}

	// multiplicador do clique, quem lê e muda é o UpgradeMultiplicadorClique
	public double getMultiplicadorClique() {
		return multiplicadorClique;
	}

	public void setMultiplicadorClique(double multiplicadorClique) {
		this.multiplicadorClique = Math.max(1, multiplicadorClique);
	}

	public void setDobrarMultiplicador(int nivel){
		this.multiplicadorClique *= nivel;
	}


	// retorna a Property da mensagem, a HelloController usa no bind do label de aviso
	public StringProperty mensagemProperty() {
		return mensagem;
	}

	// troca o texto de aviso da tela
	// quem usa é o UpgradeCellController (compra e erro de compra) e a HelloController (salvar, carregar etc)
	public void setMensagem(String texto) {
		mensagem.set(texto);
	}

}