package com.yardi.userServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.UserServices;
import com.yardi.shared.userServices.LoginRequest;
import com.yardi.shared.userServices.LoginResponse;

import java.util.Collection;

/**
 * Entry point for processing login requests.<p>
 * http://localhost:8080/yardiWeb/yardiLogin.html
 */
@WebServlet("/doLogin")
public class LoginHandler extends HttpServlet {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginHandler() {
        super();
    }
    
    /**
	 * Ensure that the UserServices object stored in the session is the same object that was originally obtained from JNDI.<p>
	 * 
	 * When a reference to UserServices is obtained from JNDI, the HTTP session ID is stored on the object.<p>
	 * 
	 * Get the session<br>
	 * Get the UserServicesBean either from the session or JNDI<br>
	 * If UserServicesBean came from JNDI, store the session ID on UserServicesBean<br>
	 * If the session ID from UserServicesBean does not match <code>HttpServletRequest.getSession()</code> throw InvalidSessionException
	 * 
	 * @param request {@link HttpServletRequest HttpServletRequest} 
	 * @return UserServices {@link com.yardi.ejb.UserServicesBean#UserServicesBean() com.yardi.ejb.UserServicesBean} 
	 * @throws InvalidSessionException {@link com.yardi.userServices.InvalidSessionException#InvalidSessionException(String, String) 
	 *     com.yardi.userServices.InvalidSessionException}
	 */
	private UserServices checkSession(HttpServletRequest request) throws InvalidSessionException {
		HttpSession session = request.getSession();
		//debug
		System.out.println("com.yardi.userServices.LoginHandler checkSession() 000D "
			+ "\n"
			+ "    sessionID=" + session.getId());
		//debug

		UserServices userSvcBean = (UserServices)session.getAttribute("userSvcBean");
 
		if (userSvcBean == null) {
			try {
				//debug
				System.out.println("com.yardi.userServices.LoginHandler checkSession() 000A ");
				//debug
				InitialContext ctx = new InitialContext();
				userSvcBean = (UserServices)ctx.lookup("java:global/yardiWeb/UserServicesBean");
			} catch (NamingException e) {
				//debug
				System.out.println("com.yardi.userServices.LoginHandler checkSession() 000B ");
				//debug
				e.printStackTrace();
				return null;
			}
			
			userSvcBean.setSessionID(session.getId());
			session.setAttribute("userSvcBean", userSvcBean);
			//debug
			System.out.println("com.yardi.userServices.LoginHandler checkSession() 000C");
			//debug
		}
		
		
		if (!(userSvcBean.getSessionID().equals(session.getId()))) {
			throw new InvalidSessionException(session.getId(), userSvcBean.getSessionID());
		}
		
		return userSvcBean;
	}
	
	/**
	 * Give feedback from the change password process
	 * 
	 * @param request {@link HttpServletRequest HttpServletRequest} 
	 * @param response {@link HttpServletResponse HttpServletResponse}
	 * @param userSvcBean {@link com.yardi.ejb.UserServicesBean#UserServicesBean() com.yardi.ejb.UserServicesBean} 
	 * @param loginRequest {@link com.yardi.shared.userServices.LoginRequest#LoginRequest() com.yardi.shared.userServices.LoginRequest}
	 * @param mapper provides functionality for converting between Java objects and matching JSON constructs.
	 * @throws IOException {@link IOException IOException}
	 * @throws JsonProcessingException signals a problem encountered when processing JSON content that is not a pure I/O problem
	 */
	private void chgPwdFeedback(HttpServletRequest request, HttpServletResponse response, UserServices userSvcBean, LoginRequest loginRequest, ObjectMapper mapper) 
			throws IOException, JsonProcessingException {
		//debug
		System.out.println("com.yardi.ejb.LoginHandler chgPwdFeedback() 002D ");
		//debug
		String msg [] = userSvcBean.getFeedback().split("=");
		String formData = mapper.writeValueAsString(
			new LoginResponse(
					loginRequest.getUserName(),
					loginRequest.getPassword(),
					loginRequest.getNewPassword(),
					msg[0],
					msg[1]
		)); 
		System.out.println("com.yardi.userServices.LoginHandler chgPwdFeedback() 0004 "
				+ "\n "
				+ "  formData = " + formData
				);
		//debug
		webResponse(request, response, formData, userSvcBean);
		//debug
	}
	
