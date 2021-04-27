package progettoTIW.beans;

import java.util.HashMap;
import java.util.Map;

public class Category {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private boolean isTop;
	private String name;
	private Map<Category, Integer> subcategories = new HashMap<Category, Integer>();
	
	public Category() {
		
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean getIsTop() {
		return this.isTop;
	}
	
	public void setIsTop(boolean isTop) {
		this.isTop = isTop;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<Category, Integer> getSubcategories() {
		return this.subcategories;
	}
	
	public void addSubcategory(Category category, Integer number) {
		//Need fix
		this.subcategories.put(category, number);
	}
	
	public void removeSubCategory(Category category) {
		this.subcategories.remove(category);
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer("Category");
		buffer.append(" id: ");
		buffer.append(this.id);
		buffer.append(" name: ");
		buffer.append(this.name);
		return buffer.toString();
	}
	
}
