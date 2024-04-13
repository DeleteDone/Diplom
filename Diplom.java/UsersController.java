package com.example.website.controllers;
import com.example.website.dto.UserDto;
import com.example.website.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
@Controller
public class UsersController {
@Autowired
private UsersService usersService;
@PreAuthorize("hasAuthority('ADMIN')")
@GetMapping("/users")
public String getUsersPage(Model model) {
List<UserDto> users = usersService.getAllUsers();
model.addAttribute("users", users); return "users";
}
@PreAuthorize("hasAuthority('ADMIN')")
@PostMapping("/users/{user-id}/delete")
public String deleteUser(@PathVariable("user-id") Long userId) { usersService.deleteUser(userId);
return "redirect:/users";
}
}
