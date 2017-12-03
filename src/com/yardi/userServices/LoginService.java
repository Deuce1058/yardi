package com.yardi.userServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import com.yardi.ejb.SessionsTable;
import com.yardi.ejb.SessionsTableSessionBeanRemote;
import com.yardi.ejb.UniqueTokensSesssionBeanRemote;
import com.yardi.ejb.UserProfileSessionBeanRemote;
import com.yardi.rentSurvey.YardiConstants;
import com.yardi.QSECOFR.TokenRequest;

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
	@EJB SessionsTableSessionBeanRemote sessionsBean;
       
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
		//debug
		System.out.println("com.yardi.userServices LoginService doGet() 0008 " + toString());
		//debug
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
		//debug
		System.out.println("com.yardi.userServices LoginService doGet() 0009 " + loginRequest.toString());
		//debug
		userSvc = new UserServices(loginRequest);

    	if (loginRequest.getChangePwd()==false) { //normal login
    		//debug
    		System.out.println("com.yardi.userServices LoginService doGet() 0000 "
    				+ "\n "
    				+ "  formData =" + formData
    				+ "\n "
    				+ "  loginRequest = " + loginRequest
    				);
    		//debug
        	userSvc.authenticate();
    	}

    	//debug
		System.out.println("com.yardi.userServices LoginService doGet() 0006 "
				+ "\n "
				+ "  loginRequest = " + loginRequest
				);
		//debug
    	
    	if (loginRequest.getChangePwd()) { //change password
    		//debug
    		System.out.println("com.yardi.userServices LoginService doGet() 0001 "
    				+ "\n "
    				+ "  loginRequest.getChangePwd() =" + loginRequest.getChangePwd()
    				+ "\n "
    				+ "  loginRequest = " + loginRequest
    				);
    		//debug
        	userSvc.chgPwd();
    	}
    	
		if (userSvc.getFeedback().equals(com.yardi.rentSurvey.YardiConstants.YRD0000)) {
			/*
			 * lookup initial page with join GroupsMaster and UserGroups
			 * if user is in multiple groups set ST_LAST_REQUEST to the html select group page. User picks the initial page
			 * if user is in only one group set ST_LAST_REQUEST to GM_INITIAL_PAGE
			 */
			request.getSession().setAttribute("userID", loginRequest.getUserName());
			String sessionID = request.getSession().getId();
			TokenRequest tokenRequest = new TokenRequest(sessionID, "", "");
			response.reset();
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			formData = mapper.writeValueAsString(tokenRequest);  
			out.print(formData);
			out.flush();
			RequestDispatcher rd = request.getRequestDispatcher("../QSECOFR/CreateTokenService");
			rd.include(request, response); //get a token for the session ID
			br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			formData = "";
			
	        if(br != null){
	        	formData = br.readLine();
	        }
	        
			mapper = new ObjectMapper();
			tokenRequest = mapper.readValue(formData, TokenRequest.class);
			SessionsTable sessionsTable = null;
			sessionsTable = sessionsBean.findSession(sessionID);
			
			if (sessionsTable == null) {
				sessionsBean.persist(
						loginRequest.getUserName(), 
						sessionID, 
						tokenRequest.getPassword(), 
						"GM_INITIAL_PAGE from GROUPS_MASTER", 
						new java.util.Date());
			} else {
				sessionsBean.update(
						loginRequest.getUserName(), 
						sessionID, 
						tokenRequest.getPassword(), 
						"GM_INITIAL_PAGE from GROUPS_MASTER", 
						new java.util.Date());
			}
			
	    	//debug xyzzy
			System.out.println("com.yardi.userServices LoginService doGet() 0007 "
					+ "\n "
					+ "  loginRequest = " + loginRequest
					);
			//debug
			return;
		}
				
		if (loginRequest.getChangePwd() || userSvc.getFeedback().equals(com.yardi.rentSurvey.YardiConstants.YRD0002)) {
    		//debug
    		System.out.println("com.yardi.userServices LoginService doGet() 0002 "
    				+ "\n "
    				+ "  loginRequest.getChangePwd() =" + loginRequest.getChangePwd()
    				+ "\n "
    				+ "  loginRequest = " + loginRequest
    				+ "\n "
    				+ "  userSvc.getFeedback() = " + userSvc.getFeedback()
    				);
    		//debug
			
			if (loginRequest.getChangePwd()==false) {
				response.reset();
				response.setContentType("application/json");
				String msg[] = userSvc.getFeedback().split("=");
				LoginResponse loginResponse = new LoginResponse("", "", "", msg[0], "views/changePwd.html");
				PrintWriter out = response.getWriter();
				formData = mapper.writeValueAsString(loginResponse); //convert the feedback to json 
				out.print(formData);
				out.flush();
	    		//debug
	    		System.out.println("com.yardi.userServices LoginService doGet() 0003 "
	    				+ "\n "
	    				+ "  loginRequest.getChangePwd() == false"
	    				+ "\n "
	    				+ "  loginRequest = " 
	    				+ loginRequest  
	    				+ "\n "
	    				+ "  formData = "
	    				+ formData
	    		);
	    		//debug
				return;
			} 
			
			String msg [] = userSvc.getFeedback().split("=");
			LoginResponse loginResponse = new LoginResponse( 
				loginRequest.getUserName(),
				loginRequest.getPassword(),
				loginRequest.getNewPassword(),
				msg[0],
				msg[1]);
			response.reset();
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			formData = mapper.writeValueAsString(loginResponse); //convert the feedback to json 
			out.print(formData);
    		//debug
    		System.out.println("com.yardi.userServices LoginService doGet() 0004 "
    				+ "\n "
    				+ "  loginResponse =" + loginResponse
    				+ "\n "
    				+ "  formData = " + formData
    				);
    		//debug
			out.flush();
			return;
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
    		//debug
    		System.out.println("com.yardi.userServices LoginService doGet() 0005 "
    				+ "\n "
    				+ "  userSvc.getFeedback() =" + userSvc.getFeedback()
    				+ "\n "
    				+ "  loginResponse =" + loginResponse
    				+ "\n "
    				+ "  formData = " + formData
    				+ "\n "
    				);
    		//debug
			out.flush();
			return;
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public String toString() {
		return "LoginService [userProfileBean=" + userProfileBean + ", passwordPolicyBean="
				+ passwordPolicyBean + ", uniqueTokensBean=" + uniqueTokensBean + "]"
				+ "\n  "
				+ userProfileBean.stringify() 
				+ "\n  "
				+ passwordPolicyBean.stringify() 
				+ "\n  "
				+ uniqueTokensBean.stringify();
	}
}
