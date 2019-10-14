package br.ce.scoimbra.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;



public class UserJsonTest {

//validando o title	
	@Test
	public void deveVerificarListaRaiz() {
		given()
		.when()
		     .get("https://swapi.co/api/films/")
		.then()
			 .statusCode(200)
			 .log().body()
		.body("count", is(7))
		.body("results.title", hasItem("A New Hope"))
		.body("results.title",hasItem("Attack of the Clones"))
		.body("results.title",hasItem("The Phantom Menace"))
		.body("results.title", hasItem("Revenge of the Sith"))
		.body("results.title", hasItem("Return of the Jedi"))
		.body("results.title", hasItem("The Empire Strikes Back"))
		.body("results.title", hasItem("The Force Awakens"))
		;
	}

	//buscando um id específico
	@Test
	public void deveVerificarPrimeiroNivel() {
		given()
	    .when()
		     .get("https://swapi.co/api/films/1/")
		.then()
			 .statusCode(200)
			 .log().body()
			 .body("episode_id", is(4))
			 .body("title", containsString("A New Hope"))
			 .body("director", containsString("George Lucas"))
		;
	}
	
	@Test
	//outra forma de trabalhar
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		Response response = RestAssured.request(Method.GET,"https://swapi.co/api/films/1/");
	
		//path (imprimindo o response) 
		System.out.println(response.path("episode_id"));
		System.out.println(response.path("title"));
		
		//assert sucesso
		Assert.assertEquals(new Integer(4), response.path("episode_id"));
		
		//assert falha
		//Assert.assertEquals(new Integer(2), response.path("episode_id"));
		
		//assert enviando o id via parametro (string)
		Assert.assertEquals(new Integer(4), response.path("%s","episode_id"));
		
		//assert enviando o jsonpath 
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals(4, jpath.getInt("episode_id"));
		
		//from
		int id = JsonPath.from(response.asString()).getInt("episode_id");
		Assert.assertEquals(4, id);
	}
	
	@Test
	public void deveVerificarSegundoNivel() {
		given()
		.when()
		     .get("https://swapi.co/api/films/2/")
		.then()
			 .statusCode(200)
			 .log().body()
			 .body("episode_id", is(5))
			 .body("title", containsString("The Empire Strikes Back"))
			 .body("director", containsString("Irvin Kershner"))		;
	}
	
	@Test
	public void deveVerificarLista() {
		given()
		.when()
		     .get("https://swapi.co/api/films/3/")
		.then()
			 .statusCode(200)
			 .log().body()
		     .body("characters", hasItem ("https://swapi.co/api/people/4/"))
	         .body("vehicles", hasItems("https://swapi.co/api/vehicles/16/", "https://swapi.co/api/vehicles/25/"))
	     	 .body("release_date", containsString("1983-05-25"))
		;
	}
	
	@Test
	public void deveRetornarErroIdInexistente() {
		given()
		.when()
		     .get("https://swapi.co/api/films/9/")
		.then()
			 .statusCode(404)
			 .log().body()
			 .body("detail", is("Not found"))
		;
	}
	
}
