package com.example.ac;

/*
 * Nesta classe implemento um Singleton (https://refactoring.guru/pt-br/design-patterns/singleton)
 * visando manter sincronicidade de alterações no escopo completo do projeto
 * então por isso que o construtor é privado, quaisquer chamadas aqui devem utilizar o getInstance
 * */
public final class Adrian {

    private long vidaMax;
    private long vidaAtual;
    
    private static Adrian INSTANCE = new Adrian();

    private Adrian(){
        this.vidaMax = 10_000_000_000_000L;
        this.vidaAtual = vidaMax;
    }
    
    public static Adrian getInstance() {
    	return INSTANCE;
    }

	public long getVidaMax() {
		return vidaMax;
	}

	public void setVidaMax(long vidaMax) {
		this.vidaMax = vidaMax;
	}

	public long getVidaAtual() {
		return vidaAtual;
	}

	public void setVidaAtual(long vidaAtual) {
		this.vidaAtual = vidaAtual;
	}
    
    

}
