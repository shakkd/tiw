package it.polimi.tiw.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

import it.polimi.tiw.beans.Utente;
import it.polimi.tiw.beans.UtenteVoto;
import it.polimi.tiw.dao.DataAccess;


@WebServlet("/studente")
public class Stud extends HttpServlet {
	
	
	private Connection connection = null;
	private TemplateEngine templateEngine;
	private static final long serialVersionUID = 1L;
	
	DataAccess dao;
	
	int matr;
	UtenteVoto curr = null;
	List<UtenteVoto> list;
	
    public Stud() {
        super();
        // TODO Auto-generated constructor stub
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
	  	   
		
		String path = "/WEB-INF/html/studform.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext);
		
		matr = Integer.parseInt(request.getParameter("matr"));
		
		
		try {
			
			list = (List<UtenteVoto>) dao.findUtentiVoto((String)request.getSession().getAttribute("data"), 
					(String)request.getSession().getAttribute("corso"), null, null);
			
			for (UtenteVoto tmp : list) if(tmp.getUtente().getMatricola() == matr) {
				curr = tmp;
				break;
			}
			
			ctx.setVariable("utenteVoto", curr);
			ctx.setVariable("voti", new DataAccess(connection).findVoti());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		templateEngine.process(path, ctx, out);
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String mode = request.getParameter("submit");
		
		switch (mode){
			case "back":
				response.sendRedirect("/test/iscritti?data=" + request.getSession().getAttribute("data") + 
						"&corso=" + request.getSession().getAttribute("corso") + "&order=Matricola");
				break;
			case "insert":
								
				try {
					
					curr.setVoto(request.getParameter("voto"));
					curr.setStato("Inserito");
							
					
					new DataAccess(connection).updateUtenteVoto(curr);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				doGet(request, response);
				break;
				
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
