package com.example.ac;

// o * importa todas as Property do JavaFX de uma vez (Double, Integer, Boolean e as ReadOnly)
// Property = variável que avisa a tela quando muda (é o que faz o bind funcionar)
import javafx.beans.property.*;
import javafx.scene.image.Image;

/*
 * classe mãe de TUDO que aparece na loja, guarda o que todo upgrade tem (nome, descrição,
 * ícone, sprite, preço, nível) e a regra de compra que é igual pra todos
 *
 * é abstract pq não existe "um upgrade genérico" no jogo, ninguém pode dar new Upgrade()
 * e o executar() é abstract pq a mãe não sabe o efeito de cada um, então obriga cada filha a escrever o seu
 *
 * antes cada filha repetia o if de bufunfa dentro do executar() e os 4 personagens nem descontavam
 * (dava pra comprar de graça), agora a compra é o comprar() daqui e o executar() ficou só com o efeito
 */
public abstract class Upgrade {

    // nível máximo de quem pode ser comprado infinitas vezes
    // Integer.MAX_VALUE = o maior int que existe (uns 2 bilhões), na prática é "sem limite"
    public static final int SEM_LIMITE = Integer.MAX_VALUE;

    // protected = essa classe e as filhas enxergam
    protected String nome;
    protected String descricao;
    protected String imagem;       // caminho do ícone da loja
    protected Renderable sprite;   // desenho na arena (null = não aparece na arena)

    // é Property pro preço na loja atualizar sozinho quando muda
    protected DoubleProperty preco;

    // guardados pra conseguir voltar ao estado inicial no resetar()
    private final double precoInicial;
    private final Renderable spriteInicial;

    // nível = quantas vezes foi comprado, Property pra loja mostrar "Nv. 3" sozinha
    private final IntegerProperty nivel = new SimpleIntegerProperty(0);
    private final int nivelMaximo;

    // true depois da primeira compra, Property pq a HelloController fica escutando
    // pra colocar o personagem na arena e as melhorias na aba
    private final BooleanProperty comprado =
            new SimpleBooleanProperty(false);

    // ícone da loja, carregado uma vez só na primeira vez que pedirem
    private Image icone;

    // construtor original, upgrade que pode ser comprado sem limite
    // o this(...) chama o OUTRO construtor daqui de baixo passando SEM_LIMITE (dois construtores = sobrecarga)
    // quem usa são TesteUpgrade, AutoDamager e os dois de clique
    public Upgrade(
            String nome,
            String descricao,
            String imagem,
            double preco,
            Renderable sprite
    ) {
        this(nome, descricao, imagem, preco, sprite, SEM_LIMITE);
    }

    // construtor com nível máximo, quem usa são o AutoClicker (passa 1) e a MelhoriaAutoClicker
    public Upgrade(
            String nome,
            String descricao,
            String imagem,
            double preco,
            Renderable sprite,
            int nivelMaximo
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
        this.preco = new SimpleDoubleProperty(preco);
        this.sprite = sprite;
        this.precoInicial = preco;
        this.spriteInicial = sprite;
        this.nivelMaximo = nivelMaximo;
    }

    // o efeito do upgrade, cada filha escreve o seu
    // aqui dentro NAO se cobra bufunfa nem confere saldo, isso é trabalho do comprar()
    public abstract void executar();

    // a compra de verdade, quem chama é o botão Comprar da loja (UpgradeCellController)
    // é final pra nenhuma filha conseguir sobrescrever e mudar a regra de compra
    // "throws" = avisa que pode lançar essas duas exceções, quem trata é o UpgradeCellController
    public final void comprar() throws BufunfaInsuficienteException, NivelMaximoException {
        // 1) já tá no máximo? lança a exceção e para aqui (é isso que impede o segundo fefo)
        if (estaNoNivelMaximo()) {
            throw new NivelMaximoException(nome);
        }

        // 2) cobra, se não tiver saldo o gastarBufunfa lança a exceção dele
        // e a compra para aqui mesmo sem subir nível nem aplicar efeito
        GameState.getInstance().gastarBufunfa(getPreco());

        // 3) deu tudo certo, sobe o nível e aplica o efeito
        aplicarNivel();
    }

