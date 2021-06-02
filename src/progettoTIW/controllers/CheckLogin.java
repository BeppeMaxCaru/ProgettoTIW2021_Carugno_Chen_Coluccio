package progettoTIW.controllers;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import progettoTIW.utils.ConnectionHandler;
import progettoTIW.DAOs.UserDAO;
import progettoTIW.beans.User;

import java.io.IOException;
import java.sql.*;

@WebServlet("/CheckLogin")
@MultipartConfig
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	//private TemplateEngine templateEngine;

	public CheckLogin() {
		super();
	}

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
		//ServletContext servletContext = getServletContext();
		//ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		//templateResolver.setTemplateMode(TemplateMode.HTML);
		//this.templateEngine = new TemplateEngine();
		//this.templateEngine.setTemplateResolver(templateResolver);
		//templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// obtain and escape params
		String username = null;
		String password = null;
		try {
			username = request.getParameter("username");
			password = request.getParameter("password");
			if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}

		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
			return;
		}

		// query db to authenticate for user
		UserDAO userDao = new UserDAO(connection);
		User user = null;
		try {
			user = userDao.checkCredentials(username, password);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to check credentials");
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		/*String path;
		if (user == null) {
			ServletContext servletContext = getServletContext();
			//final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
			//context.setVariable("errorMsg", "Incorrect username or password");
			//path = "/UserLogin.html";
			//templateEngine.process(path, context, response.getWriter());
		} else {
			request.getSession().setAttribute("username", user);
			path = getServletContext().getContextPath() + "/GoToHomePage";
			response.sendRedirect(path);
		}*/
		
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Incorrect credentials");
		} else {
			request.getSession().setAttribute("user", user);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(username);
		}

	}

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}