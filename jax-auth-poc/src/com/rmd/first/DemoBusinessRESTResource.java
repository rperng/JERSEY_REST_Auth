package com.rmd.first;


import com.rmd.first.DemoBusinessRESTResourceProxy;
import com.rmd.first.DemoHTTPHeaderNames;

import java.security.GeneralSecurityException;
import java.security.Principal;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.security.auth.login.LoginException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.ext.*;
import javax.ws.rs.core.SecurityContext;



@Stateless( name = "DemoBusinessRESTResource", mappedName = "ejb/DemoBusinessRESTResource" )
@Path( "demo-business-resource" )
public class DemoBusinessRESTResource implements DemoBusinessRESTResourceProxy {

    private static final long serialVersionUID = -6663599014192066936L;

    @Context private SecurityContext securityContext;
    
    @Override
    @POST
    @Path( "login" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response login(
        @Context HttpHeaders httpHeaders,
        @FormParam( "username" ) String username,
        @FormParam( "password" ) String password ) {
    	
        DemoAuthenticator demoAuthenticator = DemoAuthenticator.getInstance();
        String serviceKey = httpHeaders.getHeaderString( DemoHTTPHeaderNames.SERVICE_KEY );

        try {
        	
            // retrieve the authentication scheme that was used(e.g. BASIC)
            String authnScheme = securityContext.getAuthenticationScheme();
            System.out.println("authnScheme: " + authnScheme);
            // retrieve the name of the Principal that invoked the resource
            Principal principal = securityContext.getUserPrincipal();
            //String un = securityContext.getUserPrincipal().getName();
            String un = (principal==null ? null : principal.getName() );
            System.out.println("un: " + un);
            // check if the current user is in Role1 
            Boolean isUserInRole = securityContext.isUserInRole("Role1");
            
             
            String authToken = demoAuthenticator.login( serviceKey, username, password );

            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "auth_token", authToken );
            JsonObject jsonObj = jsonObjBuilder.build();

            return getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();

        } catch ( final LoginException ex ) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "message", "Problem matching service key, username and password" );
            JsonObject jsonObj = jsonObjBuilder.build();

            return getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        }
    }

    @Override
    @GET
    @Path( "demo-get-method" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response demoGetMethod() {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add( "message", "Executed demoGetMethod" );
        JsonObject jsonObj = jsonObjBuilder.build();

        return getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
    }

    @Override
    @POST
    @Path( "demo-post-method" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response demoPostMethod() {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add( "message", "Executed demoPostMethod" );
        JsonObject jsonObj = jsonObjBuilder.build();

        return getNoCacheResponseBuilder( Response.Status.ACCEPTED ).entity( jsonObj.toString() ).build();
    }

    @Override
    @POST
    @Path( "logout" )
    public Response logout(
        @Context HttpHeaders httpHeaders ) {
        try {
            DemoAuthenticator demoAuthenticator = DemoAuthenticator.getInstance();
            String serviceKey = httpHeaders.getHeaderString( DemoHTTPHeaderNames.SERVICE_KEY );
            String authToken = httpHeaders.getHeaderString( DemoHTTPHeaderNames.AUTH_TOKEN );

            demoAuthenticator.logout( serviceKey, authToken );

            return getNoCacheResponseBuilder( Response.Status.NO_CONTENT ).build();
        } catch ( final GeneralSecurityException ex ) {
            return getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).build();
        }
    }

    private Response.ResponseBuilder getNoCacheResponseBuilder( Response.Status status ) {
        CacheControl cc = new CacheControl();
        cc.setNoCache( true );
        cc.setMaxAge( -1 );
        cc.setMustRevalidate( true );

        return Response.status( status ).cacheControl( cc );
    }
}