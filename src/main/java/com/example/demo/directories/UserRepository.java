package com.example.demo.directories;

/**
 * Created by student on 6/30/17.
 */


import com.example.demo.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by student on 6/29/17.
 */
public interface UserRepository extends CrudRepository<User,Long> {

    public User findByUsername(String username);

    public List<User> findAllByUsername(String username);
    public User findById(long id);
}
