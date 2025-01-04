package com.example.OAuth2._CustomLoginAndRegistration.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.OAuth2._CustomLoginAndRegistration.Model.User;
import com.example.OAuth2._CustomLoginAndRegistration.Repository.UserRepository;
import com.example.OAuth2._CustomLoginAndRegistration.Service.UserService;

@Controller
public class AuthController {

	   private final UserService userService;
	   
	   @Autowired
	   private UserRepository userRepository;

		public AuthController(UserService userService) {
			this.userService = userService;
		}
	   
		@GetMapping("/login")
		public String showLoginPage() {
			return "login";
		}
		
		@GetMapping("/register")
		public String showRegistrationPage(Model model) {
			model.addAttribute("user", new User());
			return "register";
		}
		
		
		@PostMapping("/register")
		public String registerUser(@ModelAttribute User user) {
			userService.registerUser(user);
			return "redirect:/login?registerSuccess";
		}

		@GetMapping("/dashboard")
		public String dashboard(Authentication authentication, Model model) {
			if (authentication.getPrincipal() instanceof OAuth2User) {
				OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
				userService.processOAuthPostLogin(oauthUser);
				String name = oauthUser.getAttribute("name");
				String email = oauthUser.getAttribute("email");

				model.addAttribute("name", name);
				model.addAttribute("email", email);
			} else if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				String email = userDetails.getUsername();
				// Email as the username for custom login
				Optional<User> user = userRepository.findByEmail(email);
				// Since we don't save the name in custom login, you can handle it based on your
				// logic
				// e.g., retrieving it from the database based on the email
				model.addAttribute("name", user.get().getName()); // Placeholder name for custom login
				model.addAttribute("email", email);
			}
			return "dashboard"; // Thymeleaf template "dashboard.html"
		}

}
