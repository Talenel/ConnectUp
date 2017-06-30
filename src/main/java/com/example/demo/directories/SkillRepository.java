package com.example.demo.directories;

import com.example.demo.models.Skill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by student on 6/30/17.
 */
public interface SkillRepository extends CrudRepository<Skill,Long> {

    public List<Skill> findAllBySkillName(String skillName);

}