package com.example.ac;
import java.util.ArrayList;

public class Main {

    public int bufunfa;

    static Ajuda ajuda = new Ajuda();
    static Adrian adrian= new Adrian();
    static ArrayList<AutoClicker> party = new ArrayList<>();

    static void main(String[] args) {
        //mariestelar.start();//só por dá start em um personagem o adrian já toma dano
        //caso precisar testar é só por um adrian.getVidaAtual, por isso no while está implementado as outras mecanicas
        //party.add(mariestelar);


        while (adrian.taVivo()){
            System.out.println("1_loja");
            System.out.println("2_testar dano adrian");
            int esc= ajuda.pegarEscolha(1,2);
            if (esc==1){
                loja();
            }else{
                testarDano();
            }
        }
    }

    public static void loja(){

        System.out.println("Personagens");
        System.out.println("1_Conde Gale");
        System.out.println("2_Final Fefo");
        System.out.println("3_Furunculos");
        System.out.println("4_Maristelar");
        System.out.println("5_Sair");

        int esc = ajuda.pegarEscolha(1,4);

        if (esc==1){
            CondeGale condeGale = new CondeGale(90, "Conde Galê", adrian);
            condeGale.start();
            party.add(condeGale);

        } else if (esc==2){
            FinalFefo finalFefo= new FinalFefo(30,"Final Fefo", adrian);
            finalFefo.start();
            party.add(finalFefo);

        } else if (esc ==3) {
            Furunculos furunculos = new Furunculos(200, "Furunculos", adrian);
            furunculos.start();
            party.add(furunculos);

        } else if (esc== 4){
            Mariestelar mariestelar= new Mariestelar(130, "Mariestelar", adrian);
            mariestelar.start();
            party.add(mariestelar);
        }
    }

    public static void testarDano(){
            System.out.println(adrian.getVidaAtual());
    }

}
