package br.com.alura.loja.model;

import com.thoughtworks.xstream.XStream;

public class Projeto {
	
	private String nome;

	public String toXML() {
		return new XStream().toXML(this);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
