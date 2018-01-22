package br.com.fiap.mercado.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.fiap.mercado.bean.Item;
import br.com.fiap.mercado.bo.MercadoBO;

import com.google.gson.Gson;

@Path("/mercado")
public class MercadoResource {

	private MercadoBO bo;
	
	public MercadoResource() {
		bo = new MercadoBO();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public String buscar(@PathParam("id") int id){
		Item item = bo.buscar(id);
		return new Gson().toJson(item);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String listar(){
		List<Item> lista = bo.listar();
		Map<String,List<Item>> mapa = new HashMap<>();
		mapa.put("itens", lista);
		return new Gson().toJson(mapa);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cadastrar(String itemJSON){
		Item item = new Gson().fromJson(itemJSON, Item.class);
		bo.cadastrar(item);
		return Response.status(201).entity("item cadastrado").build();
	}
	
}



