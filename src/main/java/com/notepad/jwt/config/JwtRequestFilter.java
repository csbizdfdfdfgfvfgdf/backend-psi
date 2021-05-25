package com.notepad.jwt.config;

import com.notepad.config.UserPrincipal;
import com.notepad.serviceImpl.UserServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	
	private final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);
	
	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	/**
     * Filter that runs before requests and a
     * authenticate the user using uuid and token
     *
     * @param HttpServletRequest, HttpServletResponse, FilterChain
     */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// Gets token from header
		final String requestTokenHeader = request.getHeader("Authorization");

		// Gets unique user id
		//final String uuidHeader = request.getHeader("uuid");

		String username = null;
		String uuid = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
				return;
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
				return;
			}
		}

//		else {
//			// for UUID flow
//			uuid = uuidHeader;
//            if(uuid != null && uuid.trim().length() > 0){
//				UserDetails userDetails = this.userServiceImpl.loadUserByDbUsername(uuid);
//				UserPrincipal userPrincipal = (UserPrincipal) userDetails;
//
//				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//						userPrincipal, null, userPrincipal.getAuthorities());
//				usernamePasswordAuthenticationToken
//						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//
//
//				// After setting the Authentication in the context, we specify
//				// that the current user is authenticated. So it passes the Spring Security Configurations successfully.
//				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//			}
//		}

		//Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.userServiceImpl.loadUserByUsername(username);
			UserPrincipal userPrincipal = (UserPrincipal) userDetails;

			// if token is valid configure Spring Security to manually set authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

//				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//						userDetails, null, userDetails.getAuthorities());
//				usernamePasswordAuthenticationToken
//						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userPrincipal, null, userPrincipal.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

}
