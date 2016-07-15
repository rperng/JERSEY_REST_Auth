package com.javapapers.webservices.rest.jersey.test;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.Test;

public class TestRESTHelloWorld {

    @Test
    public void testBasicAuthentication() {
        //Define basic authentication credential values
        String username = "admin";
        String password = "admin";
 
        String usernameAndPassword = username + ":" + password;
        String authorizationHeaderName = "Authorization";
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );
 
//        // Build the form for a post request
//        MultivaluedMap<String, String> formParameters = new MultivaluedHashMap();
//        formParameters.add( "field1", "fieldValue1" );
//        formParameters.add( "field2", "fieldValue2" );
 
        // localhost:8080/RESTfulJerseyFilter/rest/helloworld
        // Perform a post request
        String restResource = "http://localhost:8080/RESTfulJerseyFilter/rest";
        Client client = ClientBuilder.newClient();
        Response res = client.target( restResource )
            .path( "helloworld" ) // API Module Path
            .request( "text/xml" ) // Expected response mime type
            .header( authorizationHeaderName, authorizationHeaderValue ) // The basic authentication header goes here
            .get();
            //.post( Entity.form( formParameters ) );     // Perform a post with the form values
 
        //assertTrue( res.getStatus() == 200 );
        String returnData = res.readEntity(String.class);
        System.out.println(res.getStatus());
        System.out.println(returnData);
    }

}
