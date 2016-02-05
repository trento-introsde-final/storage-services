package introsde.storageservices.rest;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@LocalBean
@Path("/")
public class Dispatcher {

	    /**
	     * Very important to say hello before requesting anything. Also to wake heroku up
	     * @return 'Hola'
	     */
	    @GET
	    @Path("hola")
	    @Produces(MediaType.TEXT_PLAIN)
	    public String getHola() {
	        return "Hola";
	    }

	    @Path("pretty-pic")
	    public PictureResource routeGoal() {
	        return new PictureResource();
	    }
	    
	    @Path("motivation-quote")
	    public QuoteResource routeUserCollection() {
	        return new QuoteResource();
	    }
}




