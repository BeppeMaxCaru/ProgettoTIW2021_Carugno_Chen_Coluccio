package progettoTIW.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Category {
	private static final long serialVersionUID = 1L;
	
	private int id_categoria;
	private String nome_categoria;
	private int id_categoriapadre;
	private int num_figli;
	private ArrayList<Category> subCategories = new ArrayList<>();
	//private Map<Category, Integer> subcategories = new HashMap<Category, Integer>();
	
	public Category() {
		
	}
	
	public int getId_categoria() {
		return this.id_categoria;
	}
	
	public void setId_categoria(int id) {
		this.id_categoria = id;
	}
	
	public String getNome_categoria() {
		return this.nome_categoria;
	}
	
	public void setNome_categoria(String name) {
		this.nome_categoria = name;
	}

	public int getId_categoriapadre() {
		return this.id_categoriapadre;
	}
	
	public void setId_categoriapadre(int id) {
		this.id_categoriapadre = id;
	}
	
	public int getNum_figli() {
		return this.num_figli;
	}
	
	public void setNum_figli(int id) {
		this.num_figli = id;
	}
	
	public ArrayList<Category> getSubCategories() {
		return this.subCategories;
	}
	
	/*public Map<Category, Integer> getSubcategories() {
		return this.subcategories;
	}*/
	
	/*public void addSubcategory(Category category, Integer number) {
		//Need fix
		this.subcategories.put(category, number);
	}
	
	public void removeSubCategory(Category category) {
		this.subcategories.remove(category);
	}*/
	
	public String toString() {
		StringBuffer buffer = new StringBuffer("Category");
		buffer.append(" id: ");
		buffer.append(this.id_categoria);
		buffer.append(" name: ");
		buffer.append(this.nome_categoria);
		return buffer.toString();
	}
	
	
}
