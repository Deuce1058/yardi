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
import com.yardi.ejb.test.PwdTester;
import com.yardi.shared.test.PwdTestRequest;
import com.yardi.shared.userServices.PasswordAuthentication;

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
	 * @param userGroupsBean 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//debug
		System.out.println("com.yardi.test.PwdTesterService doGet() 0002");
		//debug
		PasswordAuthentication passwordAuthentication  = new PasswordAuthentication();
		PwdTestRequest         pwdTestRequest          = parseRequest(request);
		PwdTester              pwdTester               = null;
		String                 s[]                     = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("=");
		pwdTestRequest.setMsgID(s[0]);
		pwdTestRequest.setMsgDescription(s[1]);

		try {
			pwdTestRequest.setToken(passwordAuthentication.hash(pwdTestRequest.passwordToChar()));
			InitialContext ctx = new InitialContext();
			pwdTester = (PwdTester) ctx.lookup("java:global/yardiWeb/PwdTesterBean");
			pwdTester.setPwdTestRequest(pwdTestRequest);
			pwdTester.enforce();
			ObjectMapper mapper = new ObjectMapper();
			webResponse(response, mapper.writeValueAsString(pwdTester.getPwdTestRequest()));
			removeBean(pwdTester);
		} catch (NoSuchAlgorithmException e) {
			//debug
			System.out.println("com.yardi.test.PwdTesterService doGet() NoSuchAlgorithmException 0007");
			//debug
			e.printStackTrace();
		} catch (NamingException e) {
			//debug
			System.out.println("com.yardi.test.PwdTesterService doGet() NamingException 0008");
			//debug
			e.printStackTrace();
		} catch (SecurityException | IllegalStateException e) {
			//debug
			System.out.println("com.yardi.test.PwdTesterService doGet() 000A "
					+ "SecurityException | IllegalStateException 000A");
			//debug
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

	private void removeBean(PwdTester pwdTesterBean) {
		//debug
		System.out.println("com.yardi.test.PwdTesterService removeBean() 0006");
		//debug
		pwdTesterBean.removeBean();
	}
	
	private void webResponse(HttpServletResponse response, String formData) throws IOException {
		//debug
		System.out.println("com.yardi.test.PwdTesterService webResponse() 0001 "
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

	private PwdTestRequest parseRequest(HttpServletRequest request) {
		//debug
        System.out.println("com.yardi.test.PwdTesterService parseRequest() 0000 ");
        //debug
		BufferedReader br=null;
		String formData = "";

		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream()));

			if(br != null){
	        	formData = br.readLine();
	        }

			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(formData, PwdTestRequest.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new PwdTestRequest();
	}
}
