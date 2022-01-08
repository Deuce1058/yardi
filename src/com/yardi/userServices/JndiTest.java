package com.yardi.userServices;

//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.UserServices;


/**
 * Servlet implementation class jndiTest
 * http://localhost:8080/yardiWeb/yardiLogin.html
 */
@WebServlet("/jndiTest")
public class JndiTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JndiTest() {
        super();
    }
    
    public void init() {
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//debug
		System.out.println("com.yardi.userServices JndiTest doGet() 0000 ");
		//debug
		HttpSession session=null;
		if (request.getSession(false)==null) {
			//debug
			System.out.println("com.yardi.userServices JndiTest doGet() 0007 ");
			//debug
			session = request.getSession(true);
		} else {
			//debug
			System.out.println("com.yardi.userServices JndiTest doGet() 0008 ");
			//debug
			session = request.getSession(false);
		}
		UserServices userSvcBean = (UserServices)session.getAttribute("userSvcBean");
		UserServices b2 = null;
		InitialContext ctx = null;
		try {
			//debug
			System.out.println("com.yardi.userServices JndiTest doGet() 0001 ");
			//debug
			ctx = new InitialContext();
			b2 = (UserServices)ctx.lookup("java:global/yardiWeb/UserServicesBean");
		} catch (NamingException e) {
			//debug
			System.out.println("com.yardi.userServices JndiTest doGet() 0002 ");
			//debug
			e.printStackTrace();
			return;
		}
				
		if (userSvcBean == null) {
			try {
				//debug
				System.out.println("com.yardi.userServices JndiTest doGet() 0003 ");
				//debug
				ctx = new InitialContext();
				userSvcBean = (UserServices)ctx.lookup("java:global/yardiWeb/UserServicesBean");
				session.setAttribute("userSvcBean", userSvcBean);
			} catch (NamingException e) {
				//debug
				System.out.println("com.yardi.userServices JndiTest doGet() 0004 ");
				//debug
				e.printStackTrace();
			}
		}
		
		if(b2.equals(userSvcBean)) {
			//debug
			System.out.println("com.yardi.userServices JndiTest doGet() 0005 ");
			//debug
		} else {
			//debug
			System.out.println("com.yardi.userServices JndiTest doGet() 0006 ");
			//debug
		}
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
