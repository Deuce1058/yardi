package com.yardi.QSECOFR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.shared.userServices.PasswordAuthentication;

/**
 * Servlet implementation class CreateTokenService
 * Convert passwords to tokens
 */
@WebServlet("/XnewToken")
public class XCreateTokenService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XCreateTokenService() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String formData = "";
		
        if(br != null){
        	formData = br.readLine();
        }
        
		System.out.println("com.yardi.QSECOFR.XCreateTokenService doGet() 0000 formData=" + formData);
		ObjectMapper mapper = new ObjectMapper();
		TokenRequest tokenRequest = new TokenRequest();
		tokenRequest = mapper.readValue(formData, TokenRequest.class);
		tokenRequest.setPasswordSave(tokenRequest.getPassword());
		String userToken="";
		try {
			userToken = passwordAuthentication.hash(tokenRequest.passwordToChar());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		tokenRequest.setMsgID("");
		tokenRequest.setMsgDescription("");
		tokenRequest.setPassword("");

		if (!(userToken==null) && !(userToken.equals("")) ) {
			String s [] = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("=");
			tokenRequest.setMsgID(s[0]);
			tokenRequest.setMsgDescription(s[1]);
			tokenRequest.setPassword(userToken);
		}
		
		showResponseHeaders(response);
		response.resetBuffer();
		showResponseHeaders(response);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		formData = mapper.writeValueAsString(tokenRequest); //convert the feedback to json 
		out.print(formData);
		out.flush();
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void showResponseHeaders(HttpServletResponse response) {
		//debug 
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.isEmpty()) {
			System.out.println("com.yardi.QSECOFR.XCreateTokenService showResponseHeaders() 0001 headerNames is empty");
		}
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.QSECOFR.XCreateTokenService showResponseHeaders() 0002 "
						+ "\n"
						+ "   Response header name="
						+ n
						+ "   no headerValues");
			}
			for (String v :  headerValues) {
				System.out.println("com.yardi.QSECOFR.XCreateTokenService showResponseHeaders() 0003 "
						+ "\n"
						+ "   Response header name="
						+ n
						+ "   Value="
						+ v
						);
			}
		}
		//debug
	}
}
