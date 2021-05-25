package progettoTIW.DAOs;

import java.sql.*;

import progettoTIW.beans.User;

public class UserDAO {
	private Connection connection;
	
	public UserDAO(Connection connection) {
		this.connection = connection;
	}
	
	public User checkCredentials(String username, String password) throws SQLException {
		String query = "SELECT * FROM users WHERE username = ? AND password = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			try (ResultSet result = preparedStatement.executeQuery()) {
				//error insert twice login credentials
				if (!result.isBeforeFirst()) 
					return null;
				else {
					result.next();
					User user = new User();
					//user.setId(result.getInt("id"));
					user.setUsername(result.getString("username"));
					user.setPassword(result.getString("password"));
					return user;
				}
			}
		}
	}
}
