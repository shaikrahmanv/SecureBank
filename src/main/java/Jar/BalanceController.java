package Jar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.banking.User;
import com.banking.UserDAO;

import jakarta.servlet.http.HttpSession;

@Controller
public class BalanceController {

	private UserDAO userDAO = new UserDAO();

	@GetMapping("/balance")
	public String showBalance(HttpSession session, Model model) {

		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/login";
		}

		User user = userDAO.getUserById(userId);

		model.addAttribute("user", user);

		return "balance";
	}

}