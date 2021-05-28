package progettoTIW.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import progettoTIW.DAOs.CategoryDAO;
import progettoTIW.beans.Category;

@WebServlet("/MoveCategory")
public class MoveCategory extends HttpServlet{
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    private Connection connection = null;
       
    public MoveCategory() {
        super();
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

            ServletContext servletContext = getServletContext();
            ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
            templateResolver.setTemplateMode(TemplateMode.HTML);
            this.templateEngine = new TemplateEngine();
            this.templateEngine.setTemplateResolver(templateResolver);
            templateResolver.setSuffix(".html");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnavailableException("Couldn't get db connection");
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    	String nome_categoria = null;
    	//String nome_categoria_padre = null;
        boolean badRequest = false;
        List<Category> allCategories = null;
        List<Category> topCategories = null;
        Category categoryToMove = null;
        
        try {
            nome_categoria = request.getParameter("category");
            //nome_categoria_padre = request.getParameter("father_category");
            
            request.getSession().setAttribute("categoriaDaSpostare", nome_categoria);
            
            //Father category name can be null
            if (/*nome_categoria_padre == null || nome_categoria_padre.isEmpty() ||*/ nome_categoria == null || nome_categoria.isEmpty()) {
                badRequest = true;
            }
            
        } catch (NullPointerException e) {
            badRequest = true;
        }
        
        if (badRequest) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or incorrect parameters");
            return;
        }
        
        //Category cat = new Category();
        //cat.setNome_categoria(nome_categoria);
        //cat.set_nomeCategoriaPadre(nome_categoria_padre);
        
        CategoryDAO categoryDAO = new CategoryDAO(connection);
        try {
        	//allCategories = categoryDAO.findAllCategories();
        	topCategories = categoryDAO.findTopCategoriesAndSubCategoriesSecondVersion(nome_categoria);
        	//categoryToMove = categoryDAO.getCategoryToMove(nome_categoria);
        	//System.out.println(categoryToMove.getNome_categoria());
        	//System.out.println(categoryToMove.isToMove());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error in moving the category in the database");
            return;
        }
        
        //List<Category> allCategories = (ArrayList) getServletContext().getAttribute("allcategories");
        //String path = contextPath + "/GoToHomePage";
        //response.sendRedirect(path);
        
        String path = "/WEB-INF/HomePage.html";
        ServletContext servletContext = getServletContext();
        //Object object = servletContext.getAttribute("category");
        //System.out.println(object.toString());
        
        final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
        //context.setVariable("allcategories", allCategories);
        context.setVariable("topcategories", topCategories);
        //context.setVariable("categoryToMove", categoryToMove);
        context.setVariable("toMove", true);
        //this is too static make it dynamic so he can get every father subcategories
        templateEngine.process(path, context, response.getWriter());
    }
    
    @Override
    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
                //Print cannot close connection
            }
        }
    }


}
