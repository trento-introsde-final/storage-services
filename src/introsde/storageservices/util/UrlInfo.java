package introsde.storageservices.util;

public class UrlInfo {
	
	public UrlInfo() {}
	
	static final String localDBurl = "http://local-database-services.herokuapp.com/";
	static final String adapterUrl = "https://radiant-dawn-54444.herokuapp.com/";
	
	public String getLocalDBURL() {
		return localDBurl;
	}
	
	public String getAdapterURL() {
		return adapterUrl;
	}
}
