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
		}
	}
	
	public List<Category> findAllCategories() throws SQLException {
		List<Category> categories = new ArrayList<Category>();
		try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM categories");) {
			try (ResultSet result = preparedStatement.executeQuery();) {
				while (result.next()) {
					Category category = new Category();
					category.setId(result.getInt("id"));
					category.setName(result.getString("name"));
					//Adds the new category
					categories.add(category);
				}
			}
		}
		return categories;
	}
	
	public List<Category> findTopCategoriesAndSubtrees() throws SQLException {
		List<Category> categories = new ArrayList<Category>();
		try (PreparedStatement preparedStatement = connection
				.prepareStatement("SELECT * FROM categories WHERE id NOT IN (select child FROM categories)");) {
			try (ResultSet result = preparedStatement.executeQuery();) {
				while (result.next()) {
					Category category = new Category();
					category.setId(result.getInt("id"));
					category.setName(result.getString("name"));
					category.setIsTop(true);
					categories.add(category);
				}
				for (Category category : categories) {
					findSubcategories(category);
				}
			}
		}
		return categories;
	}
	
	public void findSubcategories(Category category) throws SQLException {
		Category cat = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				//Fix query
				"SELECT C1.id, C1.name FROM categories JOIN categories P on P.id = S.child WHERE S.father = ?");) {
			preparedStatement.setInt(1, category.getId());
			try (ResultSet result = preparedStatement.executeQuery();) {
				while (result.next()) {
					cat = new Category();
					cat.setId(result.getInt("id"));
					cat.setName(result.getString("name"));
					findSubcategories(cat);
					/*category.addSubcategory(cat);*/
				}
			}
		}
	}
	
}
