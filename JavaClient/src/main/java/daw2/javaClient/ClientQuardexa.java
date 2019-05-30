package daw2.javaClient;

import com.ttdev.post.*;
import com.ttdev.token.*;
import com.ttdev.user.*;
import com.ttdev.createPostResponse.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;

import java.util.*;

public class ClientQuardexa {
	
	public static void main (String [] args) {
		Scanner keyboard = new Scanner(System.in);
		System.out.println("------- Quardexa by Zixiong Lin  & jabier sanchez biyal -------");
		System.out.println("           --------------------------------------");
		System.out.println();
		boolean exit;
		titleChanger();
	}
	
	public static void titleChanger() {
		// Rest Client Preparation
		WebClient client = null;
		
		// Keyboard Input
		Scanner keyboard = new Scanner(System.in);
		System.out.println("	--- Vamos a cambiar el titulo!!! ---");
		System.out.println("	        ---------------------");
		System.out.println();
		boolean existPath = false;
		String toModifyTile = "";
		int postId;
		do {
			System.out.print("	Introduzca un ID de la Noticia: ");
			postId = keyboard.nextInt();
			System.out.println();
			try {
				// Get - Drupal Post Content Type
				client = WebClient.create("http://localhost/docroot/restful/");
		    	client.accept("application/xml");
		    	client.path("node/" + postId);
		    	
				Post post = client.get(Post.class);
				if (post.getType().equalsIgnoreCase("noticia")) {
					toModifyTile = post.getTitle();
					existPath = true;
				} else {
					System.out.println("	Introduce un ID de una Noticia.");
					System.out.println();
					existPath = false;
				}
			} catch (javax.ws.rs.NotFoundException e) {
				System.out.println("	¿Tú eres tonto o masticas agua? Introduce un ID que sirva.");
				System.out.println();
				existPath = false;
			}
		} while (!existPath);
		
		// Soap Server Call - Converting title string
		
		// Async Petition
		SoapAsyncRequest soapCall = new SoapAsyncRequest(toModifyTile);
		soapCall.start();
		
		// Get Token
		client.back(true);
		client.path("user/token");
		Token token = client.type(MediaType.APPLICATION_FORM_URLENCODED).post(null, Token.class);

		// Log In
		client.back(true);
		client.path("user/login");
		client.header("x-CSRF-Token", token.getToken());
		
		Form form = new Form();
		form.set("username", "test");
		form.set("password", "test");
		
		Usuario usuario = client.type(MediaType.APPLICATION_FORM_URLENCODED).post(form, Usuario.class);

		// Prepare Put - Drupal Post Content Type
		client.back(true);
		client.path("node/"+postId);
		
		// Prepare Put - Building body
		form = new Form();
		try {
			soapCall.join(); // wait for async request if needed
		} catch (InterruptedException e) {}
		String soapResponse = soapCall.getModifiedTitle();
		form.set("title", soapResponse);
		
		// Prepare Put - Building headers
		client.header("cookie", usuario.getSessionName() + "=" + usuario.getSessid());
		client.replaceHeader("X-CSRF-Token", usuario.getToken());
		
		// Put - Send petition
		String resp = client.type(MediaType.APPLICATION_FORM_URLENCODED).put(form, String.class);

		// Log Out
		client.back(true);
		client.path("user/logout");
		client.post(null, String.class);
		
		System.out.println();
		System.out.println("	¡Título Cambiado!");
		System.out.println("	Original = " + toModifyTile);
		System.out.println("	Modificado = " + soapResponse);
		System.out.println();
	}

}
