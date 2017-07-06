package com.example.demo.controllers;

import com.example.demo.models.Posting;
import com.example.demo.models.User;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created by student on 7/6/17.
 */
@Controller
@RequestMapping("/posting")
public class PostingController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private PostingRepository postingRepository;

    @GetMapping("/addPosting")
    public String postingForm(Model model) {
        model.addAttribute("posting", new Posting());

        return "addPosting";
    }

    @PostMapping("/addPosting")
    public String postingSubmit(@Valid Posting posting, BindingResult bindingResult, Model model, Principal principal) {


        if (bindingResult.hasErrors()) {
            return "addPosting";
        }

        User user=userRepository.findByUsername(principal.getName());
        posting.setUserId(user.getId());

        postingRepository.save(posting);
        List<Posting> postings=postingRepository.findTop10ByTitleOrderByIdDesc(posting.getTitle());
        long id=postings.get(0).getId();

        return "redirect:/addSkill/"+id;
    }




}
