package introsde.storageservices.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import introsde.storageservices.util.UrlInfo;

@LocalBean
@Stateless
public class SlackResource {
	UrlInfo u;
	String localDBUrl = "", jsonResponse = "";
	
	public SlackResource() {
		u = new UrlInfo();
		localDBUrl = u.getLocalDBURL();
	}
	
	@GET
    @Path("{slackUserId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUserBySlackId(@PathParam("slackUserId") String slackUserId) throws Exception{
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(localDBUrl+"user-id/"+slackUserId);
		HttpResponse response = client.execute(request);
		
		BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		JSONObject o = new JSONObject(result.toString());
		
		if(response.getStatusLine().getStatusCode() == 200 && o.getString("status") != "ERROR"){
			
			return Response.ok(result.toString()).build();
			
		} else if(response.getStatusLine().getStatusCode() == 404){
			jsonResponse += "{\"status\": \"ERROR\","
					+ "\"error\": \""+o.getString("error")+"\"}";
			return Response.status(404).entity(jsonResponse).build();
		} else if(o.getString("status") == "ERROR"){
			jsonResponse += "{\"status\": \"ERROR\","
					+ "\"error\": \""+o.getString("error")+"\"}";
			
			return Response.status(404).entity(jsonResponse).build();
		} else {
			jsonResponse += "{\"status\": \"ERROR\","
					+ "\"error\": \""+response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase()+"\"}";
			return Response.status(404).entity(jsonResponse).build();
		}
    }

}
