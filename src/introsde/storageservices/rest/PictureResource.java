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
import org.json.JSONArray;
import org.json.JSONObject;

import introsde.storageservices.util.UrlInfo;

@Stateless
@LocalBean
public class PictureResource {
	UrlInfo u;
	String adapterUrl = "", jsonResponse = "";

    public PictureResource() {
    	u = new UrlInfo();
		adapterUrl = u.getAdapterURL();
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPicture() throws Exception{
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(adapterUrl+"instagram-pics");
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
			
			JSONArray arr = o.getJSONArray("results");
			
			int random_hashtag = 0 + (int)(Math.random()*(arr.length()-1));
			
			jsonResponse += "\"picture\": {";
			
			String url = arr.getJSONObject(random_hashtag).getString("url");
			String thumbUrl = arr.getJSONObject(random_hashtag).getString("thumbUrl");
			jsonResponse += "\"url\": \""+url+"\",";
			jsonResponse += "\"thumbUrl\": \""+thumbUrl+"\"";
			
			jsonResponse += "}}";
			
			return Response.ok(jsonResponse).build();
		} else {
			jsonResponse += "{\"status\": \"ERROR\","
					+ "\"error\": \""+response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase()+"\"}";
			return Response.status(404).entity(jsonResponse).build();
		}
    }
}




