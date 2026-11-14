package com.example.ac;

public final class Adrian {

    private long vidaMax;
    private long vidaAtual;
    
    private static Adrian INSTANCE = new Adrian();

    private Adrian(){
        this.vidaMax = 10_000_000_000_000L;
        this.vidaAtual = vidaMax;
    }

    public void levarDano(AutoClicker clico){
        this.vidaAtual -= clico.getPoderClique();
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
