package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by student on 6/28/17.
 */
@Entity
public class Education {


    private long userId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(max=50)
    private String schoolName;


    private String yearGrad;
//    @NotNull
//    private Date yearGrad;

    @NotNull
    @Size(min=2,max=20)
    private String degreeType;

    @NotNull
    @Size(min=2,max=50)
    private String degreeMajor;


    public Education()
    {

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getEmail() {
        return userId;
    }

    public void setEmail(long userId) {
        this.userId = userId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
/*
    public Date getYearGrad() {
        return yearGrad;
    }

    public void setYearGrad(Date yearGrad) {
        this.yearGrad = yearGrad;
    }
*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getYearGrad() {
        return yearGrad;
    }

    public void setYearGrad(String yearGrad) {
        this.yearGrad = yearGrad;
    }

    public String getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(String degreeType) {
        this.degreeType = degreeType;
    }

    public String getDegreeMajor() {
        return degreeMajor;
    }

    public void setDegreeMajor(String degreeMajor) {
        this.degreeMajor = degreeMajor;
    }

}
