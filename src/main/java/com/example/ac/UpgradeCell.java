package com.example.ac;

// erro de arquivo, pode acontecer se o fxml não for encontrado
import java.io.IOException;

// tudo do JavaFX
// FXMLLoader = lê o .fxml e monta o visual, Node = qualquer pedaço de tela, ListCell = uma linha de um ListView
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

/*
 * é UMA linha da loja (ícone, nome, descrição, preço, botão Comprar)
 * o construtor carrega o listcell.fxml e pra cada upgrade que entra na lista ele configura a linha
 * as duas abas (Loja e Melhorias) usam essa mesma célula
 *
 * herda de ListCell<Upgrade> do JavaFX, o <Upgrade> diz que o item de cada linha é um Upgrade
 * quem dá new é o setCellFactory lá no initialize() da HelloController
 */

public class UpgradeCell extends ListCell<Upgrade> {

    // o visual da linha já montado (o que veio do fxml)
    private final Node graphic;
    // a controller da linha, é ela que preenche os labels e cuida do botão
    private final UpgradeCellController controller;

    // construtor, carrega o fxml da linha e guarda o visual e a controller dele
    public UpgradeCell() {
        try {
            // aponta pro arquivo, getResource procura na pasta resources
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("listcell.fxml")
            );

            // load() monta o visual e já cria a UpgradeCellController
            graphic = loader.load();
            controller = loader.getController();

        } catch (IOException e) {
            // sem o listcell.fxml não tem como montar a loja, então não tem o que "tratar"
            // avisa no console e relança como RuntimeException pra o erro subir e o jogo parar
            System.out.println("Não consegui carregar o listcell.fxml");
            throw new RuntimeException(e);
        }
    }

    // o JavaFX chama sozinho toda vez que precisa mostrar um item numa linha (inclusive rolando a lista)
    // a gente sobrescreve pra dizer COMO mostrar, empty = true quando a linha tá vazia
    @Override
    protected void updateItem(Upgrade upgrade, boolean empty) {
        // obrigatório chamar o da mãe primeiro, o JavaFX precisa disso pra funcionar direito
        super.updateItem(upgrade, empty);
        // linha vazia, não mostra nada
        if (empty || upgrade == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        // manda a controller preencher a linha com os dados desse upgrade
        controller.configurar(upgrade);

        // sem texto solto, o que aparece é o visual do fxml
        setText(null);
        setGraphic(graphic);
    }
}