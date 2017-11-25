package com.yardi.QSECOFR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.UserProfile;
import com.yardi.ejb.UserProfileSessionBeanRemote;

/**
 * Servlet implementation class EditUserProfileService
 */
@WebServlet(description = "Handle edit user profile requests", urlPatterns = { "/editUserProfile" })
public class EditUserProfileService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserProfile userProfile;
	@EJB UserProfileSessionBeanRemote userProfileBean;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUserProfileService() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String formData = "";
		
        if(br != null){
        	formData = br.readLine();
        }
        
		ObjectMapper mapper = new ObjectMapper();
		EditUserProfileRequest editRequest = mapper.readValue(formData, EditUserProfileRequest.class);
		String feedback [] = com.yardi.rentSurvey.YardiConstants.YRD0000.split("="); 
		editRequest.setMsgID(feedback[0]);
		editRequest.setMsgDescription(feedback[1]);
		
		if (editRequest.getAction().equals(com.yardi.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_FIND)) {
			userProfile = null;
			userProfile = userProfileBean.find(editRequest.getFindUser());
			
			if (userProfile == null) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD000D.split("="); 
				editRequest.setMsgID(feedback[0]);
				editRequest.setMsgDescription(feedback[1]);
				response.reset();
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				formData = mapper.writeValueAsString(editRequest); //convert the feedback to json 
				out.print(formData);
				out.flush();
				return;
			}

			editRequest.setFirstName(userProfile.getUpFirstName());
			editRequest.setLastName(userProfile.getUpLastName());
			editRequest.setAddress1(userProfile.getUpAddress1());
			editRequest.setAddress2(userProfile.getUpAddress2());
			editRequest.setCity(userProfile.getUpCity());
			editRequest.setSsn(userProfile.getUpState());
			editRequest.setZip(userProfile.getUpZip());
			editRequest.setZip4(userProfile.getUpZip4());
			editRequest.setPhone(userProfile.getUpPhone());
			editRequest.setFax(userProfile.getUpFax());
			editRequest.setEmail(userProfile.getUpEmail());
			editRequest.setSsn(userProfile.getUpssn());
			editRequest.setDob(editRequest.stringify(userProfile.getUpdob()));
			editRequest.setHomeMarket(new Short(userProfile.getUpHomeMarket()).toString());
			editRequest.setActiveYN(userProfile.getUpActiveYn());
			editRequest.setPwdExpDate(editRequest.stringify(userProfile.getUpPwdexpd()));
			editRequest.setDisabledDate(editRequest.stringify(userProfile.getUpDisabledDate()));
			editRequest.setPwdAttempts(new Short(userProfile.getUpPwdAttempts()).toString());
			editRequest.setCurrentToken(userProfile.getUptoken());
			editRequest.setLastLogin(editRequest.stringify(userProfile.getUpLastLoginDate()));
			
			response.reset();
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			formData = mapper.writeValueAsString(editRequest); //convert the feedback to json 
			out.print(formData);
			out.flush();
			return;
		}
		
		if (editRequest.getAction().equals(com.yardi.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_ADD)) {
			int rows = userProfileBean.persist(
				editRequest.getFindUser(),
				editRequest.getCurrentToken(),
				Short.parseShort(editRequest.getHomeMarket()),   
				editRequest.getFirstName(), 
				editRequest.getLastName(), 
				editRequest.getAddress1(), 
				editRequest.getAddress2(), 
				editRequest.getCity(), 
				editRequest.getState(), 
				editRequest.getZip(), 
				editRequest.getZip4(), 
				editRequest.getPhone(), 
				editRequest.getFax(), 
				editRequest.getEmail(), 
				editRequest.getSsn(),
				new java.util.Date(editRequest.toDate(editRequest.getDob()).getTime()),
				editRequest.getActiveYN(),
				new java.util.Date(editRequest.toDate(editRequest.getPwdExpDate()).getTime()),
				new java.util.Date(editRequest.toDate(editRequest.getDisabledDate()).getTime()),
				new java.util.Date(editRequest.toDate(editRequest.getLastLogin()).getTime()),
				editRequest.getPasswordAttempts()
				);
			feedback = com.yardi.rentSurvey.YardiConstants.YRD000D.split("="); 
			editRequest.setMsgID(feedback[0]);
			editRequest.setMsgDescription(feedback[1]);
			response.reset();
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			formData = mapper.writeValueAsString(editRequest); //convert the feedback to json 
			out.print(formData);
			out.flush();
			return;
		}

			
			
		editRequest.setUpHomeMarket(Integer.parseInt(editRequest.getHomeMarket()));
		editRequest.setBirthDate(editRequest.toDate(editRequest.getDob()));
		editRequest.setPasswordExpirationDate(editRequest.toDate(editRequest.getPwdExpDate()));
		editRequest.setProfileDisabledDate(editRequest.toDate(editRequest.getDisabledDate()));  
		editRequest.setLastLoginDate(editRequest.toDate(editRequest.getLastLogin()));
		editRequest.setPasswordAttempts(Integer.parseInt(editRequest.getPwdAttempts()));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}