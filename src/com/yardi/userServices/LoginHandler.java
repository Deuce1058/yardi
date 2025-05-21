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
import com.yardi.shared.helpdesk.ResetPwdRequest;
import com.yardi.shared.userServices.LoginRequest;
import com.yardi.shared.userServices.LoginResponse;

import java.util.Collection;

/**
 * Entry point for processing login requests.<p>
 * http://localhost:8080/yardiWeb/yardiLogin.html<p>
 * Main functions include:
 * <ul>
 * <li>Receiving the login request from the web</li>
 * <li>Lookup and obtain a reference to {@link com.yardi.ejb.UserServicesBean}</li>
 * <li>Mapping the raw request from the input stream to {@link com.yardi.shared.userServices.LoginRequest}</li>
 * <li>Injecting the login request into UserServicesBean</li>
 * <li>Calling {@link com.yardi.ejb.UserServicesBean#authenticate() UserServicesBean.authenticate()} or 
 * {@link com.yardi.ejb.UserServicesBean#chgPwd() UserServicesBean.chgPwd()} as appropriate</li>
 * <li>Handling the feedback from UserServicesBean</li>
 * <li>Responding to the login request</li>
 * </ul>
 */
@WebServlet("/doLogin")
public class LoginHandler extends HttpServlet {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Handle login requests. Entry point. Begin by calling <code>checkSession()</code> to test whether the session ID on the request matches the session ID  
	 * stored on {@link com.yardi.ejb.UserServicesBean#UserServicesBean() com.yardi.ejb.UserServicesBean}.<p>
	 * 
	 * Summary of key points in doGet():
	 * 
	 * <ul>
	 *   <li>JNDI lookup of {@link com.yardi.ejb.UserServicesBean}</li>
	 *   <li>Read the raw request data from the input stream</li>
	 *   <li>Map the request from the web to {@link com.yardi.shared.userServices.LoginRequest}</li>
	 *   <li>Inject the LoginRequest into UserServicesBean</li>
	 *   <li>Call {@link com.yardi.ejb.UserServicesBean#chgPwd() UserServicesBean.chgPwd()} or 
	 *   {@link com.yardi.ejb.UserServicesBean#authenticate() UserServicesBean.authenticate()} as appropriate</li>
	 *   <li>Handle feedback from UserServicesBean</li>
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
		System.out.println("com.yardi.userServices.LoginHandler.doGet() 0008 ");
		UserServices userSvcBean = lookupUserServicesBean(request);
	    String formdata = readRawBufferedLine(request);
	    LoginRequest loginRequest = mapRequestStringToLoginRequest(formdata);
		/*
		 * About change password request and authenticate request:
		 * 
		 * In both the login page and the change password page a script sets the change password indicator and builds a JSON 
		 * object containing the change password indicator. The JSON is passed via POST and then parsed into LoginRequest. This
		 * allows methods know whether the login data originated from the regular login page or the change password page. 
	     *
		 * As a convenience, there is a boolean in LoginRequest which is the same as the change password indicator. 
		 */
		loginRequest.setChangePwd(loginRequest.getChgPwd()); 
		System.out.println("com.yardi.userServices.LoginHandler.doGet() 0009 " 
				+ "\n"
				+ "    formData =" + formdata
				+ "\n"
				+ "    loginRequest="
				+ loginRequest
				);
		userSvcBean.setLoginRequest(loginRequest);
	
		if (loginRequest.getChangePwd()) { //change password
			System.out.println("com.yardi.userServices.LoginHandler.doGet() 0001 ");
			userSvcBean.chgPwd();
		} else {
			System.out.println("com.yardi.userServices.LoginHandler.doGet() 0026 ");
			userSvcBean.authenticate();	
		}
	
		handleFeedback(request, response, userSvcBean, loginRequest);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * Handle feedback from {@link com.yardi.ejb.UserServicesBean}, construct and serialize a response to the web request, and respond to the web request
	 *   
	 * @param request {@link HttpServletRequest} 
	 * @param response {@link HttpServletResponse}
	 * @param userSvcBean {@link com.yardi.ejb.UserServicesBean}
	 * @param loginRequest {@link com.yardi.shared.userServices.LoginRequest}
	 * @throws JsonProcessingException all problems encountered when processing (parsing, generating) JSON content that are not pure I/O problems.
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	private void handleFeedback(HttpServletRequest request, HttpServletResponse response, UserServices userSvcBean,
			LoginRequest loginRequest) throws JsonProcessingException, IOException {
		System.out.println("com.yardi.userServices.LoginHandler.handleFeedback() 0006 ");
		String formData = serializeLoginResponse(userSvcBean, loginRequest);
		request.getSession(false).setAttribute("userID", loginRequest.getUserName()); 
		System.out.println("com.yardi.userServices.LoginHandler.handleFeedback() 0010 "
				+ "\n"
				+ "    formData="
				+ formData
				);
		webResponse(request, response, formData, userSvcBean);
		return;
	}

	/**
     * JNDI Lookup {@link com.yardi.ejb.UserServicesBean com.yardi.ejb.UserServicesBean} and return the reference. 
     * HttpServletRequest is needed to synchronize the lookup on the HttpSession.
     * @param request {@link HttpServletRequest HttpServletRequest} 
     * @return UserServices {@link com.yardi.ejb.UserServicesBean com.yardi.ejb.UserServicesBean}
     */
    private UserServices lookupUserServicesBean(HttpServletRequest request) {
		HttpSession session = request.getSession();
		System.out.println("com.yardi.userServices.LoginHandler.lookupUserServicesBean() 000A "
				+ "\n"
				+ "    session="
				+ session.getId()
				);
		UserServices userServicesBean = null;

    	synchronized(session) {
    		try {
    			InitialContext ctx = new InitialContext();
    			userServicesBean = (UserServices)ctx.lookup("java:global/yardiWeb/UserServicesBean");
    		} catch (NamingException e) {
    			System.out.println("com.yardi.userServices.LoginHandler.lookupUserServicesBean() 000B ");
    			e.printStackTrace();
    			return null;
    		}
    	}
    	
		return userServicesBean;
    }
	
	/**
	 * Map the raw request data to the {@link com.yardi.shared.userServices.LoginRequest} container.    
	 * @param request String read from the input stream 
	 * @return java representation of the String read from the request stream
	 */
	private LoginRequest mapRequestStringToLoginRequest(String request) {
		System.out.println("com.yardi.userServices.LoginHandler.mapRequestStringToLoginRequest() 0004 ");
		ObjectMapper m = new ObjectMapper(); 
		LoginRequest loginRequest = null;
		try {
			loginRequest = m.readValue(request, LoginRequest.class);
		} catch (Exception e) {
			System.out.println("com.yardi.userServices.LoginHandler.mapRequestStringToLoginRequest() 0005 ");
			e.printStackTrace();
		}
		
		return loginRequest;
	}

	/**
	 * Read the raw input stream and store the contents in a String 
	 * @param request {@link HttpServletRequest}
	 * @return String containing the data read from the input stream
	 */
	private String readRawBufferedLine(HttpServletRequest request) {
		System.out.println("com.yardi.userServices.LoginHandler.readRawBufferedLine() 000D ");
		String formData = ""; 		 
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			
			if(br != null){ 
				formData = br.readLine();
			} 		  
		} catch (IOException e) {
			System.out.println("com.yardi.userServices.LoginHandler.readRawBufferedLine() 000C ");
			e.printStackTrace();
		} 

		return formData;
	}

	/**
	 * Clear the content of the underlying buffer in the response without clearing headers or status code.
	 * The headers are logged before and after the buffer is cleared to ensure that the session ID is being retained. 
	 * @param response {@link HttpServletResponse HttpServletResponse}
	 */
	private void resetBuffer(HttpServletResponse response) {
		System.out.println("com.yardi.userServices.LoginHandler.resetBuffer() 000E headerNames is empty");
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.isEmpty()) {
		System.out.println("com.yardi.userServices.LoginHandler.resetBuffer() 0020 headerNames is empty");
		}
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.userServices.LoginHandler.resetBuffer() 0021 "
					+ "\n"
					+ "   Response header name="
					+ n
					+ "   no headerValues");
			}
			for (String v :  headerValues) {
				System.out.println("com.yardi.userServices.LoginHandler.resetBuffer() 0022 "
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
			System.out.println("com.yardi.userServices.LoginHandler.resetBuffer() 0023 headerNames is empty");
		}
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.userServices.LoginHandler.resetBuffer() 0024 "
					+ "\n"
					+ "   Response header name="
					+ n
					+ "   no headerValues");
			}
			for (String v :  headerValues) {
				System.out.println("com.yardi.userServices.LoginHandler.resetBuffer() 0025 "
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
	 * Construct and serialize {@link com.yardi.shared.userServices.LoginResponse}.<p>
	 * Feedback from {@link com.yardi.ejb.UserServicesBean} determines how LoginResponse is constructed.

	 * <pre>
	 * Feedback from UserServicesBean is <span style="font-family:consolas;">YRD0000</span> or <span style="font-family:consolas;">YRD000E</span>: 
	 *   serialize {@link com.yardi.ejb.UserServicesBean#getLoginResponse() UserServicesBean.getLoginResponse()} directly.
	 * 
	 * Feedback from UserServicesBean is <span style="font-family:consolas;">YRD0002</span> or <span style="font-family:consolas;">YRD001B:</span>
     *   <strong>If not changing the password:             If password is being changed:</strong>
     *   new LoginResponse(                        new LoginResponse(
     *     userName=loginRequest.getUserName()       userName=loginRequest.getUserName()
     *     password=""                               password=loginRequest.getPassword()
     *     newPassword=""                            newPassword=loginRequest.getNewPassword()
     *     msgID=message ID from UserServiceBean     msgID=message ID from UserServiceBean 
     *     msgDescription="views/changePwd.html"     msgDescription=message data from UserServiceBean
     *     )                                         )
     * 
	 * Feedback from UserServicesBean is not <span style="font-family:consolas;">YRD0000</span>:
     *   new LoginResponse(
     *     userName=loginRequest.getUserName()
     *     password=loginRequest.getPassword()
     *     newPassword=""
     *     msgID=message ID from UserServiceBean
     *     msgDescription=message data from UserServiceBean
     *     )
     * </pre>
     * 
	 * @param userServicesBean a reference to UserServicesBean
	 * @param loginRequest {@link com.yardi.shared.userServices.LoginRequest}
	 * @return serialized LoginResponse
	 */
    private String serializeLoginResponse(UserServices userServicesBean, LoginRequest loginRequest) {
		System.out.println("com.yardi.userServices.LoginHandler.serializeLoginResponse() 0003 ");
		ObjectMapper mapper = new ObjectMapper();
		String msg [] = userServicesBean.getFeedback().split("=");
		String view = loginRequest.getChangePwd() ? "views/changePwd.html" : msg[1];
		System.out.println("com.yardi.userServices.LoginHandler.serializeLoginResponse() 000F "
				+ "\n"
				+ "    userServicesBean.getFeedback()="
				+ userServicesBean.getFeedback()
				+ "\n"
				+ "    loginRequest.getUserName()="
				+ loginRequest.getUserName()
				+ "\n"
				+ "    loginRequest.getPassword()="
				+ loginRequest.getPassword()
				+ "\n"
				+ "    loginRequest.getNewPassword()="
				+ loginRequest.getNewPassword()
				+ "\n"
				+ "    msg[1]="
				+ msg[1]
				+ "\n"
				+ "    view="
				+ view
				);
		
		try {
			if (userServicesBean.getFeedback().equals(com.yardi.shared.rentSurvey.YardiConstants.YRD0000) || 
				userServicesBean.getFeedback().equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000E)) {
			    /*
				 * Respond to yardiLogin.html/changePwd.html. The page sees that the login request is successful (YRD0000) or 
				 * that the user is in multiple groups (YRD000E) and looks at the 5th parm (initialPage) in loginResponse to 
				 * get the next page to load. yardiLogin.html/changePwd.html tells index.html to load the initialPage page.
				 */
				System.out.println("com.yardi.userServices.LoginHandler.serializeLoginResponse() 0007 "
						+ "\n"
						+ "    userSvcBean.getFeedback()=" 
						+ userServicesBean.getFeedback()
						+ "\n"
						+ "    writeValueAsString(userSvcBean.getLoginResponse())="
						+ mapper.writeValueAsString(userServicesBean.getLoginResponse()) 
						);
				return mapper.writeValueAsString(userServicesBean.getLoginResponse());
			}
			
			if (loginRequest.getChangePwd() || 
					userServicesBean.getFeedback().equals(com.yardi.shared.rentSurvey.YardiConstants.YRD0002) ||
					userServicesBean.getFeedback().equals(com.yardi.shared.rentSurvey.YardiConstants.YRD001B)
					) {
				System.out.println("com.yardi.userServices.LoginHandler.serializeLoginResponse() 0002 "
						+ "\n"
						+ "    userServicesBean.getFeedback()="
						+ userServicesBean.getFeedback()
						);
				
				if (!loginRequest.getChangePwd()) { //expired password or authenticating with temp pwd
					System.out.println("com.yardi.userServices.LoginHandler.serializeLoginResponse() 002B ");
					return mapper.writeValueAsString(
							new LoginResponse(loginRequest.getUserName(), 
											  "", 
											  "", 
											  msg[0], 
											  view
									));
				}
				
				System.out.println("com.yardi.userServices.LoginHandler.serializeLoginResponse() 002C ");
				return mapper.writeValueAsString(
						new LoginResponse(
								loginRequest.getUserName(),
								loginRequest.getPassword(),
								loginRequest.getNewPassword(),
								msg[0],
								view
								));
			}
			
			if (!userServicesBean.getFeedback().equals(com.yardi.shared.rentSurvey.YardiConstants.YRD0000)) {
				System.out.println("com.yardi.userServices.LoginHandler.serializeLoginResponse() 002E "
						+ "\n"
						+ "    userServicesBean.getFeedback()="
						+ userServicesBean.getFeedback()
						);
				return mapper.writeValueAsString(
						new LoginResponse(
										   loginRequest.getUserName(),
										   loginRequest.getPassword(),
										   "",
										   msg[0],
										   view
				));
			}

		} catch (JsonProcessingException e) {
			System.out.println("com.yardi.userServices.LoginHandler.serializeLoginResponse() 0011 ");
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Print the response to the login request to a text-output stream. {@link com.yardi.ejb.UserServicesBean#remove() com.yardi.ejb.UserServicesBean.remove()} is
	 * called to remove the bean. 
	 * @param request {@link HttpServletRequest HttpServletRequest}
	 * @param response {@link HttpServletResponse HttpServletResponse}
	 * @param formData JSON formatted string containing the response
	 * @param userSvcBean {@link com.yardi.ejb.UserServicesBean#UserServicesBean() com.yardi.ejb.UserServicesBean}
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	private void webResponse(HttpServletRequest request, HttpServletResponse response, String formData, UserServices userSvcBean) throws IOException {
		System.out.println("com.yardi.userServices.LoginHandler.webResponse() 0000 ");
		resetBuffer(response);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(formData);
		out.flush();
		userSvcBean.remove();
	}
}
