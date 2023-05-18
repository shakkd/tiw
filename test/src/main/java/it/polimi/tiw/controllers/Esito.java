package it.polimi.tiw.controllers;

import java.io.IOException;
import java.io.PrintWriter;
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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.beans.UtenteVoto;
import it.polimi.tiw.dao.DataAccess;


@WebServlet("/esito")
public class Esito extends HttpServlet {
	
	private Connection connection = null;
	private TemplateEngine templateEngine;
	private static final long serialVersionUID = 1L;
	
	
	String data = null;
	String corso = null;
	String idUtente = null;
	
	DataAccess dao;
       
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
    	
    	dao = new DataAccess(connection);
    	
		//web engine setup
		ServletContext ctx = getServletContext();
		
		ServletContextTemplateResolver tmpl = new ServletContextTemplateResolver(ctx);  
		tmpl.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(tmpl);
		
		//tmpl.setPrefix("/WEB-INF/");
		//tmpl.setSuffix(".html");
		
	}
	
    public Esito() {
        super();
        // TODO Auto-generated constructor stub
    }


    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
				
		String path = "/WEB-INF/html/esito.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext);
		
		data = request.getParameter("data");
		corso = request.getParameter("corso");
		idUtente = (String) request.getSession().getAttribute("idUtente");
		
		UtenteVoto ret = null;
		
		try {
			ret = dao.findEsito(data, corso, idUtente);
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(ret.getVoto() == null) out.println("voto non ancora definito");
		else ctx.setVariable("utenteVoto", ret);
				
		templateEngine.process(path, ctx, out);	
		
		
	}


	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			dao.rifiutaVoto(data, corso, idUtente);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		doGet(request, response);
		response.getWriter().println("il voto è stato rifiutato");
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
