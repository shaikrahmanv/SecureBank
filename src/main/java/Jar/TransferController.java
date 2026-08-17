package Jar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banking.UserDAO;

import jakarta.servlet.http.HttpSession;

@Controller
public class TransferController {

	private final UserDAO userDAO = new UserDAO();

	@GetMapping("/transfer")
	public String showTransferPage(HttpSession session) {

		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/login";
		}

		return "transfer";
	}

	@PostMapping("/transfer")
	public String transferMoney(@RequestParam String receiverAccount, @RequestParam double amount, HttpSession session,
			Model model) {

		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/login";
		}

		if (receiverAccount == null || receiverAccount.trim().isEmpty()) {
			model.addAttribute("error", "Please enter the receiver account number.");
			return "transfer";
		}

		if (amount <= 0) {
			model.addAttribute("error", "Please enter a valid transfer amount.");
			return "transfer";
		}

		boolean success = userDAO.transferMoney(userId, receiverAccount.trim(), amount);

		if (success) {
			model.addAttribute("amount", amount);
			model.addAttribute("receiverAccount", receiverAccount);
			return "transfer-success";
		}

		model.addAttribute("error", "Transfer failed. Check the receiver account and your balance.");

		return "transfer";
	}
}