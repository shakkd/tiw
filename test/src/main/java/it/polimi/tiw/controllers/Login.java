package it.polimi.tiw.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

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

import it.polimi.tiw.beans.Utente;
import it.polimi.tiw.dao.DataAccess;


//sfrutta motore per main page(gestita da home servlet),
//  la pagina di login reinderizza alla main page, dalla main page si sfruttano
//  tutte le funzioni e ritornano

@WebServlet("/login")
public class Login extends HttpServlet {
	
	private Connection connection = null;
	private TemplateEngine templateEngine;
	private static final long serialVersionUID = 1L;
	       

    public Login() {
        super();
    }

    public void init() throws ServletException {
    	
    	//connection setup
    	try {
    		final String DB_URL = "jdbc:mysql://localhost:3306/dbtest?serverTimezone=UTC";
    	    final String USER = "root";
    	    final String PASS = "MirkoGentile9!";
    	    
		    Class.forName("com.mysql.cj.jdbc.Driver");
	    	connection = DriverManager.getConnection(DB_URL, USER, PASS);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new UnavailableException("Can't load database driver");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UnavailableException("Couldn't get db connection");
		}
    	
		//web engine setup
		ServletContext ctx = getServletContext();
		
		ServletContextTemplateResolver tmpl = new ServletContextTemplateResolver(ctx);  
		tmpl.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(tmpl);
		
		//tmpl.setPrefix("/WEB-INF/");
		//tmpl.setSuffix(".html");
		
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();

		
		//check session-context attributes
		//request.getRequestDispatcher("/WEB-INF/testpage.html").include(request, response);
	  	    
				
		String path = "/WEB-INF/html/loginpage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext);
		
		//String var = "var123";
		//ctx.setVariable("var", var);
		
		templateEngine.process(path, ctx, out);		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		PrintWriter out = response.getWriter();
				
		String email = request.getParameter("email");
		String passw = request.getParameter("pass");
		
		boolean ok = false;
		
		try {
			for(Utente tmp : new DataAccess(connection).findAllUtenti())
				if(tmp.getEmail().equals(email) && tmp.getPassword().equals(passw)) {
					ok = true;
					request.getSession().setAttribute("type", tmp.getTipo());
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(ok)
			response.sendRedirect("/test/home");
		else {
			doGet(request, response);
			out.println("access denied");
		}
	    
		
	    
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
	