package Jar;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

	@GetMapping("/logout")
	public String logout(HttpSession session) {

		// Destroy the current user's session
		session.invalidate();

		// Send the user back to login page
		return "redirect:/login";
	}
}