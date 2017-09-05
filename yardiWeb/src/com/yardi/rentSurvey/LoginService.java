package com.yardi.rentSurvey;

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
import com.yardi.userServices.PasswordAuthentication;
import com.yardi.userServices.UserServices;

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
		ObjectMapper mapper = new ObjectMapper();
		String formData = request.getParameter("formData");
		LoginData loginData = mapper.readValue(formData, LoginData.class);
		String user = loginData.getUserName();
		String pwd = loginData.getPassword();
		out.println("Raw form data: " + formData);
		out.println("User: " + user);
		out.println("Pwd: " + pwd);
    	userSvc = new UserServices(user);
		userSvc.authenticate(user, pwd.toCharArray());
		
		if (userSvc.getFeedback().equals(com.yardi.rentSurvey.YardiConstants.YRD0000)) {
			/*
			 * dispatch to the servlet that handles the main app
			 *   servlet gets data for initial display
			 *   servlet dispatches to html for main app
			 */
		} else {
			
			if (userSvc.getFeedback().equals(com.yardi.rentSurvey.YardiConstants.YRD0002)) {
				/*
				 * dispatch to the html for password expired 
				 *   get current pwd, new pwd and verified new pwd
				 */
				loginData.setChangePwd("true");
				loginData.setChgPwd("true");
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
			} else {
				
			}
			
			//return to the login form and give feedback
			// see http://wiki.fasterxml.com/JacksonInFiveMinutes
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
