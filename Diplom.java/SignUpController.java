package com.example.website.controllers;
import com.example.website.dto.SignUpDto;
import com.example.website.services.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
@Controller
public class SignUpController {
@Autowired
private SignUpService service;
@GetMapping("/signUp")
public String getSignUpPage() { return "sign_up";
}	”
@PostMapping("/signUp")
public String signUp(@Valid @ModelAttribute("form") SignUpDto form, BindingResult bindingResult) {
if (bindingResult.hasErrors()) { return "sign_up";
}	”
service.signUp(form);
return "redirect:/signIn";
}
}
