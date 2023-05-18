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

import it.polimi.tiw.beans.UtenteVoto;
import it.polimi.tiw.dao.DataAccess;


@WebServlet("/iscritti")
public class Iscritti extends HttpServlet {
	
	private Connection connection = null;
	private TemplateEngine templateEngine;
	private static final long serialVersionUID = 1L;
       
    DataAccess dao;
	
    public Iscritti() {
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
	  	   
				
		String path = "/WEB-INF/html/iscritti.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext);
		
		try {
			
			//prendo input ordine corrente
			String curr = request.getParameter("order");
			
			//recupero vecchi ordine e direzione
			String last = (String)request.getSession().getAttribute("last");
			String dir = (String)request.getSession().getAttribute("dir");

			if (last != null && last.equals(curr)) { //se stesso ordine cambio direz
				dir = switchDir(dir);
				request.getSession().setAttribute("dir", dir);
			} else {  //primo giro last nullo, salvo ordine e dir correnti
				dir = "asc";
				request.getSession().setAttribute("last", curr);
				request.getSession().setAttribute("dir", "asc");
			}
			
			
			//nelle post request non ne ho piu accesso, ciclo if successivo gestisce
			String arg1 = request.getParameter("data");
			String arg2 = request.getParameter("corso");
			
			if (arg1 != null) {
				request.getSession().setAttribute("data", arg1);
				request.getSession().setAttribute("corso", arg2);
			}
			


			List <UtenteVoto> ret = dao.findUtentiVoto( (String) request.getSession().getAttribute("data"), 
					(String) request.getSession().getAttribute("corso"), curr, dir);

			
			ctx.setVariable("utentiVoto", ret);		

			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		templateEngine.process(path, ctx, out);
		
	}

	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("matr") != null) {

			request.getSession().removeAttribute("last");
			request.getSession().removeAttribute("dir");
			
			String matr = (String)request.getParameter("matr");
			response.sendRedirect("/test/studente?matr=" + matr);
			
		} else try{
			switch(request.getParameter("submit")) {
		
				case "pubblica":
					dao.pubblicaEsiti( (String) request.getSession().getAttribute("data"),
							(String)request.getSession().getAttribute("corso"));
					doGet(request, response);
					break;
					
				case "verbalizza":
					dao.verbalizzaEsiti( (String) request.getSession().getAttribute("data"),
							(String)request.getSession().getAttribute("corso"));
					doGet(request, response);
					break;
					
				default:
					doGet(request, response);
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
			
	}

	
	
	public String switchDir(String arg) {
		String ret = "asc";
	
		if (arg.equals("asc")) ret = "desc";
				
		return ret;
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
