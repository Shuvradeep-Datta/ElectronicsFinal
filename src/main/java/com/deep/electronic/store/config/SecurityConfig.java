package com.deep.electronic.store.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.deep.electronic.store.constants.AppConstants;
import com.deep.electronic.store.security.JwtAuthenticationEntryPoint;
import com.deep.electronic.store.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity(debug = true)

//@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	
	

	@Autowired
	private JwtAuthenticationFilter filter;
	
	@Autowired
	private JwtAuthenticationEntryPoint entryPoint;
	
	private final String[] PUBLIC_URLS = {
		"/swagger-ui/**",
		"/webjars/**",
		"swagger-resources/**"
	};
	
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		//Cors Origin
		
		
		httpSecurity.cors(t ->t.configurationSource(new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				
				CorsConfiguration corsConfiguration = new CorsConfiguration();
				
				corsConfiguration.setAllowedOriginPatterns(List.of("*"));
				corsConfiguration.setAllowedMethods(List.of("*"));
				corsConfiguration.setAllowCredentials(true);
				corsConfiguration.setAllowedHeaders(List.of("Authorization"));
				corsConfiguration.setMaxAge(3000L);
				
				
				
				return corsConfiguration;
			}
		}) );
		
		httpSecurity.csrf()
        .disable()
        .authorizeRequests()
        .requestMatchers("/auth/login")
        .permitAll()
        .requestMatchers(HttpMethod.POST, "/users")
        .permitAll()
        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
        .requestMatchers(PUBLIC_URLS)
        .permitAll()
        .requestMatchers(HttpMethod.GET)
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(entryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	 @Bean
	 AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
	        return builder.getAuthenticationManager();
	    }
	
	

}
