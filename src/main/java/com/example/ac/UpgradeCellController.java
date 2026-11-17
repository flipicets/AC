
package com.example.ac;

// tudo do JavaFX
// Bindings = monta os binds, @FXML = liga a variável/método com o arquivo fxml
// o resto são os componentes da linha (botão, label, imagem)
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/*
 * controller de cada linha da loja (listcell.fxml), preenche o ícone, nome, descrição, preço e nível
 * e cuida do botão Comprar
 *
 * é AQUI que as nossas duas exceções são tratadas, o throw nasce lá no GameState / Upgrade
 * e o try/catch fica aqui pq é quem sabe falar com o jogador
 *
 * sobre o Bindings.createStringBinding, lê assim "o texto do label é o resultado dessa função,
 * e refaz a conta sempre que a property do final mudar"
 */
public class UpgradeCellController {

    // componentes da linha, o JavaFX preenche sozinho procurando o fx:id com o mesmo nome no listcell.fxml
    @FXML private ImageView imagem;
    @FXML private Label nome;
    @FXML private Label descricao;
    @FXML private Label preco;
    @FXML private Label nivel;
    @FXML private Button botaoComprar;

    // o upgrade que essa linha tá mostrando agora
    private Upgrade upgrade;

    // preenche a linha com os dados de um upgrade, quem chama é o UpgradeCell.updateItem()
    public void configurar(Upgrade upgrade) {
        this.upgrade = upgrade;

        // esses 3 não mudam durante o jogo então é setText normal, sem bind
        nome.setText(upgrade.getNome());
        descricao.setText(upgrade.getDescricao());
        imagem.setImage(upgrade.getIcone());

        // preço, mostra "-" se já tá no máximo senão o preço formatado (1,50 K)
        // "() ->" é lambda (função sem nome), "condição ? a : b" é if curtinho (ternário)
        // refaz quando o preço OU o nível mudar
        preco.textProperty().bind(Bindings.createStringBinding(
                () -> upgrade.estaNoNivelMaximo() ? "-" : Formatador.numero(upgrade.getPreco()),
                upgrade.precoProperty(), upgrade.nivelProperty()
        ));

        // texto do nível, refaz quando o nível mudar
        nivel.textProperty().bind(Bindings.createStringBinding(
                () -> textoDoNivel(upgrade),
                upgrade.nivelProperty()
        ));

        // texto do botão, vira "MÁX" quando chega no nível máximo
        botaoComprar.textProperty().bind(Bindings.createStringBinding(
                () -> upgrade.estaNoNivelMaximo() ? "MÁX" : "Comprar",
                upgrade.nivelProperty()
        ));

        // desabilita o botão (fica cinza) quando nível >= nível máximo
        // greaterThanOrEqualTo = "maior ou igual a", é o >= em forma de bind
        botaoComprar.disableProperty().bind(
                upgrade.nivelProperty().greaterThanOrEqualTo(upgrade.getNivelMaximo())
        );
    }

    // monta o texto do nível, é private pq só o configurar() daqui usa
    private String textoDoNivel(Upgrade upgrade) {
        // quem só pode ter 1 (os auto clickers) mostra "Comprado" em vez de "Nv. 1/1"
        if (upgrade.getNivelMaximo() == 1) {
            return upgrade.getNivel() > 0 ? "Comprado" : "";
        }
        // sem limite mostra só "Nv. 3"
        if (upgrade.getNivelMaximo() == Upgrade.SEM_LIMITE) {
            return "Nv. " + upgrade.getNivel();
        }
        // com limite mostra "Nv. 3/10"
        return "Nv. " + upgrade.getNivel() + "/" + upgrade.getNivelMaximo();
    }

    // clique no botão Comprar, quem chama é o onAction="#comprar" lá no listcell.fxml
    @FXML
    private void comprar() {
        // segurança, linha sem upgrade não faz nada
        if (this.upgrade == null) {
            return;
        }

        GameState gs = GameState.getInstance();

        // try = tenta comprar, se o comprar() lançar exceção pula direto pro catch certo
        try {
            this.upgrade.comprar();
            // só chega nessa linha se a compra deu certo
            gs.setMensagem("Comprou: " + upgrade.getNome());

        } catch (BufunfaInsuficienteException e) {
            // faltou bufunfa, a mensagem já vem pronta da exceção ("Não há money: faltam X")
            // setMensagem muda o aviso amarelo lá embaixo da tela
            gs.setMensagem(e.getMessage());

        } catch (NivelMaximoException e) {
            // já tava no nível máximo
            gs.setMensagem(e.getMessage());
        }
    }
}