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
	
	public void removeFather(String categoria) {
		
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
					category.setToMove(false);
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
					category.setToMove(false);
					categories.add(category);
				}
				for (Category category : categories) {
					findSubcategories(category);
				}
			}
		} catch (Exception e) {
			//System.out.println("FIND TOP CATEGORIES AND SUB");
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
						cat.setToMove(true);
						category.getSubCategories().add(cat);
						//here recursion starts
						//System.out.println(cat.getNome_categoria());
						findSubcategories(cat);
					}
					//System.out.println("fine while");
				}
			} catch (Exception e) {
				System.out.println("ERRORE FIND SUB");
			}
		}
	
	public Category getCategoryToMove (String category_name) throws SQLException {
		//List<Category> categories = new ArrayList<Category>();
		Category categoryToMove = new Category();
		System.out.print(category_name);
		//System.out.println("ok");
		
		String query = "SELECT * FROM catalog WHERE category = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			//System.out.println("ok1");
			
			preparedStatement.setString(1, category_name);

			
			//int i = 0;
			
			try (ResultSet result = preparedStatement.executeQuery();) {
				//System.out.println("ok2");
				

				//preparedStatement.setString(1, category_name);
				//System.out.println("ok");
				
				/*Category category = new Category();
				category.setNome_categoria(result.getString("category"));
				
				try {
					category.set_nomeCategoriaPadre(result.getString("father_category"));
					category.setIsTop(false);
					category.setToMove(true);
				} catch (Exception e) {
					category.set_nomeCategoriaPadre(null);
					category.setIsTop(true);
					category.setToMove(true);
				}
				
				categoryToMove = category;*/

				
				while (result.next()) {
					//System.out.println("ok");
					Category category = new Category();
					//category.setId_categoria(result.getInt("id_categoria"));
					category.setNome_categoria(result.getString("category"));
					//category.set_nomeCategoriaPadre(result.getString("father_category"));
					//System.out.println(result.getString("category"));
					//i++;
					try {
						category.set_nomeCategoriaPadre(result.getString("father_category"));
						category.setIsTop(false);
						category.setToMove(true);
					} catch (Exception e) {
						category.set_nomeCategoriaPadre(null);
						category.setIsTop(true);
						category.setToMove(true);
					}
					
					/*if (result.getString("father_category").equals(null)) {
						category.setIsTop(true);
					} else {
						category.setIsTop(false);
						category.set_nomeCategoriaPadre(result.getString("father_category"));
					}*/
					category.setToMove(true);
					//System.out.println(category.isToMove());
					//System.out.print(category.getNome_categoria());
					//System.out.println("ok");

					//category.setIsToMove(false);
					//categories.add(category);
					categoryToMove = category;

				}
					
				findSubcategories(categoryToMove);
				//System.out.println(i);
				
			}
		} catch (Exception e) {
			System.out.println("FIND TOP CATEGORIES AND SUB");
			System.out.println("ok");
		}
		return categoryToMove;
		
	}
	//recursive function
	public void findSubcategoriesToMove(Category category) throws SQLException {
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
					cat.setToMove(true);
					category.getSubCategories().add(cat);
					//here recursion starts
					findSubcategories(cat);
					System.out.println(cat.getNome_categoria());
				}
				System.out.println("fine while");
			}
		} catch (Exception e) {
			System.out.println("ERRORE FIND SUB");
		}
	}
	
	public List<Category> findTopCategoriesAndSubCategoriesSecondVersion(String nomeCategoria) throws SQLException {
		
		String nomeCatToMove = nomeCategoria;
		
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
					
					if (category.getNome_categoria().equals(nomeCategoria)) {
						category.setToMove(true);
					} else {
						category.setToMove(false);
					}
					
					//category.setToMove(false);
					categories.add(category);
				}
				for (Category category : categories) {
					findSubcategoriesSecondVersion(category, nomeCatToMove);
				}
			}
		} catch (Exception e) {
			//System.out.println("FIND TOP CATEGORIES AND SUB");
		}
		return categories;
	}
	
	//recursive function
	public void findSubcategoriesSecondVersion(Category category, String nomeCatToMove) throws SQLException {
		String nomeCatToMoveRecursive = nomeCatToMove;
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
					
					//Bisogna sistemare questo if!!!!!!!!
					if (category.isToMove()==true) cat.setToMove(true);
					else if (category.isToMove()==false) {
						if (cat.getNome_categoria().equals(nomeCatToMoveRecursive)) cat.setToMove(true);
						else cat.setToMove(false);
					}
					
					/*if (cat.getNome_categoria().equals(nomeCatToMoveRecursive)) {
						cat.setToMove(true);
					} else {
						cat.setToMove(false);
					}*/
					
					//cat.setToMove(true);
					category.getSubCategories().add(cat);
					//here recursion starts
					//System.out.println(cat.getNome_categoria());
					findSubcategoriesSecondVersion(cat, nomeCatToMoveRecursive);
				}
				//System.out.println("fine while");
			}
		} catch (Exception e) {
			System.out.println("ERRORE FIND SUB");
		}
	}
	
	public void updateTree(String categoriaDaSpostare, String categoriaInCuiSpostarla) {
		String query = "UPDATE catalog SET father_category = ? WHERE category = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			//String temp = String.valueOf(id_padre) + String.valueOf(subCategoriesListSize + 1);
			//from string to int
			//int temp2 = Integer.parseInt(temp);
			preparedStatement.setString(1, categoriaInCuiSpostarla);
			preparedStatement.setString(2, categoriaDaSpostare);
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
	
}
