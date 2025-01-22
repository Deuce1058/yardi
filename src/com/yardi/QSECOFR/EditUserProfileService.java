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
import com.yardi.ejb.QSECOFR.EditUserProfileCTRL;
import com.yardi.shared.QSECOFR.EditUserProfileRequest;

/**
 * Entry point for edit user profile.<p> Receive, process and respond to requests to edit the user profile.
 */
@WebServlet(description = "Handle edit user profile requests", urlPatterns = { "/editUserProfile" })
public class EditUserProfileService extends HttpServlet {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * User transaction 
	 */
	@Resource UserTransaction tx;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
    public EditUserProfileService() {
    }

	/**
	 * Determine whether the session contains a reference to {@link com.yardi.ejb.QSECOFR.EditUserProfileCTRLBean EditUserProfileCTRLBean}.<p> 
	 * If the session does not have a reference to EditUserProfileCTRLBean then get the reference from JNDI and save it in the session. Return the reference that came 
	 * from either the session or JNDI.   
	 * @param request {@link HttpServletRequest}
	 * @return a reference to EditUserProfileCTRLBean
	 */
	private EditUserProfileCTRL checkSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		System.out.println("com.yardi.QSECOFR.EditUserProfileService.checkSession() 0000 "
				+ "\n    "
				+ "sessionID="
				+ session.getId()
				);
		EditUserProfileCTRL editUserProfileCTRLBean = (EditUserProfileCTRL)session.getAttribute("editUserProfileCTRL");
		
		synchronized(session) {
			if (editUserProfileCTRLBean==null) {
				System.out.println("com.yardi.QSECOFR.EditUserProfileService.checkSession() 0001 ");
				try {
					InitialContext ctx = new InitialContext();
					editUserProfileCTRLBean = (EditUserProfileCTRL)ctx.lookup("java:global/yardiWeb/EditUserProfileCTRLBean");
				} catch (NamingException e) {
					System.out.println("com.yardi.QSECOFR.EditUserProfileService.checkSession() 0002 NamingException ");
					e.printStackTrace();
					return null;
				}
				
				session.setAttribute("editUserProfileCTRL", editUserProfileCTRLBean);
			}
		}
		
		return editUserProfileCTRLBean;
	}
		
	/**
	 * Handle requests to edit the user profile.<p>
	 * 
	 * <code>doGet()</code> begins by checking for a session attribute named <code>editUserProfileCTRL</code>. This attribute is a reference to 
	 * {@link com.yardi.ejb.QSECOFR.EditUserProfileCTRLBean#EditUserProfileCTRLBean() com.yardi.ejb.EditUserProfileCTRLBean}. If attribute <code>editUserProfileCTRL</code>
	 * is not found <code>doGet()</code> obtains a reference to <code>EditUserProfileCTRLBean</code> from JNDI and sets attribute <code>editUserProfileCTRL</code> to 
	 * be a reference to <code>EditUserProfileCTRLBean</code>.<p>
	 * 
	 * The raw JSON request is mapped to {@link com.yardi.shared.QSECOFR.EditUserProfileRequest#EditUserProfileRequest() com.yardi.shared.QSECOFR.EditUserProfileRequest}.<p>
	 * 
	 * A remove request is a special case which indicates the user is leaving the page <code>userProfile_CRUD.html</code>. Therefore they are finished editing user profiles. 
	 * <code>doGet()</code> calls <code>remove()</code> to clean up resources before further servicing the request.<p>
	 * 
	 * The <code>EditUserProfileRequest</code> is injected into <code>EditUserProfileCTRLBean</code>.<p>
	 * 
	 * The <code>EditUserProfileCTRLBean</code> is instructed to initialize itself.<p>
	 * 
	 * The <code>EditUserProfileCTRLBean</code> is instructed to handle the request.<p>
	 * 
	 * <code>webResponse()</code> handles responding to the request.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(
				  "com.yardi.QSECOFR.EditUserProfileService doGet() 0014 " 
				+ "\n    "
				+ "JSESSIONID="
				+ request.getSession().getId()
				);
		EditUserProfileCTRL editUserProfileCTRL = checkSession(request);
		String formData = readBuffer(request);
		EditUserProfileRequest editRequest = mapRequest(formData);
		editUserProfileCTRL.setEditUserProfileRequest(editRequest);
		editUserProfileCTRL.inzEditRequest();
		editUserProfileCTRL.handleRequest();
		webResponse(request, response, editUserProfileCTRL.getEditUserProfileRequest(), editUserProfileCTRL);
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
        System.out.println("com.yardi.QSECOFR.EditUserProfileService.mapRequest() 0015 ");
		/*debug*/
		ObjectMapper mapper = new ObjectMapper();
		EditUserProfileRequest editRequest = new EditUserProfileRequest();
		try {
			editRequest = mapper.readValue(formData, EditUserProfileRequest.class);
		} catch (JsonProcessingException e) {
			/*debug*/
			System.out.println("com.yardi.QSECOFR.EditUserProfileService.mapRequest() exception 0003 ");
			/*debug*/
			e.printStackTrace();
			return null;
		}
		
		return editRequest;
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
		String formData = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			
	        if(br != null){
	        	formData = br.readLine();
	        }
	        
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService.readBuffer() 000E "
	        	+ "\n"
	        	+ "   formData=" + formData);
		} catch (IOException e) {
			/*debug*/
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService.readBuffer() exception 000F ");
   			/*debug*/
			e.printStackTrace();
			return null;
		}
		
        return formData;
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
				+ request.getSession().getId()
				);
		Collection<String> headerNames = response.getHeaderNames();
		
		if (headerNames.isEmpty()) {
			System.out.println("com.yardi.QSECOFR.EditUserProfileService showResponseHeaders() 0011 headerNames is empty");
		}
		
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.QSECOFR.EditUserProfileService showResponseHeaders() 0010 "
						+ "\n"
						+ "   Response header name="
						+ n
						+ "   no headerValues");
			}
			
			for (String v :  headerValues) {
				System.out.println("com.yardi.QSECOFR.EditUserProfileService showResponseHeaders() 0013 "
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
	 * Respond to the web request by mapping the java representation of the request to JSON. 
	 * 
	 * @param request a HttpServletRequest
	 * @param response a HttpServletResponse
	 * @param editRequest POJO representation of the web request
	 */
	private void webResponse(HttpServletRequest request, HttpServletResponse response, EditUserProfileRequest editRequest, EditUserProfileCTRL editUserProfileCTRLbean) {
		/*debug*/
		System.out.println(
				 "com.yardi.QSECOFR.EditUserProfileService.webResponse() 0005 " 
				+ "\n    "
				+ "JSESSIONID="
				+ request.getSession().getId()
				);
		/*debug*/
		HttpSession session = request.getSession();
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
			editUserProfileCTRLbean.removeBean();
			synchronized(session) {
				request.getSession().setAttribute("editUserProfileCTRL", null);
			}
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
				+ request.getSession().getId()
				);
	}
}
