package progettoTIW.DAOs;

import java.sql.*;
import java.util.*;

import progettoTIW.beans.*;

public class CategoryDAO {
	private Connection connection;
	
	public CategoryDAO (Connection connection) {
		this.connection = connection;
	}
	
	public void createCategory(String nome, int id_padre/*, int subCategoriesListSize*/) throws SQLException {
		//Check if the table is effectively called categoria
		String query = "INSERT into categoria (nome_categoria, id_categoriapadre) VALUES (?, ?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			//String temp = String.valueOf(id_padre) + String.valueOf(subCategoriesListSize + 1);
			//from string to int
			//int temp2 = Integer.parseInt(temp);
			preparedStatement.setString(1, nome);
			preparedStatement.setInt(2, id_padre);
			//to change
			//preparedStatement.setInt(1,temp2);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.out.println("ERRORE CREATE CATEGORY");
		}
	}
	
	
	public List<Category> findAllCategories() throws SQLException {
		List<Category> categories = new ArrayList<Category>();
		try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM categoria");) {
			try (ResultSet result = preparedStatement.executeQuery();) {
				while (result.next()) {
					Category category = new Category();
					category.setId_categoria(result.getInt("id_categoria"));
					category.setNome_categoria(result.getString("nome_categoria"));
					try {
						category.setId_categoriapadre(result.getInt("id_categoriapadre"));
					} catch (Exception e) {
						category.setId_categoriapadre(-1);
					}
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
				.prepareStatement("SELECT * FROM categoria WHERE id_categoriapadre IS NULL");) {
			try (ResultSet result = preparedStatement.executeQuery();) {
				while (result.next()) {
					Category category = new Category();
					category.setId_categoria(result.getInt("id_categoria"));
					category.setNome_categoria(result.getString("nome_categoria"));
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
				"SELECT * FROM categoria WHERE id_categoriapadre = ?");) {
			preparedStatement.setInt(1, category.getId_categoria());
			try (ResultSet result = preparedStatement.executeQuery();) {
				while (result.next()) {
					cat = new Category();
					cat.setId_categoria(result.getInt("id_categoria"));
					cat.setNome_categoria(result.getString("nome_categoria"));
					//here recursion starts
					findSubcategories(cat);
					category.getSubCategories().add(cat);
				}
			}
		} catch (Exception e) {
			System.out.println("ERRORE FIND SUB");
		}
	}
	
}
