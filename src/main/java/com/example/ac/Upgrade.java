package com.example.ac;

public class Upgrade {
	public String nome;
	public String descricao;
	public String imagem;
	public Runnable acao;
	
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
	
	public Upgrade(String nome, String descricao, String imagem, Runnable acao) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.imagem = imagem;
		this.acao = acao;
	}
}
