package it.polimi.tiw.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
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

import com.google.gson.Gson;

import it.polimi.tiw.beans.Utente;
import it.polimi.tiw.dao.DataAccess;


@WebServlet("/home")
public class Home extends HttpServlet {
	
	private Connection connection = null;
	private TemplateEngine templateEngine;
	private static final long serialVersionUID = 1L;
	
	String idUtente = null;
	String flag = null;
	DataAccess dao;
		
	
    public Home() {
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
		
		
		//request.getSession().removeAttribute("utentiVoto");
		request.getSession().removeAttribute("data");
		request.getSession().removeAttribute("corso");

		
				
		String path = "html/home.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext);
		
		idUtente = (String) request.getSession().getAttribute("idUtente");
		
		try {
			flag = (String)request.getSession().getAttribute("type");
			String arg = request.getParameter("corso");
			ctx.setVariable("sel1", arg);
			ctx.setVariable("corsi", dao.getCorsi(flag, idUtente));
			
			ctx.setVariable("appelli", dao.findAppelliFromCorso(arg, flag, idUtente));			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		templateEngine.process(path, ctx, out);			
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String mode = request.getParameter("mode");
		switch(mode) {
			case "refresh":
				
				try {
					
					String arg = request.getParameter("corso");
					String json = new Gson().toJson(dao.findAppelliFromCorso(arg, flag, idUtente));
					
					
					response.getWriter().write(json);


				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				break;
			
				
			/*
			case "submit":
				
				if(request.getParameter("appello") != null) {
					
					switch ((String)request.getSession().getAttribute("type")) {
						case "S":
							response.sendRedirect("/test/esito?data=" + request.getParameter("appello") + 
									"&corso=" + request.getParameter("corso") + "&idUtente=" + idUtente);
							break;
						case "D":
							response.sendRedirect("/test/iscritti?data=" + request.getParameter("appello") + 
									"&corso=" + request.getParameter("corso") + "&order=Matricola");
							break;					
					}
					
					
				}
				else {
					doGet(request, response);
					response.getWriter().println("select a date");	
				}
				
				break;
			*/
				
			
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
