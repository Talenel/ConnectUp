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
    private RoleRepository roleRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DutyRepository dutyRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserService userService;


    @RequestMapping("/")
    public String home(Model model)
    {

          
        return "home";

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
        if(user.getRole().equals("recruiter"))
        {
            userService.saveRecruiter(user);
            model.addAttribute("message", "Recruiter Account Successfully Created");
        }
        if(user.getRole().equals("job seeker"))
        {
            userService.saveJobSeeker(user);
            model.addAttribute("message", "Job Seeker Account Successfully Created");
        }


        return "userHome";
    }
    @GetMapping("/addEdu")
    public String eduForm(Model model) {
        model.addAttribute("education", new Education());
          
        return "addEducation";
    }

    @PostMapping("/addEdu")
    public String eduSubmit(@Valid Education education, BindingResult bindingResult, Model model, Principal principal) {

          
        if (bindingResult.hasErrors()) {
            return "addEducation";
        }
        //Account account =accountRepository.findOneByUserName(principal.getName());
        //transaction.setAcctNum(account.getAcctNum());
        User user=userRepository.findByUsername(principal.getName());
        education.setUserId(user.getId());

        educationRepository.save(education);

        return "redirect:/addEdu";
    }

    @GetMapping("/addJob")
    public String jobForm(Model model) {
        model.addAttribute("job", new Job());
          
        return "addJob";
    }

    @PostMapping("/addJob")
    public String jobSubmit(@Valid Job job, BindingResult bindingResult, Model model, Principal principal) {

          
        if (bindingResult.hasErrors()) {
            return "addJob";
        }
        if(job.getEndDate().isEmpty())
        {
            job.setEndDate("Present");
        }
        //Account account =accountRepository.findOneByUserName(principal.getName());
        //transaction.setAcctNum(account.getAcctNum());
        User user=userRepository.findByUsername(principal.getName());
        job.setUserId(user.getId());

        jobRepository.save(job);
        List<Job> jobs=jobRepository.findTop10ByTitleOrderByIdDesc(job.getTitle());
        long id=jobs.get(0).getId();

        return "redirect:/addDuties/"+id;
    }
    @GetMapping("/addDuty")
    public String dutyForm(Model model) {
        model.addAttribute("duty", new Duty());
          
        return "addDuty";
    }
    @GetMapping("/addDuties/{id}")
    public String dutyMore(Model model, @PathVariable("id")long id) {

        Duty duty=new Duty();
        duty.setJobId(id);
        model.addAttribute("duty", duty);
          

        return "addDuty";
    }


    @PostMapping("/addDuty")
    public String dutySubmit(@Valid Duty duty, BindingResult bindingResult, Model model) {

          
        if (bindingResult.hasErrors()) {
            return "addDuty";
        }
        //Account account =accountRepository.findOneByUserName(principal.getName());
        //transaction.setAcctNum(account.getAcctNum());

        dutyRepository.save(duty);

        return "redirect:/addDuties/"+duty.getJobId();
    }

    @GetMapping("/addSkill")
    public String skillForm(Model model) {
        model.addAttribute("skill", new Skill());
          
        return "addSkill";
    }

    @PostMapping("/addSkill")
    public String skillSubmit(@Valid Skill skill, BindingResult bindingResult, Model model, Principal principal) {



        if (bindingResult.hasErrors()) {
            return "addSkill";
        }
        //Account account =accountRepository.findOneByUserName(principal.getName());
        //transaction.setAcctNum(account.getAcctNum());
        User user=userRepository.findByUsername(principal.getName());
        skill.setUserId(user.getId());

        skillRepository.save(skill);

        return "redirect:/addSkill";
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
                searches.add(userRepository.findById(skill.getUserId()));
            }
            model.addAttribute("searchList", searches);
        }

        return "results";
    }

    @RequestMapping("/myresume")
    public String myResume(Model model, Principal principal)
    {
        User user=userRepository.findByUsername(principal.getName());
        List<Education> edus=educationRepository.findAllByUserId(user.getId());
        List<Job> jobs=jobRepository.findAllByUserId(user.getId());
        List<Skill> skills=skillRepository.findAllByUserId(user.getId());
        ArrayList<String> duties=new ArrayList<>();
        for(Job job:jobs)
        {
            List<Duty> duties2=dutyRepository.findAllByJobId(job.getId());
            StringBuilder sb=new StringBuilder("<br/>");
            for(Duty duty:duties2)
            {

                sb.append("<p>"+duty.getDutyMessage()+"</p></br>");

                ;
            }
            duties.add(sb.toString());

        }
        model.addAttribute("user", user);
        model.addAttribute("dutiesList", duties);
        model.addAttribute("jobsList", jobs);
        model.addAttribute("edusList", edus);
        model.addAttribute("skillsList", skills);

        return "displayResume";

    }
    public UserValidator getUserValidator() {
        return userValidator;
    }
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

}
