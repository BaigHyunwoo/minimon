package com.minimon.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	  return new BCryptPasswordEncoder();
	}
		

	/* 인증 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	  auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}
	
	/* 제외 패턴 */
	@Override
	public void configure(WebSecurity web) throws Exception	{
		web.ignoring()
			.antMatchers("/js/**")
			.antMatchers("/css/**");
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
				.authorizeRequests()
				.antMatchers("/").permitAll()
	        .and()
	        	.csrf().disable()
				.formLogin()
				.loginPage("/")
				.loginProcessingUrl("/login")
				.successHandler(successHandler("/"))
		    	.failureUrl("/loginFail");
	}

	@Bean
	public AuthenticationSuccessHandler successHandler(String url) {
	  return new CustomLoginSuccessHandler(url);
	}

}
