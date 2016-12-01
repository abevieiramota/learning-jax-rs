package br.com.alura.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.modelo.Projeto;

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
		String content = target.path("/carrinhos/1").request().get(String.class);

		Carrinho carrinho = (Carrinho) new XStream().fromXML(content);

		assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}

	@Test
	public void testaBuscaProjetoTrazOProjetoEsperado() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		String content = target.path("/projetos/1").request().get(String.class);

		Projeto carrinho = (Projeto) new XStream().fromXML(content);

		assertEquals("Minha loja", carrinho.getNome());
	}

	@Test
	public void testaAdicionaCarrinho() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		
		{
			System.out.println("oaisia");
			System.out.println("");
		}

		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
		carrinho.setRua("Rua Vergueiro");
		carrinho.setCidade("Sao Paulo");
		String xml = carrinho.toXML();
		
		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

		Response response = target.path("/carrinhos").request().post(entity);

		assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
		String location = response.getHeaderString("Location");
		String conteudo = client.target(location).request().get(String.class);

		assertTrue(conteudo.contains("Rua Vergueiro"));
	}

	@Test
	public void testaAdicionaProjeto() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");

		Projeto projeto = new Projeto();
		projeto.setNome("Abelardo Projeto");
		String xml = projeto.toXML();

		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

		Response response = target.path("/projetos").request().post(entity);

		assertEquals(Status.CREATED.getStatusCode(), response.getStatus());

		String location = response.getHeaderString("Location");
		String conteudo = client.target(location).request().get(String.class);
		assertTrue(conteudo.contains("Abelardo Projeto"));
	}
}
