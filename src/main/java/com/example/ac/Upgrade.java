package com.example.ac;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class Upgrade {

    protected String nome;
    protected String descricao;
    protected String imagem;
    protected DoubleProperty preco;

    public Upgrade(String nome, String descricao, String imagem, double preco) {
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
        this.preco = new SimpleDoubleProperty(preco);
    }

    public abstract void executar();

    public Double getPreco() {
        return preco.get();
    }

    public void setPreco(Double preco) {
        this.preco.set(preco);
    }

    public DoubleProperty precoProperty() {
        return preco;
    }

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public void setPreco(DoubleProperty preco) {
		this.preco = preco;
	}
    
    
}