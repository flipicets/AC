package com.example.ac;

/*
 * esse é o Cesar Pateta, dano passivo simples, cada compra dá +1 de dano por segundo no adrian
 * e dá pra comprar quantas vezes quiser
 *
 * NAO CONFUNDIR com AutoClicker, aqueles são os 4 personagens únicos, esse aqui é só um
 * upgrade normal que herda direto de Upgrade
 *
 * o TODO antigo de trocar pra herança já foi, e o if de bufunfa saiu daqui e foi pro Upgrade.comprar()
 * SE ALGUEM CRIAR UM UPGRADE NOVO NESSE MOLDE PFV não esquece de registrar na HelloController
 */
public class AutoDamager extends Upgrade{

	// números de balanceamento, static final = constante
	private static final double DANO_POR_SEGUNDO = 1;
	private static final double AUMENTO_DE_PRECO = 13;

	// construtor, super manda tudo pro Upgrade (nome, descrição, ícone da loja, preço inicial e o sprite da arena)
	// Renderable é a nossa classe de sprite, os 130 são o tamanho da caixa onde a imagem cabe
	// quem dá new nele é a HelloController no initialize()
	public AutoDamager() {
		super("Cesar Pateta",
				"kkkkk esse cesar kkkk vai tomando adrian",
				"images/cesar_pateta.jpg",
				10.0,
				new Renderable("images/cesar_pateta-nobg.png", 130, 130));
	}

	// o efeito da compra, quem chama é o Upgrade.comprar() depois de cobrar
	// soma +1 no dano passivo lá do GameState e deixa a próxima compra 13 mais cara
	// ele não bate no adrian aqui, quem aplica o dano é o GameState.atualizarPassivos() no loop do jogo
	@Override
	public void executar() {
		GameState gs = GameState.getInstance();
		gs.setPassiveDamage(gs.getPassiveDamage() + DANO_POR_SEGUNDO);
		this.setPreco(getPreco() + AUMENTO_DE_PRECO);
	}

	// textinho que aparece embaixo do sprite dele na arena, tipo "3 dano/s"
	// dano por segundo x quantas vezes comprou (getNivel), quem usa é o Renderer
	@Override
	public String getTextoStatus() {
		return Formatador.numero(DANO_POR_SEGUNDO * getNivel()) + " dano/s";
	}
}