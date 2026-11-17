package com.example.ac;

public class UpgradeMultiplicadorQntdClique extends Upgrade{

    private static final double CRESCIMENTO_DO_PRECO = 7;


    public UpgradeMultiplicadorQntdClique() {
        super("Dedo de Midas",
                "Dobra a quantidade possuida de cliques",
                "images/toqueDeMidas.png",
                3000.0,
                null);
    }
    @Override
    public void executar(){
        GameState gs = GameState.getInstance();
        gs.setDobrarMultiplicador(2);
        this.setPreco(getPreco() * CRESCIMENTO_DO_PRECO);

        if (getNivel() < 20 & getNivel() >=6) {
            this.setImagem("images/clickPerSec2.png");
        } else if ( getNivel() >=21){
            this.setImagem("images/clickPerSec3.png");
        }
    }
}
