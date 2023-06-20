package it.polimi.tiw.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import it.polimi.tiw.beans.Utente;
import it.polimi.tiw.beans.UtenteVoto;
import it.polimi.tiw.dao.DataAccess;


@WebServlet("/stud")
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
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		JsonArray obj = new Gson().fromJson(getBody(request), JsonArray.class);
		
		String data = (String) request.getSession().getAttribute("data");
		String corso = (String) request.getSession().getAttribute("corso");
		
		
		try {
		
			for(int i = 0; i < obj.size(); i++) {

				String arg = ( (JsonObject) obj.get(i) ).get("matr").toString();
				
				/*
				int argi = Integer.parseInt(arg.replace("\"", ""));
				
				System.out.println(argi);
				System.out.println(String.valueOf(argi));
				System.out.println(arg);
				
				arg.replace("\"", "");
				
				System.out.println(arg);
				
				*/
				
				Integer id = dao.getIdByMatricola(arg.replace("\"", ""));
				
				String esito = ( (JsonObject) obj.get(i) ).get("esito").toString();
				
				dao.updateEsitoById(data, corso, id.toString(), esito.replace("\"", ""));
				
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	public static String getBody(HttpServletRequest request)  {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        // throw ex;
	        return "";
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {

	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;
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