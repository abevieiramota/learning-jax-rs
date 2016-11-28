package br.com.alura.loja.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import br.com.alura.loja.modelo.Projeto;

public class ProjetoDAO {

	public Projeto busca(long l) {
		Projeto projeto = new Projeto();
		projeto.setNome("Minha loja");
		
		return projeto ;
	}
	
	private static Map<Long, Projeto> banco = new HashMap<Long, Projeto>();
	private static AtomicLong contador = new AtomicLong(1);
	
	static {
		Projeto projeto = new Projeto();
		projeto.setNome("Minha loja");
		
		banco.put(1l, projeto);
	}
	
	public void adiciona(Projeto projeto) {
		long id = contador.incrementAndGet();
		projeto.setId(id);
		banco.put(id, projeto);
	}
	
	public Projeto busca(Long id) {
		return banco.get(id);
	}
	
	public Projeto remove(long id) {
		return banco.remove(id);
	}

}
