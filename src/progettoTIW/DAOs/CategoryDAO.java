package progettoTIW.DAOs;

import java.sql.*;
import java.util.*;

import progettoTIW.beans.*;

public class CategoryDAO {
	private Connection connection;
	
	public CategoryDAO (Connection connection) {
		this.connection = connection;
	}
	
	///Volendo si potrebbe renderlo boolean invece di void
	public void createCategory(String nome_categoria, String nome_categoria_padre) throws SQLException {
		//Check if the table is effectively called categoria
		/*String query1 = "SELECT COUNT (*) FROM catalog WHERE father_category = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query1);) {
			preparedStatement.setString(1, nome_categoria_padre);
			try (ResultSet result = preparedStatement.executeQuery();) {
				//Stops method if there are already 9 subcategories
		        System.out.println(nome_categoria + nome_categoria_padre);
				if (result.last()) {
					if (result.getInt("COUNT (*)")==9) //return false;
						return;
				}
			}
		} catch (Exception e) {
			//return false;
			return;
		}*/
		
		String query2 = "INSERT INTO catalog (category, father_category) VALUES (?, ?) ";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query2);) {
			//String temp = String.valueOf(id_padre) + String.valueOf(subCategoriesListSize + 1);
			//from string to int
			//int temp2 = Integer.parseInt(temp);
			preparedStatement.setString(1, nome_categoria);
			preparedStatement.setString(2, nome_categoria_padre);
			//preparedStatement.setString(3, nome_categoria_padre);
			//preparedStatement.setString(3, nome_categoria_padre);
			//to change
			//preparedStatement.setInt(1,temp2);
			preparedStatement.executeUpdate();
			//return true;
			//return;
		} catch (Exception e) {
			//return false;
			return;
		}
	}
	
	public void moveCategory(String categoria, String categoria_padre) {
		
		//String query = ""
		
	}
	
	public List<Category> findAllCategories() throws SQLException {
		List<Category> categories = new ArrayList<Category>();
		try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM catalog");) {
			try (ResultSet result = preparedStatement.executeQuery();) {
				while (result.next()) {
					Category category = new Category();
					//category.setId_categoria(result.getInt("id_categoria"));
					category.setNome_categoria(result.getString("category"));
					category.set_nomeCategoriaPadre(result.getString("father_category"));
					/*try {
						category.setId_categoriapadre(result.getInt("id_categoriapadre"));
					} catch (Exception e) {
						category.setId_categoriapadre(-1);
					}*/
					//Adds the new category
					categories.add(category);
				}
			} 
		} catch (Exception e) {
			System.out.println("ERRORE FIND ALL CAT");
		}
		return categories;
	}
	
	public List<Category> findTopCategoriesAndSubCategories() throws SQLException {
		List<Category> categories = new ArrayList<Category>();
		try (PreparedStatement preparedStatement = connection
				//can't create fathers in our project
				.prepareStatement("SELECT * FROM catalog WHERE father_category IS NULL");) {
			try (ResultSet result = preparedStatement.executeQuery();) {
				while (result.next()) {
					Category category = new Category();
					//category.setId_categoria(result.getInt("id_categoria"));
					category.setNome_categoria(result.getString("category"));
					category.setIsTop(true);
					categories.add(category);
				}
				for (Category category : categories) {
					findSubcategories(category);
				}
			}
		} catch (Exception e) {
			System.out.println("FIND TOP CATEGORIES AND SUB");
		}
		return categories;
	}
	
	//recursive function
	public void findSubcategories(Category category) throws SQLException {
		Category cat = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				//
				"SELECT * FROM catalog WHERE father_category = ?");) {
			preparedStatement.setString(1, category.getNome_categoria());
			try (ResultSet result = preparedStatement.executeQuery();) {
				while (result.next()) {
					cat = new Category();
					//cat.setId_categoria(result.getInt("id_categoria"));
					cat.setNome_categoria(result.getString("category"));
					cat.set_nomeCategoriaPadre(result.getString("father_category"));
					category.getSubCategories().add(cat);
					//here recursion starts
					findSubcategories(cat);
				}
			}
		} catch (Exception e) {
			System.out.println("ERRORE FIND SUB");
		}
	}
	
}
