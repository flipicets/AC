
package com.example.ac;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UpgradeCellController {

    @FXML private ImageView imagem;
    @FXML private Label nome;
    @FXML private Label descricao;

    private Runnable acao;

    public void configurar(Upgrade upgrade, Runnable acao) {
        nome.setText(upgrade.getNome());
        descricao.setText(upgrade.getDescricao());
        imagem.setImage(new Image(
                new File(upgrade.getImagem()).toURI().toString(),
                64,
                64,
                false,
                false
            )
        );

        this.acao = acao;
    }

    @FXML
    private void comprar() {
        if (acao != null) {
            acao.run();
        }
    }
}
