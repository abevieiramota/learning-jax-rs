package br.com.alura.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.model.Projeto;
import br.com.alura.loja.modelo.Carrinho;

@RunWith(JUnit4.class)
public class ClienteTest {

	private static HttpServer server;

	@BeforeClass
	public static void beforeClass() {
		server = Servidor.inicializaServidor();
	}

	@AfterClass
	public static void afterClass() {

		server.shutdownNow();
	}

	// http://www.mocky.io/v2/583b8e8d100000ae24764129
	@Test
	@Ignore
	public void testaQueAConexaoComOServidorFunciona() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://www.mocky.io");
		String content = target.path("/v2/583b8e8d100000ae24764129").request().get(String.class);

		assertTrue(content.contains("Abelardo Vieira Mota"));
	}

	@Test
	public void testaBuscaCarrinhoTrazOCarrinhoEsperado() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		String content = target.path("/carrinhos").request().get(String.class);

		Carrinho carrinho = (Carrinho) new XStream().fromXML(content);

		assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}

	@Test
	public void testaBuscaProjetoTrazOProjetoEsperado() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		String content = target.path("/projetos").request().get(String.class);

		Projeto carrinho = (Projeto) new XStream().fromXML(content);

		assertEquals("Minha loja", carrinho.getNome());
	}
}
