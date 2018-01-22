package br.com.fiap.mercado.bean;

import java.io.Serializable;

public class Item implements Serializable {

	private long codigo;
	
	private String descricao;
	
	private int quantidade;
	
	private float preco;
	
	public Item() {
		super();
	}

	public Item(long codigo, String descricao, int quantidade, float preco) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.preco = preco;
	}

	public long getCodigo() {
		return codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public float getPreco() {
		return preco;
	}

	public void setPreco(float preco) {
		this.preco = preco;
	}
	
	
}