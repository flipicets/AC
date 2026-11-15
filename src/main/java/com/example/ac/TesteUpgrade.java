package com.example.ac;

public class TesteUpgrade extends Upgrade{
	/*
	 * TODO: Trocar para implementação usando herança.
	 * 
	 * Para por enquanto para criar um upgrade novo, ele deve seguir esta estrutura obrigatoriamente
	 * utilizando lambda (função anônima) por não se tratar de uma função padrão
	 * siga este exemplo para implementar um upgrade novo,
	 * 
	 * não se esqueça de adicionar ele na controller
	 * */
	public TesteUpgrade() {
		super("Link furry",
			  "furry é tudo rico pqp mais dinero",
			  "images/link_pateta.png",
			  15.0);
		super.acao = (u) -> {
			GameState gs = GameState.getInstance();
			if(gs.getBufunfa().doubleValue() >= u.getPreco()) {
				gs.setBufunfaMultiplier(gs.getBufunfaMultiplier() + 0.2);
				gs.setBufunfa(gs.getBufunfa().doubleValue() - u.preco.doubleValue());
				super.setPreco(getPreco() + 6);
			} else {
				System.out.println("Não há money");
			}
		};
	}
}
