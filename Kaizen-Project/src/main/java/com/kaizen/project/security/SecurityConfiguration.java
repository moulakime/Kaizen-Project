package com.kaizen.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.kaizen.project.services.MyUserDetails;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    
	@Autowired
    private MyUserDetails userService;
    
	@Autowired
	private CustomLoginSuccessHandler successHandler;
	
	@Override	
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
		.antMatchers("/register").permitAll()
		.antMatchers("/exist/**").permitAll()
		.antMatchers("**/email_not_allowed/**").permitAll()
		.antMatchers("**/get_admins_email/**").permitAll()
		.antMatchers("/reporting/**").permitAll()
		.antMatchers("/kaizen/portal/suggestionslist/**").hasAnyAuthority("ROLE_USER")
		.antMatchers("/kaizen/portal/suggestionssent/**").hasAnyAuthority("ROLE_USER")
		.antMatchers("/kaizen/portal/suggestionsreceived/**").hasAnyAuthority("ROLE_USER")
		.antMatchers("/kaizen/admin/suggestionslist/**").hasAnyAuthority("ROLE_ADMIN")
		.antMatchers("/kaizen/admin/suggestionssent/**").hasAnyAuthority("ROLE_ADMIN")
		.antMatchers("/kaizen/admin/suggestionsreceived/**").hasAnyAuthority("ROLE_ADMIN")
		.antMatchers("/kaizen/admin/dashboard/**").hasAnyAuthority("ROLE_ADMIN")
		.antMatchers("/kaizen/admin/report/**").hasAnyAuthority("ROLE_ADMIN")
		.antMatchers("/kaizen/admin/users/**").hasAnyAuthority("ROLE_ADMIN")
		.antMatchers("/users/**").hasAnyAuthority("ROLE_ADMIN")
		.and().formLogin()
		.loginPage("/login")
		.failureUrl("/login?error=true")
		.successHandler(successHandler)
		.and().logout()
		.logoutUrl("/logout")
		.logoutSuccessUrl("/login")
		.and().exceptionHandling()
		.accessDeniedPage("/403");
	}
	
	 @Bean
	    public BCryptPasswordEncoder passwordEncoder(){
	        return new BCryptPasswordEncoder();
	    }
	 
	@Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
	
}
