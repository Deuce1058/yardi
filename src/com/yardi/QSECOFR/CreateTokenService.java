package com.yardi.QSECOFR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.crypto.Jargon2Bean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Convert a user supplied passwords to a token. Entry point for the create token application.
 */
@WebServlet("/newToken")
public class CreateTokenService extends HttpServlet {
	/** 
	 * Version ID
	 */
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateTokenService() {
        super();
    }

	/**
	 * Obtain a new instance of {@link com.yardi.shared.userServices.PasswordAuthentication#PasswordAuthentication() com.yardi.shared.userServices.PasswordAuthentication}.
	 * Read the request from the input stream and map it from JSON to {@link com.yardi.QSECOFR.TokenRequest#TokenRequest() com.yardi.QSECOFR.TokenRequest} container.
	 * Hash the supplied user password. 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("com.yardi.QSECOFR.CreateTokenService.doget() 0000 ");
		Jargon2Bean jargon2Bean = lookupJargon2Bean(request);
		String formData = readRawBufferedLine(request);
		System.out.println("com.yardi.QSECOFR.CreateTokenService.doGet() 0001 formData=" + formData);
		TokenRequest tokenRequest = mapRequestStringToTokenRequest(formData);
		processTokenRequest(jargon2Bean, tokenRequest);		
		tokenRequest.setMsgID("");
		tokenRequest.setMsgDescription("");
		ObjectMapper mapper = new ObjectMapper();
	    webResponse(request, response, mapper.writeValueAsString(tokenRequest)); 
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private Jargon2Bean lookupJargon2Bean(HttpServletRequest request) {
		HttpSession session = request.getSession();
		System.out.println("com.yardi.QSECOFR.CreateTokenService.lookupJargon2Bean() 0005 "
				+ "\n"
				+ "    session="
				+ session.getId()
				);
		Jargon2Bean jargon2Bean = null;

    	synchronized(session) {
    		try {
    			InitialContext ctx = new InitialContext();
    			jargon2Bean = (Jargon2Bean)ctx.lookup("java:global/yardiWeb/Jargon2Bean");
    		} catch (NamingException e) {
    			System.out.println("com.yardi.QSECOFR.CreateTokenService.lookupJargon2Bean() 0006 ");
    			e.printStackTrace();
    			return null;
    		}
    	}
    	
		return jargon2Bean;
    }
	
	/**
	 * Map the request String data to the {@link com.yardi.QSECOFR.TokenRequest TokenRequest} container.    
	 * @param request String read from the input stream 
	 * @return String representation of the data read from the request stream
	 */
	private TokenRequest mapRequestStringToTokenRequest(String request) {
		System.out.println("com.yardi.QSECOFR.CreateTokenService.mapRequestStringToTokenRequest() 0009 ");
		ObjectMapper m = new ObjectMapper(); 
		TokenRequest tokenRequest = null;
		try {
			tokenRequest = m.readValue(request, TokenRequest.class);
		} catch (Exception e) {
			System.out.println("com.yardi.QSECOFR.CreateTokenService.mapRequestStringToTokenRequest() 000A Exception ");
			e.printStackTrace();
		}
		
		return tokenRequest;
	}

	/**
	 * Determine what is being requested and call the appropriate com.yeari.ejb.crypto.Jargon2Bean method. 
	 * @param jargon2Bean reference to com.yeari.ejb.crypto.Jargon2Bean 
	 * @param tokenRequest reference to com.yardi.QSECOFR.Tokenrequest
	 */
	private void processTokenRequest(Jargon2Bean jargon2Bean, TokenRequest tokenRequest) {
		try {
			System.out.println("com.yardi.QSECOFR.CreateTokenService.processTokenRequest() 000B ");
			
			if (tokenRequest.getAction().equals("hash")) {
				System.out.println("com.yardi.QSECOFR.CreateTokenService.processTokenRequest() 000C ");
				tokenRequest.setHash(jargon2Bean.hash(tokenRequest.getPassword()));
			}
			
			if (tokenRequest.getAction().equals("verify")) {
				System.out.println("com.yardi.QSECOFR.CreateTokenService.processTokenRequest() 000D ");
				boolean verifed = jargon2Bean.verify(tokenRequest.getPassword(), tokenRequest.getHash());
				tokenRequest.setVerifiedYn("n");
				
				if (verifed) {tokenRequest.setVerifiedYn("y");}
			}
		} catch (NoSuchAlgorithmException e) {
			System.out.println("com.yardi.QSECOFR.CreateTokenService.processTokenRequest() 000E Exception ");
			e.printStackTrace();
		}
	}
	
