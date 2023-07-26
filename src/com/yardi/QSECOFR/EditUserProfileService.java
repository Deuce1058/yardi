package com.yardi.QSECOFR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;

import jakarta.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jakarta.transaction.UserTransaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.EditUserProfileCTRL;
import com.yardi.shared.QSECOFR.EditUserProfileRequest;

/**
 * Entry point for edit user profile.<p> Receive, process and respond to requests to edit the user profile.
 */
@WebServlet(description = "Handle edit user profile requests", urlPatterns = { "/editUserProfile" })
public class EditUserProfileService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource UserTransaction tx;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
    public EditUserProfileService() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(
				  "com.yardi.QSECOFR.EditUserProfileService doGet() 0019 " 
				+ "\n    "
				+ "JSESSIONID="
				+ request.getSession(false).getId()
				);
		HttpSession session = request.getSession(false);
		InitialContext ctx;
		EditUserProfileCTRL editUserProfileCTRL = (EditUserProfileCTRL)session.getAttribute("editUserProfileCTRL");
		
		if (editUserProfileCTRL == null) {
			try {
				ctx = new InitialContext();
				editUserProfileCTRL = (EditUserProfileCTRL)ctx.lookup("java:global/yardiWeb/EditUserProfileCTRLBean");
				//debug
				System.out.println(
						 "com.yardi.QSECOFR.EditUserProfileService doGet() 0008 " 
						+ "\n    "
						+ "JSESSIONID="
						+ session.getId()
						);
				//debug
			} catch (NamingException e) {
				//debug
				System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0009 ");
				//debug
				e.printStackTrace();
			}
			session.setAttribute("editUserProfileCTRL", editUserProfileCTRL);
			//debug
			System.out.println(
					 "com.yardi.QSECOFR.EditUserProfileService doGet() 000A " 
					+ "\n    "
					+ "JSESSIONID="
					+ session.getId()
					);
			//debug
		}
		
		String formData = readBuffer(request);
		EditUserProfileRequest editRequest = mapRequest(formData);
		
		/*
		 * When the user requests another page, the current page notifies this servlet so that it can call the remove method on 
		 * stateful com.yardi.ejb.EditUserProfileCTRLBean and release session resources that are being used. Next this servlet responds
		 * to the request with YRD0000 and the current page causes the requested page to load
		 */

		if (editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_REMOVE)) {
			System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 000B ");
			remove(request, response, new EditUserProfileRequest());
			return;
		}

		editUserProfileCTRL.setEditUserProfileRequest(editRequest);
		editUserProfileCTRL.inzEditRequest();
		editUserProfileCTRL.handleRequest();
		webResponse(request, response, editUserProfileCTRL.getEditUserProfileRequest());
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Map the web request in JSON to a java representation for ease of use.
	 * 
	 * @param formData String containing the JSON web request. 
	 * @return java representation of web request 
	 */
	private EditUserProfileRequest mapRequest(String formData) {
		/*debug*/
        System.out.println("com.yardi.QSECOFR.EditUserProfileService.mapRequest() 0002 ");
		/*debug*/
		try {
			EditUserProfileRequest editRequest = new EditUserProfileRequest();
			ObjectMapper mapper = new ObjectMapper();
			editRequest = mapper.readValue(formData, EditUserProfileRequest.class);
			return editRequest;
		} catch (JsonProcessingException e) {
			/*debug*/
			System.out.println("com.yardi.QSECOFR.EditUserProfileService.mapRequest() exception 0003 ");
			/*debug*/
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Read the request from the input stream.
	 * 
	 * @param request a HttpServletRequest
	 * @return String containing the web request in JSON format
	 */
	private String readBuffer(HttpServletRequest request) {
		/*debug*/
		System.out.println("com.yardi.QSECOFR.EditUserProfileService.readBuffer() 0004 ");
		/*debug*/
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String formData = new String("");
			
	        if(br != null){
	        	formData = br.readLine();
	        }
	        
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService.readBuffer() 0000 "
	        	+ "\n"
	        	+ "   formData=" + formData);
	        return formData;
		} catch (IOException e) {
			/*debug*/
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService.readBuffer() exception 0001 ");
   			/*debug*/
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Release session resources.<p>
	 * Call the remove method on stateful com.yardi.ejb.EdidUserProfileCTRLBean so it can release the resources it is using.
	 * 
	 * @param request a HttpServletRequest
	 * @param response a HttpServletResponse
	 * @param editRequest POJO representation of the web request
	 * @throws IOException Signals that an I/O exception of some sort has occurred. 
	 */
	private void remove(HttpServletRequest request, HttpServletResponse response, EditUserProfileRequest editRequest) throws IOException {
		//debug
		System.out.println("com.yardi.QSECOFR.EditUserProfileService remove() 0007 ");
		//debug
		EditUserProfileCTRL editUserProfileCTRLbean = (EditUserProfileCTRL)request.getSession(false).getAttribute("editUserProfileCTRL");
		request.getSession(false).setAttribute("editUserProfileCTRL", null);
		editUserProfileCTRLbean.removeBean();
		String feedback [] = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
		editRequest.setMsgID(feedback[0]);
		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0014.split("=");
		editRequest.setMsgDescription(feedback[1]);
		webResponse(request, response, editRequest); 
	}
	
	/**
	 * Log the response headers in the HttpServletResponse.
	 * @param request a HttpServletRequest
	 * @param response a HttpServletResponse
	 */
	private void showResponseHeaders(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(
				 "com.yardi.QSECOFR.EditUserProfileService showResponseHeaders() 000C " 
				+ "\n    "
				+ "JSESSIONID="
				+ request.getSession(false).getId()
				);
		Collection<String> headerNames = response.getHeaderNames();
		
		if (headerNames.isEmpty()) {
			System.out.println("com.yardi.QSECOFR.EditUserProfileService showResponseHeaders() 0014 headerNames is empty");
		}
		
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.QSECOFR.EditUserProfileService showResponseHeaders() 0015 "
						+ "\n"
						+ "   Response header name="
						+ n
						+ "   no headerValues");
			}
			
			for (String v :  headerValues) {
				System.out.println("com.yardi.QSECOFR.EditUserProfileService showResponseHeaders() 0016 "
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
	 * Respond to the web request by converting the java representation of the request into JSON. 
	 * 
	 * @param request a HttpServletRequest
	 * @param response a HttpServletResponse
	 * @param editRequest POJO representation of the web request
	 */
	private void webResponse(HttpServletRequest request, HttpServletResponse response, EditUserProfileRequest editRequest) {
		/*debug*/
		System.out.println(
				 "com.yardi.QSECOFR.EditUserProfileService.webResponse() 0005 " 
				+ "\n    "
				+ "JSESSIONID="
				+ request.getSession(false).getId()
				);
		/*debug*/
		ObjectMapper mapper = new ObjectMapper();
		showResponseHeaders(request, response);
		response.resetBuffer();
		showResponseHeaders(request, response);
		response.setContentType("application/json");

		try {
			PrintWriter out = response.getWriter();
			String formData = mapper.writeValueAsString(editRequest); //convert the feedback to json 
			out.print(formData);
			out.flush();
		} catch (IOException e1) {
			/*debug*/
			System.out.println("com.yardi.QSECOFR.EditUserProfileService.webResponse() exception 0006 ");
			/*debug*/
			e1.printStackTrace();
		}
		
		System.out.println(
				 "com.yardi.QSECOFR.EditUserProfileService.webResponse() 000D "
				+ "\n    "
				+ "JSESSIONID="
				+ request.getSession(false).getId()
				);
	}
}
