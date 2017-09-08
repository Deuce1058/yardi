package com.yardi.userServices;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.rentSurvey.YardiConstants;

/**
 * Servlet implementation class LoginService
 */
@WebServlet("/doLogin")
public class LoginService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserServices userSvc;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginService() {
        super();
    }
    
    public void init() {
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		String formData = request.getParameter("formData");

		ObjectMapper mapper = new ObjectMapper();
		LoginData loginData = mapper.readValue(formData, LoginData.class);
		/*
		 * Set boolean to indicate whether user is changing the password
		 * 
		 * In both the login page and the change password page a script sets the change password indicator and builds a JSON 
		 * object that contains the change password indicator. The JSON is passed via POST and then parsed into LoginData. This
		 * allows methods know whether the login data originated from the regular login page or the change password page. 
         *
		 * As a convenience, there is a boolean in LoginData which is the same as the change password indicator. 
		 */
		loginData.setChangePwd(loginData.getChgPwd()); 
		userSvc = new UserServices(loginData);

    	if (loginData.getChangePwd()==false) { //normal login
        	userSvc.authenticate();
    	}
    	
    	if (loginData.getChangePwd()) { //change password
        	userSvc.chgPwd();
    	}
    	
		if (userSvc.getFeedback().equals(com.yardi.rentSurvey.YardiConstants.YRD0000)) {
			/*
			 * dispatch to the servlet that handles the main app
			 *   servlet gets data for initial display
			 *   servlet dispatches to html for main app
			 */
		}
				
		if (loginData.getChangePwd() ||	userSvc.getFeedback().equals(com.yardi.rentSurvey.YardiConstants.YRD0002)) {
			/*
			 * dispatch to the html for password expired 
			 *   get current pwd, new pwd and verified new pwd
			 */
			loginData.setChgPwd("true");
			loginData.setChangePwd(loginData.getChgPwd()); 
			loginData.setUserName("");
			loginData.setPassword("");
			loginData.setNewPassword("");
			String msg [] = com.yardi.rentSurvey.YardiConstants.YRD0002.split("$");
			loginData.setMsgID(msg[0]);
			loginData.setMsgDescription(msg[1]);
			formData = mapper.writeValueAsString(LoginData.class); //convert the feedback to json 
			request.setAttribute("formData", formData);
			RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/changePwd.html");
			rd.forward(request, response);
		}
		
		//YRD0001$Invalid user name or pasword
		//YRD0003$This account is disabled
		//YRD0004$This account is not active
		//YRD0005$Password is too short
		//YRD0006$Password must contain at least 1 upper case
		//YRD0007$Password must contain at least 1 lower case
		//YRD0008$Password must contain at least 1 number
		//YRD0009$Password must contain at least 1 special character
		//YRD000A$Password matches a password that was previously used
		//YRD000B$Password policy is missing
		//YRD000C$Maximum signon attempts exceeded. The user profile has been disabled
		
		if (userSvc.getFeedback().equals(com.yardi.rentSurvey.YardiConstants.YRD0000)==false) {
			loginData.setChgPwd("false");
			loginData.setChangePwd(loginData.getChgPwd()); 
			loginData.setNewPassword("");
			String msg [] = userSvc.getFeedback().split("$");
			loginData.setMsgID(msg[0]);
			loginData.setMsgDescription(msg[1]);
			formData = mapper.writeValueAsString(LoginData.class); //convert the feedback to json 
			request.setAttribute("formData", formData);
			RequestDispatcher rd = request.getRequestDispatcher("yardiLogin.html");
			rd.forward(request, response);
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
