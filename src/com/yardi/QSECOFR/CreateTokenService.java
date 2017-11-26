package com.yardi.QSECOFR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.userServices.PasswordAuthentication;

/**
 * Servlet implementation class CreateTokenService
 * Convert passwords to tokens
 */
@WebServlet("/newToken")
public class CreateTokenService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PasswordAuthentication passwordAuthentication = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateTokenService() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		passwordAuthentication = new PasswordAuthentication();
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String formData = "";
		
        if(br != null){
        	formData = br.readLine();
        }
        
		ObjectMapper mapper = new ObjectMapper();
		TokenRequest tokenRequest = mapper.readValue(formData, TokenRequest.class);
		tokenRequest.setPasswordSave(tokenRequest.getPassword());
		String userToken = passwordAuthentication.hash(tokenRequest.passwordToChar());
		tokenRequest.setMsgID("");
		tokenRequest.setMsgDescription("");
		tokenRequest.setPassword("");

		if (!(userToken==null) && !(userToken.equals("")) ) {
			String s [] = com.yardi.rentSurvey.YardiConstants.YRD0000.split("=");
			tokenRequest.setMsgID(s[0]);
			tokenRequest.setMsgDescription(s[1]);
			tokenRequest.setPassword(userToken);
		}
		
		response.reset();
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
}
