package com.yardi.QSECOFR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.EJB;
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
	private Vector<Unique_Tokens> uniqueTokens = new Vector<Unique_Tokens>();
	private Vector<EditUniqueTokensRequest> updatedTokens = new Vector<EditUniqueTokensRequest>();

	EditUniqueTokensRequest editRequest;
	String feedback;
	@EJB UserProfile userProfileBean;
	@EJB UniqueTokens uniqueTokenBean;
       
    public EditUniqueTokensService() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//debug
		System.out.println("com.yardi.QSECOFR EditUniqueTokensService doGet() 0000");
		//debug
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String formData = new String();
		formData = "";
		
        if(br != null){
        	formData = br.readLine();
        }
        
		//debug
		System.out.println("com.yardi.QSECOFR EditUniqueTokensService doGet() 0014"
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
		System.out.println("com.yardi.QSECOFR EditUniqueTokensService doGet() 0001"
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
			System.out.println("com.yardi.QSECOFR EditUniqueTokensService doGet() 0008");
			//debug
			updatedTokens = mapper.readValue(editRequest.getUniqueTokensString(),
					new TypeReference<Vector<EditUniqueTokensRequest>>() {}
					);
			//debug
			System.out.println("com.yardi.QSECOFR EditUniqueTokensService doGet() 0015"
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
			updateTokens();
		}
		
        if (editRequest.getAction().equals(com.yardi.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_FIND)) {
    		//debug
    		System.out.println("com.yardi.QSECOFR EditUniqueTokensService doGet() 0002");
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
    		//debug
			findTokens(editRequest.getFindUser());
			String msg[] = feedback.split("=");
			editRequest.setMsgID(msg[0]);
			editRequest.setMsgDescription(msg[1]);
			editRequest.setUniqueTokensString(mapper.writeValueAsString(editRequest.getUniqueTokens()));
    		//debug
    		System.out.println("com.yardi.QSECOFR EditUniqueTokensService doGet() 0006"
    				+ "\n "
    				+ "  uniqueTokensString="
    				+ editRequest.getUniqueTokensString() 
    				);
    		//debug
			response.reset();
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			formData = mapper.writeValueAsString(editRequest);
    		//debug
    		System.out.println("com.yardi.QSECOFR EditUniqueTokensService doGet() 0007"
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
	
	private boolean findTokens(String userName) {
		//debug
		System.out.println("com.yardi.QSECOFR EditUniqueTokensService findTokens() 0003");
		//debug

		if (userProfileBean.find(userName) == null) {
			//debug
			System.out.println("com.yardi.QSECOFR EditUniqueTokensService findTokens() 0004");
			//debug
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0001;
			editRequest.setUniqueTokens(null);
			return false;
		} 
		
		uniqueTokens = uniqueTokenBean.findTokens(userName);
		editRequest.setUniqueTokens(uniqueTokens);
		//debug
		System.out.println("com.yardi.QSECOFR EditUniqueTokensService findTokens() 0005"
				+ "\n "
				+ "  uniqueTokens=" 
				+ editRequest.getUniqueTokens()
				);
		//debug
		return true;
	}

	private void updateTokens() {
		for (EditUniqueTokensRequest r : updatedTokens) {
			for (int i=0; i < uniqueTokens.size(); i++) {
				String [] s = r.getUp1DateAdded().split("-");
				Calendar c = Calendar.getInstance();
				c.set(Integer.parseInt(s[2]),
					  Integer.parseInt(s[1]) - 1,
					  Integer.parseInt(s[0]),
					  0, 0, 0
					 );	
				if ((Long.parseLong(r.getUp1Rrn()) > 0L) && 
					(Long.parseLong(r.getUp1Rrn()) == uniqueTokens.get(i).getUp1Rrn()) ) {
					if (Boolean.valueOf(r.getDeleteToken())) {
						uniqueTokenBean.remove(uniqueTokens.get(i).getUp1Rrn());
						continue;
					}
					if (! (r.getUp1Token().equals(uniqueTokens.get(i).getUp1Token()))) {
						uniqueTokenBean.updateToken(uniqueTokens.get(i).getUp1Rrn(), 
								r.getUp1Token());
					}
					if (c.getTimeInMillis() != r.getTokenAddedDate().getTime()) {
						uniqueTokenBean.updateDateAdded(uniqueTokens.get(i).getUp1Rrn(), 
								new java.util.Date(c.getTimeInMillis()));
					}
				}
				if (Long.parseLong(r.getUp1Rrn()) < 0L) {
					if	(!(r.getUp1Token().isEmpty()) && 
						 !(r.getUp1DateAdded().isEmpty()) 
						 
						) {
						s = r.getUp1DateAdded().split("-");
						c = Calendar.getInstance();
						c.set(Integer.parseInt(s[2]),
							  Integer.parseInt(s[1]) - 1,
							  Integer.parseInt(s[0]),
							  0, 0, 0
							 );	
						uniqueTokenBean.persist(r.getUp1UserName(), r.getUp1Token(), c.getTime());
					} 
				}
			}
		}
	}
	
	private void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> map, List<Integer> suffix) {
		System.out.println("com.yardi.QSECOFR EditUniqueTokensService addKeys() 0009");
	    if (jsonNode.isObject()) {
			System.out.println("com.yardi.QSECOFR EditUniqueTokensService addKeys() 000A");
	        ObjectNode objectNode = (ObjectNode) jsonNode;
	        Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
	        String pathPrefix = currentPath.isEmpty() ? "" : currentPath + "-";

	        while (iter.hasNext()) {
				System.out.println("com.yardi.QSECOFR EditUniqueTokensService addKeys() 000D");
	            Map.Entry<String, JsonNode> entry = iter.next();
	            addKeys(pathPrefix + entry.getKey(), entry.getValue(), map, suffix);
	        }
	    } else if (jsonNode.isArray()) {
			System.out.println("com.yardi.QSECOFR EditUniqueTokensService addKeys() 000F");
	        ArrayNode arrayNode = (ArrayNode) jsonNode;

	        for (int i = 0; i < arrayNode.size(); i++) {
				System.out.println("com.yardi.QSECOFR EditUniqueTokensService addKeys() 0010"
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
					System.out.println("com.yardi.QSECOFR EditUniqueTokensService addKeys() 0011"
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
			System.out.println("com.yardi.QSECOFR EditUniqueTokensService addKeys() 000B");
	        if (currentPath.contains("-")) {
				System.out.println("com.yardi.QSECOFR EditUniqueTokensService addKeys() 000C");
	            for (int i = 0; i < suffix.size(); i++) {
					System.out.println("com.yardi.QSECOFR EditUniqueTokensService addKeys() 000E");
	                currentPath += "-" + suffix.get(i);
	            }

				System.out.println("com.yardi.QSECOFR EditUniqueTokensService addKeys() 0012");
	            suffix = new ArrayList<>();
	        }

	        ValueNode valueNode = (ValueNode) jsonNode;
	        map.put(currentPath, valueNode.asText());
			System.out.println("com.yardi.QSECOFR EditUniqueTokensService addKeys() 0013"
					+ "\n "
					+ "  currentPath="
					+ currentPath
					+ "\n "
					+ "  valueNode.asText()="
					+ valueNode.asText()
					);
	    }
	}	
}
