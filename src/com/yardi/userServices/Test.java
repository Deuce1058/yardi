package com.yardi.userServices;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.yardi.shared.userServices.InitialPage;
import com.yardi.shared.userServices.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class Test
 */
@WebServlet(description = "Just testing", urlPatterns = { "/testing" })
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Test() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Vector<InitialPage> initialPageList = new Vector<InitialPage>();
		initialPageList.add(new InitialPage("page1", "url1"));
		initialPageList.add(new InitialPage("page2", "url2"));
		initialPageList.add(new InitialPage("page3", "url3"));
		initialPageList.add(new InitialPage("page4", "url4"));
		ObjectMapper mapper = new ObjectMapper();
		String formData = "";
		try {
			LoginResponse loginResponse = new LoginResponse(
					"user name",
					"",  //Password
					mapper.writeValueAsString(initialPageList), //new password
					"YRD000E",
					"views/selectGroup.html"
			);

			formData = mapper.writeValueAsString(loginResponse); 

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		response.reset();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out = response.getWriter();
		out.print(formData);
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
