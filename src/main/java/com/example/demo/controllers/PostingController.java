package com.example.demo.controllers;

import com.example.demo.models.Posting;
import com.example.demo.models.Skill;
import com.example.demo.models.User;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
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

    @PostMapping("/myposts")
    public String viewMyPosts(Model model, Principal principal) {
        User user=userRepository.findByUsername(principal.getName());
        List<Posting> searches=postingRepository.findAllByUserId(user.getId());
        model.addAttribute("searchList2", searches);


        return "results";
    }

    @RequestMapping("/viewAllNotifications")
    public String viewAllNotifs(Principal principal, Model model)

    {
        User user=userRepository.findByUsername(principal.getName());
        List<Skill> skills=skillRepository.findAllByUserId(user.getId());
        List<Posting> postings=new ArrayList<>();
        for(Skill skill:skills)
        {
            List<Skill> skills2=skillRepository.findAllByUserIdAndSkillNameOrderByPostingIdDesc(0,skill.getSkillName());
            for(Skill skill2:skills2)
            {
                if(searchList(postings,skill2)) {
                    postings.add(postingRepository.findById(skill2.getPostingId()));
                }
            }
        }
        model.addAttribute("notifList",postings);
        return "results";
    }

    public boolean searchList(List<Posting> list, Skill skill)
    {
        for(Posting posting:list)
        {
            if(posting.getId()==skill.getPostingId())
            {
                return false;
            }
        }



        return true;
    }


}
