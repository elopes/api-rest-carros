package org.com.enlopes.infra.security;

import org.com.enlopes.infra.security.jwt.JwtAuthenticationFilter;
import org.com.enlopes.infra.security.jwt.JwtAuthorizationFilter;
import org.com.enlopes.infra.security.jwt.handler.AccessDeniedHandler;
import org.com.enlopes.infra.security.jwt.handler.UnauthorizedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDetailsService;
	  
	@Autowired
	 private UnauthorizedHandler unauthorizedHandler;

	 @Autowired
	 private AccessDeniedHandler accessDeniedHandler;
	
	    @Override
	protected void configure(HttpSecurity http) throws Exception {
	    	 
		// Removi segurança para subir o servidor.
		// http
		// .authorizeRequests()
		// .anyRequest().permitAll()
		// .and().csrf().disable();

		// http
		// .authorizeRequests()
		// .anyRequest().authenticated()
		// .and().httpBasic()
		// .and().csrf().disable();
	    	
	    	AuthenticationManager authManager = authenticationManager();
		
	    	http
         .authorizeRequests()
         .antMatchers(HttpMethod.GET, "/api/v1/login").permitAll()
         .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
         .permitAll()
         .anyRequest().authenticated()
         .and().csrf().disable()
         .addFilter(new JwtAuthenticationFilter(authManager))
         .addFilter(new JwtAuthorizationFilter(authManager, userDetailsService))
         .exceptionHandling()
         .accessDeniedHandler(accessDeniedHandler)
         .authenticationEntryPoint(unauthorizedHandler)
         .and()
         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		auth.userDetailsService(userDetailsService).passwordEncoder(encoder);

//        auth
//            .inMemoryAuthentication().passwordEncoder(encoder)
//                .withUser("user").password(encoder.encode("user")).roles("USER")
//                .and()
//                .withUser("admin").password(encoder.encode("admin")).roles("USER", "ADMIN");
	}

}
