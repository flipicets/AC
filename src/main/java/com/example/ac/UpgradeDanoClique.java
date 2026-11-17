package com.example.ac;

/*
 * upgrade de aumento base do clique, cada compra dá +1 de dano por clique
 * dá pra comprar quantas vezes quiser e o preço sobe 15% a cada compra
 *
 * dano final do clique = danoClique (esse upgrade) x multiplicadorClique (o UpgradeMultiplicadorClique)
 * a conta fica no GameState.getDanoPorClique()
 *
 * falta o ícone, é só salvar em images/cliqueDanoIcone.png (enquanto não tiver aparece o quadrado rosa)
 */
public class UpgradeDanoClique extends Upgrade {

	// números de balanceamento, static final = constante
	private static final double DANO_POR_NIVEL = 1;
	private static final double CRESCIMENTO_DO_PRECO = 1.15;   // x1.15 = +15%

	// construtor, super manda tudo pro Upgrade (nome, descrição, ícone da loja, preço inicial e sprite)
	// o null é o sprite, ele não aparece na arena, só na loja
	// quem dá new nele é a HelloController no initialize()
	public UpgradeDanoClique() {
		super("AAAIIAIIII IAIAIII QUE DOOOORRRRR",
				"+1 de dano em cada clique",
				"images/clickDano1.png",
				20.0,
				null);
	}

	// o efeito da compra, quem chama é o Upgrade.comprar() depois de cobrar
	// soma +1 no dano base do clique lá do GameState e multiplica o preço da próxima compra por 1.15
	@Override
	public void executar() {
		GameState gs = GameState.getInstance();
		gs.setDanoClique(gs.getDanoClique() + DANO_POR_NIVEL);
		this.setPreco(getPreco() * CRESCIMENTO_DO_PRECO);

		if (getNivel()< 50 & getNivel() >=25) {//imagem começa na mais básica devido ao construtor
			this.setImagem("images/clickDano2.png");
		}else if ( getNivel() >=51){
			this.setImagem("images/clickDano3.png");
		}
	}
}