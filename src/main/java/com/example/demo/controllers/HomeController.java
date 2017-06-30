package com.example.demo.controllers;

import com.example.demo.directories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import com.example.demo.models.*;

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
    public String registerSubmit(@Valid User user, BindingResult bindingResult, Model model) {

          
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

        return "userHome";
    }
    @GetMapping("/addEdu")
    public String eduForm(Model model) {
        model.addAttribute("education", new Education());
          
        return "addEducation";
    }

    @PostMapping("/addEdu")
    public String eduSubmit(@Valid Education education, BindingResult bindingResult, Model model) {

          
        if (bindingResult.hasErrors()) {
            return "addEducation";
        }
        //Account account =accountRepository.findOneByUserName(principal.getName());
        //transaction.setAcctNum(account.getAcctNum());

        educationRepository.save(education);

        return "redirect:/addEdu";
    }

    @GetMapping("/addJob")
    public String jobForm(Model model) {
        model.addAttribute("job", new Job());
          
        return "addJob";
    }

    @PostMapping("/addJob")
    public String jobSubmit(@Valid Job job, BindingResult bindingResult, Model model) {

          
        if (bindingResult.hasErrors()) {
            return "addJob";
        }
        if(job.getEndDate().isEmpty())
        {
            job.setEndDate("Present");
        }
        //Account account =accountRepository.findOneByUserName(principal.getName());
        //transaction.setAcctNum(account.getAcctNum());

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
    public String skillSubmit(@Valid Skill skill, BindingResult bindingResult, Model model) {
          

        if (bindingResult.hasErrors()) {
            return "addSkill";
        }
        //Account account =accountRepository.findOneByUserName(principal.getName());
        //transaction.setAcctNum(account.getAcctNum());

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


}
