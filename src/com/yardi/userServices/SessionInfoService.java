package com.yardi.userServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Exposes details of the http session to clients. Clients may use this servlet to discover details available to the HttpServletRequest and HttpServletResponse objects.
 */ 
@WebServlet("/SessionInfo")
public class SessionInfoService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SessionInfoService() {
        super();
    }

	/**
	 * Entry point.<p> The request, formatted as JSON, is retrieved from the input stream and mapped to 
	 * {@link com.yardi.userServices.SessionInfo#SessionInfo() com.yardi.userServices.SessionInfo}. Determine what the client is asking for and obtain that info from the 
	 * HttpServletRequest and HttpServletResponse. Store the info in <code>SessionInfo</code>. Map <code>SessionInfo</code> to JSON and respond to the request.
	 * 
	 * @param request {@link HttpServletRequest HttpServletRequest}
	 * @param response {@link HttpServletResponse HttpServletResponse} 
	 * @throws ServletException 
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean useAttribute = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String formData = "";
		
        if(br != null){
        	formData = br.readLine();
        }
        
        if (formData == null) {
        	formData = (String) request.getAttribute("formData"); //"formData" needs to be a stringified SessionInfo object  
        	useAttribute = true;
        }
                
        System.out.println("com.yardi.userServices.SessionInfoService doGet() 0000 " 
           	+ "\n"
        	+ "  formData=" 
        	+ formData
        	+ "\n"
        	+ "  useAttribute=" 
        	+ useAttribute);
        ObjectMapper mapper = new ObjectMapper();
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo = mapper.readValue(formData, SessionInfo.class);
		
		if (sessionInfo.getRequest().equals(com.yardi.shared.rentSurvey.YardiConstants.REQUEST_SESSION_INFO)) {
			sessionInfo.setUserID((String) request.getSession().getAttribute("userID"));
		}
		
		formData = mapper.writeValueAsString(sessionInfo);
        System.out.println("com.yardi.userServices.SessionInfoService doGet() 0001 " 
            	+ "\n"
            	+ "  formData=" 
        		+ formData);
        showResponseHeaders(response);
		response.resetBuffer(); 
        showResponseHeaders(response);
		response.setContentType("application/json"); 
		PrintWriter out = response.getWriter();

		if (useAttribute) {
			request.setAttribute("formData", formData);
		} else {
			out.print(formData);
			out.flush();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Log the response headers to ensure that the session ID is being retained 
	 * @param response {@link HttpServletResponse HttpServletResponse}
	 */
	private void showResponseHeaders(HttpServletResponse response) {
		//debug 
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.isEmpty()) {
			System.out.println("com.yardi.userServices.SessionInfoService showResponseHeaders() 0002 headerNames is empty");
		}
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.userServices.SessionInfoService showResponseHeaders() 0003 "
						+ "\n"
						+ "   Response header name="
						+ n
						+ "   no headerValues");
			}
			for (String v :  headerValues) {
				System.out.println("com.yardi.userServices.SessionInfoService showResponseHeaders() 0004 "
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
}
