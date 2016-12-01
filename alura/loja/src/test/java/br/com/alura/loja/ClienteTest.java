package br.com.alura.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.modelo.Projeto;

@SuppressWarnings("deprecation")
@RunWith(JUnit4.class)
public class ClienteTest {

	private static HttpServer server;
	private static ClientConfig clientConfig;

	@BeforeClass
	public static void beforeClass() {
		server = Servidor.inicializaServidor();
		clientConfig = new ClientConfig();
		clientConfig.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY);
		// TODO: rever e usar LoggingFeature
		LoggingFilter loggingFilter = new LoggingFilter();
		clientConfig.register(loggingFilter);
	}

	@AfterClass
	public static void afterClass() {
		server.shutdownNow();
	}

	private Client client;
	private WebTarget target;

	@Before
	public void before() {
		this.client = ClientBuilder.newClient(clientConfig);
		this.target = this.client.target("http://localhost:8080");
	}

	//	// http://www.mocky.io/v2/583b8e8d100000ae24764129
	//	@Test
	//	@Ignore
	//	public void testaQueAConexaoComOServidorFunciona() {
	//		Client client = ClientBuilder.newClient();
	//		WebTarget target = client.target("http://www.mocky.io");
	//		String content = target.path("/v2/583b8e8d100000ae24764129").request().get(String.class);
	//
	//		assertTrue(content.contains("Abelardo Vieira Mota"));
	//	}

	@Test
	public void testaBuscaCarrinhoTrazOCarrinhoEsperado() {
		Carrinho carrinho = this.target.path("/carrinhos/1").request().get(Carrinho.class);

		assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}

	@Test
	public void testaBuscaProjetoTrazOProjetoEsperado() {
		Projeto projeto = this.target.path("/projetos/1").request().get(Projeto.class);

		assertEquals("Minha loja", projeto.getNome());
	}

	@Test
	public void testaAdicionaCarrinho() {
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
		carrinho.setRua("Rua Vergueiro");
		carrinho.setCidade("Sao Paulo");

		Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);

		Response response = this.target.path("/carrinhos").request().post(entity);

		assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
		String location = response.getHeaderString("Location");
		String conteudo = this.client.target(location).request().get(String.class);

		assertTrue(conteudo.contains("Rua Vergueiro"));
	}

	@Test
	public void testaAdicionaProjeto() {
		Projeto projeto = new Projeto();
		projeto.setNome("Abelardo Projeto");

		Entity<Projeto> entity = Entity.entity(projeto, MediaType.APPLICATION_XML);

		Response response = this.target.path("/projetos").request().post(entity);

		assertEquals(Status.CREATED.getStatusCode(), response.getStatus());

		String location = response.getHeaderString("Location");
		String conteudo = this.client.target(location).request().get(String.class);
		assertTrue(conteudo.contains("Abelardo Projeto"));
	}

	@Test
	public void testaAlteraQuantidadeCarrinho() {
		Integer quantidadeAntiga = 2;
		Integer quantidadeNova = quantidadeAntiga - 1;
		// cria
		Carrinho carrinho = new Carrinho();
		carrinho.setCidade("Fortaleza");
		carrinho.setRua("De Studart");
		Produto produto = new Produto(222L, "Brinquedão", 991, quantidadeAntiga);
		carrinho.adiciona(produto);

		Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);

		Response response = this.target.path("/carrinhos").request().post(entity);
		String location = response.getHeaderString("Location");

		assertEquals(Status.CREATED.getStatusCode(), response.getStatus());

		// altera

		produto.setQuantidade(quantidadeNova);

		Entity<Produto> entityProduto = Entity.entity(produto, MediaType.APPLICATION_XML);

		Response response2 = this.client.target(location).path("/produtos/quantidade").request().put(entityProduto);

		assertEquals(Status.OK.getStatusCode(), response2.getStatus());

		// verifica

		Carrinho carrinhoCriado = this.client.target(location).request().get(Carrinho.class);
		
		assertEquals(quantidadeNova, carrinhoCriado.getQuantidade(produto.getId()));
	}
}
