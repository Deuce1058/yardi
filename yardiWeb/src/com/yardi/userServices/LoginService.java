package com.yardi.userServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.PasswordPolicySessionBeanRemote;
import com.yardi.ejb.UniqueTokensSesssionBeanRemote;
import com.yardi.ejb.UserProfileSessionBeanRemote;
import com.yardi.rentSurvey.YardiConstants;

/**
 * Servlet implementation class LoginService
 * http://localhost:8080/yardiWeb/yardiLogin.html
 */
@WebServlet("/doLogin")
public class LoginService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserServices userSvc;
	@EJB UserProfileSessionBeanRemote userProfileBean; //bean is thread safe unless marked reentrant in the deployment descriptor
	@EJB PasswordPolicySessionBeanRemote passwordPolicyBean;
	@EJB UniqueTokensSesssionBeanRemote uniqueTokensBean;
       
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
		/*
		 * When using .ajax jQuery does not put a named parm in the request, it just sends raw JSON like
		 * {"userName":"z","password":"aaa","newPassword":"","msgID":"","msgDescription":"","chgPwd":"false"}
		 * see: http://hmkcode.com/java-servlet-send-receive-json-using-jquery-ajax/
		 * 
		 * Intro to java transaction API
		 * https://www.java-tips.org/java-ee-tips-100042/17-enterprise-java-beans/1472-introduction-to-the-java-transaction-api.html
		 */
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String formData = "";
		
        if(br != null){
        	formData = br.readLine();
        }
        
		ObjectMapper mapper = new ObjectMapper();
		LoginRequest loginRequest = mapper.readValue(formData, LoginRequest.class);
		loginRequest.setPasswordPolicyBean(passwordPolicyBean);
		loginRequest.setUniqueTokensBean(uniqueTokensBean);
		loginRequest.setUserProfileBean(userProfileBean);
		/*
		 * Set boolean to indicate whether user is changing the password
		 * 
		 * In both the login page and the change password page a script sets the change password indicator and builds a JSON 
		 * object containing the change password indicator. The JSON is passed via POST and then parsed into LoginRequest. This
		 * allows methods know whether the login data originated from the regular login page or the change password page. 
         *
		 * As a convenience, there is a boolean in LoginRequest which is the same as the change password indicator. 
		 */
		loginRequest.setChangePwd(loginRequest.getChgPwd()); 
		userSvc = new UserServices(loginRequest);

    	if (loginRequest.getChangePwd()==false) { //normal login
        	userSvc.authenticate();
    	}
    	
    	if (loginRequest.getChangePwd()) { //change password
        	userSvc.chgPwd();
    	}
    	
		if (userSvc.getFeedback().equals(com.yardi.rentSurvey.YardiConstants.YRD0000)) {
			/*
			 * dispatch to the servlet that handles the main app
			 *   servlet gets data for initial display
			 *   servlet dispatches to html for main app
			 */
		}
				
		if (loginRequest.getChangePwd() ||	userSvc.getFeedback().equals(com.yardi.rentSurvey.YardiConstants.YRD0002)) {
			/*
			 * dispatch to the html for password expired 
			 *   get current pwd, new pwd and verified new pwd
			 */
			loginRequest.setUserName("");
			loginRequest.setPassword("");
			loginRequest.setNewPassword("");
			String msg [] = com.yardi.rentSurvey.YardiConstants.YRD0002.split("$");
			loginRequest.setMsgID(msg[0]);
			loginRequest.setMsgDescription(msg[1]);
			formData = mapper.writeValueAsString(LoginRequest.class); //convert the feedback to json 
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
			String msg [] = userSvc.getFeedback().split("=");
			LoginResponse loginResponse = new LoginResponse(
				loginRequest.getUserName(),
				loginRequest.getPassword(),
				"",
				msg[0],
				msg[1]
			);
			response.reset();
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			formData = mapper.writeValueAsString(loginResponse); //convert the feedback to json 
			out.print(formData);
			out.flush();
			//RequestDispatcher rd = request.getRequestDispatcher("yardiLogin.html");
			//the client gets the entire HTML document never sees the JSON object. Can see the JSON in the response if debug
			//rd.forward(request, response);
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
