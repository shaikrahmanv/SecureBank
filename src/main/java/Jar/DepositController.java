package Jar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banking.UserDAO;

import jakarta.servlet.http.HttpSession;

@Controller
public class DepositController {

	private final UserDAO userDAO = new UserDAO();

	@GetMapping("/deposit")
	public String showDepositPage(HttpSession session, Model model) {

		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/login";
		}

		model.addAttribute("userId", userId);

		return "deposit";
	}

	@PostMapping("/deposit")
	public String depositMoney(@RequestParam double amount, @RequestParam String method, HttpSession session,
			Model model) {

		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/login";
		}

		if (amount <= 0) {
			model.addAttribute("error", "Please enter a valid deposit amount.");
			return "deposit";
		}

		boolean success = userDAO.depositMoney(userId, amount, method);

		if (success) {
			model.addAttribute("amount", amount);
			model.addAttribute("method", method);
			return "deposit-success";
		}

		model.addAttribute("error", "Deposit failed. Please try again.");
		return "deposit";
	}
}