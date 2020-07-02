package com.zbodya.Controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.zbodya.Model.User;
import com.zbodya.Service.UserService;

@Controller
public class RegistrationController
{
	@Autowired
	UserService userService;
	
	@GetMapping("/registration")
	public String registration(Model model) 
	{
		model.addAttribute("user", new User());
		return "service/registration";
	}
	
	@PostMapping("/registration")
	public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) 
	{
		if(bindingResult.hasErrors()) 
		{
			return "service/registration";
		}
		if(!userService.saveUser(user)) 
		{
			model.addAttribute("userError", "User with this name exists");
			return "service/registration";
		}
		return "redirect:/";
	}
	
	@GetMapping("/")
	public String main(Model model) 
	{
		return "redirect:/book/allBooks?findReq=false";
	}

}
	