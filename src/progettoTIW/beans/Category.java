package progettoTIW.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Category {
	private static final long serialVersionUID = 1L;
	
	private int id_categoria;
	private String nome_categoria;
	private int id_categoriapadre = -1;
	private String nome_categoria_padre;
	private int num_figli;
	private boolean isTop;
	private ArrayList<Category> subCategories = new ArrayList<>();
	//private Map<Category, Integer> subcategories = new HashMap<Category, Integer>();
	private boolean isToMove;
	
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
	
	public String get_nomeCategoriaPadre() {
		return this.nome_categoria_padre;
	}
	
	public void set_nomeCategoriaPadre(String nome_categoria_padre) {
		this.nome_categoria_padre = nome_categoria_padre;
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
	
	public boolean getIsTop() {
		return this.isTop;
	}
	
	public void setIsTop(boolean isTop) {
		this.isTop = isTop;
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

	public boolean isToMove() {
		return this.isToMove;
	}

	public void setToMove(boolean isToMove) {
		this.isToMove = isToMove;
	}
	
	
}
