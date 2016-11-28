package br.com.alura.loja.dao;

import br.com.alura.loja.model.Projeto;

public class ProjetoDAO {

	public Projeto busca(long l) {

		Projeto projeto = new Projeto();
		projeto.setNome("Minha loja");
		
		return projeto ;
	}

}
