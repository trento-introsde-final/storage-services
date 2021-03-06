package introsde.storageservices.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import introsde.storageservices.util.UrlInfo;

@Stateless
@LocalBean
public class QuoteResource {
	UrlInfo u;
	String adapterUrl = "", jsonResponse = "";

    public QuoteResource() {
    	u = new UrlInfo();
		adapterUrl = u.getAdapterURL();}
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getQuote() throws Exception{
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(adapterUrl+"motivation-quote");
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
			jsonResponse += "{\"status\": \"OK\",";
			
			jsonResponse += "\"result\": {";
			
			String quoteText = o.getJSONObject("result").getString("quote");
			String quoteAuthor = o.getJSONObject("result").getString("author");
			
			jsonResponse += "\"quote\": \""+quoteText+"\",";
			
			if(quoteAuthor != null && !quoteAuthor.isEmpty()){
				jsonResponse += "\"author\": \""+quoteAuthor+"\"";
			} else {
				jsonResponse += "\"author\": \"Anonymous\"";
			}
			
			jsonResponse += "}}";
			
			return Response.ok(jsonResponse).build();
		} else {
			jsonResponse += "{\"status\": \"ERROR\","
					+ "\"error\": \""+response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase()+"\"}";
			return Response.status(404).entity(jsonResponse).build();
		}
    }
}
    
    
    
    
    
    
    
