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
 * Entry point for helpdesk password reset function. Two requests are serviced: find details about the user profile for the heldesk to review before assigning a temporary 
 * password and assignment of a temporary password that has a life defined by password policy.
 */
@WebServlet("/doPwdReset")
public class PwdResetService extends HttpServlet {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Determine whether the session contains a reference to {@link com.yardi.ejb.helpdesk.PwdResetCtrlBean PwdResetCtrlBean}.<p> 
	 * If the session does not have a reference to PwdResetCtrlBean then get the reference from JNDI and save it in the session. Return the reference that came 
	 * from either the session or JNDI.   
	 * @param request {@link HttpServletRequest}
	 * @return a reference to PwdResetCtrlBean
	 */
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
	 * Entry point for helpdesk reset password function.<p>
	 * <ul>
	 *   <li>obtain a reference to {@link com.yardi.ejb.helpdesk.PwdResetCtrlBean PwdResetCtrlBean}</li>
	 *   <li>read the request from the input stream</li>
	 *   <li>map the raw input stream to the reset password request container {@link com.yardi.shared.helpdesk.ResetPwdRequest ResetPwdRequest}</li>
	 *   <li>inject the request container into PwdResetCtrlBean</li>
	 *   <li>if the request is to find a user profile delegate to {@link com.yardi.ejb.helpdesk.PwdResetCtrlBean#findUserDetails() PwdResetCtrlBean.findUserDetails()}<l/i>
	 *   <li>if the request is password reset delegate to {@link com.yardi.ejb.helpdesk.PwdResetCtrlBean#resetPwd() PwdResetCtrlBean.resetPwd()}</li>
	 *   <li>feedback from the request is provided in the request container ResetPwdRequest. It is also available from
	 *   {@link com.yardi.ejb.helpdesk.PwdResetCtrlBean#getFeedback() PwdResetCtrlBean.getFeedback()}</li>
	 *   <li>respond to the web request</li>
	 * </ul>
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("com.yardi.helpdesk.PwdResetService.doGet() 000F ");
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

	/**
	 * Map the request String data to the {@link com.yardi.shared.helpdesk.ResetPwdRequest ResetPwdRequest} container.    
	 * @param request String read from the input stream 
	 * @return java representation of the String read from the request stream
	 */
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
	
	/**
	 * Read the raw input stream and store the contents in a String 
	 * @param request {@link HttpServletRequest}
	 * @return String containing the data read from the input stream
	 */
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

	/**
	 * Log the response headers, clear the content of the underlying buffer in the response without clearing headers or status code and log the response headers again.
	 * @param response {@link HttpServletResponse}
	 */
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
	
	/**
	 * Respond to the web request, {@link com.yardi.helpdesk.PwdResetService#resetBuffer(HttpServletResponse response) clear} the content of the underlying 
	 * buffer in the response without clearing headers or status code, remove stateful bean {@link com.yardi.ejb.helpdesk.PwdResetCtrlBean#remove() PwdResetCtrlBean}.
	 * set session attribute "pwdResetCtrlBean" to null and set the reference to PwdResetCtrlBean to null.
	 * 
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param formData String containing the response in JSON format
	 * @param pwdResetCtrlBean a reference to {@link com.yardi.ejb.helpdesk.PwdResetCtrlBean PwdResetCtrlBean}
	 */
	private void webResponse(HttpServletRequest request, HttpServletResponse response, String formData, PwdResetCtrl pwdResetCtrlBean) {
		System.out.println("com.yardi.helpdesk.PwdResetService.webResponse() 000D "
				+ "\n    "
				+ formData
				);
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
		} catch (IOException e) {
			System.out.println("com.yardi.helpdesk.PwdResetService.webResponse() 000E IOException ");
			e.printStackTrace();
		} 
	}
}