	/**
	 * Read the raw input stream and store the contents in a String 
	 * @param request {@link HttpServletRequest}
	 * @return String containing the data read from the input stream
	 */
	private String readRawBufferedLine(HttpServletRequest request) {
		System.out.println("com.yardi.QSECOFR.CreateTokenService.readRawBufferedLine() 0007");
		String formData = "";
		BufferedReader br;
		
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			
			if(br != null){ 
				formData = br.readLine();
			} 		  
		} catch (IOException e) {
			System.out.println("com.yardi.QSECOFR.CreateTokenService.readRawBufferedLine() 0008 Exception ");
			e.printStackTrace();
		} 

		return formData;
	}

	/**
	 * Log the response headers, clear the content of the underlying buffer in the response without clearing headers or status code and log the response headers again.
	 * @param response {@link HttpServletResponse}
	 */
	private void resetBuffer(HttpServletResponse response) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.isEmpty()) {
			System.out.println("com.yardi.QSECOFR.CreateTokenService.resetBuffer() 0011 headerNames is empty");
		}
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.QSECOFR.CreateTokenService.resetBuffer() 0012 "
					+ "\n"
					+ "   Response header name="
					+ n
					+ "   no headerValues");
			}
			for (String v :  headerValues) {
				System.out.println("com.yardi.QSECOFR.CreateTokenService.resetBuffer() 0013 "
						+ "\n"
						+ "   Response header name="
						+ n
						+ "   Value="
						+ v
					);
			}
		}
        response.resetBuffer();
		headerNames = response.getHeaderNames();
		if (headerNames.isEmpty()) {
			System.out.println("com.yardi.QSECOFR.CreateTokenService.resetBuffer() 0014 headerNames is empty");
		}
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.QSECOFR.CreateTokenService.resetBuffer() 0015 "
					+ "\n"
					+ "   Response header name="
					+ n
					+ "   no headerValues");
			}
			for (String v :  headerValues) {
				System.out.println("com.yardi.QSECOFR.CreateTokenService.resetBuffer() 0016 "
						+ "\n"
						+ "   Response header name="
						+ n
						+ "   Value="
						+ v
					);
			}
		}
	}
	
	/**
	 * Respond to the web request, {@link com.yardi.QSECOFR.CreateTokenService#resetBuffer(HttpServletResponse response) clear} the content of the underlying 
	 * buffer in the response without clearing headers or status code.
	 * 
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param formData String containing the response in JSON format
	 */
	private void webResponse(HttpServletRequest request, HttpServletResponse response, String formData) {
		System.out.println("com.yardi.QSECOFR.CreateTokenService.webResponse() 000F "
				+ "\n    "
				+ formData
				);
	    try {
	    	resetBuffer(response);
	    	response.setContentType("application/json"); 
			PrintWriter out = response.getWriter();
		    out.print(formData); 
		    out.flush(); 
			System.out.println("com.yardi.QSECOFR.CreateTokenService.webResponse() 0017 "
					+ "JSESSIONID="
					+ request.getSession().getId()
					);
		} catch (IOException e) {
			System.out.println("com.yardi.QSECOFR.CreateTokenService.webResponse() 0010 IOException ");
			e.printStackTrace();
		} 
	}
}
