package com.example.ac;

import java.util.function.Consumer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Upgrade {
	public String nome;
	public String descricao;
	public String imagem;
	public DoubleProperty preco;
	
	public Double getPreco() {
	    return preco.get();
	}

	public void setPreco(Double preco) {
	    this.preco.set(preco);
	}

	public DoubleProperty precoProperty() {
	    return preco;
	}

	/*
	 * TODO: Trocar de Consumer para subclasse implementada pela classe filha.
	 * */
	public Consumer<Upgrade> acao;
	
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
	
	public Upgrade(String nome, String descricao, String imagem, Consumer<Upgrade> acao, Double preco) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.imagem = imagem;
		this.acao = acao;
		this.preco = new SimpleDoubleProperty(preco);
	}
	
	public Upgrade(String nome, String descricao, String imagem, Double preco) {
		this.nome = nome;
		this.descricao = descricao;
		this.imagem = imagem;
		this.acao = (u) -> {throw new UnsupportedOperationException("Ao utilizar o contrutor sem action, implemente a action");};
		this.preco = new SimpleDoubleProperty(preco);
	}
}
