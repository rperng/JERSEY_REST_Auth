package com.javapapers.webservices.rest.jersey;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class JaxRsFilterAuthentication implements ContainerRequestFilter {
	
    // Exception thrown if user is unauthorized.
    private final static WebApplicationException unauthorized =  new WebApplicationException(
    		Response.status(Status.UNAUTHORIZED)
                    .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"realm\"")
                    .entity("Page requires login.").build() );	
	
	public static final String AUTHENTICATION_HEADER = "Authorization";

	@Override
	public void filter(ContainerRequestContext containerRequest) throws WebApplicationException {

		String authCredentials = containerRequest.getHeaderString(AUTHENTICATION_HEADER);

		// better injected
		AuthenticationService authenticationService = new AuthenticationService();

		boolean authenticationStatus = authenticationService.authenticate(authCredentials);

		if (!authenticationStatus) {
			//throw new WebApplicationException(Status.UNAUTHORIZED);
			throw unauthorized;
		}

	}
}