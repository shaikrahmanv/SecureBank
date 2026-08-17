package Jar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banking.LoginDAO;
import com.banking.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	private final LoginDAO loginDAO = new LoginDAO();

	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

	@PostMapping("/login")
	public String loginUser(@RequestParam String email, @RequestParam String password, Model model,
			HttpSession session) {

		User user = loginDAO.loginUser(email, password);

		if (user != null) {
			session.setAttribute("userId", user.getId());
			return "redirect:/dashboard";
		}

		model.addAttribute("error", "Invalid email or password.");
		return "login";
	}
}