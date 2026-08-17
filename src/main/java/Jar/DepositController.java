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

	private UserDAO userDAO = new UserDAO();

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
	public String depositMoney(
	        @RequestParam int userId,
	        @RequestParam double amount,
	        @RequestParam String method) {

	    System.out.println("DEPOSIT REQUEST RECEIVED");
	    System.out.println("User ID: " + userId);
	    System.out.println("Amount: " + amount);
	    System.out.println("Method: " + method);

	    boolean success = userDAO.depositMoney(userId, amount, method);

	    if (success) {
	        return "deposit-success";
	    }

	    return "deposit";
	}
}