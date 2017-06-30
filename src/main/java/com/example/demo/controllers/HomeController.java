package com.example.demo.controllers;

import com.example.demo.directories.RoleRepository;
import com.example.demo.directories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import com.example.demo.models.*;

/**
 * Created by student on 6/28/17.
 */
@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping("/")
    public String home(){
        return "home";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String depositForm(Model model) {
        model.addAttribute("user", new User());

        return "register";
    }

    @PostMapping("/register")
    public String depositSubmit(@Valid User user, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return "register";
        }
        //Account account =accountRepository.findOneByUserName(principal.getName());
        //transaction.setAcctNum(account.getAcctNum());
        user.setEnabled(true);
        Role role=new Role();
        role.setRole(user.getRole());
        roleRepository.save(role);
        userRepository.save(user);

        return "result";
    }

}
