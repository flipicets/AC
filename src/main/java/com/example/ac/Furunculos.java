package com.example.ac;
import java.util.Random;

    public class Furunculos extends from AutoClicker{
        private int cooldown;

        private int qntFurunculos;

        private int danoPorFurunculo;
        private int tempoAtaque;
        private long ultimoAtaque;

        private int briga;
        private int tempoBriga;
        private long ultimoTempoBriga

    }

//---------------------------------------------

    public Furunculos(int poderClique, String nome) {
        super(poderClique, nome);
        
        this.ultimoTempoBriga = System.currentTimeMillis();
        this.ultimoAtaque = String.currentTimeMillis();   
    }

//a qntidade máxima é de 3 ao mesmo tempo :P

    protected void furunculosAtivos(int qntFurunculos) {

        if (this.qntFurunculos + qntFurunculos <= 3) {
            this.qntFurunculos += qntFurunculos;
        }
        else {
            this.qntFurunculos = 3;
        }
    }

    public int getQntFurunculos() {
        return qntFurunculos;
    }

//------- dano base por furunculo ativo --------
protected int danoFurunculos1(){
    danoPorFurunculo = 7000;
    tempoAtaque = 15;

    long tempoAtual = System.currentTimeMillis();
    if (tempoAtual - ultimoAtaque>= tempoAtaque * 1000L){
        int danoTotal = getQntFurunculos * danoPorFurunculo;
        ultimoAtaque = tempoAtual;

        return danoTotal;
    }
    retunr 0;
}

protected int danoFurunculos2(){
    danoPorFurunculo = 10000;
    tempoAtaque = 12;

    long tempoAtual = System.currentTimeMillis();
    if (tempoAtual - ultimoAtaque>= tempoAtaque * 1000L){
        int danoTotal = getQntFurunculos * danoPorFurunculo;
        ultimoAtaque = tempoAtual;

        return danoTotal;
    }
    retunr 0;
}

protected int danoFurunculos3(){
    danoPorFurunculo = 13000;
    tempoAtaque = 7;

    long tempoAtual = System.currentTimeMillis();
    if (tempoAtual - ultimoAtaque>= tempoAtaque * 1000L){
        int danoTotal = getQntFurunculos * danoPorFurunculo;
        ultimoAtaque = tempoAtual;

        return danoTotal;
    }
    retunr 0;
}

/----------------------------------------------

    @Override
    protected void diminuirCooldawn(double multiplicador) {

    }

//-------- uprgrades da chance de briga ---------------

//UP1 ----------------------------------------
protected void brigaChance1() {
    int briga = 20;

    long tempoAtual = System.currentTimeMillis
    if(tempoAtual) - ultimoTempoBriga >=tempoBriga * 20000L{ //verifica o intervalo de tempo (em segundos, 1000milisegundos = 1 segundo)
            
            //sorteio da chance
        Random random = new Random();
        int chance = random.nextInt(100); 
            
        if (chance < briga && qntFurunculos > 0){ //verifica o sorteio e ve se é possivel diminuir furunculos
            int perda = random.nextInt(2) + 1; //sorteia qntos serão perdidos (1 ou 2)
            qntFurunculos -= perda;
                
            if(qntFurunculos < 0){ //impede que a qnt fique abaixo de 0
                qntFurunculos = 0;
            }
        }
        ultimoTempoBriga = tempoAtual; //atualiza a ultima verificação
    }
}

//UP2 ----------------------------------------
protected void brigaChance2() {
    int briga = 10;

    long tempoAtual = System.currentTimeMillis
    if(tempoAtual) - ultimoTempoBriga >=tempoBriga * 30000L{

        Random random = new Random();
        int chance = random.nextInt(100);

        if (chance < briga && qntFurunculos > 0){
            int perda = random.nextInt(2) + 1;
            qntFurunculos -= perda;

            if(qntFurunculos < 0){
                qntFurunculos = 0;
            }
        }
        ultimoTempoBriga = tempoAtual;
    }
}

//UP3 ----------------------------------------
protected void brigaChance3() {
    long tempoAtual = System.currentTimeMillis
    if(tempoAtual) - ultimoTempoBriga >=tempoBriga * 60000{
        ultimoTempoBriga = tempoAtual;
    }
}