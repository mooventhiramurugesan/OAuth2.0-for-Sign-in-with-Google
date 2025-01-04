package com.example.OAuth2._CustomLoginAndRegistration.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.OAuth2._CustomLoginAndRegistration.Model.User;
import com.example.OAuth2._CustomLoginAndRegistration.Repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

		return org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
				.password(user.getPassword()) // Password stored in the database
				.authorities("USER") // Use a default role or authority if necessary
				.build();
	}

}
