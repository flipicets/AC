package com.example.ac;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

/*
 * Isso é uma implementação para que a lista de upgrades seja interessante
 * 
 * basicamente o construtor desse cara vai carregar o fxml da celula
 * vai transformar o fxml em um nodo que pode ser utilizado e vai,
 * para cada item que entrar na lista configurar ele para aparecer na UI
 * aqui não tem muito o que mudar a não, acho que tá bem safe
 * */

public class UpgradeCell extends ListCell<Upgrade> {

    private final Node graphic;
    private final UpgradeCellController controller;

    public UpgradeCell() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("listcell.fxml")
            );

            graphic = loader.load();
            controller = loader.getController();

        } catch (IOException e) {
        	System.out.println("fodeu");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Upgrade upgrade, boolean empty) {
    	super.updateItem(upgrade, empty);
        if (empty || upgrade == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        controller.configurar(upgrade);

        setText(null);
        setGraphic(graphic);
    }
}