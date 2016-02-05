package introsde.storageservices.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

@Stateless
@LocalBean
public class PictureResource {

	@Context
    UriInfo uriInfo;
    @Context
    Request request;

    public PictureResource(UriInfo uriInfo, Request request) {
        this.uriInfo = uriInfo;
        this.request = request;
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPicture() throws Exception{
    	
    	String adapter_pic = "https://radiant-dawn-54444.herokuapp.com/instagram-pics";
		
		String jsonResponse = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(adapter_pic);
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
			
			JSONArray arr = o.getJSONArray("result");
			
			jsonResponse += "\"picture\": {";
			
			String url = arr.getJSONObject(0).getString("url");
			String thumbUrl = arr.getJSONObject(0).getString("thumbUrl");
			jsonResponse += "\"url\": \""+url+"\",";
			jsonResponse += "\"thumbUrl\": \""+thumbUrl+"\"";
			
			jsonResponse += "}}";
			
			return Response.ok(jsonResponse).build();
		} else {
			jsonResponse += "{\"status\": \"ERROR\","
					+ "\"error\": \""+response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase()+"\"}";
			return Response.ok(jsonResponse).build();
		}
    }
}



