package com.example.ac;

/*
 * upgrade multiplicador do clique, cada compra DOBRA o dano do clique
 * é bem mais forte que o de +1 de dano, então o preço cresce muito mais rápido (x4 por compra)
 *
 * falta o ícone, é só salvar em images/cliqueMultIcone.png (enquanto não tiver aparece o quadrado rosa)
 * o nome e a descrição são provisórios
 */
public class UpgradeMultiplicadorClique extends Upgrade {

	// números de balanceamento, static final = constante
	private static final double MULTIPLICADOR_POR_NIVEL = 2;
	private static final double CRESCIMENTO_DO_PRECO = 4;

	// construtor, super manda tudo pro Upgrade (nome, descrição, ícone da loja, preço inicial e sprite)
	// o null é o sprite, ele não aparece na arena, só na loja
	// quem dá new nele é a HelloController no initialize()
	public UpgradeMultiplicadorClique() {
		super("Mouse Gamer",
				"Dobra o dano do clique",
				"images/clickMult1.png",
				500.0,
				null);
	}

	// o efeito da compra, quem chama é o Upgrade.comprar() depois de cobrar
	// multiplica por 2 o multiplicador do clique lá do GameState e deixa a próxima compra 4x mais cara
	// repara que aqui é VEZES e no UpgradeDanoClique é MAIS, por isso esse escala bem mais
	@Override
	public void executar() {
		GameState gs = GameState.getInstance();
		gs.setMultiplicadorClique(gs.getMultiplicadorClique() * MULTIPLICADOR_POR_NIVEL);
		this.setPreco(getPreco() * CRESCIMENTO_DO_PRECO);

		if (getNivel() < 60 & getNivel() >=26) {
			this.setImagem("images/clickMult2.png");
		} else if ( getNivel() >=61){
			this.setImagem("images/clickMult3.png");
		}
	}
}