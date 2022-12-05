package com.yardi.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.test.LoginState;
import com.yardi.shared.test.LoginStateRequest;

/**
 * Servlet implementation class LoginStateService
 */
@WebServlet(description = "Handler for login state requests", urlPatterns = { "/LoginState" })
public class LoginStateService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//debug
		System.out.println("com.yardi.test.LoginStateService doGet() 0003 ");
		//debug
		response.getWriter().append("Served at: ").append(request.getContextPath());
		LoginState loginStateBean = null;
		
		try {
			//debug
			System.out.println("com.yardi.test.LoginStateService doGet() 0000 ");
			//debug
			InitialContext ctx = new InitialContext();
			loginStateBean = (LoginState)ctx.lookup("java:global/yardiWeb/LoginStateBean");
		} catch (NamingException e) {
			// see https://stackoverflow.com/questions/5705922/ejb3-glassfish-jndi-lookup
			//debug
			System.out.println("com.yardi.test.LoginStateService doGet() 0001 ");
			//debug
			e.printStackTrace();
			return;
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String formData = new String();
		
        if(br != null){
        	formData = br.readLine();
        }
        
		//debug
		System.out.println("com.yardi.test.LoginStateService doGet() 0002 "
				+ "\n "
				+ "  formData="
				+ formData
				);
		//debug
        ObjectMapper mapper = new ObjectMapper();
        LoginStateRequest loginStateRequest = new LoginStateRequest();
        loginStateRequest = mapper.readValue(formData, LoginStateRequest.class);
		//debug
		System.out.println("com.yardi.test.LoginStateService doGet() 0004 "
				+ "\n "
				+ "  loginStateRequest="
				+ loginStateRequest.toString()
				);
		//debug
		loginStateBean.setLoginStateRequest(loginStateRequest);
		loginStateBean.mapEntities();
		loginStateRequest = loginStateBean.getLoginStateRequest();
		webResponse(response, mapper.writeValueAsString(loginStateRequest));
		loginStateBean.removeBean();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void webResponse(HttpServletResponse response, String formData) throws IOException {
		//debug
		System.out.println("com.yardi.test.LoginStateService webResponse() 0005 "
				+ "\n   "
				+ "formData="
				+ formData);
		//debug
		response.resetBuffer();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(formData);
		out.flush();
	}
}
