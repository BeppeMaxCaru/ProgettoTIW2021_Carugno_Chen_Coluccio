package progettoTIW.controllers;

 

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

 

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 

import progettoTIW.DAOs.CategoryDAO;

 

@WebServlet("/CreateCategory")
public class CreateCategory extends HttpServlet{
    private static final long serialVersionUID = 1L;
    
    

    private Connection connection = null;
    
    public CreateCategory() {
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

 

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnavailableException("Couldn't get db connection");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nome_categoria = null;
        //int id_categoria_padre;
        String nome_categoria_padre = null;
        boolean badRequest = false;
        try {
            nome_categoria = request.getParameter("nome_nuovacategoria");
            nome_categoria_padre = request.getParameter("nome_categoria_padre");
            //Father category name can be null
            if (nome_categoria == null || nome_categoria.isEmpty() || nome_categoria_padre == null || nome_categoria_padre.isEmpty()) {
                badRequest = true;
            }
            
        } catch (NullPointerException e) {
            badRequest = true;
            }
        
        if (badRequest) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or incorrect parameters");
            return;
        }
        CategoryDAO categoryDAO = new CategoryDAO(connection);
        try {
        	
        	categoryDAO.createCategory(nome_categoria, nome_categoria_padre);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error in creating the category in the database");
            return;
        }
        String contextPath = getServletContext().getContextPath();
        String path = contextPath + "/GoToHomePage";
        response.sendRedirect(path);
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