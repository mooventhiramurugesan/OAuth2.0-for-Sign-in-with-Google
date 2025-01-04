package com.example.OAuth2._CustomLoginAndRegistration.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.OAuth2._CustomLoginAndRegistration.Service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	  private final CustomUserDetailsService customUserDetailsService;

	    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
	        this.customUserDetailsService = customUserDetailsService;
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests(authorize -> authorize
	                .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
	                .anyRequest().authenticated()
	            )
	            .formLogin(form -> form
	                .loginPage("/login")  // Custom login page for email/password
	                .loginProcessingUrl("/login")  // Handles POST login requests
	                .defaultSuccessUrl("/dashboard", true)  // Redirect after successful login
	                .failureUrl("/login?error=true")  // Redirect on failure
	                .permitAll()
	            )
	            .oauth2Login(oauth2 -> oauth2
	                .loginPage("/login")  // Custom login page for OAuth 2.1 (same as regular login page)
	                .defaultSuccessUrl("/dashboard",true)  // Redirect after successful OAuth login
	                .failureUrl("/login?error=true")  // Redirect on OAuth failure
	            )
	            .logout(logout -> logout
	                .logoutUrl("/logout")
	                .logoutSuccessUrl("/login?logout=true")
	                .permitAll()
	            )
	            .userDetailsService(customUserDetailsService);  // Custom user service for local login

	        return http.build();
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();  // This creates the BCryptPasswordEncoder bean
	    }

}
