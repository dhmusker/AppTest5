package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query("SELECT p FROM Permission p WHERE p.organisation = ?1 and p.name = ?2")
    List<Permission> getByName(Integer organisation, String name );

    @Query("SELECT p.id FROM Permission p WHERE p.organisation = ?1 and p.name = ?2")
    List<Integer> getIdByName( Integer organisation, String name );

}
