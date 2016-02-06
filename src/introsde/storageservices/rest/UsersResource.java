package introsde.storageservices.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import introsde.storageservices.util.UrlInfo;

@Stateless
@LocalBean
public class UsersResource {

	UrlInfo u;
	String localDBUrl = "", jsonResponse = "", location = "";
	@Context
    UriInfo uriInfo;

	public UsersResource(UriInfo uriInfo) {
		u = new UrlInfo();
		localDBUrl = u.getLocalDBURL();
		this.uriInfo = uriInfo;
	}
	
	@GET
    @Path("{userId}/goals")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPersonalGoals(@PathParam("userId") int userId) throws Exception{
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(localDBUrl+"users/"+userId+"/goals");
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

    @GET
    @Path("{userId}/runs")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRecentRuns(
    		@PathParam("userId") int userId, 
    		@DefaultValue("0") @QueryParam("start_date") long startDate) throws Exception{
    	
    	HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(localDBUrl+"users/"+userId+"/runs?start_date="+startDate);
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
    
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response newPerson(String inputUser) throws Exception{
    	
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(localDBUrl+"users");
		
		StringEntity input = new StringEntity(inputUser);
		input.setContentType("application/json");
		postRequest.setEntity(input);
		URI stringlocation = null;
		
		HttpResponse response = httpClient.execute(postRequest);
		
		Header[] headers = response.getAllHeaders();
		
		for(Header h: headers) {
			if(h.getName().equals("Location")){
				location = h.getValue();
			}
		}
		stringlocation = new URI(location);
		

		if (response.getStatusLine().getStatusCode() == 400) {
			jsonResponse = "{\"status\": \"ERROR\","
					+ "\"error\": \""+response.getStatusLine().getStatusCode()+"\"}";
			return Response.status(400).entity(jsonResponse).build();
		} else if (response.getStatusLine().getStatusCode() == 404) {
			jsonResponse = "{\"status\": \"ERROR\","
					+ "\"error\": \""+response.getStatusLine().getStatusCode()+"\"}";
			return Response.status(404).entity(jsonResponse).build();
		} else {
			BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			httpClient.getConnectionManager().shutdown();
			return Response.created(stringlocation).entity(result.toString()).build();
		}
    }
    
    @POST
    @Path("{userId}/runs")
    @Produces({MediaType.APPLICATION_JSON})
    public Response registerRun(@PathParam("userId") int userId, String inputRun) throws Exception{
    	
    	DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(localDBUrl+"users/"+userId+"/runs");
		
		StringEntity input = new StringEntity(inputRun);
		input.setContentType("application/json");
		postRequest.setEntity(input);
		
		URI stringlocation = null;
		
		HttpResponse response = httpClient.execute(postRequest);
		
		Header[] headers = response.getAllHeaders();
		
		for(Header h: headers) {
			if(h.getName().equals("Location")){
				location = h.getValue();
			}
		}
		stringlocation = new URI(location);
		

		if (response.getStatusLine().getStatusCode() == 400) {
			jsonResponse = "{\"status\": \"ERROR\","
					+ "\"error\": \""+response.getStatusLine().getStatusCode()+"\"}";
			return Response.status(400).entity(jsonResponse).build();
		} else if (response.getStatusLine().getStatusCode() == 404) {
			jsonResponse = "{\"status\": \"ERROR\","
					+ "\"error\": \""+response.getStatusLine().getStatusCode()+"\"}";
			return Response.status(404).entity(jsonResponse).build();
		} else {
			BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			httpClient.getConnectionManager().shutdown();
			return Response.created(stringlocation).entity(result.toString()).build();
		}
    }
    
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("{userId}")
    public Response updatePerson(@PathParam("userId") int userId, String inputUser) throws Exception{
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	HttpPut postRequest = new HttpPut(localDBUrl+"users/"+userId);
		
		StringEntity input = new StringEntity(inputUser);
		input.setContentType("application/json");
		postRequest.setEntity(input);
		
		HttpResponse response = httpClient.execute(postRequest);		

		if (response.getStatusLine().getStatusCode() == 400) {
			jsonResponse = "{\"status\": \"ERROR\","
					+ "\"error\": \""+response.getStatusLine().getStatusCode()+"\"}";
			return Response.status(400).entity(jsonResponse).build();
		} else if (response.getStatusLine().getStatusCode() == 404) {
			jsonResponse = "{\"status\": \"ERROR\","
					+ "\"error\": \""+response.getStatusLine().getStatusCode()+"\"}";
			return Response.status(404).entity(jsonResponse).build();
		} else {
			BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			httpClient.getConnectionManager().shutdown();
			return Response.ok(result.toString()).build();
		}
    }
}




