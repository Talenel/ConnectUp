package com.example.demo.directories;

import com.example.demo.models.Job;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by student on 6/30/17.
 */
public interface JobRepository extends CrudRepository<Job,Long> {

    public List<Job> findTop10ByTitleOrderByIdDesc(String title);

    public List<Job> findAllByCompany(String company);

    public List<Job> findAllByUserId(long userId);
}
