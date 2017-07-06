package com.example.demo.repositories;

import com.example.demo.models.Posting;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by student on 7/6/17.
 */
public interface PostingRepository extends CrudRepository<Posting,Long> {

    public List<Posting> findTop10ByTitleOrderByIdDesc(String title);

    public List<Posting> findAllByEmployer(String employer);

    public List<Posting> findAllByUserId(long userId);

    public Posting findById(long id);

    public List<Posting> findAllByTitle(String title);
}