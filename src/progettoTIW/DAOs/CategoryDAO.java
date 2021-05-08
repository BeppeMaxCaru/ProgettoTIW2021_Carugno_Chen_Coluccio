package progettoTIW.DAOs;

import java.sql.*;
import java.util.*;

import progettoTIW.beans.*;

public class CategoryDAO {
	private Connection connection;
	
	public CategoryDAO (Connection connection) {
		this.connection = connection;
	}
	
	public void createCategory(String name) throws SQLException {
		//Check if the table is effectively called category
		String query = "INSERT into categories (name) VALUES (?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, name);
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
				.prepareStatement("SELECT * FROM categoria WHERE id_categoriapadre IS NULL");) {
			try (ResultSet result = preparedStatement.executeQuery();) {
				while (result.next()) {
					Category category = new Category();
					category.setId_categoria(result.getInt("id_categoria"));
					category.setNome_categoria(result.getString("nome_categoria"));
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
	
	public void findSubcategories(Category category) throws SQLException {
		Category cat = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				//Fix query
				"SELECT * FROM categoria WHERE id_categoriapadre = ?");) {
			preparedStatement.setInt(1, category.getId_categoria());
			try (ResultSet result = preparedStatement.executeQuery();) {
				while (result.next()) {
					cat = new Category();
					cat.setId_categoria(result.getInt("id_categoria"));
					cat.setNome_categoria(result.getString("nome_categoria"));
					findSubcategories(cat);
					category.getSubCategories().add(cat);
				}
			}
		} catch (Exception e) {
			System.out.println("ERRORE FIND SUB");
		}
	}
	
}
