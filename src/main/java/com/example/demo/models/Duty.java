package com.example.demo.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by student on 6/29/17.
 */
@Entity
public class Duty {

    private long jobId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String dutyMessage;

    public Duty()
    {

    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDutyMessage() {
        return dutyMessage;
    }

    public void setDutyMessage(String dutyMessage) {
        this.dutyMessage = dutyMessage;
    }
}
