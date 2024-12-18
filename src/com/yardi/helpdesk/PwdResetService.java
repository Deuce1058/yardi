package com.yardi.helpdesk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.helpdesk.PwdResetCtrl;
import com.yardi.shared.helpdesk.ResetPwdRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class PwdResetService
 */
@WebServlet("/doPwdReset")
public class PwdResetService extends HttpServlet {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;

	private PwdResetCtrl checkSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		System.out.println("com.yardi.helpdesk.PwdResetService.checkSession() 0000 "
				+ "\n    "
				+ "sessionID="
				+ session.getId()
				);
		PwdResetCtrl pwdpwdResetCtrlBean = (PwdResetCtrl)session.getAttribute("pwdpwdResetCtrlBean");
		
		synchronized(session) {
			if (pwdpwdResetCtrlBean==null) {
				System.out.println("com.yardi.helpdesk.PwdResetService.checkSession() 0001 ");
				try {
					InitialContext ctx = new InitialContext();
					pwdpwdResetCtrlBean = (PwdResetCtrl)ctx.lookup("java:global/yardiWeb/PwdResetCtrlBean");
				} catch (NamingException e) {
					System.out.println("com.yardi.helpdesk.PwdResetService.checkSession() 0002 NamingException ");
					e.printStackTrace();
					return null;
				}
				
				session.setAttribute("pwdpwdResetCtrlBean", pwdpwdResetCtrlBean);
			}
		}
		
		return pwdpwdResetCtrlBean;
	}
	
	/**
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    PwdResetCtrl pwdResetCtrlBean = checkSession(request);
	    String formdata = readRawBufferedLine(request); 
	    ResetPwdRequest resetPwdRequest = mapRequestStringToResetPwdRequest(formdata); 
	    pwdResetCtrlBean.setResetPwdRequest(resetPwdRequest); 
	    
	    if (resetPwdRequest.getAction().equalsIgnoreCase(com.yardi.shared.rentSurvey.YardiConstants.EDIT_PASSWORD_POLICY_REQUEST_ACTION_FIND)) {
	    	resetPwdRequest = pwdResetCtrlBean.findUserDetails();
	    }
	    
	    if (resetPwdRequest.getAction().equalsIgnoreCase(com.yardi.shared.rentSurvey.YardiConstants.PASSWORD_RESET_REQUEST)) {
	        resetPwdRequest = pwdResetCtrlBean.resetPwd(); 
	    }
	    
	    ObjectMapper mapper = new ObjectMapper(); 
	    webResponse(request, response, mapper.writeValueAsString(resetPwdRequest), pwdResetCtrlBean); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private ResetPwdRequest mapRequestStringToResetPwdRequest(String request) {
		System.out.println("com.yardi.helpdesk.PwdResetService.mapRequest() 0003 ");
		ObjectMapper m = new ObjectMapper(); 
		ResetPwdRequest resetPwdRequest = null;
		try {
			resetPwdRequest = m.readValue(request, ResetPwdRequest.class);
		} catch (Exception e) {
			System.out.println("com.yardi.helpdesk.PwdResetService.mapRequest() 0004 Exception ");
			e.printStackTrace();
		}
		
		return resetPwdRequest;
	}
	private String readRawBufferedLine(HttpServletRequest request) {
		System.out.println("com.yardi.helpdesk.PwdResetService.readRawBufferedLine() 0005 ");
		String formData = ""; 		 
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			
			if(br != null){ 
				formData = br.readLine();
			} 		  
		} catch (IOException e) {
			System.out.println("com.yardi.helpdesk.PwdResetService.readRawBufferedLine() 0006 Exception ");
			e.printStackTrace();
		} 

		return formData;
	}
	
	private void resetBuffer(HttpServletResponse response) {
		//debug
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.isEmpty()) {
			System.out.println("com.yardi.helpdesk.PwdResetService.resetBuffer() 0007 headerNames is empty");
		}
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.helpdesk.PwdResetService.resetBuffer() 0008 "
					+ "\n"
					+ "   Response header name="
					+ n
					+ "   no headerValues");
			}
			for (String v :  headerValues) {
				System.out.println("com.yardi.helpdesk.PwdResetService.resetBuffer() 0009 "
						+ "\n"
						+ "   Response header name="
						+ n
						+ "   Value="
						+ v
					);
			}
		}
		//debug
        response.resetBuffer();
        //debug
		headerNames = response.getHeaderNames();
		if (headerNames.isEmpty()) {
			System.out.println("com.yardi.helpdesk.PwdResetService.resetBuffer() 000A headerNames is empty");
		}
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.helpdesk.PwdResetService.resetBuffer() 000B "
					+ "\n"
					+ "   Response header name="
					+ n
					+ "   no headerValues");
			}
			for (String v :  headerValues) {
				System.out.println("com.yardi.helpdesk.PwdResetService.resetBuffer() 000C "
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
	
	private void webResponse(HttpServletRequest request, HttpServletResponse response, String formData, PwdResetCtrl pwdResetCtrlBean) {
		System.out.println("com.yardi.helpdesk.PwdResetService.webResponse() 000D ");
	    try {
			HttpSession session = request.getSession();
	    	resetBuffer(response);
	    	response.setContentType("application/json"); 
			PrintWriter out = response.getWriter();
		    out.print(formData); 
		    out.flush(); 
		    pwdResetCtrlBean.remove();
		    synchronized(session) {
			    request.getSession().setAttribute("pwdResetCtrlBean", null); 
		    }
		    pwdResetCtrlBean = null;
		} catch (IOException e) {
			System.out.println("com.yardi.helpdesk.PwdResetService.webResponse() 000E IOException ");
			e.printStackTrace();
		} 
	}
}
