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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * Servlet implementation class EditUniqueTokensService
 */
@WebServlet(description = "Handle edit unique tokens requests", urlPatterns = {"/editUniqueTokens"})
public class EditUniqueTokensService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EditUniqueTokensService() {
    }

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
		feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
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

		if (editRequest.getAction().equals(com.yardi.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_UPDATE)) {
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
		
        if (editRequest.getAction().equals(com.yardi.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_FIND)) {
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
			findTokens(editRequest.getFindUser(), userProfileBean, uniqueTokenBean, uniqueTokens, editRequest);
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
			for (int i=0; i < uniqueTokens.size(); i++) {
				//debug
				System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 0024 "
						+ "\n "
						+ "   Long.parseLong(r.getUp1Rrn()="
						+ Long.parseLong(r.getUp1Rrn())
						+ "\n "
						+ "  uniqueTokens.get("
						+ i
						+ ").getUp1Rrn()="
						+ uniqueTokens.get(i).getUp1Rrn()
						+ "\n "
						+ "   uniqueTokens.get("
						+ i
						+ ").getUp1Token()="
						+ uniqueTokens.get(i).getUp1Token()
						);
				//debug
				String [] s = r.getUp1DateAdded().split("-");
				Calendar c = Calendar.getInstance();
				c.set(Integer.parseInt(s[2]),
					  Integer.parseInt(s[1]) - 1,
					  Integer.parseInt(s[0]),
					  0, 0, 0
					 );	
				if ((Long.parseLong(r.getUp1Rrn()) > 0L) && 
					(Long.parseLong(r.getUp1Rrn()) == uniqueTokens.get(i).getUp1Rrn())) {
					//debug
					System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 0019 ");
					//debug
					if (Boolean.valueOf(r.getDeleteToken())) {
						//debug
						System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 001A ");
						//debug
						uniqueTokenBean.remove(uniqueTokens.get(i).getUp1Rrn());
						continue;
					}
					if (! (r.getUp1Token().equals(uniqueTokens.get(i).getUp1Token()))) {
						//debug
						System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 001B ");
						//debug
						uniqueTokenBean.updateToken(uniqueTokens.get(i).getUp1Rrn(), 
								r.getUp1Token());
					}
					if (c.getTimeInMillis() != r.getTokenAddedDate().getTime()) {
						//debug
						System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 0020 ");
						//debug
						uniqueTokenBean.updateDateAdded(uniqueTokens.get(i).getUp1Rrn(), 
								new java.util.Date(c.getTimeInMillis()));
					}
				}
			}			
			if (Long.parseLong(r.getUp1Rrn()) < 0L) {
				//debug
				System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 0021 ");
				//debug
				if	(!(r.getUp1Token().isEmpty()) && 
					 !(r.getUp1DateAdded().isEmpty()) 
					 
					) {
					String [] s = r.getUp1DateAdded().split("-");
					Calendar c = Calendar.getInstance();
					c.set(Integer.parseInt(s[2]),
						  Integer.parseInt(s[1]) - 1,
						  Integer.parseInt(s[0]),
						  0, 0, 0
						 );	
					//debug
					System.out.println("com.yardi.QSECOFR.EditUniqueTokensService updateTokens() 0022 ");
					//debug
					uniqueTokenBean.persist(r.getUp1UserName(), r.getUp1Token(), c.getTime());
				} 
			}
		}
	}
}
