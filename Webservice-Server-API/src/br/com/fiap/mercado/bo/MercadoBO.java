package br.com.fiap.mercado.bo;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.mercado.bean.Item;

public class MercadoBO {

	private static List<Item> lista = new ArrayList<Item>();
	
	static{
		lista.add(new Item(1, "Arroz", 2, 5.50f));
		lista.add(new Item(2, "Molho de Tomate", 3, 1.50f));
		lista.add(new Item(3, "Cerveja", 24, 2.5f));
		lista.add(new Item(4, "Leite", 2, 3));
	}
	
	public Item buscar(int id){
		for (Item i : lista) {
			if (i.getCodigo() == id){
				return i;
			}
		}
		return null;
	}
	
	public void cadastrar(Item i){
		i.setCodigo(lista.size()+1);
		lista.add(i);
	}
	
	public List<Item> listar(){
		return lista;
	}
	
}
