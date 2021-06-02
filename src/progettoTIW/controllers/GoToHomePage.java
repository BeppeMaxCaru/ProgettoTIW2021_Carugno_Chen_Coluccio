package progettoTIW.controllers;

 

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

 

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import progettoTIW.DAOs.CategoryDAO;
import progettoTIW.beans.Category;

 

import java.util.List;

 

@WebServlet("/GoToHomePage")
public class GoToHomePage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    private Connection connection = null;

    
    public GoToHomePage() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    public void init() throws ServletException {
        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String username = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnavailableException("Couldn't get db connection");
        }

        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }
 

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> allCategories = null;
        List<Category> topCategories = null;
        List<Category> subCategories = null;
        
        CategoryDAO categoryDAO = new CategoryDAO(connection);
        try {
            allCategories = categoryDAO.findAllCategories();
            topCategories = categoryDAO.findTopCategoriesAndSubCategories();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error in retrieving products from the database");
            return;
        }
        // Redirect to the Home page and add missions to the parameters
        /*String path = "/WEB-INF/HomePage.html";
        ServletContext servletContext = getServletContext();
        final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
        context.setVariable("allcategories", allCategories);
        context.setVariable("topcategories", topCategories);
        context.setVariable("toMove", false);
        //this is too static make it dynamic so he can get every father subcategories
        templateEngine.process(path, context, response.getWriter());*/
        
        Gson gson = new GsonBuilder().create();
		String json = gson.toJson(topCategories);
		//String json2 = gson.toJson(allCategories);
		//json = json.concat(json2);
		
		// Specify our response is JSON format
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		// Write json on the response
		response.getWriter().write(json);
		//response.getWriter().write(json2);
		
    }

    
    @Override
    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e){
                
            }
        }
    }

 

}
