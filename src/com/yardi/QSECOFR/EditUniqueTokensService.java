package com.yardi.QSECOFR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.yardi.ejb.UniqueTokens;
import com.yardi.ejb.Unique_Tokens;
import com.yardi.ejb.UserProfile;

/**
 * Entry point for the edit unique tokens app. Handle requests from the web to edit unique tokens.
 */
@WebServlet(description = "Handle edit unique tokens requests", urlPatterns = {"/editUniqueTokens"})
public class EditUniqueTokensService extends HttpServlet {
	/**
	 * serial version ID
	 */
	private static final long serialVersionUID = 1L;
    
	/**
	 * Default constructor
	 */
    public EditUniqueTokensService() {
    }

    /**
     * Not used
     * @param currentPath currentPath
     * @param jsonNode jsonNode 
     * @param map map 
     * @param suffix suffix 
     */
	private void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> map, List<Integer> suffix) {
		System.out.println("com.yardi.QSECOFR.EditUniqueTokensService addKeys() 0009");
	    if (jsonNode.isObject()) {
			System.out.println("com.yardi.QSECOFR.EditUniqueTokensService addKeys() 000A");
	        ObjectNode objectNode = (ObjectNode) jsonNode;
	        Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
	        String pathPrefix = currentPath.isEmpty() ? "" : currentPath + "-";

	        while (iter.hasNext()) {
				System.out.println("com.yardi.QSECOFR.EditUniqueTokensService addKeys() 000D");
	            Map.Entry<String, JsonNode> entry = iter.next();
	            addKeys(pathPrefix + entry.getKey(), entry.getValue(), map, suffix);
	        }
	    } else if (jsonNode.isArray()) {
			System.out.println("com.yardi.QSECOFR.EditUniqueTokensService addKeys() 000F");
	        ArrayNode arrayNode = (ArrayNode) jsonNode;

	        for (int i = 0; i < arrayNode.size(); i++) {
				System.out.println("com.yardi.QSECOFR.EditUniqueTokensService addKeys() 0010"
						+ "\n "
						+ "  i="
						+ i
						+ "\n"
						+ "  arrayNode.size()"
						+ arrayNode.size()
						);
	            suffix.add(i + 1);
	            addKeys(currentPath, arrayNode.get(i), map, suffix);

	            if (i + 1 <arrayNode.size()){
					System.out.println("com.yardi.QSECOFR.EditUniqueTokensService addKeys() 0011"
							+ "\n "
							+ "  i="
							+ i
							+ "\n"
							+ "  arrayNode.size()="
							+ arrayNode.size()
							+ "\n"
							+ "  suffix.size()="
							+ suffix.size()
							);
	                //suffix.remove(arrayNode.size() - 1);
	                suffix.remove(suffix.size()-1);
	            }
	        } 
	    } else if (jsonNode.isValueNode()) {
			System.out.println("com.yardi.QSECOFR.EditUniqueTokensService addKeys() 000B");
	        if (currentPath.contains("-")) {
				System.out.println("com.yardi.QSECOFR.EditUniqueTokensService addKeys() 000C");
	            for (int i = 0; i < suffix.size(); i++) {
					System.out.println("com.yardi.QSECOFR.EditUniqueTokensService addKeys() 000E");
	                currentPath += "-" + suffix.get(i);
	            }

				System.out.println("com.yardi.QSECOFR.EditUniqueTokensService addKeys() 0012");
	            suffix = new ArrayList<>();
	        }

	        ValueNode valueNode = (ValueNode) jsonNode;
	        map.put(currentPath, valueNode.asText());
			System.out.println("com.yardi.QSECOFR.EditUniqueTokensService addKeys() 0013"
					+ "\n "
					+ "  currentPath="
					+ currentPath
					+ "\n "
					+ "  valueNode.asText()="
					+ valueNode.asText()
					);
	    }
	}

	/**
	 * Handle requests for editing unique tokens. Two basic requests are supported:
	 * <ul>
	 *   <li> find the unique tokens for a given user </li>
	 *   <li> the update request is a combination of adding new tokens, updating existing tokens and deleting existing tokens</li>
	 * </ul>
	 * <p>
	 * doGet() begins by obtaining a reference to {@link com.yardi.ejb.UserProfileBean#UserProfileBean() com.yardi.ejb.UserProfileBean} and 
	 * {@link com.yardi.ejb.UniqueTokensBean#UniqueTokensBean() com.yardi.ejb.UniqueTokensBean} from JNDI. 
	 * <p>
	 * The request is first deserialized to {@link com.yardi.QSECOFR.EditUniqueTokensRequest#EditUniqueTokensRequest() com.yardi.QSECOFR.EditUniqueTokensRequest}. At this 
	 * point, only the <code>action</code>, <code>findUser</code>, and <code>uniqueTokensString</code> fields in 
	 * <code>EditUniqueTokensRequest</code> are populated. Field <code>uniqueTokensString</code> contains a JSON array.
	 * <p>
	 * If the request is an update request, then the <code>uniqueTokensString</code> field in <code>EditUniqueTokensRequest</code> is 
	 * deserialized to a <code>Vector&lt;EditUniqueTokensRequest&gt;</code>. Now fields <code>up1UserName</code>, <code>up1Token</code>, <code>up1DateAdded</code>,
	 * <code>up1Rrn</code>, and <code>deleteToken</code> are populated. doGet() calls <code>updateTokens()</code> to handle the rest of the update request.
	 * <p>
	 * If the request is a find request doGet calls <code>findTokens()</code> to further handle the request.
	 * <p>
	 * Finally doGet() responds to the web request.
	 * 
	 * @param request see jakarta.servlet.http.HttpServletRequest 
	 * @param response see jakarta.servlet.http.HttpServletResponse  
	 * @throws ServletException a general servlet exception 
	 * @throws IOException a general I/O exception
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//debug
		System.out.println("com.yardi.QSECOFR.EditUniqueTokensService doGet() 0000");
		//debug
		String feedback;
		EditUniqueTokensRequest editRequest;
		Vector<EditUniqueTokensRequest> updatedTokens = new Vector<EditUniqueTokensRequest>();
		Vector<Unique_Tokens> uniqueTokens = new Vector<Unique_Tokens>();
		InitialContext ctx;
		UserProfile userProfileBean = null;
		UniqueTokens uniqueTokenBean = null;
		
		try {
			ctx = new InitialContext();
			userProfileBean = (UserProfile) ctx.lookup("java:global/yardiWeb/UserProfileBean");
			uniqueTokenBean = (UniqueTokens)ctx.lookup("java:global/yardiWeb/UniqueTokensBean");
			// debug
			System.out.println("com.yardi.QSECOFR.EditUniqueTokensService doGet() 0016");
			// debug
		} catch (NamingException e) {
			// debug
			System.out.println("com.yardi.QSECOFR.EditUniqueTokensService doGet() 0017");
			// debug
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String formData = new String();
		formData = "";
		
        if(br != null){
        	formData = br.readLine();
        }
        
		//debug
		System.out.println("com.yardi.QSECOFR.EditUniqueTokensService doGet() 0014"
				+ "\n "
				+ "  formData="
				+ formData
				);
		//debug
		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
        ObjectMapper mapper = new ObjectMapper();
        editRequest = new EditUniqueTokensRequest();
        editRequest = mapper.readValue(formData, EditUniqueTokensRequest.class);
		//debug
		System.out.println("com.yardi.QSECOFR.EditUniqueTokensService doGet() 0001"
				+ "\n "
				+ "  editRequest="
				+ editRequest
				);
		//debug

		if (editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_UPDATE)) {
			/*
			 * How to iterate all subnodes of a json object? 
			 * https://stackoverflow.com/questions/48642450/how-to-iterate-all-subnodes-of-a-json-object 
			 */
			//debug
			System.out.println("com.yardi.QSECOFR.EditUniqueTokensService doGet() 0008");
			//debug
			updatedTokens = mapper.readValue(editRequest.getUniqueTokensString(),
					new TypeReference<Vector<EditUniqueTokensRequest>>() {}
					);
			//debug
			System.out.println("com.yardi.QSECOFR.EditUniqueTokensService doGet() 0015"
					+ "\n "
					+ "  updatedTokens =["
					);
			for (int i = 0; i < updatedTokens.size(); i++) {
				EditUniqueTokensRequest r = updatedTokens.get(i); 
				r.setUniqueTokensString("");
				System.out.println(
						  "\n "
						+ "  up1UserName="
						+ r.getUp1UserName()
						+ "\n "
						+ "  up1Token="
						+ r.getUp1Token()
						+ "\n "
						+ "  up1DateAdded="
						+ r.getUp1DateAdded()
						+ "\n "
						+ "  up1Rrn="
						+ r.getUp1Rrn()
						+ "\n "
						+ "  deleteToken="
						+ r.getDeleteToken()
						+ "\n "
						);
			}
			//debug
			updateTokens(uniqueTokenBean, uniqueTokens, updatedTokens);
		}
		
        if (editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_FIND)) {
    		//debug
    		System.out.println("com.yardi.QSECOFR.EditUniqueTokensService doGet() 0002");
    		//debug
    		/*
    		 * Need to map this JSON
    		 * {
    		 *   "up1UserName":"aa",
    		 *   "up1Token":"$31$16$qba-udEOtO0ZqNe73OuJCspHqHm0ILLZTk6mI6ISa6w",
    		 *   "up1DateAdded":"01-01-2018",
    		 *   "up1Rrn":"570",
    		 *   "delete":true
    		 * },
    		 * 
    		 * If we try to map the JSON object to Unique_Tokens (entity) delete doesnt match
    		 * What happens when mapper.readValue() cant match 
    		 * 
    		 * How to extract an element from the JSON string using .readtree()
    		 * https://stackoverflow.com/questions/10113512/readvalue-and-readtree-in-jackson-when-to-use-which
    		 * 
    		 * How to map JSON object to HashMap in case we cant map to Unique_Tokens (entity) because delete doesnt match
    		 * http://www.baeldung.com/jackson-map see 4.1. Map<String, String> Deserialization
    		 * TypeReference is part of Jackson core
    		 */
    		if (findTokens(editRequest.getFindUser(), userProfileBean, uniqueTokenBean, uniqueTokens, editRequest) == false) {
    			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000D;
    		} 
			
			String msg[] = feedback.split("=");
			editRequest.setMsgID(msg[0]);
			editRequest.setMsgDescription(msg[1]);
			editRequest.setUniqueTokensString(mapper.writeValueAsString(editRequest.getUniqueTokens()));
    		//debug
    		System.out.println("com.yardi.QSECOFR.EditUniqueTokensService doGet() 0006"
    				+ "\n "
    				+ "  uniqueTokensString="
    				+ editRequest.getUniqueTokensString() 
    				);
    		//debug
    		showResponseHeaders(response);
			response.resetBuffer();
    		showResponseHeaders(response);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			formData = mapper.writeValueAsString(editRequest);
    		//debug
    		System.out.println("com.yardi.QSECOFR.EditUniqueTokensService doGet() 0007"
    				+ "\n "
    				+ "  formData="
    				+ formData 
    				);
    		//debug
			out.print(formData);
			out.flush();
        }
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Handle the find request by finding all tokens for the given user. Delegate to <code>com.yardi.ejb.UniqueTokensBean.findTokens(userName)</code> to get the tokens. 
	 * The user tokens are stored in field uniqueTokens on the edit request <code>com.yardi.QSECOFR.EditUniqueTokensRequest</code>.
	 * 
	 * @param userName user name
	 * @param userProfileBean reference to {@link com.yardi.ejb.UserProfileBean#UserProfileBean() com.yardi.ejb.UserProfileBean}
	 * @param uniqueTokenBean reference to {@link com.yardi.ejb.UniqueTokensBean#UniqueTokensBean() com.yardi.ejb.UniqueTokensBean}
	 * @param uniqueTokens Vector&lt;{@link com.yardi.ejb.Unique_Tokens#Unique_Tokens() com.yardi.ejb.Unique_Tokens}&gt;
	 * @param editRequest reference to {@link com.yardi.QSECOFR.EditUniqueTokensRequest#EditUniqueTokensRequest() com.yardi.QSECOFR.EditUniqueTokensRequest}
	 * @return boolean indicating whether the given <code>userNane</code> is valid
	 */
	private boolean findTokens(String userName, UserProfile userProfileBean, UniqueTokens uniqueTokenBean, Vector<Unique_Tokens> uniqueTokens, EditUniqueTokensRequest editRequest) {
		//debug
		System.out.println("com.yardi.QSECOFR.EditUniqueTokensService findTokens() 0003");
		//debug

		if (userProfileBean.find(userName) == null) {
			//debug
			System.out.println("com.yardi.QSECOFR.EditUniqueTokensService findTokens() 0004");
			//debug
			editRequest.setUniqueTokens(null);
			return false;
		} 
		
		uniqueTokens = uniqueTokenBean.findTokens(userName);
		editRequest.setUniqueTokens(uniqueTokens);
		//debug
		System.out.println("com.yardi.QSECOFR.EditUniqueTokensService findTokens() 0005"
				+ "\n "
				+ "  uniqueTokens=" 
				+ editRequest.getUniqueTokens()
				);
		//debug
		return true;
	}
	
	/**
	 * Show the response headers to ensure the session ID isn't getting wiped somewhere
	 * @param response see jakarta.servlet.http.HttpServletResponse
	 */
	private void showResponseHeaders(HttpServletResponse response) {
		//debug 
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.isEmpty()) {
			System.out.println("com.yardi.QSECOFR.EditUniqueTokensService showResponseHeaders() 001C headerNames is empty");
		}
		for (String n : headerNames) {
			Collection<String> headerValues = response.getHeaders(n);
			if (headerValues.isEmpty()) {
				System.out.println("com.yardi.QSECOFR.EditUniqueTokensService showResponseHeaders() 001D "
						+ "\n"
						+ "   Response header name="
						+ n
						+ "   no headerValues");
			}
			for (String v :  headerValues) {
				System.out.println("com.yardi.QSECOFR.EditUniqueTokensService showResponseHeaders() 001E "
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
	 * Handle the update request. An update request is a combination of adding new tokens, updating existing tokens and deleting existing tokens.
	 * <p>
	 * Because {@link com.yardi.QSECOFR.EditUniqueTokensRequest#up1DateAdded com.yardi.QSECOFR.EditUniqueTokensRequest.up1DateAdded} maps to <code>String</code>,
	 * a <code>java.util.Calendar</code> is constructed from the month, day, century and year components of <code>up1DateAdded</code> so that the mills from this calendar 
	 * can be passed to <code>UniqueTokensBean.updateToken</code>.
	 * <p>
	 * Tokens with a relative record number of &gt;0 are tokens that exist in the database table UNIQUE_TOKENS. Tokens being added have a negative relative record number.
	 * Tokens having a positive relative record number that are not being deleted are updated.
	 * <p>
	 * If <code>EditUniqueTokensRequest.deleteToken</code> is true then the token is being deleted. Delegate to 
	 * {@link com.yardi.ejb.UniqueTokensBean#remove(long) UniqueTokensBean.remove()} to remove the token from the database.
	 * <p>
	 * If the relative record number is greater than zero and the token is not being deleted then the token is updated. Delegate to 
	 * {@link com.yardi.ejb.UniqueTokensBean#updateToken(Long, String, Long) UniqueTokensBean.updateToken()}.
	 * <p>
	 * If the relative record number is negative then persist the token to database table UNIQUE_TOKENS. Delegate to 
	 * {@link com.yardi.ejb.UniqueTokensBean#persist(String, String, java.util.Date) UniqueTokensBean.persist()}.
	 * 
	 * @param uniqueTokenBean reference to {@link com.yardi.ejb.UniqueTokensBean#UniqueTokensBean() com.yardi.ejb.UniqueTokensBean}
	 * @param uniqueTokens Vector&lt;{@link com.yardi.ejb.Unique_Tokens#Unique_Tokens() com.yardi.ejb.Unique_Tokens}&gt;
	 * @param updatedTokens Vector&lt;{@link com.yardi.QSECOFR.EditUniqueTokensRequest#EditUniqueTokensRequest() com.yardi.QSECOFR.EditUniqueTokensRequest}&gt;
	 */
	private void updateTokens(UniqueTokens uniqueTokenBean, Vector<Unique_Tokens> uniqueTokens, Vector<EditUniqueTokensRequest> updatedTokens) {
		//debug
		System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 0018 \n   EditUniqueTokensRequest");
		
		for (EditUniqueTokensRequest r : updatedTokens) {
			System.out.println("   " + r);
		}
		
		System.out.println("   uniqueTokens");
		
		for (Unique_Tokens t : uniqueTokens) {
			System.out.println("   " + t);
		}
		//debug
		for (EditUniqueTokensRequest r : updatedTokens) {
			//debug
			System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 0023 "
					+ "\n "
					+ r
					);
			//debug
			String [] s = r.getUp1DateAdded().split("/");
			Calendar c = Calendar.getInstance();

			if (Boolean.valueOf(r.getDeleteToken()) == false) {
				//HTML does not edit up1DateAdded if the token is being deleted. up1DateAdded might be an empty string 
				c.set(Integer.parseInt(s[2]),
						  Integer.parseInt(s[0]) - 1,
						  Integer.parseInt(s[1]),
						  0, 0, 0
						 );	
			}
			
			if (Boolean.valueOf(r.getDeleteToken())) {
				//debug
				System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 001A ");
				//debug
				uniqueTokenBean.remove(Long.parseLong(r.getUp1Rrn()));
			}

			if ((Long.parseLong(r.getUp1Rrn()) > 0L) && (Boolean.valueOf(r.getDeleteToken())==false)) {
				//debug
				System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 001B ");
				//debug
				uniqueTokenBean.updateToken(Long.parseLong(r.getUp1Rrn()), r.getUp1Token(), c.getTimeInMillis());
			}
			
			if (Long.parseLong(r.getUp1Rrn()) < 0L) {
				//debug
				System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 0021 ");
				//debug
				if	(!(r.getUp1Token().isEmpty()) && 
					 !(r.getUp1DateAdded().isEmpty())					 
					) {
					//debug
					System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 0022 ");
					//debug
					uniqueTokenBean.persist(r.getUp1UserName(), r.getUp1Token(), c.getTime());
				} 
			}
		}
	}
}
