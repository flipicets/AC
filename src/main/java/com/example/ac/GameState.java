package com.example.ac;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

public class GameState {
	public DoubleProperty bufunfa;
	public LongProperty adrianVida;
	public double bufunfaMultiplier = 0.0;
	public double passiveDamage = 0.0;
	
	private static GameState gs = new GameState(0);
	
	private GameState(double bufunfa) {
		this.bufunfa = new SimpleDoubleProperty(bufunfa);
		this.adrianVida = new SimpleLongProperty(Adrian.getInstance().getVidaMax());
	}
	
	public static GameState getInstance() {
		return gs;
	}

	public DoubleProperty getBufunfa() {
		return bufunfa;
	}

	public void setBufunfa(Double bufunfa) {
		this.bufunfa.set(bufunfa);
	}

	public LongProperty getAdrianVida() {
		return adrianVida;
	}

	public void setAdrianVida(long adrianDanado) {
		this.adrianVida.set(adrianDanado);
	}

	public double getBufunfaMultiplier() {
		return bufunfaMultiplier;
	}

	public void setBufunfaMultiplier(double bufunfaMultiplier) {
		this.bufunfaMultiplier = bufunfaMultiplier;
	}
	
	
	
	
	
}
