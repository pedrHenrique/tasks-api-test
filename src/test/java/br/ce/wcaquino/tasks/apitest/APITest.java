package br.ce.wcaquino.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend"; //Testes estão sendo feitos com base na banco Postgres subido via docker
	}
	
	@Test
	public void deveRetonarTarefas() {
		RestAssured.given() // Inicia a dependência baixada do Rest. Given começa a construção de uma simulação de Requisição
		.when()//Quando acontecer essa solicitação 
			 .get("/todo")//faça um get (pega o valor)
		.then()	//então, depois da solicitação
			.statusCode(200); //retorne esse status esperado que indica sucesso nesta solicitação
		;
	}
	
	@Test
	public void deveAdicionarTarefaComSucesso() {
		RestAssured.given()
		.body("{\"task\" : \"Teste via APi\", \"dueDate\" : \"2020-12-30\" }")// Criação de Arquivo Json 
		.contentType(ContentType.JSON) // informando ao eclipse que esté tipo de arquivo é Json.... Importante colar o arquivo já entra aspas
		.when()
			.post("/todo") //poste oq foi solicitado
		.then()	
		.log().all()//loguei oq foi feito após o post
			.statusCode(201)//e retorne esse tipo de status experado
		;
	}
	
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		RestAssured.given()
		.body("{\"task\" : \"Teste via APi\", \"dueDate\" : \"2010-12-30\" }")
		.contentType(ContentType.JSON) 
		.when()
			.post("/todo") //adiciona
		.then()	
		.log().all()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past")) //o body não sabe identificar dois textos... Por isso teve que ser chamado o CoreMatchers
		;
	}
	
	@Test
	public void deveRemoverTarefaComSucesso() {
		Integer id = RestAssured.given()
		.body("{\"task\" : \"Tarefa Para Remocao\", \"dueDate\" : \"2020-12-30\" }")// Criação de Arquivo Json 
			.contentType(ContentType.JSON) 
		.when()
			.post("/todo") //adiciona
		.then()	
			.log().all()
			.statusCode(201)
			.extract().path("id"); //extrai específico do caminho.... Agora como ele faz isso ¯\_(ツ)_/¯
			;
			System.out.println("ID recebido de RestAssured: " + id);
		
		//remover
			
		RestAssured.given()
		.when()
			.delete("/todo/"+id)
		.then()
			.statusCode(204) //status code 204 = ok porém sem conteúdo para informar
		;
	}

}


