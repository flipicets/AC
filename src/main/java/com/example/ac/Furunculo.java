package com.example.ac;

/*
 * Furúnculos, o contrário do fefo, ficam carregando vários segundos e soltam um golpe grande de uma vez
 * o dano é o dano base x a quantidade de furúnculos (dá pra ter até 3)
 *
 * não tem melhoria de velocidade de propósito pq nele velocidade e recarga seriam a mesma coisa
 *
 * da versão antiga ficou o qntdFurunculos, só que antes o dano nunca recebia valor (ficava 0 e não batia nada)
 */
public class Furunculo extends AutoClicker{

    // números de balanceamento (valores iniciais), static final = constante
    private static final double PRECO = 100;
    private static final double DANO_POR_FURUNCULO = 60;
    private static final double SEGUNDOS_DE_CARGA = 10;
    private static final int MAXIMO_DE_FURUNCULOS = 3;

    // quantos furúnculos tem agora, começa com 1 e sobe com a melhoria especial
    private int qntdFurunculos = 1;

    // construtor, quem dá new é a HelloController no initialize()
    // super manda tudo pro AutoClicker (nome, descrição, ícone, preço, sprite, dano e intervalo)
    public Furunculo(){
        super("Furúnculos", "Carregam e estouram tudo de uma vez", "images/furunculosIcone.png", PRECO,
                new Renderable("images/furunculoG.png", 140, 140),
                DANO_POR_FURUNCULO, SEGUNDOS_DE_CARGA);

        // cria as melhorias dele e guarda na lista do AutoClicker, "this" = o próprio furúnculo (o dono)
        // a ordem dos números é preço, valor, quanto o preço multiplica por compra, nível máximo
        // o "Mais Um" tem nível máximo 2 (3 - 1) pq ele já começa com 1 furúnculo
        adicionarMelhoria(new MelhoriaDano(this, "Oiieee", "Furúnculos: +60% de dano no golpe", 80,  1.6,  2.0, SEM_LIMITE));
        adicionarMelhoria(new MelhoriaCooldown(this, "Agregados agora", "Furúnculos: carregam 15% mais rápido", 200, 1.15, 2.2, 10));
        adicionarMelhoria(new MelhoriaEspecial(this, "Quem é esse cara?", "Furúnculos: nasce mais um (máx. 3)",     500, 1,    6.0, MAXIMO_DE_FURUNCULOS - 1));
    }

    // o ataque dele, quem chama é o AutoClicker.atualizar() quando a carga enche
    // bate o dano base x quantos furúnculos tem, causarDano é do AutoClicker e manda pro GameState
    @Override
    protected void atacar() {
        causarDano(getDano() * qntdFurunculos);
    }

    // especial dele, nasce mais um furúnculo (até 3) e troca o sprite, quem chama é a MelhoriaEspecial
    // o "valor" não é usado aqui pq é sempre +1, só tá no parâmetro pq o método abstrato do AutoClicker exige
    @Override
    public void melhorarEspecial(double valor) {
        if (qntdFurunculos < MAXIMO_DE_FURUNCULOS) {
            qntdFurunculos++;
            atualizarSprite();
        }
    }

    // troca o desenho da arena conforme a quantidade, é private pq só o melhorarEspecial daqui usa
    // setSprite é do Upgrade e o Renderer já desenha o novo no próximo frame
    // FALTA SPRITE, só tem o de 1 (furunculoG) e o de 3 juntos (furunculos), o de 2 não existe
    // então com 2 aparece o quadrado rosa, é só salvar o desenho em images/furunculos2.png
    // (furunculoJ e furunculoL tão na pasta mas não são usados em lugar nenhum)
    private void atualizarSprite() {
        if (qntdFurunculos == 2) {
            setSprite(new Renderable("images/furunculos2.png", 140, 140));
        } else if (qntdFurunculos >= 3) {
            setSprite(new Renderable("images/furunculos.png", 140, 140));
        }
    }

    // volta tudo pro começo (Novo jogo e Carregar)
    // super.resetar() faz a parte do AutoClicker e do Upgrade e já volta o sprite pro de 1 furúnculo
    @Override
    public void resetar() {
        super.resetar();
        qntdFurunculos = 1;
    }

    // textinho embaixo do sprite dele na arena, tipo "2x 60 / a cada 10,0s"
    // o \n quebra a linha, quem usa é o Renderer
    @Override
    public String getTextoStatus() {
        return qntdFurunculos + "x " + Formatador.numero(getDano())
                + "\na cada " + Formatador.segundos(getIntervalo());
    }
}