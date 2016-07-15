package com.javapapers.webservices.rest.jersey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/*
 * from here:
 * http://javapapers.com/web-service/intercept-jax-rs-request-with-jersey-containerrequestfilter/
 * 
 * run with:
 * 
 * http://localhost:8080/RESTfulJerseyFilter/rest/helloworld
 */

@Path("/helloworld")
public class HelloWorld {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
//	public String sayPlainTextHello() {
	public String sayPlainTextHello(@Context HttpServletRequest req) {
    	HttpSession session= req.getSession(true);
    	Object foo = session.getAttribute("foo");
    	if (foo!=null) {
    		System.out.println("session_of_foo:" + foo.toString());
    	} else {
    		foo = "bar";
    		session.setAttribute("foo", "bar");
    	}
		return "Hello World RESTful Jersey!";
	}

	@GET
	@Produces(MediaType.TEXT_XML)
//	public String sayXMLHello() {
	public String sayXMLHello(@Context HttpServletRequest req) {
    	HttpSession session= req.getSession(true);
    	Object foo = session.getAttribute("foo");
    	if (foo!=null) {
    		System.out.println(foo.toString());
    	} else {
    		foo = "bar";
    		session.setAttribute("foo", "bar");
    	}
		return "<?xml version=\"1.0\"?>" + "<hello> Hello World RESTful Jersey -" // + foo.toString()
				+ "</hello>";
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html> " + "<title>" + "Hello World RESTful Jersey"
				+ "</title>" + "<body><h1>" + "Hello World RESTful Jersey"
				+ "</body></h1>" + "</html> ";
	}

}