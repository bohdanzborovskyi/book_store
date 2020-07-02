package com.zbodya;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.zbodya.Service.UserService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter 
{
	
	@Autowired
	UserService userService;
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() 
	{
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.csrf()
		.disable()
		.authorizeRequests()
			.antMatchers("/images/**").permitAll()
			.antMatchers("/registration").not().fullyAuthenticated()
			.anyRequest().authenticated()
		.and()
			.formLogin().loginPage("/login")
			.defaultSuccessUrl("/book/allBooks?findReq=false",true)
			.permitAll()
		.and()
			.logout()
			.permitAll()
			.logoutSuccessUrl("/login");
	}
	
}
