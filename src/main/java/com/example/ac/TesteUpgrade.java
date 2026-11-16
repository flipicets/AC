package com.example.ac;

/*
 * esse é o Link furry, renda passiva, cada compra dá +0.2 de bufunfa por segundo
 * e dá pra comprar quantas vezes quiser, é o irmão do Cesar Pateta (AutoDamager) só que dá dinheiro em vez de dano
 */
public class TesteUpgrade extends Upgrade{

	// números de balanceamento, static final = constante
	private static final double BUFUNFA_POR_SEGUNDO = 0.2;
	private static final double AUMENTO_DE_PRECO = 6;

	// construtor, super manda tudo pro Upgrade (nome, descrição, ícone da loja, preço inicial e o sprite da arena)
	// quem dá new nele é a HelloController no initialize()
	public TesteUpgrade() {
		super("Link furry",
				"furry é tudo rico pqp mais dinero",
				"images/link_pateta.png",
				15.0,
				new Renderable("images/link_pateta-nobg.png", 130, 130));
	}

	// o efeito da compra, quem chama é o Upgrade.comprar() depois de cobrar
	// soma +0.2 na renda por segundo lá do GameState e deixa a próxima compra 6 mais cara
	// ele não dá a bufunfa aqui, quem paga é o GameState.atualizarPassivos() no loop do jogo
	@Override
	public void executar(){
		GameState gs = GameState.getInstance();
		gs.setBufunfaMultiplier(gs.getBufunfaMultiplier() + BUFUNFA_POR_SEGUNDO);
		this.setPreco(getPreco() + AUMENTO_DE_PRECO);
	}

	// textinho que aparece embaixo do sprite dele na arena, tipo "+0.6 bufunfa/s"
	// renda por segundo x quantas vezes comprou (getNivel), quem usa é o Renderer
	@Override
	public String getTextoStatus() {
		return "+" + Formatador.numero(BUFUNFA_POR_SEGUNDO * getNivel()) + " bufunfa/s";
	}
}