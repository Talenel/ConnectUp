package com.example.demo.directories;

import com.example.demo.models.Education;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by student on 6/30/17.
 */
public interface EducationRepository extends CrudRepository<Education,Long> {

        }