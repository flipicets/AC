package com.example.ac;

public class AutoDamager extends Upgrade{
	/*
	 * TODO: Trocar para implementação usando herança.
	 * 
	 * Para por enquanto para criar um upgrade novo, ele deve seguir esta estrutura obrigatoriamente
	 * utilizando lambda (função anônima) por não se tratar de uma função padrão
	 * siga este exemplo para implementar um upgrade novo,
	 * 
	 * não se esqueça de adicionar ele na controller
	 * */
	public AutoDamager() {
		super("Cesar Pateta", 
			  "kkkkk esse cesar kkkk vai tomando adrian",
			  "images/cesar_pateta.jpg",
			  10.0,
			  new Renderable("images/cesar_pateta-nobg.png", 300,300));
	}
	
	@Override
	public void executar() {
		GameState gs = GameState.getInstance();
		if(gs.getBufunfa().doubleValue() >= this.getPreco()) {
			gs.passiveDamage += 1;
			gs.setBufunfa(gs.getBufunfa().doubleValue() - this.preco.doubleValue());
			this.setPreco(getPreco() + 13);
			registrarCompra();
		} else {
			System.out.println("Não há money");
		}
	};
}
