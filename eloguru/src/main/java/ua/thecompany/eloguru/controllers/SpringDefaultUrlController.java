package ua.thecompany.eloguru.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpringDefaultUrlController {
	@GetMapping("/login")
	@ResponseBody
	public String showLoginPage() { return ""; }
	@GetMapping("/sign-up")
	@ResponseBody
	public String showSignUpPage() { return ""; }
	@GetMapping("/profile")
	@ResponseBody
	public String showProfilePage() { return ""; }
	@GetMapping("/about")
	public String showAboutPage() {
		return "";
	}
	@GetMapping("/createCourse")
	@ResponseBody
	public String showTeachersPage() {
		return "";
	}
	@GetMapping("/course")
	public String showCoursesPage() {
		return "";
	}
	@GetMapping("/mistake")
	public String showMistakePage() {
		return "";
	}
	@GetMapping("/activate")
	@ResponseBody
	public String showActivatePage() { return ""; }
	@GetMapping("/course/:id")
	public String showQuizPage() { return ""; }
	@GetMapping("/admin")
	@ResponseBody
	public String showAdminPage() { return ""; }
}
