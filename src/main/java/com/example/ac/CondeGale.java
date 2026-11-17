package com.example.ac;

// Random é o "dado" do java, a gente usa pra sortear se o ataque crita ou não
import java.util.Random;

/*
 * ataca a cada 3s com dano baixo, mas cada ataque tem uma chance de critar (dano x multiplicador)
 * e se ficar muitos ataques seguidos sem critar o próximo é crítico garantido, pra não depender só de sorte
 *
 * da versão antiga que a gente fez ficou o crítico x5 e o contador vezesCritico, só que antes ele nunca zerava
 * então depois do 5º ataque tudo era crítico, agora zera quando crita
 */
public class CondeGale extends AutoClicker{

    // números de balanceamento (valores iniciais), static final = constante
    private static final double PRECO = 2_000;
    private static final double DANO = 60;
    private static final double SEGUNDOS_ENTRE_ATAQUES = 3;
    private static final double CHANCE_CRITICO = 0.15;          // 15%
    private static final double CHANCE_MAXIMA = 0.90;           // nunca passa de 90%
    private static final double MULTIPLICADOR_CRITICO = 5;
    private static final double BONUS_MULTIPLICADOR = 1;        // quanto o x do crítico sobe por nível do especial
    private static final int ATAQUES_PARA_GARANTIR = 5;
    private static final int MINIMO_PARA_GARANTIR = 2;

    // esses aqui mudam durante o jogo com as melhorias, começam valendo as constantes de cima
    private double chanceCritico = CHANCE_CRITICO;
    private double multiplicadorCritico = MULTIPLICADOR_CRITICO;
    private int vezesCritico = 0;                               // ataques desde o último crítico
    private int ataquesParaGarantir = ATAQUES_PARA_GARANTIR;

    // o dado, nextDouble() sorteia um número de 0.0 a 1.0
    private final Random random = new Random();

    // construtor, quem dá new é a HelloController no initialize()
    // super manda tudo pro AutoClicker (nome, descrição, ícone, preço, sprite, dano e intervalo)
    public CondeGale(){
        super("Conde Gale", "Vampiresco y viadjinho", "images/condegaleIcone.png", PRECO,
                new Renderable("images/condegale.png", 140, 140),
                DANO, SEGUNDOS_ENTRE_ATAQUES);

        // cria as 4 melhorias dele e guarda na lista do AutoClicker
        // "this" = o próprio gale, é assim que a melhoria sabe quem é o dono
        // a ordem dos números é preço, valor, quanto o preço multiplica por compra, nível máximo
        adicionarMelhoria(new MelhoriaDano(this, "Dentinho pontuidonho", "Gale: +50% de dano base",                    1_000, 1.5,  2.0, SEM_LIMITE));
        adicionarMelhoria(new MelhoriaVelocidade(this, "Nhomnhomnhom", "Gale: ataca 15% mais rápido",          2_500, 1.15, 2.5, 10));
        adicionarMelhoria(new MelhoriaCooldown(this, "do roockkk porraaa 😠🤘", "Gale: crítico garantido chega antes",    4_000, 1.25, 4.0, 3));
        adicionarMelhoria(new MelhoriaEspecial(this, "Safadinhe master", "Gale: +5% de chance e +1x no crítico",    3_000, 0.05, 2.2, 15));
    }

    // o ataque dele, quem chama é o AutoClicker.atualizar() quando passa o intervalo
    // é aqui que tá o polimorfismo, cada personagem tem o seu atacar()
    @Override
    protected void atacar() {
        // conta mais um ataque desde o último crítico
        vezesCritico++;

        // garantido = já bateu vezes demais sem critar
        boolean garantido = vezesCritico >= ataquesParaGarantir;
        // sorteado = o dado caiu abaixo da chance, ex sorteou 0.09 e a chance é 0.15 então critou
        boolean sorteado = random.nextDouble() < chanceCritico;

        // "||" é OU, se qualquer um dos dois for true é crítico
        if (garantido || sorteado) {
            // causarDano é do AutoClicker, manda o dano pro GameState e mostra "CRÍTICO!" na tela
            causarDano(getDano() * multiplicadorCritico, "CRÍTICO! ");
            vezesCritico = 0;   // critou, zera o contador
        } else {
            causarDano(getDano());
        }
    }

    // no gale cooldown = quantos ataques sem critar até o crítico garantido
    // sobrescreve o padrão do AutoClicker, quem chama é a MelhoriaCooldown
    // Math.round arredonda (dá long, o (int) na frente converte pra int)
    // Math.max = o maior dos dois, não deixa descer abaixo de 2
    @Override
    public void diminuirCooldawn(double multiplicador) {
        if (multiplicador > 0) {
            int novo = (int) Math.round(ataquesParaGarantir / multiplicador);
            ataquesParaGarantir = Math.max(MINIMO_PARA_GARANTIR, novo);
        }
    }

    // especial dele, "valor" é quanto de chance entra (0.05 = +5%) e o multiplicador sobe +1
    // Math.min = o menor dos dois, trava a chance em 90%, quem chama é a MelhoriaEspecial
    @Override
    public void melhorarEspecial(double valor) {
        chanceCritico = Math.min(CHANCE_MAXIMA, chanceCritico + valor);
        multiplicadorCritico += BONUS_MULTIPLICADOR;
    }

    // volta tudo pro começo (Novo jogo e Carregar)
    // super.resetar() faz a parte do AutoClicker e do Upgrade, depois volta o que é só do gale
    @Override
    public void resetar() {
        super.resetar();
        chanceCritico = CHANCE_CRITICO;
        multiplicadorCritico = MULTIPLICADOR_CRITICO;
        vezesCritico = 0;
        ataquesParaGarantir = ATAQUES_PARA_GARANTIR;
    }

    // textinho embaixo do sprite dele na arena, tipo "60 a cada 3.0s / crítico 15% x5"
    // o \n quebra a linha, chanceCritico * 100 transforma 0.15 em 15, quem usa é o Renderer
    @Override
    public String getTextoStatus() {
        return Formatador.numero(getDano()) + " a cada " + Formatador.segundos(getIntervalo())
                + "\ncrítico " + Math.round(chanceCritico * 100) + "% x" + Formatador.numero(multiplicadorCritico);
    }
}