package Jar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banking.User;
import com.banking.UserDAO;

@Controller
public class RegisterController {

	private final UserDAO userDAO = new UserDAO();

	@GetMapping("/register")
	public String showRegisterPage() {
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@RequestParam String name, @RequestParam String contactNumber,
			@RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword,
			@RequestParam String dateOfBirth, @RequestParam String address, @RequestParam String accountType,
			@RequestParam(defaultValue = "INR") String currency, Model model) {

		if (!password.equals(confirmPassword)) {
			model.addAttribute("error", "Passwords do not match.");
			return "register";
		}

		User user = new User(name, contactNumber, email, password, dateOfBirth, address, accountType);

		user.setCurrency(currency);

		boolean success = userDAO.registerUser(user);

		if (success) {

			model.addAttribute("userId", user.getId());

			model.addAttribute("accountNumber", user.getAccountNumber());

			model.addAttribute("name", user.getName());

			model.addAttribute("currency", user.getCurrency());

			return "register-success";

		} else {

			model.addAttribute("error", "Email or contact number is already registered.");

			return "register";
		}
	}
}