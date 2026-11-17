package com.example.ac;

public class UpgradeZaralhaCliques extends Upgrade{

    private static final double CRESCIMENTO_DO_PRECO = 9;

    public UpgradeZaralhaCliques() {
        super("Zaralha", "+1 cliques por vez",
                "images/clickPerSec1.png",
                800.0,
                null);
    }

    @Override
    public void executar() {
        GameState gs = GameState.getInstance();
        gs.zaralharCliques(getNivel());
        this.setPreco(getPreco() * CRESCIMENTO_DO_PRECO);

        if (getNivel() < 20 & getNivel() >=6) {
            this.setImagem("images/clickPerSec2.png");
        } else if ( getNivel() >=21){
            this.setImagem("images/clickPerSec3.png");
        }
        gs.setCliquesPorClique(1);
    }
}
