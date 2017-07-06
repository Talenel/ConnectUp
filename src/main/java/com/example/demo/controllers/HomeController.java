package com.example.demo.controllers;

import com.example.demo.repositories.*;
import com.example.demo.services.UserService;
import com.example.demo.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import com.example.demo.models.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 6/28/17.
 */
@Controller
public class HomeController {

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

    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserService userService;


    @RequestMapping("/")
    public String home(Principal principal, Model model)
    {
        if(principal==null)
        {
            return "home";
        }
        User user=userRepository.findByUsername(principal.getName());
        List<Skill> skills=skillRepository.findAllByUserId(user.getId());
        List<Posting> postings=new ArrayList<>();
        for(Skill skill:skills)
        {
            List<Skill> skills2=skillRepository.findTop2ByUserIdAndSkillNameOrderByPostingIdDesc(0,skill.getSkillName());
            for(Skill skill2:skills2)
            {
                postings.add(postingRepository.findById(skill2.getPostingId()));
            }
        }
        model.addAttribute("notifList",postings);

        return "userHome";
          


    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
          

        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        userValidator.validate(user, result);
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "register";
        }
        //Account account =accountRepository.findOneByUserName(principal.getName());
        //transaction.setAcctNum(account.getAcctNum());
        if(user.getRoleName().equals("recruiter"))
        {
            userService.saveRecruiter(user);
            model.addAttribute("message", "Recruiter Account Successfully Created");
        }
        if(user.getRoleName().equals("job seeker"))
        {
            userService.saveJobSeeker(user);
            model.addAttribute("message", "Job Seeker Account Successfully Created");
        }


        return "home";
    }



    @GetMapping("/addSkill/{id}")
    public String skillPostingForm(Model model, @PathVariable("id")long id) {
        Skill skill=new Skill();
        skill.setPostingId(id);

        model.addAttribute("skill", skill);

        return "addSkill";
    }




    @GetMapping("/addSkill")
    public String skillForm(Model model) {
        Skill skill=new Skill();
        skill.setPostingId(-1);

        model.addAttribute("skill", skill);
        return "addSkill";
    }

    @PostMapping("/addSkill")
    public String skillSubmit(@Valid Skill skill, BindingResult bindingResult, Model model, Principal principal) {



        if (bindingResult.hasErrors()) {
            return "addSkill";
        }
        if(skill.getPostingId()<=0) {
            User user = userRepository.findByUsername(principal.getName());
            skill.setUserId(user.getId());
            skillRepository.save(skill);
            return "redirect:/addSkill";

        }
        Posting post=postingRepository.findById(skill.getPostingId());
        post.setSkills(skill.getSkillName()+", "+post.getSkills());
        skillRepository.save(skill);
        return "redirect:/addSkill/"+skill.getPostingId();
    }
    @PostMapping("/search")
    public String searchSubmit(@RequestParam("searching") String searching,@RequestParam("select") String select, Model model) {
         
        if(select.equals("username"))
        {
            List<User> searches=userRepository.findAllByUsername(searching);
            model.addAttribute("searchList", searches);
        }
        if(select.equals("company"))
        {
            List<Job> jobs=jobRepository.findAllByCompany(searching);
            List<User> searches=new ArrayList<User>();
            for(Job job:jobs)
            {
                searches.add(userRepository.findById(job.getUserId()));
            }
            model.addAttribute("searchList", searches);
        }
        if(select.equals("college"))
        {
            List<Education> edus=educationRepository.findAllBySchoolName(searching);
            List<User> searches=new ArrayList<User>();
            for(Education edu:edus)
            {
                searches.add(userRepository.findById(edu.getUserId()));
            }
            model.addAttribute("searchList", searches);
        }
        if(select.equals("skill"))
        {
            List<Skill> skills=skillRepository.findAllBySkillName(searching);
            List<User> searches=new ArrayList<User>();
            for(Skill skill:skills)
            {
                if(skill.getUserId()>0) {
                    searches.add(userRepository.findById(skill.getUserId()));
                }
            }
            model.addAttribute("searchList", searches);
        }

        return "results";
    }

    public UserValidator getUserValidator() {
        return userValidator;
    }
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

}
