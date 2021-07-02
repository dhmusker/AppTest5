package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("SELECT r FROM Role r WHERE r.organisation = ?1 and r.name = ?2")
    public List<Role> getByName(Integer organisation, String name );

    @Query("SELECT r.id FROM Role r WHERE r.organisation = ?1 and r.name = ?2")
    public List<Integer> getIdByName( Integer organisation, String name );

}
