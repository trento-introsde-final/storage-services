package introsde.storageservices.rest;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Stateless
@LocalBean
@Path("/")
public class Dispatcher {
	 	@Context
	    UriInfo uriInfo;
	    @Context
	    Request request;

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
	        return new PictureResource(uriInfo, request);
	    }
	    
	    @Path("motivation-quote")
	    public QuoteResource routeUserCollection() {
	        return new QuoteResource(uriInfo, request);
	    }
}




