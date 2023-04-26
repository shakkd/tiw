package it.polimi.tiw.servlets;

import java.io.IOException;
import java.io.PrintWriter;

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

@WebServlet("/testing")
public class servlet1 extends HttpServlet {
	
	private TemplateEngine templateEngine;
	private static final long serialVersionUID = 1L;
	       

    public servlet1() {
        super();
    }

    public void init() throws ServletException {
		
		ServletContext ctx = getServletContext();
		
		ServletContextTemplateResolver tmpl = new ServletContextTemplateResolver(ctx);  
		tmpl.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(tmpl);
		
		tmpl.setPrefix("/WEB-INF/");
		tmpl.setSuffix(".html");
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();

		String test = "test123";
		String var = "var123";
		
		//check session-context attributes
		//request.getRequestDispatcher("/WEB-INF/testpage.html").include(request, response);

				
		String path = "html/loginpage";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext);
		
		ctx.setVariable("test", test);
		ctx.setVariable("var", var);
		
		templateEngine.process(path, ctx, out);		
		
		
		/*
		out.println("<form method=\"post\">");
		out.println("<button type=\"submit\" name=\"btn\" value=\"button1\">test1</button>");
		out.println("<button type=\"submit\" name=\"btn\" value=\"button2\">test2</button>");
		out.println("<button type=\"submit\" name=\"btn\" value=\"button3\">test3</button>");
		out.println("</form>");
		*/
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		PrintWriter out = response.getWriter();
		
		String user = request.getParameter("user");
		String pass = request.getParameter("pass");
		
		if(user.equals("admin") && pass.equals("password"))
			out.println("validated");
		else
			out.println("denied");
	
		
		/*
		PrintWriter out = response.getWriter();
        String button = request.getParameter("btn");
		
        doGet(request, response);
        
        out.println("response: \n\n");
        
		if ("button1".equals(button)) {
            out.println("button1");
        } else if ("button2".equals(button)) {
        	out.println("button2");
        } else if ("button3".equals(button)) {
        	out.println("button3");
        }
		*/		
	}

}
	