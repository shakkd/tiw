
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class servlet1
 */
@WebServlet("/servlet1")
public class servlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public servlet1() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		
		// action=\"servlet1\"
		// <input type="submit" name="button1" value="Button 1" />
		
		out.println("<form method=\"post\">");
		out.println("<button type=\"submit\" name=\"btn\" value=\"button1\">test1</button>");
		out.println("<button type=\"submit\" name=\"btn\" value=\"button2\">test2</button>");
		out.println("<button type=\"submit\" name=\"btn\" value=\"button3\">test3</button>");
		out.println("</form>");
	}

	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		PrintWriter out = response.getWriter();
        String button = request.getParameter("btn");
		
        doGet(request, response);
        
        out.println("response: \n\n");
        
		if ("button1".equals(button)) {
            out.println("francescp ha cliccato test1");
        } else if ("button2".equals(button)) {
        	out.println("francesco ha cliccato test2");
        } else if ("button3".equals(button)) {
        	out.println("francesco ha cliccato test3");
        }
		
		//request.getRequestDispatcher("testpage.html").forward(request, response); da rived
		
	}

}
