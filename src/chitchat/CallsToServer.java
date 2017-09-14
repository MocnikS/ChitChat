package chitchat;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallsToServer {
	
	private static String URLAddress = "http://chitchat.andrej.com";
	
	public String username = System.getProperty("user.name");
	
	public List<String> getUsernames() throws URISyntaxException, ClientProtocolException, IOException {
		String time = Long.toString(new Date().getTime());

		URI uri = new URIBuilder(URLAddress +"/users")
				.addParameter("stop_cache", time)
				.build();

		String responseBody = Request.Get(uri)
				.execute()
				.returnContent()
				.asString();
		
		JSONArray users = new JSONArray(responseBody);
		List<String> usernames = new ArrayList<String>();
		for(int i = 0; i < users.length(); i = i + 1) {
			usernames.add(users.getJSONObject(i).getString("username"));
		}
		
		return usernames;
	}

	public String loggin(String username) throws URISyntaxException, ClientProtocolException, IOException{
		URI uri = new URIBuilder(URLAddress +"/users")
				.addParameter("username", username)
				.build();

		String responseBody = Request.Post(uri)
				.execute()
				.returnContent()
				.asString();

		return responseBody;
	}

	public void loggout(String username) throws URISyntaxException, ClientProtocolException, IOException{
		URI uri = new URIBuilder(URLAddress +"/users")
				.addParameter("username", username)
				.build();

		String responseBody = Request.Delete(uri)
				.execute()
				.returnContent()
				.asString();

	}
	
	public String getMessages(String username) throws URISyntaxException, ClientProtocolException, IOException{
		URI uri = new URIBuilder(URLAddress +"/messages")
				.addParameter("username", username)
				.build();

		String responseBody = Request.Get(uri)
				.execute()
				.returnContent()
				.asString();
		String returnResponse = "";
		
		JSONArray messages = new JSONArray(responseBody);
		if(messages.length() != 0) {
			for(int i = 0; i < messages.length(); i = i + 1) {
				JSONObject obj = messages.getJSONObject(i);
				if(obj.getBoolean("global")) {
					returnResponse = returnResponse + "Global message from " + obj.getString("sender") + ": " + obj.getString("text") + "\n";
				}
				else {
					returnResponse = returnResponse + "Private message from " + obj.getString("sender") + ": " + obj.getString("text") + "\n";
				}
			}
		}
		return returnResponse;
	}
	
	public void sendPrivateMessage(String username, String to, String message) throws URISyntaxException, ClientProtocolException, IOException{
		URI uri = new URIBuilder(URLAddress +"/messages")
				.addParameter("username", username)
				.build();
		
		 String data = "{ \"global\" : false, \"recipient\": \""+to+"\", \"text\" : \""+message+"\"  }";
		 String responseBody = Request.Post(uri)
		          .bodyString(data, ContentType.APPLICATION_JSON)
		          .execute()
		          .returnContent()
		          .asString();
		
		 
	}
	
	public void sendPublicMessage(String username, String message) throws URISyntaxException, ClientProtocolException, IOException{
		URI uri = new URIBuilder(URLAddress +"/messages")
				.addParameter("username", username)
				.build();
		
		 String data = "{ \"global\" : true, \"text\" : \""+message+"\"  }";

		  String responseBody = Request.Post(uri)
		          .bodyString(data, ContentType.APPLICATION_JSON)
		          .execute()
		          .returnContent()
		          .asString();

	}
}