	/**
	 * Handle login requests. Entry point. Begin by calling <code>checkSession()</code> to test whether the session ID on the request matches the session ID  
	 * stored on {@link com.yardi.ejb.UserServicesBean#UserServicesBean() com.yardi.ejb.UserServicesBean}.<p>
	 * 
	 * Summary of key points in doGet():
	 * 
	 * <ul>
	 *   <li>Get the request from the input stream.</li>
	 *   <li>If the password is not being changed delegate to <code>com.yardi.ejb.UserServicesBean.authenticate()</code></li>
	 *   <li>If the password is being changed delegate to <code>com.yardi.ejb.UserServicesBean.chgPwd()</code></li>
	 *   <li>If the login was successful, delegate to <code>loginSuccess()</code>.</li>
	 *   <li>If the password has expired and the password is not currently being changed call <code>informChgPwd()</code> to construct a response to inform that the 
	 *       password must be changed.</li>
	 *   <li>If the password is currently being changed call <code>chgPwdFeedback()</code> to construct a response that provides feedback on the 
	 *       process of changing the password.</li>
	 *   <li>If the login was unsuccessful for any other reason call <code>otherFeedback()</code> to construct a response that provides feedback on the login request</li>
	 * </ul> 
	 * 
	 * @param request see HttpServletRequest
	 * @param response see HttpServletResponse
	 * @throws ServletException a general servlet exception 
	 * @throws IOException an I/O exception of some sort
	 * 
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
		System.out.println("com.yardi.userServices.LoginHandler doGet() 0008 ");
		UserServices userSvcBean;
		
		try {
			userSvcBean = checkSession(request);
		} catch (InvalidSessionException e1) {
			e1.printStackTrace();
			return;
		}
	
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String formData = "";
		
	    if(br != null){
	    	formData = br.readLine();
	    }
	    
		ObjectMapper mapper = new ObjectMapper();
		LoginRequest loginRequest = mapper.readValue(formData, LoginRequest.class);
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
		System.out.println("com.yardi.userServices.LoginHandler doGet() 0009 " 
				+ "\n   "
				+ loginRequest.toString());
		//debug
		userSvcBean.setLoginRequest(loginRequest);
	
		if (loginRequest.getChangePwd()==false) { 
			//debug
			System.out.println("com.yardi.ejb.LoginHandler doGet() 0026 "
					+ "\n "
					+ "  formData =" + formData
					+ "\n "
					+ "  loginRequest = " + loginRequest
					);
			//debug
			userSvcBean.authenticate();	
		}
	
		//debug
		System.out.println("com.yardi.userServices.LoginHandler doGet() 0006 "
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
			userSvcBean.chgPwd();
		}
		
		/*
		 * They have either successfully logged in without changing the password or they successfully changed the password
		 */
		if (userSvcBean.getFeedback().equals(com.yardi.shared.rentSurvey.YardiConstants.YRD0000) || 
			userSvcBean.getFeedback().equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000E)) {
			//debug
			System.out.println("com.yardi.ejb.LoginHandler doGet() 0028 ");
			//debug
			
			loginSuccess(loginRequest, request, response, mapper, userSvcBean);
			return;
		}
				
		if (loginRequest.getChangePwd() || 
			userSvcBean.getFeedback().equals(com.yardi.shared.rentSurvey.YardiConstants.YRD0002) ||
			userSvcBean.getFeedback().equals(com.yardi.shared.rentSurvey.YardiConstants.YRD001B)
			) {
			//debug
			System.out.println("com.yardi.userServices.LoginHandler doGet() 0002 "
					+ "\n "
					+ "  loginRequest.getChangePwd() =" + loginRequest.getChangePwd()
					+ "\n "
					+ "  loginRequest = " + loginRequest
					+ "\n "
					+ "  userSvcBean.getFeedback() = " + userSvcBean.getFeedback()
					);
			//debug
			
			//the password has expired and they are not yet changing the password. Inform them they need to change the password
			if (loginRequest.getChangePwd()==false) {
				//debug
				System.out.println("com.yardi.ejb.LoginHandler doget() 002B ");
				//debug
				informChgPwd(request, response, userSvcBean, mapper, loginRequest);
				return;
			} 
			
			//they are in the process of changing the password. Give feedback from change password process
			//debug
			System.out.println("com.yardi.ejb.LoginHandler doget() 002C ");
			//debug
			chgPwdFeedback(request, response, userSvcBean, loginRequest, mapper);
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
		
		if (userSvcBean.getFeedback().equals(com.yardi.shared.rentSurvey.YardiConstants.YRD0000)==false) {
			//debug
			System.out.println("com.yardi.ejb.LoginHandler doget() 002E ");
			//debug
			otherFeedback(request, response, userSvcBean, loginRequest, mapper);
			return;
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Construct a response which informs that the password has expired and must be changed 
	 * 
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @param userSvcBean reference to {@link com.yardi.ejb.UserServicesBean#UserServicesBean() com.yardi.ejb.UserServicesBean} 
	 * @param mapper provides functionality for converting between Java objects and matching JSON constructs
	 * @param loginRequest reference to {@link com.yardi.shared.userServices.LoginRequest#LoginRequest() com.yardi.shared.userServices.LoginRequest}
	 * @throws IOException a general I/O exception
	 * @throws JsonProcessingException all problems encountered when processing JSON content that are not pure I/O problems
	 */
	private void informChgPwd(HttpServletRequest request, HttpServletResponse response, UserServices userSvcBean, ObjectMapper mapper, LoginRequest loginRequest) 
			throws IOException, JsonProcessingException {
		//debug
		System.out.println("com.yardi.ejb.LoginHandler informChgPwd() 002A ");
		//debug
		String msg[] = userSvcBean.getFeedback().split("=");
		String formData = mapper.writeValueAsString(
				new LoginResponse(loginRequest.getUserName(), 
								  "", 
								  "", 
								  msg[0], 
								  "views/changePwd.html"
		));
		webResponse(request, response, formData, userSvcBean);
		//debug
		System.out.println("com.yardi.userServices.LoginHandler respondChgPwd() 0003 "
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
	}

	/**
	 * Initialization
	 */
	public void init() {
    }

	/**
	 * Perform tasks required for a successful login.
	 * <ul>
	 *   <li>lookup initial page with join Groups_Masterr and User_Groupss
	 *     <ul>
	 *       <li>if user is in multiple groups set ST_LAST_REQUEST to the html select group page. User picks the initial page</li>
	 *       <li>if user is in only one group set ST_LAST_REQUEST to GM_INITIAL_PAGE</li>
	 *     </ul>
	 *   </li>
	 *   <li>Set user ID as session attribute</li>
	 *   <li>Write/update session table
	 *     <ul>
	 *       <li>
	 *       tokenize session ID. This serves as a password for the session to login. It is not enough for the session 
	 *       to be in the session table, the session must also login in order for the session to be considered authentic.
	 *       </li>
	 *       <li>CreateTokenService is used to create a token from the session ID</li>
	 *     </ul>
	 *   </li>
	 *   <li>Respond to yardiLogin.html/changePwd.html</li>
	 * </ul>
	 *
	 * @param loginRequest {@link com.yardi.shared.userServices.LoginRequest#LoginRequest() com.yardi.shared.userServices.LoginRequest }
	 * @param request {@link HttpServletRequest HttpServletRequest }
	 * @param response {@link HttpServletResponse HttpServletResponse}
	 * @param mapper provides functionality for converting between Java objects and matching JSON constructs.
	 * @param userSvcBean {@link com.yardi.ejb.UserServicesBean#UserServicesBean() com.yardi.ejb.UserServicesBean}
	 * @throws IOException a general I/O exception
	 * @throws JsonProcessingException all problems encountered when processing JSON content that are not pure I/O problems
	 */
	private void loginSuccess(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response, ObjectMapper mapper, UserServices userSvcBean) 
		throws IOException, JsonProcessingException {
		//debug
		System.out.println("com.yardi.ejb.LoginHandler loginSuccess() 0029 ");
		//debug
		
		//store the userID in the session
		request.getSession(false).setAttribute("userID", loginRequest.getUserName()); 
		//debug
		System.out.println("com.yardi.userServices.LoginHandler loginSuccess() 0007 "
				+ "\n "
				+ "  loginRequest = " + loginRequest
				+ "\n "
				+ "  formData="
				+ mapper.writeValueAsString(userSvcBean.getLoginResponse()) 
				);
		//debug
	    /*
		 * Respond to yardiLogin.html/changePwd.html. The page sees that the login request is successful (YRD0000) or 
		 * that the user is in multiple groups (YRD000E) and looks at the 5th parm (initialPage) in loginResponse to 
		 * get the next page to load. yardiLogin.html/changePwd.html tells index.html to load the initialPage page.
		 */
		webResponse(request, response, mapper.writeValueAsString(userSvcBean.getLoginResponse()), userSvcBean);
	}

	/**
	 * If for any reason the login request was unsuccessful provide feedback from 
	 * {@link com.yardi.ejb.UserServicesBean#getFeedback() com.yardi.ejb.UserServicesBean.getFeedback()}
	 * @param request {@link HttpServletRequest HttpServletRequest}
	 * @param response {@link HttpServletResponse HttpServletResponse}
	 * @param userSvcBean {@link com.yardi.ejb.UserServicesBean#UserServicesBean() com.yardi.ejb.UserServicesBean}
	 * @param loginRequest {@link com.yardi.shared.userServices.LoginRequest#LoginRequest() com.yardi.shared.userServices.LoginRequest}
	 * @param mapper Jackson Object mapper 
	 * @throws IOException signals that an I/O exception of some sort has occurred
	 * @throws JsonProcessingException signals a problem was encountered when processing JSON content that was not a pure I/O problem
	 */
	private void otherFeedback(HttpServletRequest request, HttpServletResponse response, UserServices userSvcBean, LoginRequest loginRequest, ObjectMapper mapper) throws IOException, JsonProcessingException {
		//debug
		System.out.println("com.yardi.ejb.LoginHandler otherFeedback() 002F "
				+ "\n"
				+ "    userSvcBean.getFeedback()="
				+ userSvcBean.getFeedback()
				);
		//debug
		String msg [] = userSvcBean.getFeedback().split("=");
		String formData = mapper.writeValueAsString(
				new LoginResponse(
								   loginRequest.getUserName(),
								   loginRequest.getPassword(),
								   "",
								   msg[0],
								   msg[1]
		));
		//debug
		System.out.println("com.yardi.userServices.LoginHandler otherFeedback() 0015"
				+ " "
				+ "\n "
				+ "  userSvcBean.getFeedback() =" + userSvcBean.getFeedback()
				+ "\n "
				+ "  formData = " + formData
				+ "\n "
				);
		//debug
		webResponse(request, response, formData, userSvcBean);
	}
	
	/**
	 * Clear the content of the underlying buffer in the response without clearing headers or status code.
	 * The headers are logged before and after the buffer is cleared to ensure that the session ID is being retained. 
	 * @param response {@link HttpServletResponse HttpServletResponse}
	 */
	private void resetBuffer(HttpServletResponse response) {
		//debug
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.isEmpty()) {
			System.out.println("com.yardi.userServices.LoginHandler resetBuffer() 0020 headerNames is empty");
		}
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.userServices.LoginHandler resetBuffer() 0021 "
					+ "\n"
					+ "   Response header name="
					+ n
					+ "   no headerValues");
			}
			for (String v :  headerValues) {
				System.out.println("com.yardi.userServices.LoginHandler resetBuffer() 0022 "
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
			System.out.println("com.yardi.userServices.LoginHandler resetBuffer() 0023 headerNames is empty");
		}
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.userServices.LoginHandler resetBuffer() 0024 "
					+ "\n"
					+ "   Response header name="
					+ n
					+ "   no headerValues");
			}
			for (String v :  headerValues) {
				System.out.println("com.yardi.userServices.LoginHandler resetBuffer() 0025 "
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
	 * Print the response to the login request to a text-output stream. The reference to the User Services Bean (stored in session attribute userSvcBean) is
	 * set to null. 
	 * @param request {@link HttpServletRequest HttpServletRequest}
	 * @param response {@link HttpServletResponse HttpServletResponse}
	 * @param formData JSON formatted string containing the response
	 * @param userSvcBean {@link com.yardi.ejb.UserServicesBean#UserServicesBean() com.yardi.ejb.UserServicesBean}
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	private void webResponse(HttpServletRequest request, HttpServletResponse response, String formData, UserServices userSvcBean) throws IOException {
		//debug
		System.out.println("com.yardi.userServices.LoginHandler webResponse() 0000 ");
		//debug
		resetBuffer(response);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(formData);
		out.flush();
		userSvcBean.remove();
		HttpSession session = request.getSession();
		session.setAttribute("userSvcBean", null);
	}
}
