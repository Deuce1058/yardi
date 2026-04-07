package com.yardi.QSECOFR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
//import java.util.Collection;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.EditPasswordPolicy;
//import com.yardi.ejb.EditPasswordPolicyBean;
//import com.yardi.ejb.UserServices;
//import com.yardi.ejb.UserProfile;
import com.yardi.shared.QSECOFR.EditPwdPolicyRequest;
//import com.yardi.userServices.InvalidSessionException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;

/**
 * Handle requests to edit password policy. Entry point for edit password policy app.
 */
@WebServlet(description = "Handle edit password policy requests", urlPatterns = {"/PwdPolicy"})
public class EditPwdPolicyService extends HttpServlet implements Serializable {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Map the request String data to the {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest EditPwdPolicyRequest} container.    
	 * @param formdata String read from the input stream 
	 * @return java representation of the String read from the request stream
	 */
	private EditPwdPolicyRequest deserializeEditPasswordPolicyRequest(String formdata) {
		System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.deserializeEditPasswodPolicyRequest() 0004 ");
		ObjectMapper mapper = new ObjectMapper();
		EditPwdPolicyRequest editRequest = null;

		try {
			editRequest = mapper.readValue(formdata, EditPwdPolicyRequest.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return editRequest;
	}
	
	/**
	 * Obtain an instance of {@link com.yardi.ejb.EditPasswordPolicyBean#EditPasswordPolicyBean() com.yardi.ejb.EditPasswordPolicyBean} from JNDI. 
	 * Read the edit request from the input stream and map it to {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest#EditPwdPolicyRequest() 
	 * com.yardi.shared.QSECOFR.EditPwdPolicyRequest}.
	 * Determine the requested action (copy, find, add or update) and call the appropriate method on EditPasswordPolicyBean to handle the request.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.doGet() 0000 "
				+ "\n "
				+ "sessionId=" 
				+ request.getSession().getId()
				);		
		EditPasswordPolicy editPasswordPolicyBean = getEditPasswordPolicyBean();
		String formdata = readRawBufferedLine(request);
		EditPwdPolicyRequest editRequest = deserializeEditPasswordPolicyRequest(formdata);
		editRequest = handleEditPasswordPolicyRequest(editRequest.getAction(), editPasswordPolicyBean, editRequest);
		formdata = serializeEditPasswordPolicyRequest(editRequest);
		webResponse(response, formdata);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Get a reference to {@link com.yardi.ejb.EditPasswordPolicyBean EditPasswordPolicyBean} using JNDI Lookup  
	 * @return reference to com.yardi.ejb.EditPasswordPolicyBean
	 */
	private EditPasswordPolicy getEditPasswordPolicyBean() {
		System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.getEditPasswordPolicyBean() 0003 ");
		InitialContext ctx;
		EditPasswordPolicy editPasswordPolicyBean = null;
		
		try {
			ctx = new InitialContext();
			editPasswordPolicyBean = (EditPasswordPolicy)ctx.lookup("java:global/yardiWeb/EditPasswordPolicyBean");
		} catch (NamingException e) {
			System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.getEditPasswordPolicyBean() 0001 ");
			e.printStackTrace();
		}
		
		return editPasswordPolicyBean;
	}


	/**
	 * Handles various password policy actions such as copy, find, add, and update.
	 * Based on the provided action string, this method delegates the request to the
	 * appropriate method in the {@code EditPasswordPolicy} bean and returns the
	 * resulting {@code EditPwdPolicyRequest}.
	 *
	 * <p>Supported actions include:
	 * <ul>
	 *   <li>PASSWORD_POLICY_COPY - Copies an existing password policy</li>
	 *   <li>EDIT_PASSWORD_POLICY_REQUEST_ACTION_FIND - Retrieves a password policy</li>
	 *   <li>EDIT_PASSWORD_POLICY_REQUEST_ACTION_ADD - Persists a new password policy</li>
	 *   <li>EDIT_PASSWORD_POLICY_REQUEST_ACTION_UPDATE - Updates an existing password policy</li>
	 * </ul>
	 *
	 * @param action the action to be performed, which determines the operation type
	 * @param editPasswordPolicyBean the business logic bean used to process password policy operations
	 * @param editRequest the request object containing password policy data for add or update operations
	 * @return an {@code EditPwdPolicyRequest} containing the result of the requested operation,
	 *         or {@code null} if an exception occurs or the action is not recognized
	 */
	private EditPwdPolicyRequest handleEditPasswordPolicyRequest(String action, EditPasswordPolicy editPasswordPolicyBean, EditPwdPolicyRequest editRequest) {
		System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.handleEditPasswordPolicyRequest() 0007 ");
		
			if (action.equals(com.yardi.shared.rentSurvey.YardiConstants.PASSWORD_POLICY_COPY)) {
				System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.handleEditPasswordPolicyRequest() 000D ");
				return editPasswordPolicyBean.fromPolicyCopy();
			}			
			
			if (action.equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_PASSWORD_POLICY_REQUEST_ACTION_FIND)) {
				System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.handleEditPasswordPolicyRequest() 0008 ");
				return editPasswordPolicyBean.fromPwd_Policy();
			}

			if (action.equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_PASSWORD_POLICY_REQUEST_ACTION_ADD)) {
				System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.handleEditPasswordPolicyRequest() 000A ");
				editPasswordPolicyBean.persist(editRequest);
				return editPasswordPolicyBean.fromPwd_Policy();
			}

			if (action.equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_PASSWORD_POLICY_REQUEST_ACTION_UPDATE)) {
				System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.handleEditPasswordPolicyRequest() 000B ");
				editPasswordPolicyBean.updateAll(editRequest);
				return editPasswordPolicyBean.fromPwd_Policy();
			}
		
			return null;
	}
	
	
	/**
	 * Read the raw input stream and store the contents in a String 
	 * @param request {@link HttpServletRequest}
	 * @return String containing the data read from the input stream
	 */
	private String readRawBufferedLine(HttpServletRequest request) {
		System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.readRawBufferedLine() 0002 ");
		String formData = ""; 		 
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			
			if(br != null){ 
				formData = br.readLine();
			} 		  
		} catch (IOException e) {
			System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.readRawBufferedLine() 0005 Exception ");
			e.printStackTrace();
		} 

		return formData;
	}
	
	/**
	 * Serialize the edit request
	 * @param editRequest EditRequest to be serialized
	 * @return String containing the serialized EditRequest 
	 */
	private String serializeEditPasswordPolicyRequest(EditPwdPolicyRequest editRequest) {
		System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.serializeEditPasswordPolicyRequest() 000E ");
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(editRequest);
		} catch (JsonProcessingException e) {
			System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.serializeEditPasswordPolicyRequest() 000F ");
			e.printStackTrace();
			return null;
		}
	}	
	
	/**
	 * Reset response buffer, set content type, print the response formatted as JSON and flush output buffer
	 * @param response {@link jakarta.servlet.http.HttpServletResponse} from find()
	 * @param formData a JSON string containing the web response 
	 * @throws IOException an I/O exception
	 */
	private void webResponse(HttpServletResponse response, String formData) throws IOException {
		System.out.println("com.yardi.QSECOFR.EditPwdPolicyService.webResponse() 0006 ");
		response.resetBuffer();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(formData);
		out.flush();
	}
}