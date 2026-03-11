package com.yardi.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.crypto.Jargon2Bean;
import com.yardi.ejb.test.PwdTester;
import com.yardi.shared.test.PwdTestRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PwdTesterService
 */
@jakarta.servlet.annotation.WebServlet("/TestPwd")
public class PwdTesterService extends jakarta.servlet.http.HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Map the request String data to the {@link com.yardi.shared.test.PwdTestRequest PwdTestRequest} container.    
	 * @param request String read from the input stream 
	 * @return java representation of the String read from the request stream
	 */
	private PwdTestRequest deserializePwdTestRequest(String request) {
        System.out.println("com.yardi.test.PwdTesterService.deserializePwdTestRequest() 0000 ");
		ObjectMapper mapper = new ObjectMapper();
		PwdTestRequest pwdTestRequest = null;
		try {
			pwdTestRequest = mapper.readValue(request, PwdTestRequest.class);
		} catch (JsonProcessingException e) {
	        System.out.println("com.yardi.test.PwdTesterService.deserializePwdTestRequest() 0009 ");
			e.printStackTrace();
		}
		
		return pwdTestRequest;
	}
	
	/**
	 * Entry point for password tester service 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("com.yardi.test.PwdTesterService.doGet() 0002");
		PwdTester pwdTester = getPwdTester();
		Jargon2Bean jargon2Bean = getJargon2Bean();
		String formdata = readRawBufferedLine(request);
		PwdTestRequest pwdTestRequest = deserializePwdTestRequest(formdata);
		String s[] = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("=");
		pwdTestRequest.setMsgID(s[0]);
		pwdTestRequest.setMsgDescription(s[1]);

		try {
			pwdTestRequest.setToken(jargon2Bean.hash(pwdTestRequest.getPassword()));
			pwdTester.setPwdTestRequest(pwdTestRequest);
			pwdTester.enforce();
			ObjectMapper mapper = new ObjectMapper();
			webResponse(response, mapper.writeValueAsString(pwdTester.getPwdTestRequest()));
			removeBean(pwdTester);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("com.yardi.test.PwdTesterService.doGet() NoSuchAlgorithmException 0007");
			e.printStackTrace();
		} catch (SecurityException | IllegalStateException e) {
			System.out.println("com.yardi.test.PwdTesterService.doGet() 000A "
					+ "SecurityException | IllegalStateException 000A");
			e.printStackTrace();
		}
		
		return;
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}


	/**
	 * Lookup com.yardi.ejb.crypto.Jargon2Bean
	 * @return reference to com.yardi.ejb.crypto.Jargon2Bean
	 */
	private Jargon2Bean getJargon2Bean() {
		System.out.println("com.yardi.test.PwdTesterService.getJargon2Bean() 000B ");
		Jargon2Bean jargon2Bean = null;
		try {
			InitialContext ctx = new InitialContext();
			jargon2Bean = (Jargon2Bean) ctx.lookup("java:global/yardiWeb/Jargon2Bean");
		} catch (NamingException e) {
			System.out.println("com.yardi.test.PwdTesterService.getJargon2Bean() NamingException 0003 ");
			e.printStackTrace();
		}
		return jargon2Bean;
	}

	/**
	 * Lookup com.yardi.ejb.test.PwdTesterBean
	 * @return reference to com.yardi.ejb.test.PwdTesterBean
	 */
	private PwdTester getPwdTester() {
		System.out.println("com.yardi.test.PwdTesterService.getPwdTester() 000C ");
		PwdTester pwdTester = null;
		try {
			InitialContext ctx = new InitialContext();
			pwdTester = (PwdTester) ctx.lookup("java:global/yardiWeb/PwdTesterBean");
		} catch (NamingException e) {
			System.out.println("com.yardi.test.PwdTesterService.getPwdTester() NamingException 0008");
			e.printStackTrace();
		}
		return pwdTester;
	}
	
	/**
	 * Read the raw input stream and store the contents in a String 
	 * @param request {@link HttpServletRequest}
	 * @return String containing the data read from the input stream
	 */
	private String readRawBufferedLine(HttpServletRequest request) {
		System.out.println("com.yardi.test.PwdTesterService.readRawBufferedLine() 0004 ");
		String formData = ""; 		 
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			
			if(br != null){ 
				formData = br.readLine();
			} 		  
		} catch (IOException e) {
			System.out.println("com.yardi.test.PwdTesterService.readRawBufferedLine() 0005 Exception ");
			e.printStackTrace();
		} 

		return formData;
	}
	
	private void removeBean(PwdTester pwdTesterBean) {
		System.out.println("com.yardi.test.PwdTesterService.removeBean() 0006");
		pwdTesterBean.removeBean();
	}

	private void webResponse(HttpServletResponse response, String formData) throws IOException {
		System.out.println("com.yardi.test.PwdTesterService.webResponse() 0001 "
				+ "\n   "
				+ "formData="
				+ formData);
		response.resetBuffer();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(formData);
		out.flush();
	}
}
