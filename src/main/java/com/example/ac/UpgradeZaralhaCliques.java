package com.example.ac;

public class UpgradeZaralhaCliques extends Upgrade{

    private static final double CRESCIMENTO_DO_PRECO = 3;

    public UpgradeZaralhaCliques() {
        super("Dedos furiosos", "+1 cliques por vez",
                "images/clickPerSec1.png",
                800.0,
                null);
    }

    @Override
    public void executar() {
        GameState gs = GameState.getInstance();
        gs.zaralharCliques(getNivel());
        this.setPreco(getPreco() * CRESCIMENTO_DO_PRECO);

        if (getNivel() < 60 & getNivel() >=26) {
            this.setImagem("images/clickPerSec2.png");
        } else if ( getNivel() >=61){
            this.setImagem("images/clickPerSec3.png");
        }

        gs.setCliquesPorClique(1);
    }
}
