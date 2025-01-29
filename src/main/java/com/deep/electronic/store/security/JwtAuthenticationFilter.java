package com.deep.electronic.store.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.deep.electronic.store.exception.AuthenticationFailureException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
		
	@Autowired
	JwtHelper jwtHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		
		String requestHeader = request.getHeader("Authorization");
		log.info("Header {}",requestHeader);
		
		
		String username =null;
		String token = null;
		
		if(requestHeader != null && requestHeader.startsWith("Bearer")) {
			//sob kuch thik hai
			
			
			token = requestHeader.substring(7);
			try {
				username = jwtHelper.getUsernameFromToken(token);
				log.info("Token Username : {}",username);
			} catch (IllegalArgumentException e) {
				log.info("Illegal Argument while fetching the username !!"+e.getMessage());
			} catch (ExpiredJwtException e) {
				log.info("Expired Jwt while fetching the username !!"+e.getMessage());
			} catch (MalformedJwtException e) {
				log.info("Some change has done in the token !! Invalid Token !!"+e.getMessage());
			} catch (Exception e) {
				log.info("Exception Occuered while fetching the username !!",e.getMessage());
			}
			
		}else {
			log.error("Invalid Header !! Header is not starting with Bearer");
			
		}
		
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			
			
			
			//Validation token
			if(username.equals(userDetails.getUsername()) && !jwtHelper.isTokenExpired(token)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
		
	}
	
	

}
