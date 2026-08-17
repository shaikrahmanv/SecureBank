package Jar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banking.UserDAO;

import jakarta.servlet.http.HttpSession;

@Controller
public class WithdrawController {

	private UserDAO userDAO = new UserDAO();

	@GetMapping("/withdraw")
	public String showWithdrawPage(HttpSession session, Model model) {

		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/login";
		}

		model.addAttribute("userId", userId);

		return "withdraw";
	}

	@PostMapping("/withdraw")
	public String withdrawMoney(@RequestParam double amount, @RequestParam String method, HttpSession session) {

		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/login";
		}

		boolean success = userDAO.withdrawMoney(userId, amount, method);

		if (success) {
			return "withdraw-success";
		}

		return "withdraw-failed";
	}
}