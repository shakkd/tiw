package it.polimi.tiw.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


//sfrutta motore per main page(gestita da home servlet),
//  la pagina di login reinderizza alla main page, dalla main page si sfruttano
//  tutte le funzioni e ritornano

@WebServlet("/testing")
public class home extends HttpServlet {
	
	private TemplateEngine templateEngine;
	private static final long serialVersionUID = 1L;
	       

    public home() {
        super();
    }

    public void init() throws ServletException {
    	
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
		
		String mode = request.getParameter("action");
		
		String email = request.getParameter("email");
		String passw = request.getParameter("pass");
		
		boolean ok = false;
		String userType = null;
		
		
    	//db setup
    	final String DB_URL = "jdbc:mysql://localhost:3306/dbtest?serverTimezone=UTC";
	    final String USER = "root";
	    final String PASS = "MirkoGentile9!";
	    

	    
	    //request handler
	    switch(mode) {
		    case "login":
		    	
			    try {
			    	
				    final String QUERY = "SELECT email, password, flag FROM Utente";
			    	
				    Class.forName("com.mysql.cj.jdbc.Driver");
			    	Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		            Statement stmt = conn.createStatement();
		            ResultSet rs = stmt.executeQuery(QUERY);
		            
			    	while (rs.next()) {
			    		if(email.equals(rs.getString("email")) && passw.equals(rs.getString("password"))) {
			    			ok = true;
			    		
			    			//userType = rs.getString("flag");
			    		}		
			    	}
			    
			    } catch (Exception e) {
			      e.printStackTrace();
			    }
				
				if(ok) {
					
					
					//get courses and populate list
					try {
				    	
					    final String QUERY = "SELECT nomeCorso FROM corso ORDER BY nomeCorso DESC";
				    	
					    Class.forName("com.mysql.cj.jdbc.Driver");
				    	Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			            Statement stmt = conn.createStatement();
			            ResultSet rs = stmt.executeQuery(QUERY);
			            
			            List<String> res = new ArrayList<> ();
			            
				    	while (rs.next()) res.add(rs.getString("nomeCorso"));
				    	
				    	
				    	response.setContentType("text/html");
		
						String path = "/WEB-INF/html/home.html";
						ServletContext servletContext = getServletContext();
						final WebContext ctx = new WebContext(request, response, servletContext);
						
						ctx.setVariable("corsi", res);
						
						templateEngine.process(path, ctx, out);	

						
				    
				    } catch (Exception e) {
				      e.printStackTrace();
				    }
					
					
				} else {
					doGet(request, response);
					out.println("access denied");

				}
				break;
			
				
			//possibilità di redirect ad una servlet nell attributo 
			//	th:action del form alla submit tramite @{/serverPath} che gestirà la post request
		    case "":
		    	break;
		    	
		    			    	
	    }
		
		
	}
	
	
	
	
	

}
	