package com.yardi.userServices;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class SessionInfoService. This service returns a collection of information about the session. HTML does 
 * a post to invoke this and have access to things like session attributes since html can not access this information directly.
 * The information is returned in a JSON object. 
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String formData = "";
		ObjectMapper mapper = new ObjectMapper();
		SessionInfo sessionInfo = new SessionInfo((String) request.getSession().getAttribute("userID")); 
		formData = mapper.writeValueAsString(sessionInfo);
		response.reset(); 
		response.setContentType("application/json"); 
		PrintWriter out = response.getWriter();
		out.print(formData);
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
