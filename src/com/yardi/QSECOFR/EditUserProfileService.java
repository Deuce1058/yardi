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
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.User_Profile;
import com.yardi.ejb.UserProfile;

/**
 * Servlet implementation class EditUserProfileService
 */
@WebServlet(description = "Handle edit user profile requests", urlPatterns = { "/editUserProfile" })
public class EditUserProfileService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource UserTransaction tx;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
    public EditUserProfileService() {
        super();
    }

	private void commit(UserTransaction tx) {
		try {
			tx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0019 ");
		User_Profile userProfile = null;
		HttpSession session = request.getSession(false);
		InitialContext ctx;
		UserProfile userProfileBean = (UserProfile)session.getAttribute("userProfileBean");
		
		if (userProfileBean == null) {
			try {
				ctx = new InitialContext();
				userProfileBean = (UserProfile)ctx.lookup("java:global/yardiWeb/UserProfileBean");
				//debug
				System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0011");
				//debug
			} catch (NamingException e) {
				//debug
				System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0012");
				//debug
				e.printStackTrace();
			}
			session.setAttribute("userProfileBean", userProfileBean);
			//debug
			System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0013");
			//debug
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String formData = new String();
		formData = "";
		
        if(br != null){
        	formData = br.readLine();
        }
        
        System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0000 "
        	+ "\n"
        	+ "   formData=" + formData);
        ObjectMapper mapper = new ObjectMapper();
		EditUserProfileRequest editRequest = new EditUserProfileRequest();
		editRequest = mapper.readValue(formData, EditUserProfileRequest.class);
		
		if (   editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_FIND)
			|| editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_DELETE)
			|| editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_REMOVE)) {
			/*
			 * The web page is giving us more than we need. We just need a user name for find and delete so
			 * clear out the other fields. Also, for find and delete, the other fields we dont need will have 
			 * garbage left over from the previous request.    
			 */
			System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0010");
			editRequest.specialInzsr();
		}
		
		String feedback [] = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
		editRequest.setMsgID(feedback[0]);
		editRequest.setMsgDescription(feedback[1]);
        System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0001 "
            	+ "\n"
            	+ "   editRequest=" + editRequest);

		if (!(editRequest.getHomeMarket().equals(""))) {
			editRequest.setUpHomeMarket(Short.parseShort(editRequest.getHomeMarket()));
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0002 ");
		}
		
		if (!(editRequest.getPwdAttempts().equals(""))) {
			editRequest.setPasswordAttempts(Short.parseShort(editRequest.getPwdAttempts()));
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0003 ");
		}

		if (!(editRequest.getDob().equals(""))) {
			editRequest.setBirthDate(editRequest.toDate(editRequest.getDob(), "", false));
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0004 ");
		}

		if (!(editRequest.getPwdExpDate().equals(""))) {
			editRequest.setPasswordExpirationDate(editRequest.toDate(editRequest.getPwdExpDate(), "", false));
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0005 ");
		}
		
		if (!(editRequest.getDisabledDate().equals(""))) {
			editRequest.setProfileDisabledDate(editRequest.toDate(editRequest.getDisabledDate(), 
																  editRequest.getDisabledTime(), true));  
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0006 ");
		}

		if (!(editRequest.getLastLogin().equals(""))) {
			editRequest.setLastLoginDate(editRequest.toDate(editRequest.getLastLogin(), 
															editRequest.getLastLoginTime(), true));
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0007 ");
		} 
		
		if (editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_FIND)) {
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0008 FIND"
	        	+ "\n"
	        	+ "   editRequest.getFindUser()=" + editRequest.getFindUser());
	        try {
				tx.begin();
				userProfile = null;
				userProfile = userProfileBean.find(editRequest.getFindUser());

				if (userProfile == null) {
					System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0009 userProfile == null");
					feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000D.split("="); 
					editRequest.setMsgID(feedback[0]);
					editRequest.setMsgDescription(feedback[1]);
					showResponseHeaders(response);
					response.resetBuffer();
					showResponseHeaders(response);
					response.setContentType("application/json");
					PrintWriter out = response.getWriter();
					formData = mapper.writeValueAsString(editRequest); //convert the feedback to json 
					out.print(formData);
					out.flush();
					return;
				}

				System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 000E"
						+ "\n"
						+ "   userProfile=" 
						+ userProfile
						);
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
				editRequest.setMsgID         (feedback[0]);
				editRequest.setMsgDescription(feedback[1]);
				editRequest.setFirstName     (userProfile.getUpFirstName());
				editRequest.setLastName      (userProfile.getUpLastName());
				editRequest.setAddress1      (userProfile.getUpAddress1());

				if (userProfile.getUpAddress2()==null) {
					editRequest.setAddress2  ("");
				} else {
					editRequest.setAddress2  (userProfile.getUpAddress2());
				}

				editRequest.setCity          (userProfile.getUpCity());
				editRequest.setState         (userProfile.getUpState());
				editRequest.setZip           (userProfile.getUpZip());

				if (userProfile.getUpZip4()==null) {
					editRequest.setZip4      ("");
				} else {
					editRequest.setZip4      (userProfile.getUpZip4());
				}

				editRequest.setPhone         (userProfile.getUpPhone());

				if (userProfile.getUpFax()==null) {
					editRequest.setFax       ("");
				} else {
					editRequest.setFax       (userProfile.getUpFax());
				} 

				if (userProfile.getUpEmail()==null) {
					editRequest.setEmail     ("");
				} else {
					editRequest.setEmail     (userProfile.getUpEmail());
				}

				editRequest.setSsn           (userProfile.getUpssn());
				editRequest.setDob           (editRequest.stringify(userProfile        .getUpdob()));
				editRequest.setHomeMarket    (Short.toString(userProfile.getUpHomeMarket()));
				editRequest.setActiveYN      (userProfile.getUpActiveYn());
				editRequest.setPwdExpDate    (editRequest.stringify(userProfile        .getUpPwdexpd()));
				String dateTime[] = new String[2];

				if (userProfile.getUpDisabledDate()==null) {
					editRequest.setDisabledDate("");
					editRequest.setDisabledTime("");
				} else {
					dateTime = editRequest.stringify(userProfile      .getUpDisabledDate());
					editRequest.setDisabledDate(dateTime[0]);
					editRequest.setDisabledTime(dateTime[1]);
				}

				editRequest.setPwdAttempts   (Short.toString(userProfile.getUpPwdAttempts()));
				editRequest.setCurrentToken  (userProfile.getUptoken());
				dateTime = editRequest.stringify(userProfile        .getUpLastLoginDate());
				editRequest.setLastLogin     (dateTime[0]);
				editRequest.setLastLoginTime (dateTime[1]);
				System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 000A FIND"
						+ "\n"
						+ "   editRequest=" + editRequest);
				showResponseHeaders(response);
				response.resetBuffer();
				showResponseHeaders(response);
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				formData = mapper.writeValueAsString(editRequest); //convert the feedback to json 
				out.print(formData);
				out.flush();
				System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 000F"
						+ "\n"
						+ "   formData=" + formData);
				commit(tx);
			} catch (NotSupportedException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if (editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_ADD)) {
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 000B ADD" 
	        	+ "\n"
	        	+ "editRequest=" + editRequest);
	        try {
				tx.begin();
				int rows = userProfileBean.persist(
						editRequest  .getFindUser(),
						editRequest  .getCurrentToken(),
						editRequest  .getUpHomeMarket(),
						editRequest  .getFirstName(), 
						editRequest  .getLastName(), 
						editRequest  .getAddress1(), 
						editRequest  .getAddress2(), 
						editRequest  .getCity(), 
						editRequest  .getState(), 
						editRequest  .getZip(), 
						editRequest  .getZip4(), 
						editRequest  .getPhone(), 
						editRequest  .getFax(), 
						editRequest  .getEmail(), 
						editRequest  .getSsn(),
						editRequest  .getBirthDate(),
						editRequest  .getActiveYN(),
						editRequest  .getPasswordExpirationDate(),
						editRequest  .getProfileDisabledDate(),
						editRequest  .getLastLoginDate(),
						editRequest  .getPasswordAttempts()
						);
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
				editRequest.setMsgID(feedback[0]);
				editRequest.setMsgDescription(feedback[1]);
				showResponseHeaders(response);
				response.resetBuffer();
				showResponseHeaders(response);
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				formData = mapper.writeValueAsString(editRequest); //convert the feedback to json 
				out.print(formData);
				out.flush();
				commit(tx);
			} catch (NotSupportedException | SystemException e) {
				e.printStackTrace();
			}
			return;
		}

		if (editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_DELETE)) {
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 000C DELETE");
	        try {
				tx.begin();
				userProfileBean.remove(editRequest.getFindUser());
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
				editRequest.setMsgID(feedback[0]);
				editRequest.setMsgDescription(feedback[1]);
				showResponseHeaders(response);
				response.resetBuffer();
				showResponseHeaders(response);
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				formData = mapper.writeValueAsString(editRequest); //convert the feedback to json 
				out.print(formData);
				out.flush();
				commit(tx);
			} catch (NotSupportedException | SystemException e) {
				e.printStackTrace();
			}
			return;
		}
			
		if (editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_UPDATE)) {
	        System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 000D UPDATE"
	        	+ "\n"
	        	+ "   editRequest=" + editRequest);
	        try {
				tx.begin();
				int rows = userProfileBean.updateAll(
						editRequest  .getFindUser(),
						editRequest  .getCurrentToken(),
						editRequest  .getUpHomeMarket(),
						editRequest  .getFirstName(), 
						editRequest  .getLastName(), 
						editRequest  .getAddress1(), 
						editRequest  .getAddress2(), 
						editRequest  .getCity(), 
						editRequest  .getState(), 
						editRequest  .getZip(), 
						editRequest  .getZip4(), 
						editRequest  .getPhone(), 
						editRequest  .getFax(), 
						editRequest  .getEmail(), 
						editRequest  .getSsn(),
						editRequest  .getBirthDate(),
						editRequest  .getActiveYN(),
						editRequest  .getPasswordExpirationDate(),
						editRequest  .getProfileDisabledDate(),
						editRequest  .getLastLoginDate(),
						editRequest  .getPasswordAttempts()
						);
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
				editRequest.setMsgID(feedback[0]);
				editRequest.setMsgDescription(feedback[1]);
				showResponseHeaders(response);
				response.resetBuffer();
				showResponseHeaders(response);
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				formData = mapper.writeValueAsString(editRequest); //convert the feedback to json 
				out.print(formData);
				out.flush();
				commit(tx);
			} catch (NotSupportedException | SystemException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if (editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_REMOVE)) {
			System.out.println("com.yardi.QSECOFR.EditUserProfileService doGet() 0018");
			remove(session, response, new EditUserProfileRequest());
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void remove(HttpSession session, HttpServletResponse response, EditUserProfileRequest editRequest) throws IOException {
		//debug
		System.out.println("com.yardi.QSECOFR.EditUserProfileService remove() 0017 ");
		//debug
		UserProfile userProfileBean = (UserProfile)session.getAttribute("userProfileBean");
		session.setAttribute("userProfileBean", null);
		userProfileBean.removeBean();
		String feedback [] = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
		editRequest.setMsgID(feedback[0]);
		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0014.split("=");
		editRequest.setMsgDescription(feedback[1]);
		showResponseHeaders(response);
		response.resetBuffer();
		showResponseHeaders(response);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String formData = new String();
		formData = "";
		ObjectMapper mapper = new ObjectMapper();
		formData = mapper.writeValueAsString(editRequest); //convert the feedback to json 
		out.print(formData);
		out.flush();
	}
	
	private void showResponseHeaders(HttpServletResponse response) {
		//debug 
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.isEmpty()) {
			System.out.println("com.yardi.QSECOFR.EditUserProfileService showResponseHeaders() 0014 headerNames is empty");
		}
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.QSECOFR.EditUserProfileService showResponseHeaders() 0015 "
						+ "\n"
						+ "   Response header name="
						+ n
						+ "   no headerValues");
			}
			for (String v :  headerValues) {
				System.out.println("com.yardi.QSECOFR.EditUserProfileService showResponseHeaders() 0016 "
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
