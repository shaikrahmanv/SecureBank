package Jar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.banking.User;
import com.banking.UserDAO;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    private final UserDAO userDAO = new UserDAO();

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user = userDAO.getUserById(userId);

        if (user == null) {
            session.invalidate();
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "dashboard";
    }
}