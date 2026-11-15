package com.example.ac;
import java.util.Random;

    public class Furunculos extends from AutoClicker{
        private int cooldown;
        private int qntFurunculos;
        private int briga;
        private int tempoBriga;
    }

//---------------------------------------------

    public Furunculos(int danoBase, String nome) {
        this.danoBase = danoBase;
        this.nome = nome;
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

//---------------------------------------------

    @Override
    protected void diminuirCooldawn(double multiplicador) {

    }

//-------- uprgrades da chance de briga ---------------

//UP1 ----------------------------------------
    protected void brigaChance1() {

    random = new Random();
    int chance = random.nextInt(100);

    if (chance < 20 && qntFurunculos > 0) {

            int perda = random.nextInt(2) + 1;

            qntFurunculos -= perda;

            if (qntFurunculos < 0) {
                qntFurunculos = 0;
            }
        }
    }

//UP2 ----------------------------------------
        protected void brigaChance2() {

    random = new Random();
    int chance = random.nextInt(100);

    if (chance < 10 && qntFurunculos > 0) {

            int perda = random.nextInt(2) + 1;

            qntFurunculos -= perda;

            if (qntFurunculos < 0) {
                qntFurunculos = 0;
            }
        }
    }

//UP3 ----------------------------------------