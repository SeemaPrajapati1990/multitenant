package com.sapper.spring.keycloak.multitenant.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;

public class TestFilter implements Filter {
	 
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
 
		HttpServletRequest request = (HttpServletRequest) req;
		RefreshableKeycloakSecurityContext context 
		=  (RefreshableKeycloakSecurityContext)request.getAttribute("org.keycloak.KeycloakSecurityContext");
               //or
	       //=  (RefreshableKeycloakSecurityContext)request.getSession().getAttribute("org.keycloak.KeycloakSecurityContext");
		AccessToken token = context.getToken();
		 String sub = token.getSubject();//user internal code
		System.out.println(sub);
		 String loginName = token.getPreferredUsername();//Login account
		System.out.println(loginName);
		
		 //Realm role list
		Access access = token.getRealmAccess();
		Set<String> roles =  access.getRoles();
		System.out.println(roles);
		
		 //client role list
		//Map<clientId,roleList>
		Map<String, Access>  ma = token.getResourceAccess();
		for (String key : ma.keySet()) {
			System.out.println(key);//clientID
			 // The current user has a list of roles in the key client
			System.out.println(ma.get(key).getRoles());
			
		}
		 // Build the business application's own security context
		request.getSession().setAttribute("loginName", loginName);
		
		 // Request to continue to pass
		chain.doFilter(req, res);
	}
	
	@Override
	public void destroy() {
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}