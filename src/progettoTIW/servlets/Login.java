package progettoTIW.servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import progettoTIW.beans.User;

import java.sql.*;

@WebServlet("/login")
public class Login extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private LoginDAO loginDAO;
	
	public void init() throws ServletException {
		try {
			
			ServletContext context = getServletContext();
			String driver = context.getInitParameter("dbDriver");
			String url = context.getInitParameter("dbUrl");
			String username = context.getInitParameter("dbUsername");
			String password = context.getInitParameter("dbPassword");
			
			Class.forName(driver);
			Connection connection = DriverManager.getConnection(url, username, password);
			
		} catch (ClassNotFoundException e) {
			throw new UnavailableException("Database driver loading failure");
		} catch (SQLException e) {
			throw new UnavailableException("Database connection failure");
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User loginBean = new User();
		loginBean.setUsername(username);
		loginBean.setPassword(password);
		
	}

}
