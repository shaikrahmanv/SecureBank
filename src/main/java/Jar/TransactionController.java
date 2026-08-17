package Jar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.banking.UserDAO;

import jakarta.servlet.http.HttpSession;

@Controller
public class TransactionController {

	private UserDAO userDAO = new UserDAO();

	@GetMapping("/transactions")
	public String showTransactions(HttpSession session, Model model) {

		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/login";
		}

		model.addAttribute("transactions", userDAO.getTransactions(userId));

		return "transactions";
	}
}
