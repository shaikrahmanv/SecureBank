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

	private UserDAO userDAO = new UserDAO();

	@GetMapping("/transfer")
	public String showTransferPage(HttpSession session, Model model) {

		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/login";
		}

		model.addAttribute("userId", userId);

		return "transfer";
	}

	@PostMapping("/transfer")
	public String transferMoney(@RequestParam String receiverAccount, @RequestParam double amount,
			HttpSession session) {

		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/login";
		}

		boolean success = userDAO.transferMoney(userId, receiverAccount, amount);

		if (success) {
			return "transfer-success";
		}

		return "transfer-failed";
	}
}