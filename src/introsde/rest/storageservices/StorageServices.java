package introsde.rest.storageservices;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
@Path("/storageservices")
public class StorageServices {
	
	@GET
    @Path("/pretty-pic")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPrettyPic() throws Exception {
		
		String adapter_pic = "https://radiant-dawn-54444.herokuapp.com/adapterservices/instagram-pics";
		
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
		
		if(response.getStatusLine().getStatusCode() == 200){
			
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
					+ "\"error\": \"We have encoutered some errors!!\"}";
			//response.getStatusLine().getReasonPhrase();
			return Response.ok(jsonResponse).build();
		}
		
		//return Response.ok().build();
	}
	
	@GET
    @Path("/motivation-quote")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMotivationQuote() throws Exception {
		
		String adapter_quote = "https://radiant-dawn-54444.herokuapp.com/adapterservices/motivation-quote";
		
		String jsonResponse = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(adapter_quote);
		HttpResponse response = client.execute(request);
		
		BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		JSONObject o = new JSONObject(result.toString());
		
		if(response.getStatusLine().getStatusCode() == 200){
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
					+ "\"error\": \"We have encoutered some errors!!\"}";
			return Response.ok(jsonResponse).build();
		}
	}

}