    // usado ao CARREGAR um save, quem chama é o SaveController.carregar()
    // reaplica o upgrade "niveis" vezes sem cobrar nada
    // como o executar() sempre faz a mesma coisa, repetir N vezes deixa efeito e preço iguais a como tavam
    public void restaurar(int niveis) {
        // repete enquanto não chegou no número salvo E não bateu no nível máximo
        for (int i = 0; i < niveis && !estaNoNivelMaximo(); i++) {
            aplicarNivel();
        }
    }

    // a parte em comum do comprar() e do restaurar(), por isso é private
    // sobe o nível, roda o efeito da filha (polimorfismo) e marca como comprado
    private void aplicarNivel() {
        nivel.set(nivel.get() + 1);
        executar();
        registrarCompra();
    }

    // volta o upgrade pro estado de "nunca comprado", quem chama é o SaveController.resetarJogo()
    // quem tem atributo próprio (AutoClicker e os personagens) sobrescreve e chama super.resetar() primeiro
    public void resetar() {
        nivel.set(0);
        preco.set(precoInicial);
        sprite = spriteInicial;
        comprado.set(false);
    }

    // marca como comprado, é esse set(true) que dispara o listener da HelloController
    protected void registrarCompra() {
        comprado.set(true);
    }

    // INFORMAÇÕES QUE O RENDERER DESENHA
    // aqui os 3 devolvem "nada" e o AutoClicker sobrescreve, assim o Renderer não precisa de instanceof

    // de 0 a 1 = barrinha de carga, negativo = não desenha barrinha
    public double getProgresso() {
        return -1;
    }

    // texto pequeno embaixo do sprite, vazio por padrão
    public String getTextoStatus() {
        return "";
    }

    // texto do último ataque pra subir na tela ("-150"), null = não atacou
    public String pegarTextoDeAtaque() {
        return null;
    }

    // GETTERS / SETTERS

    // devolve a Property do comprado pra HelloController escutar
    // ReadOnly = quem pega só consegue LER, não consegue dar set (encapsulamento)
    public ReadOnlyBooleanProperty compradoProperty() {
        return comprado;
    }

    // true se já foi comprado, o AutoClicker.atualizar() usa pra saber se ataca
    public boolean isComprado() {
        return comprado.get();
    }

    // quantas vezes foi comprado, usado nos textos de status e no save
    public int getNivel() {
        return nivel.get();
    }

    // Property do nível (só leitura), o UpgradeCellController usa nos binds da loja
    public ReadOnlyIntegerProperty nivelProperty() {
        return nivel;
    }

    // usado pelo UpgradeCellController pra mostrar "Nv. 2/10" e desabilitar o botão
    public int getNivelMaximo() {
        return nivelMaximo;
    }

    // true se não dá mais pra comprar, usado no comprar(), no restaurar() e na loja
    public boolean estaNoNivelMaximo() {
        return nivel.get() >= nivelMaximo;
    }

    // preço atual como número
    public double getPreco() {
        return preco.get();
    }

    // troca o preço, as filhas usam no executar() pra encarecer a próxima compra
    public void setPreco(double valor) {
        preco.set(valor);
    }

    // Property do preço, o UpgradeCellController usa no bind do label de custo
    public DoubleProperty precoProperty() {
        return preco;
    }

    // os 2 de baixo a loja (UpgradeCellController) usa, e o nome o Renderer tb

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    // caminho do ícone, a MelhoriaAutoClicker usa pra pegar o mesmo ícone do personagem dono
    public String getImagem() {
        return imagem;
    }

    // ícone da loja já carregado, o UpgradeCellController usa
    // só carrega na primeira vez (quando ainda é null) e depois reaproveita
    // antes dava new Image no PNG gigante toda vez que a lista redesenhava
    public Image getIcone() {
        if (icone == null) {
            icone = Renderable.carregarImagem(imagem, 64, 64);
        }
        return icone;
    }

    // o desenho da arena, o Renderer usa pra desenhar
    public Renderable getSprite() {
        return sprite;
    }

    // troca o desenho da arena, o furúnculo usa quando nasce mais um
    public void setSprite(Renderable sprite) {
        this.sprite = sprite;
    }

    public void setImagem(String imagem){
        this.imagem = imagem;
    }

}