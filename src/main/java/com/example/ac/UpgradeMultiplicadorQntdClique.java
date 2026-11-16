package com.example.ac;

public class UpgradeMultiplicadorQntdClique extends Upgrade{

    private static final double CRESCIMENTO_DO_PRECO = 7;


    public UpgradeMultiplicadorQntdClique() {
        super("Dedo de Midas",
                "Dobra a quantidade possuida de cliques",
                "images/placeholder.png",
                3000.0,
                null);
    }
    @Override
    public void executar(){
        GameState gs = GameState.getInstance();
        gs.setDobrarMultiplicador(2);
        this.setPreco(getPreco() * CRESCIMENTO_DO_PRECO);
    }
}
