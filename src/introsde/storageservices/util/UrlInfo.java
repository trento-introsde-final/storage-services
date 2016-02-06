package introsde.storageservices.util;

public class UrlInfo {
	
	public UrlInfo() {}
	
	static final String localDBurl = "http://local-database-services.herokuapp.com/";
	static final String adapterUrl = "https://adapter-services.herokuapp.com/";
	static final String storageUrl = "https://storage-services.herokuapp.com/";
	
	public String getLocalDBURL() {
		return localDBurl;
	}
	
	public String getAdapterURL() {
		return adapterUrl;
	}
	
	public String getStorageURL() {
		return storageUrl;
	}
}
