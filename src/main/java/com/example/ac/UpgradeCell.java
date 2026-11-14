package com.example.ac;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

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

        controller.configurar(upgrade, () -> {
            if (getListView().getItems().remove(upgrade)) {
                System.out.println("Comprado: " + upgrade.getNome());
            }
        });

        setText(null);
        setGraphic(graphic);
    }
}