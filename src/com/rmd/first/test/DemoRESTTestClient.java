package com.rmd.first.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DemoRESTTestClient {

	/*
	*	demo-business-resource / 
	*		/login
	*		/demo-get-method
	*		/demo-post-method
	*		/logout
	* 
	* "service_key": "3b91cab8-926f-49b6-ba00-920bcf934c2a"
	* "username": "username2",
    * "password": "passwordForUser2"
    * 
	*	POST //localhost:8080/jax-auth-poc/rest/demo-business-resource/login/ 
	*	POST //localhost:8080/jax-auth-poc/rest/demo-business-resource/demo-post-method/
	*   GET  //localhost:8080/jax-auth-poc/rest/demo-business-resource/demo-get-method/
	*   POST //localhost:8080/jax-auth-poc/rest/demo-business-resource/logout/
	*/
	private static String BASE_URL = "http://localhost:8080/jax-auth-poc/rest/demo-business-resource";
	private static String LOGIN_PATH = "login";
	private static String LOGOUT_PATH = "logout";
	private static String DEMOGET_PATH = "demo-get-method";
	private static String DEMOPOST_PATH = "demo-post-method";
	private static String AuthToken = "";
	
	public static void main(String[] args) throws IOException 
	{
		httpDemoPost();
		httpLogin();
		httpDemoPost();
		httpDemoGet();
		httpLogout();
		httpDemoGet();
	}
	
	private static void httpLogin() 
	{
//		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
//																    .nonPreemptive()
//																    .credentials("username2", "passwordForUser2")
//																    .build();

		ClientConfig clientConfig = new ClientConfig();
//		clientConfig.register(feature) ;
		clientConfig.register(LoggingFilter.class);

		Client client = ClientBuilder.newClient( clientConfig );
		WebTarget webTarget = client.target(BASE_URL).path(LOGIN_PATH);
		
		Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_JSON);
		
		Form form = new Form();
		form.param("username", "username2");
		form.param("password", "passwordForUser2");
		
		invocationBuilder.header("service_key", "3b91cab8-926f-49b6-ba00-920bcf934c2a");
	    Invocation invocation = invocationBuilder.buildPost(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

	    //String responseData = invocation.invoke(String.class);
	    Response response = invocation.invoke();
	    String responseData = "";
	    if (response.hasEntity()) {
	    	responseData = response.readEntity(String.class);
	    }
	    // need to save the auth_token ....
	    ObjectMapper mapper = new ObjectMapper();
	    AuthTokenClass token;
	    //JSON from String to Object
	    try {
			token = mapper.readValue(responseData, AuthTokenClass.class);
			AuthToken = token.authToken;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    System.out.println(response.getStatus());
	    System.out.println(responseData);

	}
	
	private static void httpLogout() 
	{
		Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
		WebTarget webTarget = client.target(BASE_URL).path(LOGOUT_PATH);
		
		Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_JSON);
		invocationBuilder.header("service_key", "3b91cab8-926f-49b6-ba00-920bcf934c2a");
		invocationBuilder.header("auth_token", AuthToken);
		Response response = invocationBuilder.post(Entity.entity(null, MediaType.APPLICATION_JSON));
			
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));
	}

	private static void httpDemoPost() 
	{
		Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
		WebTarget webTarget = client.target(BASE_URL).path(DEMOPOST_PATH);

		Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_JSON);
		invocationBuilder.header("service_key", "3b91cab8-926f-49b6-ba00-920bcf934c2a");
		invocationBuilder.header("auth_token", AuthToken);
		Response response = invocationBuilder.post(Entity.entity(null, MediaType.APPLICATION_JSON));
		
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));
		
	}
	
	private static void httpDemoGet() 
	{
		Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
		WebTarget webTarget = client.target(BASE_URL).path(DEMOGET_PATH);
		
		Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_JSON);
		invocationBuilder.header("service_key", "3b91cab8-926f-49b6-ba00-920bcf934c2a");
		invocationBuilder.header("auth_token", AuthToken);
		Response response = invocationBuilder.get();
			
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));
	}

}

