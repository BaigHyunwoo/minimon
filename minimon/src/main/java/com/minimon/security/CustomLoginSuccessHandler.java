package com.minimon.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomLoginSuccessHandler.class.getName());
	
	public CustomLoginSuccessHandler(String url) {
        setDefaultTargetUrl(url);
    }
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
    	Authentication authentication) throws ServletException, IOException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		
        HttpSession session = request.getSession();
        
        session.setAttribute("id", username);
        
        logger.info("LOGIN : "+ username);
        
        if (session != null) {
        	
            String redirectUrl = "/";
            
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                

        }
    }
}
