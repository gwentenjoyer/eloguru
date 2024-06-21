package ua.thecompany.eloguru.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class SpringDefaultUrlController {
	@GetMapping("/login")
	@ResponseBody
	public String showLoginPage() { return ""; }
	@GetMapping("/sign-up")
	@ResponseBody
	public String showSignUpPage() { return ""; }
	@GetMapping("/profile")
	public String showProfilePage() { return ""; }
	@GetMapping("/about")
	public String showAboutPage() {
		return "";
	}
	@GetMapping("/createCourse")
	@ResponseBody
	public byte[] showTeachersPage() throws IOException {
		Resource resource = new ClassPathResource("/static/index.html");
		return Files.readAllBytes(Path.of(resource.getURI()));
	}
	@GetMapping("/course")
	public String showCoursesPage() {
		return "";
	}
	@GetMapping("/course/{id}")
	@ResponseBody
	public byte[] showCoursesIdPage(@PathVariable String id) throws IOException {
		Resource resource = new ClassPathResource("/static/index.html");
		return Files.readAllBytes(Path.of(resource.getURI()));
	}
	@GetMapping("/mistake")
	public String showMistakePage() {
		return "";
	}
	@GetMapping("/activate")
	public String showActivatePage() { return ""; }
	@GetMapping("/course/:id")
	public String showQuizPage() { return ""; }
	@GetMapping("/admin")
	public String showAdminPage() { return ""; }
	@GetMapping("/admin/data")
	@ResponseBody
	public byte[] showAdminDataPage() throws IOException {
		Resource resource = new ClassPathResource("/static/index.html");
		return Files.readAllBytes(Path.of(resource.getURI()));
	}
	@GetMapping("/admin/course")
	@ResponseBody
	public byte[] showAdminCoursePage() throws IOException {
		Resource resource = new ClassPathResource("/static/index.html");
		return Files.readAllBytes(Path.of(resource.getURI()));
	}
}
