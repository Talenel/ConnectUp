package com.example.demo.directories;

import com.example.demo.models.Job;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by student on 6/30/17.
 */
public interface JobRepository extends CrudRepository<Job,Long> {

}