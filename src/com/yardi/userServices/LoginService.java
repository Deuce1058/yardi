package com.yardi.userServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

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
import com.yardi.ejb.UserGroups;
import com.yardi.ejb.UserGroupsSessionBeanRemote;
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
	@EJB UserGroupsSessionBeanRemote userGroupsBean;
       
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
			 * Successful login.
			 * 1 lookup initial page with join GroupsMaster and UserGroups
			 *   1A if user is in multiple groups set ST_LAST_REQUEST to the html select group page. User picks the initial page
			 *   1B if user is in only one group set ST_LAST_REQUEST to GM_INITIAL_PAGE
			 * 2 Set user ID as session attribute  
			 * 3 Write/update session table
			 *   3A tokenize session ID. This serves as a password for the session to login. It is not enough for the session 
			 *      to be in the session table, the session must also login in order for the session to be considered authentic.
			 *   3B CreateTokenService is used to create a token from the session ID
			 * 4 Respond to yardiLogin.html/changePWD.html  
			 */
			Vector<UserGroups> userGroups = new Vector<UserGroups>();
			//what groups is the user in and what is the initial page for the group?
			userGroups = userGroupsBean.find(loginRequest.getUserName());
			
			//store the userID in the session
			request.getSession().setAttribute("userID", loginRequest.getUserName()); 

			/* 
			 * Setup the parms CreateTokenService needs for generating a token from the session ID. This token will be 
			 * stored in the session table. CreateTokenService expects to get parms in a JSON object
			 */
			StringBuilder grouplist = new StringBuilder("");
			String sessionID = request.getSession().getId();
			TokenRequest tokenRequest = new TokenRequest(sessionID, "", "");
			response.reset();
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			formData = mapper.writeValueAsString(tokenRequest);  
			out.print(formData);
			out.flush();
			RequestDispatcher rd = request.getRequestDispatcher("../QSECOFR/CreateTokenService");
			rd.include(request, response); //the token for the session is the tokenized session ID
			// CreateTokenService sends back the requested token in the input stream 
			br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			formData = "";
			
	        if(br != null){
	        	formData = br.readLine();
	        }
	        
	        mapper = new ObjectMapper();
			tokenRequest = mapper.readValue(formData, TokenRequest.class);
			
			//fetch the session table row for the session
			SessionsTable sessionsTable = null;
			sessionsTable = sessionsBean.findSession(sessionID); 
			userGroups.get(0);
			String initialPage = userGroups.get(0).getMasterGroup().getGmInitialPage(); //GM_INITIAL_PAGE from GROUPS_MASTER
			
			if (userGroups.size()>1) {
				// user is in multiple groups. Set ST_LAST_REQUEST to the html select group page. User picks the initial page
				initialPage = com.yardi.rentSurvey.YardiConstants.USER_SELECT_GROUP_PAGE;
				for (UserGroups g : userGroups) {
					grouplist.append(g.getMasterGroup().getGmDescription() + ";"); //build a list of the groups
				}
			}

			if (sessionsTable == null) {
				sessionsBean.persist(
						loginRequest.getUserName(), 
						sessionID, 
						tokenRequest.getPassword(), 
						initialPage, 
						new java.util.Date());
			} else {
				sessionsBean.update(
						loginRequest.getUserName(), 
						sessionID, 
						tokenRequest.getPassword(), 
						initialPage, 
						new java.util.Date());
			}
			
			//Respond to yardiLogin.html. The page sees that the login request is successful (YRD0000) and looks at the 
			//5th parm (initialPage) in loginResponse to get the next page to load. yardiLogin.html tells index.html to
			//load the initialPage page.
			String msg [] = com.yardi.rentSurvey.YardiConstants.YRD0000.split("=");

			if (userGroups.size()>1) {
				msg = com.yardi.rentSurvey.YardiConstants.YRD000E.split("=");
			}
			
			LoginResponse loginResponse = new LoginResponse(
				loginRequest.getUserName(),
				"",  //Password
				grouplist.toString(),  
				msg[0],
				initialPage
			);
			response.reset();
			response.setContentType("application/json");
			out = response.getWriter();
			formData = mapper.writeValueAsString(loginResponse); //convert the feedback to json 
			out.print(formData);
			out.flush();
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
