package com.example.ac;

public class TesteUpgrade extends Upgrade{
	/*
	 * não se esqueça de adicionar o upgrade na controller
	 * */
	public TesteUpgrade() {
		super("Link furry",
			  "furry é tudo rico pqp mais dinero",
			  "images/link_pateta.png",
			  15.0);
	}
	
	@Override
	public void executar(){
		GameState gs = GameState.getInstance();
		if(gs.getBufunfa().doubleValue() >= this.getPreco()) {
			gs.setBufunfaMultiplier(gs.getBufunfaMultiplier() + 0.2);
			gs.setBufunfa(gs.getBufunfa().doubleValue() - this.preco.doubleValue());
			this.setPreco(getPreco() + 6);
		} else {
			System.out.println("Não há money");
		}
	};
}
