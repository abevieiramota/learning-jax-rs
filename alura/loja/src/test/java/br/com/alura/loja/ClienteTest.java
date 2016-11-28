package br.com.alura.loja;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ClienteTest {

	// http://www.mocky.io/v2/583b8e8d100000ae24764129
	@Test
	public void testaQueAConexaoComOServidorFunciona() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://www.mocky.io");
		String content = target.path("/v2/583b8e8d100000ae24764129").request().get(String.class);
		
		assertTrue(content.contains("Abelardo Vieira Mota"));
	}
	
	@Test
	public void testaMeuServidor() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		String content = target.path("/carrinhos").request().get(String.class);
		
		assertTrue(content.contains("Videogame 4"));
	}
}
