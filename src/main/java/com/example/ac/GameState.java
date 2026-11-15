package com.example.ac;

public class GameState {
	public double bufunfa;
	public long adrianVida;
	
	private static GameState gs = new GameState(0);
	
	private GameState(double bufunfa) {
		this.bufunfa = bufunfa;
		this.adrianVida = Adrian.getInstance().getVidaAtual();
	}
	
	public static GameState getInstance() {
		return gs;
	}

	public double getBufunfa() {
		return bufunfa;
	}

	public void setBufunfa(double bufunfa) {
		this.bufunfa = bufunfa;
	}

	public long getAdrianVida() {
		return adrianVida;
	}

	public void setAdrianVida(long adrianDanado) {
		this.adrianVida = adrianDanado;
	}
	
	
	
	
}
