package com.yardi.QSECOFR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.EditPasswordPolicy;
import com.yardi.ejb.EditPasswordPolicyBean;
import com.yardi.ejb.LoginUserServices;
import com.yardi.ejb.UserProfile;
import com.yardi.userServices.InvalidSessionException;

import com.yardi.QSECOFR.EditPwdPolicyRequest;

/**
 * Servlet implementation class EditPwdPolicyService
 */
@WebServlet(description = "Handle edit password policy requests", urlPatterns = {"/PwdPolicy"})
public class EditPwdPolicyService extends HttpServlet implements Serializable {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//debug
		System.out.println("com.yardi.QSECOFR.EditPwdPolicyService doGet() 0000 "
				+ "\n "
				+ "   sessionId=" + request.getSession().getId());
		//debug
		InitialContext ctx;
		EditPasswordPolicy editPasswordPolicyBean = null;
		
		try {
			ctx = new InitialContext();
			editPasswordPolicyBean = (EditPasswordPolicy)ctx.lookup("java:global/yardiWeb/EditPasswordPolicyBean");
		} catch (NamingException e) {
			System.out.println("com.yardi.QSECOFR.EditPwdPolicyService doGet() 0001 ");
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String formData = "";
		
        if(br != null){
        	formData = br.readLine();
        }
        
		ObjectMapper mapper = new ObjectMapper();
		EditPwdPolicyRequest editRequest = mapper.readValue(formData, EditPwdPolicyRequest.class);
				
		if (editRequest.getAction().equals(com.yardi.rentSurvey.YardiConstants.EDIT_PASSWORD_POLICY_REQUEST_ACTION_FIND)) {
			//debug
			System.out.println("com.yardi.QSECOFR.EditPwdPolicyService doGet() 0008 ");
			//debug
			find(editPasswordPolicyBean, response);
			return;
		}
		
		if (editRequest.getAction().equals(com.yardi.rentSurvey.YardiConstants.EDIT_PASSWORD_POLICY_REQUEST_ACTION_ADD)) {
			//debug
			System.out.println("com.yardi.QSECOFR.EditPwdPolicyService doGet() 000A ");
			//debug
			editPasswordPolicyBean.persist(editRequest);
			find(editPasswordPolicyBean, response);
			return;
		}
		
		if (editRequest.getAction().equals(com.yardi.rentSurvey.YardiConstants.EDIT_PASSWORD_POLICY_REQUEST_ACTION_UPDATE)) {
			//debug
			System.out.println("com.yardi.QSECOFR.EditPwdPolicyService doGet() 000B ");
			//debug
			editPasswordPolicyBean.updateAll(editRequest);
			find(editPasswordPolicyBean, response);
			return;
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void find(EditPasswordPolicy editPasswordPolicyBean, HttpServletResponse response) throws IOException {
		//debug
		System.out.println("com.yardi.QSECOFR.EditPwdPolicyService find() 0009 ");
		//debug
		EditPwdPolicyRequest editRequest = editPasswordPolicyBean.getPwd_Policy();
		ObjectMapper mapper = new ObjectMapper();
		String formData = mapper.writeValueAsString(editRequest); //convert the feedback to json
		webResponse(response, formData);
	}	
	
	private void webResponse(HttpServletResponse response, String formData) throws IOException {
		//debug
		System.out.println("com.yardi.QSECOFR.EditPwdPolicyService webResponse() 0006 ");
		//debug
		response.resetBuffer();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(formData);
		out.flush();
	}
}