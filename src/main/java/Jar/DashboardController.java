package Jar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.banking.User;
import com.banking.UserDAO;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

	private UserDAO userDAO = new UserDAO();

	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session, Model model) {

		System.out.println("========== DASHBOARD CONTROLLER CALLED ==========");

		Integer userId = (Integer) session.getAttribute("userId");

		System.out.println("Session User ID = " + userId);

		if (userId == null) {
			return "redirect:/login";
		}

		User user = userDAO.getUserById(userId);

		if (user == null) {
			session.invalidate();
			return "redirect:/login";
		}

		System.out.println("User Account Number = " + user.getAccountNumber());

		model.addAttribute("user", user);

		return "dashboard";
	}
}