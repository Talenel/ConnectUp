package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 7/6/17.
 */
@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EducationRepository educationRepository;


    @Autowired
    private JobRepository jobRepository;


    @Autowired
    private DutyRepository dutyRepository;

    @Autowired
    private SkillRepository skillRepository;



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

        return "redirect:/resume/addEdu";
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

        return "redirect:/resume/addDuties/"+id;
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

        return "redirect:/resume/addDuties/"+duty.getJobId();
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
            StringBuilder sb=new StringBuilder("");
            for(Duty duty:duties2)
            {

                sb.append("<span>"+duty.getDutyMessage()+"</span></br>");

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


}
