package com.example.demo.directories;

import com.example.demo.models.Duty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by student on 6/30/17.
 */
public interface DutyRepository extends CrudRepository<Duty,Long> {

    public List<Duty> findAllByJobId(long jobId);

}
