package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {


    @Query("SELECT u FROM User u WHERE u.organisation = ?1 and u.lastName = ?2")
    public List<User> getByLastName( Integer organisation, String lastName );

    @Query("SELECT u FROM User u WHERE u.organisation = ?1 and u.email = ?2")
    public List<User> getByEmail( Integer organisation, String email );

    @Query("SELECT u.id FROM User u WHERE u.organisation = ?1 and u.email = ?2")
    public List<Integer> getIdByEmail( Integer organisation, String email );

    @Query("SELECT DISTINCT u.firstName || ' ' || u.lastName FROM User u WHERE u.organisation = ?1 and u.title = ?2")
    public List<String> findNamesByTitle( Integer organisation, String title );

    @Query("SELECT u FROM User u WHERE u.organisation = ?1")
    public List<User> findAll(Integer organisation);

}
