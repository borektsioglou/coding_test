package pay4later;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Solution {
	ArrayList<User> users;
	
	public Solution(){
		users = new ArrayList<User>();
	}
	public void parseCSV(String filename){
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
        	
            br = new BufferedReader(new FileReader(filename));
            line = br.readLine();
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] userString = line.split(cvsSplitBy);
                User user = new User(Integer.parseInt(userString[0]), userString[1], userString[2], userString[3], userString[4], userString[5]);
                users.add(user);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	public void parseXML(String filename){
		try {	
	         File inputFile = new File(filename);
	         DocumentBuilderFactory dbFactory 
	            = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         doc.getDocumentElement().getNodeName();
	         NodeList nList = doc.getElementsByTagName("user");
	         
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            nNode.getNodeName();
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               int userID = Integer.parseInt(eElement.getElementsByTagName("userid")
	                  .item(0)
	                  .getTextContent());
	               String firstname = eElement
	                  .getElementsByTagName("firstname")
	                  .item(0)
	                  .getTextContent();
	               String lastname = eElement
	                  .getElementsByTagName("surname")
	                  .item(0)
	                  .getTextContent();
	               String username = eElement
	                  .getElementsByTagName("username")
	                  .item(0)
	                  .getTextContent();
	               String type = eElement
	                  .getElementsByTagName("type")
	                  .item(0)
	                  .getTextContent();
	               String lastLoginTime = eElement
    	                  .getElementsByTagName("lastlogintime")
    	                  .item(0)
    	                  .getTextContent();
	               User user = new User(userID, firstname, lastname, username, type, lastLoginTime);
	               users.add(user);
	            }
	            
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	}
	
	public void parseJSON(String filename){
		try {
			BufferedReader reader = new BufferedReader(new FileReader (filename));
		    String         line = null;
		    StringBuilder  stringBuilder = new StringBuilder();
		    String         ls = System.getProperty("line.separator");

		    //parse the json file
	        while((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        }
	        String jsonStr = stringBuilder.toString();
	        try {
	            JSONObject rootObject = new JSONObject(jsonStr); // Parse the JSON to a JSONObject
	            JSONArray rows = rootObject.getJSONArray("users"); // Get all JSONArray rows
	            for(int i=0; i < rows.length(); i++) { // Loop over each each row
	                JSONObject row = rows.getJSONObject(i); // Get row object
	                
	                //take all the elements
	                int userID = row.getInt("user_id");
	                String firstName = row.getString("first_name").toString(); 
	                String lastName = row.getString("last_name").toString(); 
	                String userName = row.getString("username").toString(); 
	                String type = row.getString("user_type").toString(); 
	                String lastLoginTime = row.getString("last_login_time").toString(); 
	                //create user and put it into the list
	                User user = new User(userID, firstName, lastName, userName, type, lastLoginTime);
	                users.add(user);

	            }
	        } catch (JSONException e) {
	            // JSON Parsing error
	            e.printStackTrace();
	        }
//	        return stringBuilder.toString();
	        reader.close();
	    } catch (Exception e) {
	         e.printStackTrace();
	    }
	}

	public void writeJSON(String filename){
		try {
			PrintWriter out = new PrintWriter(filename);
			JSONObject jsonObj = new JSONObject();
			String fileStr = "[ \n";
			for(int i = 0; i < users.size(); i++){
				
				int userID = users.get(i).getUserID();
				String firstName = users.get(i).getFirstName();
				String lastName = users.get(i).getLastName();
				String userName = users.get(i).getUserName();
				String type = users.get(i).getUserType();
				String lastLoginTime = users.get(i).getLastLoginTime();
				jsonObj.put("user_id", userID);
			    jsonObj.put("first_name", firstName);
			    jsonObj.put("last_name", lastName);
			    jsonObj.put("username", userName);
			    jsonObj.put("user_type", type);
			    jsonObj.put("last_login_time", lastLoginTime);
	//		    System.out.println(jsonObj.toString(2));
			    if( i < users.size() - 1)
			    	fileStr = fileStr + jsonObj.toString(2) + ",\n";
			    else
			    	fileStr = fileStr + jsonObj.toString(2) + "\n";
			}
			fileStr = fileStr + "]";
			out.println(fileStr);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	public void writeCSV(String filename){
		try {
			PrintWriter out = new PrintWriter(filename);
			String line;
			out.println("\"User ID\",\"First Name\",\"Last Name\",Username,\"User Type\",\"Last Login Time\"");
			for(int i = 0; i < users.size(); i++){
				int userID = users.get(i).getUserID();
				String firstName = users.get(i).getFirstName();
				String lastName = users.get(i).getLastName();
				String userName = users.get(i).getUserName();
				String type = users.get(i).getUserType();
				String lastLoginTime = users.get(i).getLastLoginTime();
				line = Integer.toString(userID) + "," + firstName + "," + lastName + "," + userName + "," + type + "," + lastLoginTime;
				out.println(line);
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public void writeXML(String filename){
		try {
			PrintWriter out = new PrintWriter(filename);
			String line;
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<users>");
			for(int i = 0; i < users.size(); i++){
				out.println("\t<user>");
				int userID = users.get(i).getUserID();
				String firstName = users.get(i).getFirstName();
				String lastName = users.get(i).getLastName();
				String userName = users.get(i).getUserName();
				String type = users.get(i).getUserType();
				String lastLoginTime = users.get(i).getLastLoginTime();
				out.println("\t\t<userid>" + userID + "</userid>");
				out.println("\t\t<firstname>" + firstName + "</firstname>");
				out.println("\t\t<surname>" + lastName + "</surname>");
				out.println("\t\t<username>" + userName + "</username>");
				out.println("\t\t<type>" + type + "</type>");
				out.println("\t\t<lastlogintime>" + lastLoginTime + "</lastlogintime>");
				out.println("\t</user>");
			}
			out.println("</users>");
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args){
		Solution s = new Solution();
		s.parseCSV("users.csv");
		s.parseXML("users.xml");
		s.parseJSON("users.json");
		Collections.sort(s.users, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
			    if (u1.getUserID() < u2.getUserID()) {
			        return -1;
			    } else if (u1.getUserID() > u2.getUserID()) {
			        return 1;
			    }
			    return 0;
			}});
		s.writeJSON("examples/users.json");
		s.writeCSV("examples/users.csv");
		s.writeXML("examples/users.xml");
	}
}
