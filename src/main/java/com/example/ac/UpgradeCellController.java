
package com.example.ac;

import java.io.File;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/*
 * Essa é a controladora de cada célula no listview de upgrades
 * aqui também não tem muito o que mudar, a não ser que o fxml seja
 * alterado, caso surja alguma nova ideia de implementação, peço que
 * pergunte, ou pra mim, ou pro Fellipe como alterar isso aqui :D
 * */
public class UpgradeCellController {

    @FXML private ImageView imagem;
    @FXML private Label nome;
    @FXML private Label descricao;
    @FXML private Label preco;

    private Upgrade upgrade;
    
    public void configurar(Upgrade upgrade) {
        nome.setText(upgrade.getNome());
        descricao.setText(upgrade.getDescricao());
        imagem.setImage(new Image(
        	    getClass().getResource(upgrade.getImagem()).toExternalForm(),
        	    64,
        	    64,
        	    false,
        	    false
        	));
        preco.textProperty().bind(upgrade.precoProperty().asString());

        this.upgrade = upgrade;
    }

    @FXML
    private void comprar() {
        if (this.upgrade != null) {
            this.upgrade.executar();
        }
    }
}
