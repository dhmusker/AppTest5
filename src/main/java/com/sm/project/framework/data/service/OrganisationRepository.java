package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.Organisation;
import com.sm.project.framework.data.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganisationRepository extends JpaRepository<Organisation, Integer> {

    @Query("SELECT p FROM Organisation p WHERE p.name = ?1")
    List<Organisation> getByName(String name );

    @Query("SELECT p.id FROM Organisation p WHERE p.name = ?1")
    List<Integer> getIdByName( String name );

}
